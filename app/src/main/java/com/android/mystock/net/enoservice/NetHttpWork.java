package com.android.mystock.net.enoservice;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;

import com.android.mystock.common.log.Logs;
import com.eno.base.utils.ENOUtils;
import com.eno.base.utils.TCRS;
import com.eno.net.channel.ENO_Channel;
import com.eno.net.channel.ENO_ChannelFactory;
import com.eno.net.codec.ENO_Decoder;
import com.eno.net.codec.ENO_Encoder;
import com.eno.net.enotc.ENO_HttpMessageRequest;
import com.eno.net.enotc.ENO_HttpResponseDecoder;
import com.eno.net.socket.ENO_SocketFactory;
import com.eno.net.worke.ENO_NetWorke;
import com.eno.net.worke.ENO_WorkeFactory;
import com.eno.net.worke.ENO_WorkeHttpFactory;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 网络通信代理封装
 */
public class NetHttpWork extends ABSNetWork {

    private ENO_NetWorke netWorke;//工作线程
    private boolean isSessioning = false;// 正在取session
    private Map<Integer, NetMessage> pool = Collections
            .synchronizedMap(new HashMap<Integer, NetMessage>());//请求消息线程池
    private ProgressDialog progressDialog;// 数据等待提示
    private int i = 0;//监听请求是否返回

    public NetHttpWork() {
        workeInit();
    }

    /**
     * 设置IP地址和端口
     *
     * @param addr
     * @param port
     */
    public NetHttpWork(String addr, int port) {
        this.addr = addr;
        this.port = port;
        workeInit();
    }

    /**
     * 工作线程初始化
     */
    private void workeInit() {
        ENO_WorkeFactory factory = new ENO_WorkeHttpFactory();
        netWorke = factory.create();
        netWorke.setEnoChannel(createNet());
        pool.clear();
    }

    /**
     * 清除请求池
     */
    public void clearNetMessagePool() {
        pool.clear();
    }

    /**
     * 创建连接对象
     *
     * @return
     */
    private ENO_Channel createNet() {
        ENO_ChannelFactory channelFactory = new ENO_SocketFactory();
        ENO_Channel channel = channelFactory.create(addr, port);

        return channel;
    }

    /**
     * 发送请求
     *
     * @param clientMessage
     * @param context
     */
    public void sendRequest(NetClientMessage clientMessage, Context context) {
        sendRequest(clientMessage, context, null);
    }

    /**
     * 发送请求
     *
     * @param clientMessage 请求 Message对象
     * @param context       上下文对象
     * @param channel       连接通道
     */
    public void sendRequest(NetClientMessage clientMessage, Context context,
                            ENO_Channel channel) {
        sendRequest(clientMessage, context, null, channel);
    }

    /**
     * 发送请求
     *
     * @param clientMessage 请求 Message对象
     * @param context       上下文对象
     * @param progressBar   界面提示用的 progressBar
     * @param channel       连接通道
     */
    public void sendRequest(NetClientMessage clientMessage, Context context,
                            ProgressBar progressBar, ENO_Channel channel) {

        sendRequest(COMPRESSION, DEFAULT, clientMessage, context, progressBar, channel);
    }

    /**
     * 发送请求
     *
     * @param compress      是否需要压缩
     * @param encrypt       是否需要加密
     * @param clientMessage 消息体
     * @param context       上下文对象
     * @param progressBar   progressBar
     * @param channel       连接通道
     */
    public void sendRequest(int compress, int encrypt,
                            NetClientMessage clientMessage, Context context,
                            ProgressBar progressBar, ENO_Channel channel) {

        Bundle data = clientMessage.sendRequest();
        this.context = context;
        boolean isUIthread = data.getBoolean("isUIthread", true);// 默认是UI线程
        boolean isSynchronized = data.getBoolean("isSynchronized", false);// 默认是异步请求
        boolean isShowProgressBar = data.getBoolean("isShowProgressBar", false);// 是否顯示 進度框
        int packageID = clientMessage.getPackageID();

        NetMessage message = new NetMessage(packageID, compress, encrypt,
                data.getString("url") + "&" + fixedParameter);
        Logs.e("请求参数：" + data.getString("url") + "&" + fixedParameter);

        message.setContext(context);
        message.setClientMessage(clientMessage);
        message.setProgressBar(progressBar);
        message.setUIThread(isUIthread);

        Message msg = Message.obtain();
        Bundle b = new Bundle();
        msg.what = 1;
        msg.obj = message;
        msg.setData(b);
        b.putBoolean("isShow", isShowProgressBar);// 是否需要progressBar
        handler.sendMessage(msg);

        //如果同步的话就不开多的连接通道
        if (isSynchronized) {
            message.setChannel(null);
            netWorke.sendRequest(message, null);
        } else {
            if (channel == null) {
                message.setChannel(createNet());//如果不同步创建一个新的通道
                netWorke.sendRequest(message, createNet());
            } else {
                message.setChannel(channel);
                netWorke.sendRequest(message, channel);
            }
        }

        if (packageID != SESSION_TIMEOUT) {
            pool.put(packageID, message);
        }
    }

    /**
     * 消息发送和监听
     */
    class NetMessage extends ENO_HttpMessageRequest {

        private NetClientMessageible clientMessage;
        private ProgressBar progressBar = null;
        private Context context;
        private boolean isUIThread = true;// 默认UI线程
        private Bundle bundle; //数据bundle
        private ENO_Channel channel;  //连接通道
        private TCRS[] tcrs;//伊诺结果集

        public ENO_Channel getChannel() {
            return channel;
        }

        public void setChannel(ENO_Channel channel) {
            this.channel = channel;
        }

        public TCRS[] getTcrs() {
            return tcrs;
        }

        public void setTcrs(TCRS[] tcrs) {
            this.tcrs = tcrs;
        }

        public ProgressBar getProgressBar() {
            return progressBar;
        }

        public void setProgressBar(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        public boolean isUIThread() {
            return isUIThread;
        }

        public void setUIThread(boolean isUIThread) {
            this.isUIThread = isUIThread;
        }

        public NetMessage(int packageid, int compress, int encrypt, String url) {
            super(packageid, compress, encrypt, url);

        }

        public NetClientMessageible getClientMessage() {
            return clientMessage;
        }

        public void setClientMessage(NetClientMessageible clientMessage) {
            this.clientMessage = clientMessage;
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        @Override
        public void netParse(ENO_Encoder encoder, ENO_Decoder decoder,
                             byte[] data) {
            if (data == null)
                return;
            ENO_HttpResponseDecoder sdecoder = (ENO_HttpResponseDecoder) decoder;

            Logs.e(sdecoder.getENOFlag() + "ENO_ResponseDataLength=" + data.length);

            if (sdecoder.getENOFlag() > 0) {
                String str = ENOUtils.bytes2Unistr(data, 0, data.length);
                Logs.d("后台直接返回了Unistr:" + str);
                try {
                    String msg = new String(data, "UTF-8");
                    Logs.d("后台直接返回了UTF-8:" + msg);
                    String msg1 = new String(data, "GBK");
                    Logs.d("后台直接返回了GBK:" + msg1);
                    String msg2 = new String(data, "ISO-8859-1");
                    Logs.d("后台直接返回了ISO-8859-1:" + msg2);
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (sdecoder.getENOFlag() == SESSION_TIMEOUT) // session 超时
                {
                    Logs.e("sesson超时");
                    //有些页面有多个请求一起发生，可以能第一取session 的请求还回来第二个又会 session 超时
                    if (isSessioning)// 如果session 还没回来就不再重新取session
                    {
                        Logs.e("正在发送sesson请求。。。。。。。。。。。。");
                        return;
                    }

                    NetMessage message = new NetMessage(SESSION_TIMEOUT, DEFAULT, DEFAULT,
                            fixedParameter + getLoginUrl());

                    Logs.e("重新发送取session" + fixedParameter + getLoginUrl());
                    message.setContext(context);
                    message.setClientMessage(clientMessage);
                    message.setProgressBar(progressBar);
                    message.setUIThread(false);
                    netWorke.sendRequest(message, this.getChannel());//发生取session的请求
                    isSessioning = true;
                } else {
                    netError(sdecoder.getENOPackage(), str);
                }

            } else {
                Logs.e("数据大小" + data.length);

                tcrs = TCRS.buildMRS(data, 0);
                if (tcrs.length == 0) {
                    tcrs = null;
                    String str = ENOUtils.bytes2Unistr(data, 0, data.length);
                    Logs.e("构建结果集失败：" + str);
                } else {
                    Logs.printTCRS(tcrs, null, true);
                }

                if (sdecoder.getENOPackage() == SESSION_TIMEOUT)// 替换session
                {
                    isSessioning = false;
                    if (tcrs != null) {
                        if (tcrs[0].IsError()) {
                            Logs.e("重新取session失败！");
                        } else {
                            //保存全局session
                            Logs.e("session重新保存了！");
                            setAppSession(tcrs[0].toString(0));
                        }
                    }

                    Iterator<Entry<Integer, NetMessage>> iter = pool.entrySet()
                            .iterator();
                    while (iter.hasNext()) {
                        Entry entry = (Entry) iter.next();
                        NetMessage clientMessage = (NetMessage) entry
                                .getValue();
                        //重新获取 session后我们要找到刚刚 session超时的请求重新发送
                        netWorke.sendRequest(clientMessage,
                                clientMessage.getChannel());
                    }

                    pool.clear();

                } else {

                    Bundle b = new Bundle();
                    b.putInt("packageID", sdecoder.getENOPackage());
                    b.putByteArray("data", data);
                    if (isUIThread) {
                        Message msg = Message.obtain();
                        msg.what = 0;
                        msg.obj = NetMessage.this;
                        msg.setData(b);
                        handler.sendMessage(msg);
                    } else {
                        clientMessage.netParse(tcrs, b);
                    }
                }
            }
        }

        @Override
        public void netError(int errorIndex, String msgcont) {
            // TODO Auto-generated method stub
            Bundle b = new Bundle();
            b.putString("errorMsg", msgcont);
            b.putInt("errorIndex", errorIndex);

            Message msg = Message.obtain();
            msg.what = 2;
            msg.obj = NetMessage.this;
            msg.setData(b);
            handler.sendMessage(msg);

        }

        @Override
        public void timeOutNotify() {
            // TODO Auto-generated method stub
            netError(0, "请求超时！");

        }

        @Override
        public void netNotifier() {
            // TODO Auto-generated method stu
            Logs.d("请求已经回收<><><><><><><><>" + i++);
            Message msg = Message.obtain();
            Bundle b = new Bundle();
            msg.what = 1;
            msg.obj = NetMessage.this;
            b.putBoolean("isShow", false);
            msg.setData(b);
            handler.sendMessage(msg);
        }

        public Bundle getBundle() {
            return bundle;
        }

        public void setBundle(Bundle bundle) {
            this.bundle = bundle;
        }
    }

    private AlertDialog dialog;// 错误提示

    private void onCreateDialog(Bundle args, Context context) {
        String errorMsg = args.getString("errorMsg");

        try {
            Logs.d("  errorMsg=  " + errorMsg + "      "
                    + context.getAssets() + "   " + context);
            if (dialog == null || !dialog.isShowing()) {
                dialog = new AlertDialog.Builder(context)
                        .setTitle("系统提示！")
                        .setMessage(errorMsg)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialoginterface,
                                            int i) {
                                        // dialoginterface.cancel();
                                        dialoginterface.dismiss();

                                    }
                                }).show();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    /**
     * 设置progressBar
     *
     * @param context
     * @param progressBar
     * @param isShowOrColsed
     */
    private void showSelfProgressBar(Context context, ProgressBar progressBar,
                                     boolean isShowOrColsed) {
        if (progressBar != null) {

            if (isShowOrColsed) {

                progressBar.setVisibility(View.VISIBLE);
            } else {

                progressBar.setVisibility(View.GONE);

            }
        }
    }


    /**
     * 显示ProgressDialog
     *
     * @param context
     * @param msg
     * @param isShowOrColsed
     */
    private void showProgressDialog(Context context, String msg,
                                    boolean isShowOrColsed) {
        try {
            if (isShowOrColsed) {
                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }
                progressDialog = ProgressDialog.show(context, null, msg, true);
                progressDialog.setCancelable(true);
                progressDialog.show();
            } else {
                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }
            }

        } catch (Exception e) {

        }
    }


    /**
     * UI线程交互
     */
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // progressBar.setVisibility(View.GONE);
            Bundle b = msg.getData();
            NetMessage netMessage = (NetMessage) msg.obj;
            Context context = netMessage.getContext();
            NetClientMessageible clientMessage = netMessage.getClientMessage();
            TCRS[] tcrs = netMessage.getTcrs();
            int index = b.getInt("index");
            switch (msg.what) {
                case 0:
                    clientMessage.netParse(tcrs, msg.getData());
                    break;
                case 1:
                    boolean isShow = b.getBoolean("isShow");
                    if (!isShow) {
                        clientMessage.netNotifier();
                    }
                    if (netMessage.getProgressBar() == null) {
                        showProgressDialog(context, "请稍等...", isShow);
                    } else {
                        showSelfProgressBar(context, netMessage.getProgressBar(),
                                isShow);
                    }
                    break;
                case 2:
                    if (!clientMessage.netError(b.getString("errorMsg"))) {
                        onCreateDialog(b, context);
                    }

                    break;
                case 3:

                    break;

                default:
                    break;
            }

        }

    };


}

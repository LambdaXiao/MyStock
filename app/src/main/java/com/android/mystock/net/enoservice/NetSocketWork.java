package com.android.mystock.net.enoservice;

import android.os.Bundle;

import com.android.mystock.application.App;
import com.android.mystock.common.log.Logs;
import com.eno.base.utils.ENOUtils;
import com.eno.base.utils.TCRS;
import com.eno.net.channel.ENO_Channel;
import com.eno.net.codec.ENO_Decoder;
import com.eno.net.enotc.ENO_HttpResponseDecoder;
import com.eno.net.enotc.ENO_HttpSocketMessageRequest;
import com.eno.net.messages.ENO_NetMessageSocketResponse;
import com.eno.net.worke.ENO_NetWorkeLongTime;
import com.eno.net.worke.ENO_WorkeLongTimeFactory;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 长连接通讯封装
 *
 * @author xmf
 * @version 1.0
 */
public class NetSocketWork extends ABSNetWork {


    private static ENO_NetWorkeLongTime netWorke;//专门用建立长连接的工作线程对象

    private NetClientPushMessageible netClientPushMessage;//推送消息对象

    private boolean isSessioning = false;//正在取session

    /**
     * 请求对象池，把每个请求放在池里面，请求回来时在回收池中对象
     */
    private Map<Integer, NetMessageRequest> pool = Collections
            .synchronizedMap(new HashMap<Integer, NetMessageRequest>());

    public NetSocketWork(NetClientPushMessageible netClientPushMessage,
                         ENO_Channel channel, App application) {
        this.netClientPushMessage = netClientPushMessage;
        this.channel = channel;
        this.application = application;
        init();
    }

    /**
     * 初始化长连接工作类
     */
    private void init() {
        ENO_WorkeLongTimeFactory factory = new ENO_WorkeLongTimeFactory();
        netWorke = factory.create();
        netWorke.setEnoChannel(channel);
        netWorke.setParseResponse(new NetMessage());
        pool.clear();
        isSessioning = false;

    }


    /**
     * 发送请求
     *
     * @param packageid
     * @param url
     */
    public void sendRequest(int packageid, String url) {
        Logs.e("请求参数：" + url + "&" + fixedParameter);
        url = url + "&" + fixedParameter;
        NetMessageRequest clientMessage = new NetMessageRequest(packageid, 0,
                0, url);
        if (packageid != SESSION_TIMEOUT) {
            pool.put(packageid, clientMessage);
        }
        netWorke.sendRequest(clientMessage, channel);

    }


    /**
     * 开始监听数据
     */
    public void stratListenting() {
        netWorke.startDataListenting();
    }

    class NetMessageRequest extends ENO_HttpSocketMessageRequest {

        public NetMessageRequest(int packageid, int compress, int encrypt,
                                 String url) {
            super(packageid, compress, encrypt, url);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void netError(int errorIndex, String msg) {
            // TODO Auto-generated method stub

        }

        @Override
        public void netNotifier() {
            // TODO Auto-generated method stub

        }

        @Override
        public void timeOutNotfy() {
            // TODO Auto-generated method stub

        }

    }

    /**
     * 数据接收对象
     */
    class NetMessage implements ENO_NetMessageSocketResponse {

        @Override
        public void netError(int errorIndex, String msg) {
            // TODO Auto-generated method stub

        }

        @Override
        public void netNotifier() {
            // TODO Auto-generated method stub

        }

        @Override
        public void netParse(ENO_Decoder decoder, byte[] data) {
            // TODO Auto-generated method stub

            Logs.v("_________数据回来了____________" + data.length);
            if (data == null)
                return;
            ENO_HttpResponseDecoder sdecoder = (ENO_HttpResponseDecoder) decoder;
            if (sdecoder.getENOFlag() > 0) {
                String str = ENOUtils.bytes2Unistr(data, 0, data.length);

                try {
                    String msg = new String(data, "UTF-8");
                    String msg1 = new String(data, "GBK");
                    String msg2 = new String(data, "ISO-8859-1");
                    Logs.e(msg);
                    Logs.e(msg1);
                    Logs.e(msg2);
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (sdecoder.getENOFlag() == SESSION_TIMEOUT) // session 超时
                {
                    if (isSessioning)// 如果session 还没回来就不在重新取session
                    {
                        return;
                    }
                    //发送登录请求重新取session
                    sendRequest(SESSION_TIMEOUT, getLoginUrl());

                    isSessioning = true;

                    netError(sdecoder.getENOPackage(), str);
                }
            } else {

                TCRS[] tcrs = TCRS.buildMRS(data, 0);

                Logs.printTCRS(tcrs, null, true);
                //Logger.printTCRS("", tcrs,"",true);
                if (sdecoder.getENOPackage() == SESSION_TIMEOUT)// 等于100表示是取到的是session结果
                {
                    isSessioning = false;
                    if (tcrs != null) {
                        if (tcrs[0].IsError()) {
                            //Logger.d("重新取session失败");
                        } else {
                            //把取回来的新的 session保存到 application中
                            setAppSession(tcrs[0].toString(0));
                        }

                    }

                    Iterator<Entry<Integer, NetMessageRequest>> iter = pool
                            .entrySet().iterator();
                    while (iter.hasNext()) {
                        Entry entry = (Entry) iter.next();
                        int key = (Integer) entry.getKey();
                        NetMessageRequest clientMessage = (NetMessageRequest) entry.getValue();
                        netWorke.sendRequest(clientMessage, channel);

                    }
                    pool.clear();
                } else {

                    Bundle b = new Bundle();
                    b.putInt("packageID", sdecoder.getENOPackage());
                    netClientPushMessage.netParse(tcrs, b);
                }

            }

        }

    }
}
package com.android.mystock.net.enoservice;

import android.content.Context;
import android.os.Bundle;

import com.android.mystock.application.App;
import com.android.mystock.common.log.Logs;
import com.android.mystock.data.consts.Consts;
import com.eno.base.utils.TCRS;
import com.eno.net.socket.ENO_SocketFactory;

/**
 * Created by xmf on 16/3/16.
 * 网络测速功能
 */
public class NetWorkSpeed {
    private static final String tag = "NetTest";
    public static String[] servletAddr;//服务器IP数组
    public static int port;
    private long[] costTimes = {-1, -1, -1};
    private NetHttpWork netWork;
    private Context context;
    private boolean isfast = false;//是否第一次请求
    private int luaServNo;//lua 功能号
    public NetTestNotifier netNotifier;//测速回调接口

    public abstract interface NetTestNotifier {
        /**
         * 回调测试结果
         *
         * @param index
         */
        abstract void onNetNotifier(int index, long time);
    }

    /**
     * 网络测速类实例化
     *
     * @param servletAddr 服务器地址
     * @param port        端口
     * @param netNotifier 回调返回接口
     * @param context     上下文对象
     */
    public NetWorkSpeed(String[] servletAddr, int port, NetTestNotifier netNotifier, Context context) {
        this.servletAddr = servletAddr;
        this.port = port;
        this.netNotifier = netNotifier;
        this.context = context;
    }

    /**
     * 设置上下文对象
     *
     * @param netNotifier
     */
    public void setNetNotifier(NetTestNotifier netNotifier) {
        this.netNotifier = netNotifier;
    }


    /**
     * 获取服务器地址
     *
     * @return
     */
    public static String[] getServletaddr() {
        return servletAddr;
    }

    /**
     * 获取服务器端口
     *
     * @return
     */
    public static int getPort() {
        return port;
    }


    /**
     * 开始执行网络测速
     *
     * @param application
     */
    public void stratrTest(App application) {

        luaServNo = Consts.LUA_NUMBE;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < servletAddr.length; i++) {
            if (netWork == null) {
                netWork = new NetHttpWork();
            }

            costTimes[i] = startTime;
            netWork.sendRequest(new ClientMessage(i), context, new ENO_SocketFactory().create(servletAddr[i], port));
            Logs.d(tag + "   i===" + i);
        }
    }


    /**
     * 测速请求消息和回调
     */
    public class ClientMessage extends NetClientMessage {

        public ClientMessage(int packageID) {
            super(packageID);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Bundle sendRequest() {
            // TODO Auto-generated method stub
            Bundle b = new Bundle();
            b.putString("url", "tc_service=" + luaServNo + "&tc_isunicode=1");
            b.putBoolean("isShowProgressBar", false);
            return b;
        }

        @Override
        public boolean netError(String msg) {
            // TODO Auto-generated method stub
            int index = this.getPackageID();
            costTimes[index] = System.currentTimeMillis() - costTimes[index];
            //Logger.d(tag,"server:"+mServletAddr[index]+" costTime:"+mCostTimes[index]+"ms");

            Logs.d(tag, msg + "     " + isfast);
            if (!isfast && msg.contains("正常")) {
                Logs.d(tag, netNotifier + "");

                if (netNotifier != null) {
                    netNotifier.onNetNotifier(index, costTimes[index]);
                }
                isfast = true;
            }
            return true;
        }

        @Override
        public void netNotifier() {
        }

        @Override
        public boolean netParse(TCRS[] tcrs, Bundle bundle) {
            // TODO Auto-generated method stub
            return false;
        }
    }
}

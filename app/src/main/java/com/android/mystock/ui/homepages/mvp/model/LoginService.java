package com.android.mystock.ui.homepages.mvp.model;

import android.content.Context;
import android.os.Bundle;

import com.android.mystock.common.log.Logs;
import com.android.mystock.common.utils.ToastUtils;
import com.android.mystock.net.enoservice.ABSNetWork;
import com.android.mystock.net.enoservice.NetClientMessage;
import com.android.mystock.net.enoservice.NetHttpWork;
import com.android.mystock.net.enoservice.NetWorkInstance;
import com.eno.base.utils.TCRS;

/**
 * 数据请求
 */
public class LoginService {
    public static int PACKAGEID = 0;//packageID自增字段，每个请求分配一个独立的标识

    /*
    100.1请求session及一些配置参数
     */
    public static void sendRequestLogin(Context context,OnLoadDataListener listener) {

        StringBuffer sbf = new StringBuffer();
        sbf.append("TC_MFUNCNO=100&TC_SFUNCNO=1" + ABSNetWork.getKey(context));
        sbf.append("&s_ac=13148794461");
        sbf.append("&imei=0000000");

        NetHttpWork.setLoginUrl(sbf.toString());

        NetHttpWork netWork = NetWorkInstance.getInstance().getNetHttpWork();
        netWork.sendRequest(NetHttpWork.COMPRESSION,NetHttpWork.DEFAULT,new ParseData(context,sbf.toString(),listener), context,null,null);

    }

    /**
     * 处理请求的回调
     */
    static class ParseData extends NetClientMessage {

        private Context context;
        private String url;//请求参数串
        private OnLoadDataListener listener;

        /**
         * 实例化对象
         *
         */
        public ParseData(Context context,String url,OnLoadDataListener listener) {
            super(PACKAGEID++);
            this.context = context;
            this.url = url;
            this.listener = listener;
        }

        @Override
        public Bundle sendRequest() {
            Bundle b = new Bundle();
            b.putString("url", this.url);
            b.putBoolean("isShowProgressBar", false);//默认关闭
            return b;
        }

        @Override
        public boolean netParse(TCRS[] tcrs, Bundle bundle) {

            if (bundle == null) {
                Logs.e("程序出现异常");
                ToastUtils.showLong(context,"程序出现异常");
                return false;
            }

            if (tcrs != null && !tcrs[0].IsError()) {
                listener.onSuccess(tcrs);//结果返回成功
            } else {
                if (tcrs == null) {
                    Logs.e("结果为空！");
                    ToastUtils.showShort(context,"结果为空！");
                    listener.onFailure(1100,"结果为空！");
                } else {
                    Logs.e(tcrs[0].getErrMsg());
                    ToastUtils.showShort(context,tcrs[0].getErrMsg());
                    listener.onFailure(1101,tcrs[0].getErrMsg());
                }
            }

            return false;
        }

        @Override
        public boolean netError(String msg) {
            Logs.e("=====netError=======" + msg);
            ToastUtils.showShort(context,msg);

            listener.onFailure(1102, msg);

            return true;
        }

        @Override
        public void netNotifier() {

        }
    }

    public interface OnLoadDataListener {
        void onSuccess(TCRS[] tcrs);//成功

        /*
        *错误类型
        * 1100，tcrs结果集为空
        * 1101,tcrs结果集错误
        * 1102,网络请求超时
        * 1103，请求回来的通知
        */
        void onFailure(int errorId, String msg);//失败
    }

}

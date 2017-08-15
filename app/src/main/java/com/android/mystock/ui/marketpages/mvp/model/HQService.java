package com.android.mystock.ui.marketpages.mvp.model;

import android.content.Context;
import android.os.Bundle;

import com.android.mystock.common.log.Logs;
import com.android.mystock.common.utils.ToastUtils;
import com.android.mystock.net.enoservice.NetClientMessage;
import com.android.mystock.net.enoservice.NetHttpWork;
import com.android.mystock.net.enoservice.NetWorkInstance;
import com.eno.base.utils.TCRS;

/**
 * 数据请求
 */
public class HQService {

    private static int PACKAGEID = 0;//packageID自增字段，每个请求分配一个独立的标识
    private static final String FIELD = "1|2|3|4|6|7|8|11|16|17|18|20|21|27|96";// 行情列表正常请求的字段

    /*
    自选请求
     */
    public static void sendRequestOptional(Context context,String params,OnLoadDataListener listener) {

        StringBuffer sbf = new StringBuffer();
        sbf.append("tc_mfuncno=31000&");
        sbf.append("tc_sfuncno=4&"+params);

        NetHttpWork netWork = NetWorkInstance.getInstance().getNetHttpWork();
        netWork.sendRequest(NetHttpWork.COMPRESSION,NetHttpWork.DEFAULT,new ParseData(context,sbf.toString(),listener), context,null,null);

    }

    /*
    请求行情沪深排序数据
     */
    public static void sendRequestSort(Context context,String params,OnLoadDataListener listener) {

        StringBuffer sbf = new StringBuffer();
        sbf.append("tc_mfuncno=191&tc_sfuncno=3&");
        sbf.append("field=" + FIELD + "&");
        sbf.append("market=1|2&class=100|110&offset=0&count=10&"+params);

        NetHttpWork netWork = NetWorkInstance.getInstance().getNetHttpWork();
        netWork.sendRequest(NetHttpWork.COMPRESSION,NetHttpWork.DEFAULT,new ParseData(context,sbf.toString(),listener), context,null,null);

    }

    /*
        分笔明细数据
     */
    public static void sendRequestDetails(Context context,String params,OnLoadDataListener listener) {

        StringBuffer sbf = new StringBuffer();
        sbf.append("tc_mfuncno=31000&tc_sfuncno=9&"+params);

        NetHttpWork netWork = NetWorkInstance.getInstance().getNetHttpWork();
        netWork.sendRequest(NetHttpWork.COMPRESSION,NetHttpWork.DEFAULT,new ParseData(context,sbf.toString(),listener), context,null,null);

    }

    /*
        分时数据
     */
    public static void sendRequestFs(Context context,String params,OnLoadDataListener listener) {

        StringBuffer sbf = new StringBuffer();
        sbf.append("tc_mfuncno=31000&tc_sfuncno=1&"+params);

        NetHttpWork netWork = NetWorkInstance.getInstance().getNetHttpWork();
        netWork.sendRequest(NetHttpWork.COMPRESSION,NetHttpWork.DEFAULT,new ParseData(context,sbf.toString(),listener), context,null,null);

    }

    /*
        5日分时数据
     */
    public static void sendRequest5Fs(Context context,String params,OnLoadDataListener listener) {

        StringBuffer sbf = new StringBuffer();
        sbf.append("tc_mfuncno=31000&tc_sfuncno=11&"+params);

        NetHttpWork netWork = NetWorkInstance.getInstance().getNetHttpWork();
        netWork.sendRequest(NetHttpWork.COMPRESSION,NetHttpWork.DEFAULT,new ParseData(context,sbf.toString(),listener), context,null,null);

    }
    /*
        K线数据
     */
    public static void sendRequestKx(Context context,String params,OnLoadDataListener listener) {

        StringBuffer sbf = new StringBuffer();
        sbf.append("tc_mfuncno=31000&tc_sfuncno=2&"+params);

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

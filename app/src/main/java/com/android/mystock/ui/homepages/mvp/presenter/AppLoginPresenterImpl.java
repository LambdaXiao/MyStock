package com.android.mystock.ui.homepages.mvp.presenter;

import android.content.Context;
import android.util.Log;

import com.android.mystock.common.log.Logs;
import com.android.mystock.net.enoservice.NetHttpWork;
import com.android.mystock.ui.homepages.mvp.model.LoginService;
import com.android.mystock.ui.homepages.mvp.view.I_View_Base;
import com.android.mystock.ui.homepages.mvp.view.I_View_Login;
import com.eno.base.utils.TCRS;

/**
 * 开启App的业务处理，请求session
 */
public class AppLoginPresenterImpl implements I_Presenter {

    public Context context;
    private I_View_Login p_login;

    /**
     * 构造函数
     *
     * @param context
     */
    public AppLoginPresenterImpl(Context context) {
        this.context = context;
    }

    @Override
    public boolean sendRequest(I_View_Base presenter) {

        if (presenter instanceof I_View_Login) {
            p_login = (I_View_Login) presenter;

            //第一个请求取session的握手请求
            LoginService.sendRequestLogin(context, new LoginService.OnLoadDataListener() {
                @Override
                public void onSuccess(TCRS[] tcrs) {
                    parseData(tcrs);
                }

                @Override
                public void onFailure(int errorId, String msg) {

                }
            });

            Log.e("tag", "--------------请求session：100.1---------------------");
        }
        return false;
    }

    /*
    解析100.1请求数据
     */
    public void parseData(TCRS[] tcrs) {

        tcrs[0].moveFirst();

        String session = tcrs[0].toString("session");
        NetHttpWork.setAppSession(session);

        Logs.e("握手成功=" + session);

        if (tcrs.length > 1 && !tcrs[1].IsError()) {
            /**
             * 如果客户端没有取到s_ac 和 imei 后台会生成一个
             * 取都后台生成 s_ac 和imei
             */
//            SPUtils.put(context, Consts.IMEI, tcrs[1].toString("imei"));
//            SPUtils.put(context, Consts.S_AC, tcrs[1].toString("s_ac"));

        }
        /**
         * 启动推送服务
         */
//                Intent intentService=new Intent();
//                intentService.setClass(oThis, PushService.class);
//                intentService.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
//                startService(intentService);

//        StockManage stockManage = new StockManage(context);
//        stockManage.synchronizationStock();

        p_login.onResponse();

    }
}

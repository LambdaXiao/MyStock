package com.android.mystock.net.enoservice;


import com.android.mystock.common.log.Logs;
import com.android.mystock.data.consts.Consts;

/**
 * 获取网络通信对象
 */
public class NetWorkInstance {

    private static NetWorkInstance netWorkInstance = null;
    private  NetHttpWork netWork;//网络通信工作对象


    private NetWorkInstance(){}
    //单例模式
    public static NetWorkInstance getInstance(){
        if(netWorkInstance ==null){
            netWorkInstance =new NetWorkInstance();
        }
        return netWorkInstance;
    }

    /**
     * 获取数据接口对象
     *
     * @return
     */
    public  NetHttpWork getNetHttpWork() {

        if (netWork == null) {
            netWork = new NetHttpWork(Consts.defaultIp, Consts.port);
        }
        Logs.e("服务器地址=" + Consts.defaultIp + ":" + Consts.port);
        netWork.setAddr(Consts.defaultIp);
        netWork.setPort(Consts.port);
        netWork.setParameter(getFixedUrl());//固定参数

        return netWork;
    }

    /**
     * 需要固定提交的后台参数（每个请求都需要带上）
     *
     * @return
     */
    public  String getFixedUrl() {
        String fixed = "tc_service=" + Consts.LUA_NUMBE +
                "&tc_isunicode=1" +
                "&s_os=" + android.os.Build.VERSION.RELEASE +
                "&TC_SESSION={" + NetHttpWork.getAppSession() + "}";
        return fixed;
    }

}

package com.android.mystock.net.enoservice;

import android.app.Application;
import android.content.Context;

import com.android.mystock.data.Setting;
import com.eno.base.enocoder.Hex;
import com.eno.base.enocoder.RSA;
import com.eno.net.channel.ENO_Channel;
import com.eno.net.enotc.ENO_buildKey;

import java.io.IOException;
import java.io.InputStream;

/**
 * 网络工作抽象基类
 */
public abstract class ABSNetWork {
    protected String addr = "";//IP地址
    protected int port = 0;//端口
    protected Context context;//Android上下文对象
    protected ENO_Channel channel;//连接通道
    protected String fixedParameter = "";// 固定参数
    protected static Application application;  //APP全局对象
    private static String loginUrl;//全局登录请求串 当用户session 失效时需要用loginUrl重新登录
    private static String appSession;//全局请求的sesson

    public static final int SESSION_TIMEOUT = 100;//session超时
    public static final int ENCRYPTION = 36;//加密
    public static final int COMPRESSION = 18;//压缩
    public static final int DEFAULT = 0;//默认为0 不加密或者不压缩

    /**
     * 网络请求前必须先初始化
     *
     * @param application
     */
    public static void init(Application application) {
        ABSNetWork.application = application;
    }

    /**
     * 或全局APP对象
     *
     * @return
     */
    public static String getAppSession() {
        return appSession;
    }

    /**
     * 设置appSession
     *
     * @param appSession
     */
    public static void setAppSession(String appSession) {
        ABSNetWork.appSession = appSession;
        if (ABSNetWork.application != null) {
            Setting.setSession(ABSNetWork.application, appSession);
        }
    }

    /**
     * 获取登录请求串
     *
     * @return
     */
    public static String getLoginUrl() {
        return loginUrl;
    }


    /**
     * 设置登录请求串
     *
     * @param loginUrl
     */
    public static void setLoginUrl(String loginUrl) {
        ABSNetWork.loginUrl = loginUrl;
    }


    /**
     * 读取公钥文件
     *
     * @return
     */
    public static String getKey(Context context) {
        InputStream in = null;
        try {
            in = context.getAssets().open("pubkey.bin");
            //in = context.getAssets().open("publickey.dat");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String strKey = ENO_buildKey.initKey();

        RSA enc = new RSA();// 用RSA加密
        if (enc.loadPublicKey(in)) {
            byte bReq[] = strKey.getBytes();
            byte[] bEncryData = enc.encode(bReq);
            strKey = "&userKey=" + Hex.encode(bEncryData);
        }
        return strKey;
    }

    /**
     * 获取IP地址
     *
     * @return
     */
    public String getAddr() {
        return addr;
    }

    /**
     * 设置地址
     *
     * @param addr
     */
    public void setAddr(String addr) {
        this.addr = addr;
    }

    /**
     * 获取端口地址
     *
     * @return
     */
    public int getPort() {
        return port;
    }

    /**
     * 设置端口
     *
     * @param port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * 设置一些固定的参数
     *
     * @param value
     */
    public void setParameter(String value) {
        fixedParameter = value;
    }

}

package com.android.mystock.net.enoservice;

import android.os.Bundle;

import com.eno.base.utils.TCRS;

/**
 * 网络推送模式通信接口封装
 */
public interface NetClientPushMessageible {
    public boolean netParse(TCRS tcrs[], Bundle bundle);//监听数据返回

    public boolean netError(String msg);//请求发生错误

    public void netNotifier();//发送请求后成功或失败都会执行

}
package com.android.mystock.net.enoservice;

import android.os.Bundle;

import com.eno.base.utils.TCRS;


/**
 * 网络http短连接的方式通信接口封装
 */
public interface NetClientMessageible {
    public Bundle sendRequest();//发送请求

    public boolean netParse(TCRS tcrs[], Bundle bundle);//监听请求

    public boolean netError(String msg);//请求发生错误

    public void netNotifier();//只要发生请求

}
package com.android.mystock.ui.homepages.mvp.view;

/**
 * 所有业务请求处理
 * 证券交易请求接口基类
 * 用于连接P层与V层之间的联系做为中介作用
 * 作为他们的基类接口：主要的作用是避免多个请求要实现多个sendRequest方法
 * 用一个基类就只要实现一个sendRequest就可以，只要在这个方法里做一个判断就可以
 * 执行不同的功能
 */
public interface I_View_Base {
    /*
        *错误类型
        * 1100，tcrs结果集为空
        * 1101,tcrs结果集错误
        * 1102,网络请求超时
        * 1103，请求回来的通知
        */
    void errorMessage(int errorId, String error);
}

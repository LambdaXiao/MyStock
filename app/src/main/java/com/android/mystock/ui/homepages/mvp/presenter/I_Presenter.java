package com.android.mystock.ui.homepages.mvp.presenter;


import com.android.mystock.ui.homepages.mvp.view.I_View_Base;

/**
 * presenters的调用接口
 * I代表接口
 * P(MVP)中的 Presenters
 */
public interface I_Presenter{
    /**
     * 调用P类发送请求:获取数据
     * @param presenter
     */
    boolean sendRequest(I_View_Base presenter);//发送接口请求

}

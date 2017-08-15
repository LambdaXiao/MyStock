package com.android.mystock.ui.marketpages.mvp.view;


import com.android.mystock.ui.homepages.mvp.view.I_View_Base;
import com.android.mystock.ui.marketpages.bean.BeanHqMaket;


/**
 * 分时k线 页面上面的基础数据
 */
public interface I_View_FSKX extends I_View_Base {

    public String getStockCode();

    public void onResponseStock(BeanHqMaket stock);

}

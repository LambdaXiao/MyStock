package com.android.mystock.ui.marketpages.mvp.view;


import com.android.mystock.ui.homepages.mvp.view.I_View_Base;
import com.mystockchart_lib.charting.mychart.bean.DataParse;

/**
 * 分时
 */
public interface I_View_FS extends I_View_Base {

    public String getStockCode();

    public void onResponseData(DataParse mData);

}

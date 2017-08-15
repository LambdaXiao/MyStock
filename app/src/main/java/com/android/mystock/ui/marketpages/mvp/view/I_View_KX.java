package com.android.mystock.ui.marketpages.mvp.view;


import com.android.mystock.ui.homepages.mvp.view.I_View_Base;
import com.mystockchart_lib.charting.mychart.bean.DataParse;

/**
 * k线
 */
public interface I_View_KX extends I_View_Base {

    public String getStockCode();

    public int getKLinePeriod();//周期

    public int getKLineIndex();//指标

    public int getKLineRehabilitation();//复权

    public void onResponseData(DataParse mData);
}

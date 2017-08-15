package com.android.mystock.ui.marketpages.mvp.view;


import com.android.mystock.ui.homepages.mvp.view.I_View_Base;
import com.android.mystock.ui.marketpages.bean.BeanHqMaket;

import java.util.ArrayList;

/**
 * 自选股列表接口
 */
public interface I_View_MyStockList extends I_View_Base {

    public String getFiled();

    public String getStockCodes();

    public void onResponse(ArrayList<BeanHqMaket> list);

}

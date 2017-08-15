package com.android.mystock.ui.marketpages.mvp.view;


import com.android.mystock.ui.homepages.mvp.view.I_View_Base;
import com.android.mystock.ui.marketpages.bean.BeanHqMaket;

import java.util.ArrayList;

/**
 * 大盘指数
 */
public interface I_View_ZS extends I_View_Base {

    public void onResponse(ArrayList<BeanHqMaket> list1, ArrayList<BeanHqMaket> list2, ArrayList<BeanHqMaket> list3);

}

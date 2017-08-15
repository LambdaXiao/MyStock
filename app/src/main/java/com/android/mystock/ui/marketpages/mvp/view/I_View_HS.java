package com.android.mystock.ui.marketpages.mvp.view;


import com.android.mystock.ui.homepages.mvp.view.I_View_Base;
import com.android.mystock.ui.marketpages.bean.BeanHqMaket;

import java.util.ArrayList;

/**
 * 沪深
 */
public interface I_View_HS extends I_View_Base {
    //涨幅榜可见性
    public boolean getVisibilityZfb();
    //跌幅榜可见性
    public boolean getVisibilityDfb();
    //换手率榜可见性
    public boolean getVisibilityHsl();
    //成交额榜可见性
    public boolean getVisibilityCje();

    public void onResponseZs(ArrayList<BeanHqMaket> list);//指数
    public void onResponseZfb(ArrayList<BeanHqMaket> list);//涨幅榜
    public void onResponseDfb(ArrayList<BeanHqMaket> list);//跌幅榜
    public void onResponseHsl(ArrayList<BeanHqMaket> list);//换手率榜
    public void onResponseCje(ArrayList<BeanHqMaket> list);//成交额榜
}

package com.android.mystock.ui.marketpages.mvp.view;


import com.android.mystock.ui.homepages.mvp.view.I_View_Base;
import com.android.mystock.ui.marketpages.bean.BeanCJMX;

import java.util.List;

/**
 * 成交明细
 */
public interface I_View_CJMX extends I_View_Base {

    /**
     * 股票代码
     *
     * @return
     */
    public String getCode();

    /**
     * 返还方向
     *
     * @return
     */
    public int direct();

    /**
     * 参考时间
     *
     * @return
     */
    public String getTime();

    /**
     * 正常返回的
     *
     * @param list
     */
    public void onResponse(List<BeanCJMX> list);

    /**
     * 返回更多
     *
     * @param list
     */
    public void onResponseMore(List<BeanCJMX> list);

}

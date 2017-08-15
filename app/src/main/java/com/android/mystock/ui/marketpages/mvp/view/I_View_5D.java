package com.android.mystock.ui.marketpages.mvp.view;


import com.android.mystock.ui.homepages.mvp.view.I_View_Base;

/**
 * 五档行情
 */
public interface I_View_5D extends I_View_Base {

    public String getStockCode();

    public String getExclude();

    /**
     * 五档行情
     *
     * @param buy1
     * @param buy2
     * @param buy3
     * @param buy4
     * @param buy5
     * @param sell1
     * @param sell2
     * @param sell3
     * @param sell4
     * @param sell5
     */
    public void onResponse5D(String buy1, String buy2, String buy3, String buy4, String buy5, String sell1, String sell2, String sell3, String sell4, String sell5);

    /**
     * 量
     *
     * @param buy1
     * @param buy2
     * @param buy3
     * @param buy4
     * @param buy5
     * @param sell1
     * @param sell2
     * @param sell3
     * @param sell4
     * @param sell5
     */
    public void onResponse5DL(String buy1, String buy2, String buy3, String buy4, String buy5, String sell1, String sell2, String sell3, String sell4, String sell5);

    /**
     * 回调市场
     *
     * @param market 交易市场
     * @param name   股票名称
     * @param type   商品类型(A,B股)
     */
    public void onResponse(String market, String name, String type);

    /**
     * 回调 昨收和现价 价格
     *
     * @param zs
     * @param xj
     */
    public void onResponseZSJK(String zs, String xj);

    /**
     * @param dt 跌停
     * @param zt 涨停
     */
    public void onResponseDTZT(String dt, String zt);
}


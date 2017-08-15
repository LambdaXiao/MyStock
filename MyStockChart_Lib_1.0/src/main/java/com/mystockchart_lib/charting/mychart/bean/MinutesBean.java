package com.mystockchart_lib.charting.mychart.bean;

/**
 * 分时线数据结构
 */
public class MinutesBean {
    public String time;//时间
    public float cjprice;//最新价
    public float cjnum;//成交量
    public float avprice = Float.NaN;//均价
    public float per;//涨跌幅
    public float cha;//涨跌额
    public float cjmoney;//成交额
    public int color = 0xff000000;//颜色
}

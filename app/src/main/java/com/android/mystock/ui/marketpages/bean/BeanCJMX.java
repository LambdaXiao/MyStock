package com.android.mystock.ui.marketpages.bean;

/**
 * 成交明细
 */
public class BeanCJMX {

    private  String time;//时间

    private String timeValue;//时间

    private  String price;//价格

    private  String cjl;//成交量

    private  int  state;//是买还是卖

    public String getTime() {
        return time;
    }


    public String getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(String timeValue) {
        this.timeValue = timeValue;
    }


    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCjl() {
        return cjl;
    }

    public void setCjl(String cjl) {
        this.cjl = cjl;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}

package com.android.mystock.ui.marketpages.sort;


import com.android.mystock.common.log.Logs;

/**
 * 排序bean
 */
public class BeanSort {
    private  int field;
    private int order;


    public BeanSort(int field,int order)
    {

        this.setField(field);
        this.setOrder(order);

    }

    public int getField() {
        return field;
    }

    public void setField(int field) {
        this.field = field;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        Logs.e("who"+order);

        this.order = order;
    }



}

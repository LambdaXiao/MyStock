package com.android.mystock.ui.marketpages.sort;


import com.android.mystock.common.log.Logs;
import com.android.mystock.ui.marketpages.bean.BeanHqMaket;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Comparator;

/**
 * 自选股行情列表排序
 */
public class ComparatorHQList implements Comparator<BeanHqMaket> {

    public final static int ASC = 1;//从小到大
    public final static int DESC = -1; //从大到小
    public final static int GONE = 0; //不排序
    BeanSort beanSort;

    public ComparatorHQList(BeanSort beanSort) {
        super();
        setFieldOrder(beanSort);
    }

    public void setFieldOrder(BeanSort beanSort) {
        this.beanSort = beanSort;
    }

    @Override
    public int compare(BeanHqMaket hq1, BeanHqMaket hq2) {

        int result = 0;
        if (beanSort.getOrder() == GONE) {

            int order1 = hq1.getOrder();
            int order2 = hq2.getOrder();
            Logs.e(order1 + "");
            if (order1 > order2) {
                result = -1;
            }
            if (order1 < order2) {
                result = 1;
            }
            return result;
        }
        switch (beanSort.getField()) {

            case 0:
                //关于Collator。
                Collator collator = Collator.getInstance();//点击查看中文api详解
                CollationKey key1 = collator.getCollationKey(hq1.getStockName());//要想不区分大小写进行比较用o1.toString().toLowerCase()
                CollationKey key2 = collator.getCollationKey(hq2.getStockName());
                result = key1.compareTo(key2);//返回的分别为1,0,-1 分别代表大于，等于，小于。要想按照字母降序排序的话 加个“-”号
                break;
            case 1://最新价
                float m1 = hq1.getValue_newPrice();
                float m2 = hq2.getValue_newPrice();
                if (m1 > m2) {
                    result = -1;
                }
                if (m1 < m2) {
                    result = 1;
                }
                break;

            case 2://涨跌幅
                float zdf1 = hq1.getValue_zdf();
                float zdf2 = hq2.getValue_zdf();
                if (zdf1 > zdf2) {
                    result = -1;
                }
                if (zdf1 < zdf2) {
                    result = 1;
                }
                break;

            case 3://涨跌额
                float zde1 = hq1.getValue_zdf();
                float zde2 = hq2.getValue_zdf();
                if (zde1 > zde2) {
                    result = -1;
                }
                if (zde1 < zde1) {
                    result = 1;
                }
                break;
            case 4://总市值
                long zsz1 = hq1.getValue_zsz();
                long zsz2 = hq2.getValue_zsz();
                if (zsz1 > zsz2) {
                    result = -1;
                }
                if (zsz1 < zsz2) {
                    result = 1;
                }
                break;
        }

        if (this.beanSort.getOrder() == ComparatorHQList.ASC) {
            // Logs.e("asc ");
            result = result * -1;
        } else if (this.beanSort.getOrder() == ComparatorHQList.DESC) {
            // Logs.e("DESC ");
        } else {
            // Logs.e("默认排序 ");
            result = 0;
        }
        return result;
    }

}



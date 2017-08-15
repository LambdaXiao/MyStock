package com.android.mystock.ui.marketpages;


import com.android.mystock.data.consts.ENOStyle;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * 行情工具方法
 */
public class HqUtils {
 private static HashMap<Integer ,DecimalFormat> decimalFormatsPools=new HashMap<Integer,DecimalFormat>();//把已经格式化的对象保存起来

    /**
     * 根据后台返回的精度位数生成格式化模板
     * @param precision
     * @return
     */
    public static DecimalFormat precisionToString(int precision) {

        DecimalFormat decimalFormat=decimalFormatsPools.get(precision);

        if(decimalFormat==null) {
            String formatStr = "0.";
            for (int i = 0; i < precision; i++) {
                formatStr = formatStr + "0";
            }
            decimalFormat = new DecimalFormat(formatStr);
            decimalFormatsPools.put(precision, decimalFormat);
        }
        return decimalFormat;

    }

    /*
    行情定时刷新的判断，周一到周五每天9:15-11:31，12:59-15:01这些时间段刷新
     */
    public static boolean isMarketOpen() {
        Calendar cal = Calendar.getInstance();
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        // 周一到周五
        if (w >= 1 && w <= 5) {
            int now = hour * 60 + minute;
            int timeAM_start = 9 * 60 + 15; // 9:15
            int timeAM_end = 11 * 60 + 31; // 11:31
            int timePM_start = 12 * 60 + 59; // 12:59
            int timePM_end = 15 * 60 + 1; // 15:01
            if (now >= timeAM_start && now <= timeAM_end) {
                return true;
            }
            if (now >= timePM_start && now <= timePM_end) {
                return true;
            }
        }
        return false;
    }
/*
格式化行情涨跌平颜色
 */
    public static String getHQColor(double sV, double value) {

        if (value > sV) {
            return ENOStyle.hq_upstr;
        } else if (value < sV) {
            return ENOStyle.hq_downstr;
        }else{
            return ENOStyle.hq_flatstr;
        }

    }

    public static int getHQColor2(double sV, double value) {

        if (value > sV) {
            return ENOStyle.hq_up;
        } else if (value < sV) {
            return ENOStyle.hq_down;
        }else{
            return ENOStyle.hq_flat;
        }

    }

    public static String getHQColor(String sV, String value) {
        if (sV == null || value == null || sV.equals("") || value.equals("") || sV.equals("--") || value.equals("--")) {
            return ENOStyle.hq_flatstr;
        }
        double svF = Double.valueOf(sV);
        double valueF = Double.valueOf(value);
        if (valueF > svF) {
            return ENOStyle.hq_upstr;
        } else if (valueF < svF) {
            return ENOStyle.hq_downstr;
        }else{
            return ENOStyle.hq_flatstr;
        }

    }

    public static int getHQColor2(String sV, String value) {
        if (sV == null || value == null || sV.equals("") || value.equals("") || sV.equals("--") || value.equals("--")) {
            return ENOStyle.hq_flat;
        }
        double svF = Double.valueOf(sV);
        double valueF = Double.valueOf(value);
        if (valueF > svF) {
            return ENOStyle.hq_up;
        } else if (valueF < svF) {
            return ENOStyle.hq_down;
        }else{
            return ENOStyle.hq_flat;
        }

    }

}
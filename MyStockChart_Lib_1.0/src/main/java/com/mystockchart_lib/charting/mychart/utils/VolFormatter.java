package com.mystockchart_lib.charting.mychart.utils;


import com.mystockchart_lib.charting.components.YAxis;
import com.mystockchart_lib.charting.formatter.YAxisValueFormatter;

import java.text.DecimalFormat;

/**
 * 坐标label格式化
 */
public class VolFormatter implements YAxisValueFormatter {

    private final int unit;
    private DecimalFormat mFormat;
    private String u;
    public VolFormatter(int unit) {
        if (unit == 1) {
            mFormat = new DecimalFormat("#0");
        } else {
            mFormat = new DecimalFormat("#0.00");
        }
        this.unit = unit;
        this.u= MyUtils.getVolUnit(unit);
    }


    @Override
    public String getFormattedValue(float value, YAxis yAxis) {
        value = value / unit;
        if(value==0){
            return u;
        }
        return mFormat.format(value);
    }
}

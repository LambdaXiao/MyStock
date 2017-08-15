package com.mystockchart_lib.charting.mychart;

import android.util.SparseArray;

import com.mystockchart_lib.charting.components.XAxis;


/**
 * 自定义X轴坐标
 */
public class MyXAxis extends XAxis {
    private SparseArray<String> labels;
    public SparseArray<String> getXLabels() {
        return labels;
    }
    public void setXLabels(SparseArray<String> labels) {
        this.labels = labels;
    }
}

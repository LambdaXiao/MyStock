package com.mystockchart_lib.charting.formatter;


import com.mystockchart_lib.charting.components.YAxis;

/**
 * Custom formatter interface that allows formatting of
 * YAxis labels before they are being drawn.
 */
public interface YAxisValueFormatter {

    /**
     * Called when a value from the YAxis is formatted
     * before being drawn. For performance reasons, avoid excessive calculations
     * and memory allocations inside this method.
     *
     * @param value the YAxis value to be formatted
     * @param yAxis the YAxis object the value belongs to
     * @return
     */
    String getFormattedValue(float value, YAxis yAxis);
}

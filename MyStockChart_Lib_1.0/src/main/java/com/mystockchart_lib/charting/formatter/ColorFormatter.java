package com.mystockchart_lib.charting.formatter;


import com.mystockchart_lib.charting.data.Entry;

/**
 * Interface that can be used to return a customized color instead of setting
 * colors via the setColor(...) method of the DataSet.
 *
 */
public interface ColorFormatter {

    int getColor(Entry e, int index);
}
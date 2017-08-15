package com.mystockchart_lib.charting.interfaces.datasets;


import com.mystockchart_lib.charting.data.Entry;


public interface IBarLineScatterCandleBubbleDataSet<T extends Entry> extends IDataSet<T> {

    /**
     * Returns the color that is used for drawing the highlight indicators.
     *
     * @return
     */
    int getHighLightColor();
}

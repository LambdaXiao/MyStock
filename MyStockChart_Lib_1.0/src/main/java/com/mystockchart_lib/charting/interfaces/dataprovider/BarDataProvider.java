package com.mystockchart_lib.charting.interfaces.dataprovider;


import com.mystockchart_lib.charting.data.BarData;

public interface BarDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BarData getBarData();
    boolean isDrawBarShadowEnabled();
    boolean isDrawValueAboveBarEnabled();
    boolean isDrawHighlightArrowEnabled();
}

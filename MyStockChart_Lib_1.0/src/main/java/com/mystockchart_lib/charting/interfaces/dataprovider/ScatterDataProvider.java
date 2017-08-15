package com.mystockchart_lib.charting.interfaces.dataprovider;


import com.mystockchart_lib.charting.data.ScatterData;

public interface ScatterDataProvider extends BarLineScatterCandleBubbleDataProvider {

    ScatterData getScatterData();
}

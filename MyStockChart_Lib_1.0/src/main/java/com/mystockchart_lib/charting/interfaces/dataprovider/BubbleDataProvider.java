package com.mystockchart_lib.charting.interfaces.dataprovider;


import com.mystockchart_lib.charting.data.BubbleData;

public interface BubbleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BubbleData getBubbleData();
}

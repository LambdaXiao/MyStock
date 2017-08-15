package com.mystockchart_lib.charting.interfaces.dataprovider;


import com.mystockchart_lib.charting.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    CandleData getCandleData();
}

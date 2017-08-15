package com.mystockchart_lib.charting.interfaces.dataprovider;


import com.mystockchart_lib.charting.components.YAxis;
import com.mystockchart_lib.charting.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}

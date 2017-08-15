package com.mystockchart_lib.charting.interfaces.dataprovider;


import com.mystockchart_lib.charting.components.YAxis;
import com.mystockchart_lib.charting.data.BarLineScatterCandleBubbleData;
import com.mystockchart_lib.charting.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(YAxis.AxisDependency axis);
    int getMaxVisibleCount();
    boolean isInverted(YAxis.AxisDependency axis);
    
    int getLowestVisibleXIndex();
    int getHighestVisibleXIndex();

    BarLineScatterCandleBubbleData getData();
}

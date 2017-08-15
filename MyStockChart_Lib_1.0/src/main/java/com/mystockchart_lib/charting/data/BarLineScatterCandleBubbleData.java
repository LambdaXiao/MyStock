
package com.mystockchart_lib.charting.data;


import com.mystockchart_lib.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;

import java.util.List;

/**
 * Baseclass for all Line, Bar, Scatter, Candle and Bubble data.
 *
 */
public abstract class BarLineScatterCandleBubbleData<T extends IBarLineScatterCandleBubbleDataSet<? extends Entry>>
        extends ChartData<T> {
    
    public BarLineScatterCandleBubbleData() {
        super();
    }
    
    public BarLineScatterCandleBubbleData(List<String> xVals) {
        super(xVals);
    }
    
    public BarLineScatterCandleBubbleData(String[] xVals) {
        super(xVals);
    }

    public BarLineScatterCandleBubbleData(List<String> xVals, List<T> sets) {
        super(xVals, sets);
    }

    public BarLineScatterCandleBubbleData(String[] xVals, List<T> sets) {
        super(xVals, sets);
    }
}

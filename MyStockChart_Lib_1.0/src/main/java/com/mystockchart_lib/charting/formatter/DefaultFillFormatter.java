package com.mystockchart_lib.charting.formatter;


import com.mystockchart_lib.charting.data.LineData;
import com.mystockchart_lib.charting.interfaces.dataprovider.LineDataProvider;
import com.mystockchart_lib.charting.interfaces.datasets.ILineDataSet;

/**
 * Default formatter that calculates the position of the filled line.
 *
 */
public class DefaultFillFormatter implements FillFormatter {

    @Override
    public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {

        float fillMin;
        float chartMaxY = dataProvider.getYChartMax();
        float chartMinY = dataProvider.getYChartMin();

        LineData data = dataProvider.getLineData();

        if (dataSet.getYMax() > 0 && dataSet.getYMin() < 0) {
            fillMin = 0f;
        } else {

            float max, min;

            if (data.getYMax() > 0)
                max = 0f;
            else
                max = chartMaxY;
            if (data.getYMin() < 0)
                min = 0f;
            else
                min = chartMinY;

            fillMin = dataSet.getYMin() >= 0 ? min : max;
        }

        return fillMin;
    }
}
package com.mystockchart_lib.charting.interfaces.datasets;


import com.mystockchart_lib.charting.data.Entry;


public interface IRadarDataSet extends ILineRadarDataSet<Entry> {

    /// flag indicating whether highlight circle should be drawn or not
    boolean isDrawHighlightCircleEnabled();

    /// Sets whether highlight circle should be drawn or not
    void setDrawHighlightCircleEnabled(boolean enabled);

    int getHighlightCircleFillColor();

    /// The stroke color for highlight circle.
    /// If Utils.COLOR_NONE, the color of the dataset is taken.
    int getHighlightCircleStrokeColor();

    int getHighlightCircleStrokeAlpha();

    float getHighlightCircleInnerRadius();

    float getHighlightCircleOuterRadius();

    float getHighlightCircleStrokeWidth();

}

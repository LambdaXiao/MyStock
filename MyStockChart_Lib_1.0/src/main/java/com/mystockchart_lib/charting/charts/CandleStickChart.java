
package com.mystockchart_lib.charting.charts;

import android.content.Context;
import android.util.AttributeSet;

import com.mystockchart_lib.charting.data.CandleData;
import com.mystockchart_lib.charting.interfaces.dataprovider.CandleDataProvider;
import com.mystockchart_lib.charting.renderer.CandleStickChartRenderer;


/**
 * Financial chart type that draws candle-sticks (OHCL chart).
 *
 */
public class CandleStickChart extends BarLineChartBase<CandleData> implements CandleDataProvider {

    public CandleStickChart(Context context) {
        super(context);
    }

    public CandleStickChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CandleStickChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();




        mRenderer = new CandleStickChartRenderer(this, mAnimator, mViewPortHandler);
        mXAxis.mAxisMinimum = -0.5f;
    }

    @Override
    protected void calcMinMax() {
        super.calcMinMax();

        mXAxis.mAxisMaximum += 0.5f;
        mXAxis.mAxisRange = Math.abs(mXAxis.mAxisMaximum - mXAxis.mAxisMinimum);
    }

    @Override
    public CandleData getCandleData() {
        return mData;
    }
}

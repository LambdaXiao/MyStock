package com.mystockchart_lib.charting.mychart;

import android.graphics.Canvas;
import android.graphics.PointF;

import com.mystockchart_lib.charting.charts.BarLineChartBase;
import com.mystockchart_lib.charting.renderer.XAxisRenderer;
import com.mystockchart_lib.charting.utils.Transformer;
import com.mystockchart_lib.charting.utils.Utils;
import com.mystockchart_lib.charting.utils.ViewPortHandler;


/**
 * 重写x轴labels
 */
public class MyXAxisRenderer2 extends XAxisRenderer {
    private final BarLineChartBase mChart;
    protected MyXAxis mXAxis;

    public MyXAxisRenderer2(ViewPortHandler viewPortHandler, MyXAxis xAxis, Transformer trans, BarLineChartBase chart) {
        super(viewPortHandler, xAxis, trans);
        mXAxis = xAxis;
        mChart = chart;
    }

    @Override
    protected void drawLabels(Canvas c, float pos, PointF anchor) {
        float[] position = new float[]{
                0f, 0f
        };
        int count = mXAxis.getXLabels().size();
        for (int i = mMinX; i <= mMaxX; i += mXAxis.mAxisLabelModulus) {

              /*获取label对应key值，也就是x轴坐标0,60,121,182,242*/
            int ix = mXAxis.getXLabels().keyAt(i);
            position[0] = ix;
            /*在图表中的x轴转为像素，方便绘制text*/
            mTrans.pointValuesToPixel(position);
            /*x轴越界*/
            if (mViewPortHandler.isInBoundsX(position[0])) {
                String label = mXAxis.getXLabels().valueAt(i);
                /*文本长度*/
                int labelWidth = Utils.calcTextWidth(mAxisLabelPaint, label);
                /*右出界*/
                if ((labelWidth / 2 + position[0]) > mChart.getViewPortHandler().contentRight()) {
                    position[0] = mChart.getViewPortHandler().contentRight() - labelWidth / 2;
                } else if ((position[0] - labelWidth / 2) < mChart.getViewPortHandler().contentLeft()) {//左出界
                    position[0] = mChart.getViewPortHandler().contentLeft() + labelWidth / 2;
                }
                c.drawText(label, position[0],
                        pos+Utils.convertPixelsToDp(mChart.getViewPortHandler().offsetBottom()*2),
                        mAxisLabelPaint);
            }

        }
    }

    /*x轴垂直线*/
    @Override
    public void renderGridLines(Canvas c) {
        if (!mXAxis.isDrawGridLinesEnabled() || !mXAxis.isEnabled())
            return;
        float[] position = new float[]{
                0f, 0f
        };

        mGridPaint.setColor(mXAxis.getGridColor());
        mGridPaint.setStrokeWidth(mXAxis.getGridLineWidth());
        mGridPaint.setPathEffect(mXAxis.getGridDashPathEffect());
        int count = mXAxis.getXLabels().size();
        if (!mChart.isScaleXEnabled()) {
            count -= 1;
        }
        for (int i = mMinX; i <= mMaxX; i += mXAxis.mAxisLabelModulus) {

            int ix = mXAxis.getXLabels().keyAt(i);
            position[0] = ix;
            mTrans.pointValuesToPixel(position);
            c.drawLine(position[0], mViewPortHandler.offsetTop(), position[0],
                    mViewPortHandler.contentBottom(), mGridPaint);
        }

    }

}

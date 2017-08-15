package com.mystockchart_lib.charting.mychart;

import android.content.Context;
import android.util.AttributeSet;

import com.mystockchart_lib.charting.charts.BarChart;
import com.mystockchart_lib.charting.components.YAxis;
import com.mystockchart_lib.charting.highlight.Highlight;


/**
 * 条形图
 */
public class MyBarChart extends BarChart {
    public MyBarChart(Context context) {
        super(context);
    }

    public MyBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyBarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        /*此处不能重新示例*/

        mAxisLeft = new MyYAxis(YAxis.AxisDependency.LEFT);
        mAxisRendererLeft = new MyYAxisRenderer(mViewPortHandler, (MyYAxis) mAxisLeft, mLeftAxisTransformer);

        mXAxis = new MyXAxis();
        mXAxisRenderer = new MyXAxisRenderer(mViewPortHandler, (MyXAxis) mXAxis, mLeftAxisTransformer,this);

        mAxisRight = new MyYAxis(YAxis.AxisDependency.RIGHT);
        mAxisRendererRight = new MyYAxisRenderer(mViewPortHandler, (MyYAxis) mAxisRight, mRightAxisTransformer);

    }
    @Override
    protected void calcModulus() {

        mXAxis.mAxisLabelModulus = 1;
    }
    /*返回转型后的左右轴*/
    @Override
    public MyYAxis getAxisLeft() {
        return (MyYAxis) super.getAxisLeft();
    }

    @Override
    public MyXAxis getXAxis() {
        return (MyXAxis)super.getXAxis();
    }


    @Override
    public MyYAxis getAxisRight() {
        return (MyYAxis) super.getAxisRight();
    }

    public void setHighlightValue(Highlight h){
        mIndicesToHighlight = new Highlight[]{
                h};
    }
}

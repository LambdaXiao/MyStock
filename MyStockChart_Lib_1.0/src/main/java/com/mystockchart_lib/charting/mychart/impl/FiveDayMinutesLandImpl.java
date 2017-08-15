package com.mystockchart_lib.charting.mychart.impl;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;

import com.mystockchart_lib.R;
import com.mystockchart_lib.charting.components.Legend;
import com.mystockchart_lib.charting.components.LimitLine;
import com.mystockchart_lib.charting.components.XAxis;
import com.mystockchart_lib.charting.components.YAxis;
import com.mystockchart_lib.charting.data.BarData;
import com.mystockchart_lib.charting.data.BarDataSet;
import com.mystockchart_lib.charting.data.BarEntry;
import com.mystockchart_lib.charting.data.Entry;
import com.mystockchart_lib.charting.data.LineData;
import com.mystockchart_lib.charting.data.LineDataSet;
import com.mystockchart_lib.charting.formatter.YAxisValueFormatter;
import com.mystockchart_lib.charting.highlight.Highlight;
import com.mystockchart_lib.charting.interfaces.datasets.ILineDataSet;
import com.mystockchart_lib.charting.listener.OnChartValueSelectedListener;
import com.mystockchart_lib.charting.mychart.MyBarChart;
import com.mystockchart_lib.charting.mychart.MyLeftMarkerView;
import com.mystockchart_lib.charting.mychart.MyLineChart;
import com.mystockchart_lib.charting.mychart.MyRightMarkerView;
import com.mystockchart_lib.charting.mychart.MyXAxis;
import com.mystockchart_lib.charting.mychart.MyYAxis;
import com.mystockchart_lib.charting.mychart.bean.DataParse;
import com.mystockchart_lib.charting.mychart.bean.MinutesBean;
import com.mystockchart_lib.charting.mychart.utils.MyUtils;
import com.mystockchart_lib.charting.mychart.utils.VolFormatter;
import com.mystockchart_lib.charting.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 横屏5日分时实现类封装，供外部调用
 */

public class FiveDayMinutesLandImpl {

    private MyXAxis xAxisLine;
    private MyYAxis axisRightLine;
    private MyYAxis axisLeftLine;

    private MyXAxis xAxisBar;
    private MyYAxis axisLeftBar;
    private MyYAxis axisRightBar;
    private SparseArray<String> stringSparseArray;
    private DataParse mData;
    private int exchid;//股票的市场
    private int mMaxDataLength = 0; //某市场下的最大采样点数
    private Context context;
    private MyLineChart lineChart;
    private MyBarChart barChart;
    private OnMinutesHighLightListener onMinutesHighLightListener;

    public FiveDayMinutesLandImpl(Context context, int exchid, MyLineChart lineChart, MyBarChart barChart) {
        this.context = context;
        this.exchid = exchid;
        this.lineChart = lineChart;
        this.barChart = barChart;

        initChart();
    }

    /**
     * 初始化画线参数
     */
    private void initChart() {
        //分时线
        lineChart.setScaleEnabled(false);//是否可以缩放
        lineChart.setDrawBorders(true);//是否画边框
        lineChart.setBorderWidth(1);//边框宽度
        lineChart.setBorderColor(ContextCompat.getColor(context, R.color.minute_grayLine));//边框颜色
        lineChart.setDescription("");// 数据描述
        Legend lineChartLegend = lineChart.getLegend();// 设置比例图标示
        lineChartLegend.setEnabled(false);//图例

        //x轴
        xAxisLine = lineChart.getXAxis();
        xAxisLine.setDrawLabels(true);//x轴时间lable
        xAxisLine.setDrawAxisLine(false);
        xAxisLine.setDrawGridLines(true);
        xAxisLine.setPosition(XAxis.XAxisPosition.BOTTOM);//x轴时间lable的位置
        xAxisLine.setGridColor(ContextCompat.getColor(context, R.color.minute_grayLine));
        xAxisLine.setAxisLineColor(ContextCompat.getColor(context, R.color.minute_grayLine));
        xAxisLine.setTextColor(ContextCompat.getColor(context, R.color.minute_zhoutext));
//        xAxisLine.setLabelsToSkip(59);

        //左y轴
        axisLeftLine = lineChart.getAxisLeft();
        axisLeftLine.setLabelCount(5, true);//标写多少个Y轴刻度值
        axisLeftLine.setDrawLabels(true);//是否显示Y轴刻度值
        axisLeftLine.setDrawGridLines(false);//画里面的网格横线
        axisLeftLine.setDrawAxisLine(false);
        axisLeftLine.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);//坐标在y轴外面
        axisLeftLine.setGridColor(ContextCompat.getColor(context, R.color.minute_grayLine));
        axisLeftLine.setTextColor(ContextCompat.getColor(context, R.color.minute_zhoutext));
        axisLeftLine.setSpaceTop(100);
        //右y轴
        axisRightLine = lineChart.getAxisRight();
        axisRightLine.setLabelCount(2, true);//标写中点上下方各多少个Y轴刻度值
        axisRightLine.setDrawLabels(true);
        axisRightLine.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);//坐标在y轴外面
        axisRightLine.setStartAtZero(true);
        axisRightLine.setDrawGridLines(false);
        axisRightLine.setDrawAxisLine(false);
        axisRightLine.setAxisLineColor(ContextCompat.getColor(context, R.color.minute_grayLine));
        axisRightLine.setTextColor(ContextCompat.getColor(context, R.color.minute_zhoutext));
        //右边y坐标的格式
        axisRightLine.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                DecimalFormat mFormat = new DecimalFormat("#0.00%");
                return mFormat.format(value);
            }
        });

        //成交量图
        barChart.setScaleEnabled(false);
        barChart.setDrawBorders(true);
        barChart.setBorderWidth(1);
        barChart.setBorderColor(ContextCompat.getColor(context, R.color.minute_grayLine));
        barChart.setDescription("");
        Legend barChartLegend = barChart.getLegend();
        barChartLegend.setEnabled(false);
        // x 轴
        xAxisBar = barChart.getXAxis();
        xAxisBar.setDrawLabels(false);
        xAxisBar.setDrawGridLines(true);//x轴网格竖线
        xAxisBar.setDrawAxisLine(false);//画横轴线
        xAxisBar.setGridColor(ContextCompat.getColor(context, R.color.minute_grayLine));
        //左 y 轴
        axisLeftBar = barChart.getAxisLeft();
        axisLeftBar.setAxisMinValue(0);
        axisLeftBar.setShowOnlyMinMax(true);
        axisLeftBar.setDrawGridLines(false);
        axisLeftBar.setDrawAxisLine(false);
        axisLeftBar.setTextColor(ContextCompat.getColor(context, R.color.minute_zhoutext));
        axisLeftBar.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);//坐标在y轴里面
        //右 y 轴
        axisRightBar = barChart.getAxisRight();
        axisRightBar.setDrawLabels(false);
        axisRightBar.setDrawGridLines(false);
        axisRightBar.setDrawAxisLine(false);
        //y轴样式
        this.axisLeftLine.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                DecimalFormat mFormat = new DecimalFormat("#0.00");
                return mFormat.format(value);
            }
        });

        //以下两个监听方法是为了让上下两个图滑动高亮线时联动
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(final Entry e, int dataSetIndex, Highlight h) {
                barChart.highlightValues(new Highlight[]{h});
                lineChart.setHighlightValue(h);
                if(onMinutesHighLightListener != null){
                    onMinutesHighLightListener.onMinutesHighLight(mData, e.getXIndex(), true);
                }
            }

            @Override
            public void onNothingSelected() {
                barChart.highlightValues(null);
                if(onMinutesHighLightListener != null) {
                    onMinutesHighLightListener.onMinutesHighLight(mData, 0, false);
                }
            }
        });
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(final Entry e, int dataSetIndex, Highlight h) {
                //  barChart.highlightValues(new Highlight[]{h});
                lineChart.setHighlightValue(new Highlight(h.getXIndex(), 0));//此函数已经返回highlightBValues的变量，并且刷新，故上面方法可以注释
                // barChart.setHighlightValue(h);
                if(onMinutesHighLightListener != null) {
                    onMinutesHighLightListener.onMinutesHighLight(mData, e.getXIndex(), true);
                }
            }

            @Override
            public void onNothingSelected() {
                lineChart.setHighlightValue(null);
                if(onMinutesHighLightListener != null) {
                    onMinutesHighLightListener.onMinutesHighLight(mData, 0, false);
                }
            }
        });

    }

    /**
     * 供外部调用来设置X轴坐标刻度
     * @param labels
     */
    public void setShowXLabels(SparseArray<String> labels) {
        stringSparseArray= labels;
    }

    /**
     * 往画线图标填充数据
     *
     * @param mData
     */
    public void setData(DataParse mData) {
        this.mData = mData;
        setMarkerView(mData);
        if(stringSparseArray != null) {
            setShowLabels(stringSparseArray);
        }

//        Logs.e("###数据总个数"+mData.getMinutesDatas().size());
        if (mData.getMinutesDatas().size() == 0) {
            lineChart.setNoDataText("暂无数据");
            barChart.setNoDataText("暂无数据");
            return;
        }
        //设置y左右两轴最大最小值
        axisLeftLine.setAxisMinValue(mData.getMin());
        axisLeftLine.setAxisMaxValue(mData.getMax());
        axisRightLine.setAxisMinValue(mData.getPercentMin());
        axisRightLine.setAxisMaxValue(mData.getPercentMax());
        /*单位*/
        String unit = MyUtils.getVolUnit(mData.getVolmax());
        int u = 1;
        if ("万手".equals(unit)) {
            u = 4;
        } else if ("亿手".equals(unit)) {
            u = 8;
        }
        /*次方*/
        axisLeftBar.setValueFormatter(new VolFormatter((int) Math.pow(10, u)));

        //基准线
        LimitLine ll = new LimitLine(0);
        ll.setLineWidth(1f);
        ll.setLineColor(ContextCompat.getColor(context, R.color.minute_jizhun));
        ll.enableDashedLine(10f, 10f, 0f);
        ll.setLineWidth(1);
        axisRightLine.addLimitLine(ll);
        axisRightLine.setBaseValue(0);

        ArrayList<Entry> lineCJEntries = new ArrayList<>();
        ArrayList<Entry> lineJJEntries = new ArrayList<>();
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        for (int i = 0, j = 0; i < mData.getMinutesDatas().size(); i++, j++) {
//            //避免数据重复，skip也能正常显示
//            if (i==120||i==241||i==362||i==483) {
//                continue;
//            }
            MinutesBean t = mData.getMinutesDatas().get(j);

            if (t == null) {
                lineCJEntries.add(new Entry(Float.NaN, i));
                lineJJEntries.add(new Entry(Float.NaN, i));
                barEntries.add(new BarEntry(Float.NaN, i));
                continue;
            }
//            if (!TextUtils.isEmpty(stringSparseArray.get(i)) &&
//                    stringSparseArray.get(i).contains("/")) {
//                i++;
//            }
            lineCJEntries.add(new Entry(mData.getMinutesDatas().get(i).cjprice, i));
            lineJJEntries.add(new Entry(mData.getMinutesDatas().get(i).avprice, i));
            barEntries.add(new BarEntry(mData.getMinutesDatas().get(i).cjnum, i));
            // dateList.add(mData.getDatas().get(i).time);
            if (i > 0 && mData.getMinutesDatas().get(i).cjprice < mData.getMinutesDatas().get(i - 1).cjprice) {
                list.add(i, ContextCompat.getColor(context, R.color.down_color));
            } else {
                list.add(i, ContextCompat.getColor(context, R.color.up_color));
            }

        }
        LineDataSet d1 = new LineDataSet(lineCJEntries, "成交价");
        LineDataSet d2 = new LineDataSet(lineJJEntries, "均价");
        d1.setDrawValues(false);
        d2.setDrawValues(false);
        d1.setCircleRadius(0);
        d2.setCircleRadius(0);
        d1.setColor(ContextCompat.getColor(context, R.color.minute_blue));
        d2.setColor(ContextCompat.getColor(context, R.color.minute_yellow));
        d1.setHighLightColor(ContextCompat.getColor(context, R.color.highlight));//高亮线颜色
        d1.setHighlightEnabled(true);
        d2.setHighlightEnabled(false);
        d1.setDrawFilled(true);//走势线底部半透明阴影颜色

        BarDataSet barDataSet = new BarDataSet(barEntries, "成交量");
        barDataSet.setBarSpacePercent(50); //bar空隙
        barDataSet.setHighLightColor(ContextCompat.getColor(context, R.color.highlight));
        barDataSet.setHighLightAlpha(255);
        barDataSet.setDrawValues(false);
        barDataSet.setHighlightEnabled(true);
        barDataSet.setColors(list);
        //谁为基准
        d1.setAxisDependency(YAxis.AxisDependency.LEFT);
        // d2.setAxisDependency(YAxis.AxisDependency.RIGHT);
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);
        sets.add(d2);
        /*注老版本LineData参数可以为空，最新版本会报错，修改进入ChartData加入if判断*/
        LineData cd = new LineData(getMinutesCount(), sets);
        lineChart.setData(cd);
        BarData barData = new BarData(getMinutesCount(), barDataSet);
        barChart.setData(barData);

        setOffset();
        lineChart.animateX(1000);
        barChart.animateXY(1000, 1000);
    }


    public void setMinutesCount(int mMaxDataLength) {
        this.mMaxDataLength = mMaxDataLength;
    }

    public String[] getMinutesCount() {
        return new String[mMaxDataLength];
    }

    /**
     * 设置高亮线两端的数据
     *
     * @param mData
     */
    private void setMarkerView(DataParse mData) {
        MyLeftMarkerView leftMarkerView = new MyLeftMarkerView(context, R.layout.mymarkerview);
        MyRightMarkerView rightMarkerView = new MyRightMarkerView(context, R.layout.mymarkerview);
        lineChart.setMarker(leftMarkerView, rightMarkerView, mData);
    }

    private void setShowLabels(SparseArray<String> labels) {
        xAxisLine.setXLabels(labels);
        xAxisBar.setXLabels(labels);
    }


    /*设置量表对齐*/
    private void setOffset() {
        float lineLeft = lineChart.getViewPortHandler().offsetLeft();
        float barLeft = barChart.getViewPortHandler().offsetLeft();
        float lineRight = lineChart.getViewPortHandler().offsetRight();
        float barRight = barChart.getViewPortHandler().offsetRight();
        float barBottom = barChart.getViewPortHandler().offsetBottom();
        float offsetLeft, offsetRight;
        float transLeft = 0, transRight = 0;
 /*注：setExtraLeft...函数是针对图表相对位置计算，比如A表offLeftA=20dp,B表offLeftB=30dp,则A.setExtraLeftOffset(10),并不是30，还有注意单位转换*/
        if (barLeft < lineLeft) {
            //offsetLeft = Utils.convertPixelsToDp(lineLeft - barLeft);
            // barChart.setExtraLeftOffset(offsetLeft);
            transLeft = lineLeft;

        } else {
            offsetLeft = Utils.convertPixelsToDp(barLeft - lineLeft);
            lineChart.setExtraLeftOffset(offsetLeft);
            transLeft = barLeft;
        }

  /*注：setExtraRight...函数是针对图表绝对位置计算，比如A表offRightA=20dp,B表offRightB=30dp,则A.setExtraLeftOffset(30),并不是10，还有注意单位转换*/
        if (barRight < lineRight) {
            //offsetRight = Utils.convertPixelsToDp(lineRight);
            //barChart.setExtraRightOffset(offsetRight);
            transRight = lineRight;
        } else {
            offsetRight = Utils.convertPixelsToDp(barRight);
            lineChart.setExtraRightOffset(offsetRight);
            transRight = barRight;
        }
        barChart.setViewPortOffsets(transLeft, 5, transRight, barBottom);
    }

    /**
     * 高亮线出现后的回调接口
     */
    public interface OnMinutesHighLightListener {
        // TODO: Update argument type and name
        void onMinutesHighLight(DataParse Data, int dataSetIndex, boolean isvisibility);
    }

    public void setOnMinutesHighLightListener(OnMinutesHighLightListener onMinutesHighLightListener) {
        this.onMinutesHighLightListener = onMinutesHighLightListener;
    }
}

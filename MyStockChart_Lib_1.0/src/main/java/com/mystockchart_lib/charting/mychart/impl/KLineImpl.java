package com.mystockchart_lib.charting.mychart.impl;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v4.content.ContextCompat;

import com.mystockchart_lib.R;
import com.mystockchart_lib.charting.components.Legend;
import com.mystockchart_lib.charting.components.XAxis;
import com.mystockchart_lib.charting.components.YAxis;
import com.mystockchart_lib.charting.data.BarData;
import com.mystockchart_lib.charting.data.BarDataSet;
import com.mystockchart_lib.charting.data.BarEntry;
import com.mystockchart_lib.charting.data.CandleData;
import com.mystockchart_lib.charting.data.CandleDataSet;
import com.mystockchart_lib.charting.data.CandleEntry;
import com.mystockchart_lib.charting.data.CombinedData;
import com.mystockchart_lib.charting.data.Entry;
import com.mystockchart_lib.charting.data.LineData;
import com.mystockchart_lib.charting.data.LineDataSet;
import com.mystockchart_lib.charting.interfaces.datasets.ILineDataSet;
import com.mystockchart_lib.charting.mychart.MyCombinedChart;
import com.mystockchart_lib.charting.mychart.MyXAxis;
import com.mystockchart_lib.charting.mychart.MyYAxis;
import com.mystockchart_lib.charting.mychart.bean.DataParse;
import com.mystockchart_lib.charting.mychart.bean.KLineBean;
import com.mystockchart_lib.charting.mychart.utils.MyUtils;
import com.mystockchart_lib.charting.mychart.utils.VolFormatter;
import com.mystockchart_lib.charting.utils.Utils;
import com.mystockchart_lib.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 竖屏K线实现类封装，供外部调用
 */

public class KLineImpl {

    private MyCombinedChart combinedchart;
    private MyCombinedChart combinedchart2;
    private Context context;
    private ArrayList<KLineBean> kLineDatas;
    private MyXAxis xAxisK, xAxisK2;
    private MyYAxis axisLeftK, axisLeftK2;
    private MyYAxis axisRightK, axisRightK2;
    private ViewPortHandler viewPortHandlerCombin,viewPortHandlerCombin2;
    public final static int INVALID_VALUE = 999999999;

    public KLineImpl(Context context, MyCombinedChart combinedchart, MyCombinedChart combinedchart2){
        this.context = context;
        this.combinedchart = combinedchart;
        this.combinedchart2 = combinedchart2;

        initChart();
    }

    private void initChart() {

        // 主图
        combinedchart.setDrawBorders(true);
        combinedchart.setBorderWidth(1);
        combinedchart.setBorderColor(ContextCompat.getColor(context, R.color.minute_grayLine));
        combinedchart.setDragEnabled(false);
        combinedchart.setScaleYEnabled(false);
        combinedchart.setScaleEnabled(false);
        combinedchart.setDescription("");
        Legend combinedchartLegend = combinedchart.getLegend();
        combinedchartLegend.setEnabled(false);
        combinedchart.setDragDecelerationEnabled(false);
        combinedchart.setDragDecelerationFrictionCoef(0.2f);
        combinedchart.setAutoScaleMinMaxEnabled(true);
        // x 轴
        xAxisK = combinedchart.getXAxis();
        xAxisK.setDrawLabels(true);
        xAxisK.setDrawGridLines(true);
        xAxisK.setDrawAxisLine(false);
        xAxisK.setTextColor(ContextCompat.getColor(context, R.color.minute_zhoutext));
        xAxisK.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisK.setGridColor(ContextCompat.getColor(context, R.color.minute_grayLine));
        // 左 y 轴
        axisLeftK = combinedchart.getAxisLeft();
        axisLeftK.setDrawGridLines(true);
        axisLeftK.setDrawAxisLine(false);
        axisLeftK.setDrawLabels(true);
        axisLeftK.setTextColor(ContextCompat.getColor(context, R.color.minute_zhoutext));
        axisLeftK.setGridColor(ContextCompat.getColor(context, R.color.minute_grayLine));
        axisLeftK.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        // 右 y 轴
        axisRightK = combinedchart.getAxisRight();
        axisRightK.setDrawLabels(false);
        axisRightK.setDrawGridLines(true);
        axisRightK.setDrawAxisLine(false);
        axisRightK.setGridColor(ContextCompat.getColor(context, R.color.minute_grayLine));


        /****************************************************************/
        //副图
        combinedchart2.setDrawBorders(true);
        combinedchart2.setBorderWidth(1);
        combinedchart2.setBorderColor(ContextCompat.getColor(context,R.color.minute_grayLine));
        combinedchart2.setDragEnabled(false);
        combinedchart2.setScaleYEnabled(false);
        combinedchart2.setDescription("");
        Legend combinedchartLegend2 = combinedchart2.getLegend();
        combinedchartLegend2.setEnabled(false);
        combinedchart2.setDragDecelerationEnabled(false);
        combinedchart2.setDragDecelerationFrictionCoef(0.2f);
        combinedchart2.setAutoScaleMinMaxEnabled(true);
        //x 轴
        xAxisK2 = combinedchart2.getXAxis();
        xAxisK2.setDrawLabels(false);
        xAxisK2.setDrawGridLines(true);
        xAxisK2.setDrawAxisLine(false);
        xAxisK2.setGridColor(ContextCompat.getColor(context,R.color.minute_grayLine));
        //左 y 轴
        axisLeftK2 = combinedchart2.getAxisLeft();
        axisLeftK2.setDrawGridLines(false);
        axisLeftK2.setDrawAxisLine(false);
        axisLeftK2.setDrawLabels(true);
        axisLeftK2.setShowOnlyMinMax(true);
        axisLeftK2.setAxisMinValue(0);
        axisLeftK2.setTextColor(ContextCompat.getColor(context,R.color.minute_zhoutext));
        axisLeftK2.setGridColor(ContextCompat.getColor(context,R.color.minute_grayLine));
        axisLeftK2.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        //右 y 轴
        axisRightK2 = combinedchart2.getAxisRight();
        axisRightK2.setDrawLabels(false);
        axisRightK2.setDrawGridLines(false);
        axisRightK2.setDrawAxisLine(false);
        axisRightK2.setGridColor(ContextCompat.getColor(context,R.color.minute_grayLine));

    }

    /**
     * 设置画线数据
     * @param mData
     */
    public void setData(final DataParse mData) {

        kLineDatas = mData.getKLineDatas();
        xAxisK.setXLabels(mData.getXValuesLabel());
        xAxisK2.setXLabels(mData.getXValuesLabel());
//        Logs.e("###数据总个数"+kLineDatas.size());
        if (kLineDatas.size() == 0) {
            combinedchart.setNoDataText("暂无数据");
            combinedchart2.setNoDataText("暂无数据");
            return;
        }
        String unit = MyUtils.getVolUnit(mData.getVolmax());
        int u = 1;
        if ("万手".equals(unit)) {
            u = 4;
        } else if ("亿手".equals(unit)) {
            u = 8;
        }
        axisLeftK2.setValueFormatter(new VolFormatter((int) Math.pow(10, u)));

        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<CandleEntry> candleEntries = new ArrayList<>();
        ArrayList<Entry> line5Entries = new ArrayList<>();
        ArrayList<Entry> line10Entries = new ArrayList<>();
        ArrayList<Entry> line30Entries = new ArrayList<>();
        List<Integer> listcolor=new ArrayList<>();
        for (int i = 0, j = 0; i < mData.getKLineDatas().size(); i++, j++) {
            xVals.add(mData.getKLineDatas().get(i).date + "");
            candleEntries.add(new CandleEntry(i, mData.getKLineDatas().get(i).high, mData.getKLineDatas().get(i).low, mData.getKLineDatas().get(i).open, mData.getKLineDatas().get(i).close));

            if(mData.getKLineDatas().get(i).ma1 != INVALID_VALUE)
                line5Entries.add(new Entry(mData.getKLineDatas().get(i).ma1, i));
            if(mData.getKLineDatas().get(i).ma2 != INVALID_VALUE)
                line10Entries.add(new Entry(mData.getKLineDatas().get(i).ma2, i));
            if(mData.getKLineDatas().get(i).ma3 != INVALID_VALUE)
                line30Entries.add(new Entry(mData.getKLineDatas().get(i).ma3, i));
            if(mData.getKLineDatas().get(i).close > mData.getKLineDatas().get(i).open){
                listcolor.add(i, ContextCompat.getColor(context, R.color.up_color));
                barEntries.add(new BarEntry(mData.getKLineDatas().get(i).vol, i,true));
            }else{
                listcolor.add(i,ContextCompat.getColor(context, R.color.down_color));
                barEntries.add(new BarEntry(mData.getKLineDatas().get(i).vol, i,false));
            }
        }

        //蜡烛线数据
        CandleDataSet candleDataSet = new CandleDataSet(candleEntries, "KLine");
        candleDataSet.setDrawHorizontalHighlightIndicator(false);
        candleDataSet.setHighlightEnabled(false);
        candleDataSet.setHighLightColor(ContextCompat.getColor(context, R.color.highlight));
        candleDataSet.setValueTextSize(10f);
        candleDataSet.setDrawValues(false);
        candleDataSet.setColors(listcolor);
        candleDataSet.setIncreasingPaintStyle(Paint.Style.FILL);
        candleDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        CandleData candleData = new CandleData(xVals, candleDataSet);
        //MA数据
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(setMaLine(5,  line5Entries));
        sets.add(setMaLine(10,  line10Entries));
        sets.add(setMaLine(30,  line30Entries));
        LineData lineData = new LineData(xVals, sets);

        CombinedData combinedData = new CombinedData(xVals);
        combinedData.setData(candleData);
        combinedData.setData(lineData);
        combinedchart.setData(combinedData);

        //成交量数据
        BarDataSet barDataSet = new BarDataSet(barEntries, "成交量");
        barDataSet.setBarSpacePercent(25); //bar空隙
        barDataSet.setHighlightEnabled(false);
        barDataSet.setHighLightAlpha(255);
        barDataSet.setHighLightColor(ContextCompat.getColor(context, R.color.highlight));
        barDataSet.setDrawValues(false);
        barDataSet.setColors(listcolor);
        BarData barData = new BarData(xVals, barDataSet);
        CombinedData combinedData2 = new CombinedData(xVals);
        combinedData2.setData(barData);
        combinedchart2.setData(combinedData2);

        //初始化放缩大小
        if(viewPortHandlerCombin == null) {
            viewPortHandlerCombin = combinedchart.getViewPortHandler();
            viewPortHandlerCombin.setMaximumScaleX(culcMaxscale(xVals.size()));
            Matrix matrixCombin = viewPortHandlerCombin.getMatrixTouch();
            final float xscaleCombin = 3;
            matrixCombin.postScale(xscaleCombin, 1f);
        }
        if(viewPortHandlerCombin2 == null) {
            viewPortHandlerCombin2 = combinedchart2.getViewPortHandler();
            viewPortHandlerCombin2.setMaximumScaleX(culcMaxscale(xVals.size()));
            Matrix matrixCombin2 = viewPortHandlerCombin2.getMatrixTouch();
            final float xscaleCombin2 = 3;
            matrixCombin2.postScale(xscaleCombin2, 1f);
        }

        setOffset();

/****************************************************************************************
 此处解决方法来源于CombinedChartDemo，k线图y轴显示问题，图表滑动后才能对齐的bug
 ****************************************************************************************/
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                combinedchart.moveViewToX(mData.getKLineDatas().size() - 1);
                combinedchart2.moveViewToX(mData.getKLineDatas().size() - 1);
            }
        });

        combinedchart.animateX(1000);
        combinedchart2.animateY(1000);
    }


    private float culcMaxscale(float count) {
        float max = 1;
        max = count / 127 * 5;
        return max;
    }

    private LineDataSet setMaLine(int ma, ArrayList<Entry> lineEntries) {
        LineDataSet lineDataSetMa = new LineDataSet(lineEntries, "ma" + ma);
        if (ma == 5) {
            lineDataSetMa.setHighlightEnabled(false);
            lineDataSetMa.setDrawHorizontalHighlightIndicator(false);
            lineDataSetMa.setHighLightColor(ContextCompat.getColor(context, R.color.highlight));
        } else {/*此处必须得写*/
            lineDataSetMa.setHighlightEnabled(false);
        }
        lineDataSetMa.setDrawValues(false);
        if (ma == 5) {
            lineDataSetMa.setColor(ContextCompat.getColor(context, R.color.kline_ma1));
        } else if (ma == 10) {
            lineDataSetMa.setColor(ContextCompat.getColor(context, R.color.kline_ma2));
        } else {
            lineDataSetMa.setColor(ContextCompat.getColor(context, R.color.kline_ma3));
        }
        lineDataSetMa.setDrawCircles(false);
        lineDataSetMa.setAxisDependency(YAxis.AxisDependency.LEFT);
        return lineDataSetMa;
    }

    /*设置量表对齐*/
    private void setOffset() {
        float lineLeft = combinedchart.getViewPortHandler().offsetLeft();
        float barLeft = combinedchart2.getViewPortHandler().offsetLeft();
        float lineRight = combinedchart.getViewPortHandler().offsetRight();
        float barRight = combinedchart2.getViewPortHandler().offsetRight();
        float barBottom = combinedchart2.getViewPortHandler().offsetBottom();
        float offsetLeft, offsetRight;
        float transLeft = 0, transRight = 0;
 /*注：setExtraLeft...函数是针对图表相对位置计算，比如A表offLeftA=20dp,B表offLeftB=30dp,则A.setExtraLeftOffset(10),并不是30，还有注意单位转换*/
        if (barLeft < lineLeft) {
           /* offsetLeft = Utils.convertPixelsToDp(lineLeft - barLeft);
            barChart.setExtraLeftOffset(offsetLeft);*/
            transLeft = lineLeft;
        } else {
            offsetLeft = Utils.convertPixelsToDp(barLeft - lineLeft);
            combinedchart.setExtraLeftOffset(offsetLeft);
            transLeft = barLeft;
        }
  /*注：setExtraRight...函数是针对图表绝对位置计算，比如A表offRightA=20dp,B表offRightB=30dp,则A.setExtraLeftOffset(30),并不是10，还有注意单位转换*/
        if (barRight < lineRight) {
          /*  offsetRight = Utils.convertPixelsToDp(lineRight);
            barChart.setExtraRightOffset(offsetRight);*/
            transRight = lineRight;
        } else {
            offsetRight = Utils.convertPixelsToDp(barRight);
            combinedchart.setExtraRightOffset(offsetRight);
            transRight = barRight;
        }
        combinedchart2.setViewPortOffsets(transLeft, 15, transRight, barBottom);
    }
}

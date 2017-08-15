package com.mystockchart_lib.charting.mychart.impl;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v4.content.ContextCompat;

import com.mystockchart_lib.R;
import com.mystockchart_lib.charting.charts.Chart;
import com.mystockchart_lib.charting.charts.ScatterChart;
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
import com.mystockchart_lib.charting.data.ScatterData;
import com.mystockchart_lib.charting.data.ScatterDataSet;
import com.mystockchart_lib.charting.highlight.Highlight;
import com.mystockchart_lib.charting.interfaces.datasets.ILineDataSet;
import com.mystockchart_lib.charting.interfaces.datasets.IScatterDataSet;
import com.mystockchart_lib.charting.listener.OnChartValueSelectedListener;
import com.mystockchart_lib.charting.mychart.CoupleChartGestureListener;
import com.mystockchart_lib.charting.mychart.MyCombinedChart;
import com.mystockchart_lib.charting.mychart.MyXAxis;
import com.mystockchart_lib.charting.mychart.MyYAxis;
import com.mystockchart_lib.charting.mychart.bean.DataParse;
import com.mystockchart_lib.charting.mychart.bean.KLineBean;
import com.mystockchart_lib.charting.mychart.utils.Consts;
import com.mystockchart_lib.charting.mychart.utils.MyUtils;
import com.mystockchart_lib.charting.mychart.utils.VolFormatter;
import com.mystockchart_lib.charting.utils.Utils;
import com.mystockchart_lib.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 横屏K线实现类封装，供外部调用
 */

public class KLineLandImpl {

    private MyCombinedChart combinedchart;
    private MyCombinedChart combinedchart2;
    private Context context;
    private int mKLineIndex = 0;//指标
    private ArrayList<KLineBean> kLineDatas;
    private MyXAxis xAxisK, xAxisK2;
    private MyYAxis axisLeftK, axisLeftK2;
    private MyYAxis axisRightK, axisRightK2;
    private ViewPortHandler viewPortHandlerCombin,viewPortHandlerCombin2;
    private DataParse mData;
    private int maColor[];
    private int zbColor[];
    private String mastr[] = {"MA5 ", " MA ", " MA30 "};
    private String zbstr[] = {"", "", ""};
    private OnKLineHighLightListener onKLineHighLightListener;
    public final static int INVALID_VALUE = 999999999;

    public KLineLandImpl(Context context, MyCombinedChart combinedchart, MyCombinedChart combinedchart2) {
        this.context = context;
        this.combinedchart = combinedchart;
        this.combinedchart2 = combinedchart2;

        maColor = new int[]{ContextCompat.getColor(context, R.color.kline_ma1), ContextCompat.getColor(context, R.color.kline_ma2), ContextCompat.getColor(context, R.color.kline_ma3)};
        zbColor = new int[]{ContextCompat.getColor(context, R.color.kline_linebg1), ContextCompat.getColor(context, R.color.kline_linebg2), ContextCompat.getColor(context, R.color.kline_linebg3)};

        initChart();
    }

    private void initChart() {
        // 主图
        combinedchart.setDrawBorders(true);
        combinedchart.setBorderWidth(1);
        combinedchart.setBorderColor(ContextCompat.getColor(context, R.color.minute_grayLine));
        combinedchart.setDragEnabled(true);
        combinedchart.setScaleYEnabled(false);
        combinedchart.setDescriptionCustom(maColor, mastr);
        Legend combinedchartLegend = combinedchart.getLegend();
        combinedchartLegend.setEnabled(false);
//        combinedchart.setDragDecelerationEnabled(false);
//        combinedchart.setDragDecelerationFrictionCoef(0.2f);
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
        axisLeftK.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
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
        combinedchart2.setBorderColor(ContextCompat.getColor(context, R.color.minute_grayLine));
        combinedchart2.setDragEnabled(true);
        combinedchart2.setScaleYEnabled(false);
        combinedchart2.setDescriptionCustom(zbColor,zbstr);
        Legend combinedchartLegend2 = combinedchart2.getLegend();
        combinedchartLegend2.setEnabled(false);
//        combinedchart2.setDragDecelerationEnabled(false);
//        combinedchart2.setDragDecelerationFrictionCoef(0.2f);
        combinedchart2.setAutoScaleMinMaxEnabled(true);
        //x 轴
        xAxisK2 = combinedchart2.getXAxis();
        xAxisK2.setDrawLabels(false);
        xAxisK2.setDrawGridLines(true);
        xAxisK2.setDrawAxisLine(false);
        xAxisK2.setGridColor(ContextCompat.getColor(context, R.color.minute_grayLine));
        //左 y 轴
        axisLeftK2 = combinedchart2.getAxisLeft();
        axisLeftK2.setDrawGridLines(false);
        axisLeftK2.setDrawAxisLine(false);
        axisLeftK2.setDrawLabels(true);
        axisLeftK2.setShowOnlyMinMax(true);
        axisLeftK2.setTextColor(ContextCompat.getColor(context, R.color.minute_zhoutext));
        axisLeftK2.setGridColor(ContextCompat.getColor(context, R.color.minute_grayLine));
        axisLeftK2.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        //右 y 轴
        axisRightK2 = combinedchart2.getAxisRight();
        axisRightK2.setDrawLabels(false);
        axisRightK2.setDrawGridLines(false);
        axisRightK2.setDrawAxisLine(false);
        axisRightK2.setGridColor(ContextCompat.getColor(context, R.color.minute_grayLine));

        // 将主图的滑动事件传递给副图
        combinedchart.setOnChartGestureListener(new CoupleChartGestureListener(combinedchart, new Chart[]{combinedchart2}));
        // 将副图的滑动事件传递给主图
        combinedchart2.setOnChartGestureListener(new CoupleChartGestureListener(combinedchart2, new Chart[]{combinedchart}));
        //副图触摸高亮线事件
        combinedchart2.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

                combinedchart.highlightValues(new Highlight[]{h});

                mastr[0] = "MA5 " + mData.getKLineDatas().get(e.getXIndex()).ma1;
                mastr[1] = " MA10 " + mData.getKLineDatas().get(e.getXIndex()).ma2;
                mastr[2] = " MA30 " + mData.getKLineDatas().get(e.getXIndex()).ma3;

                dragzbdata(e.getXIndex());

                if (onKLineHighLightListener != null) {
                    onKLineHighLightListener.onKLineHighLight(mData, e.getXIndex(), true);
                }
            }

            @Override
            public void onNothingSelected() {
                combinedchart.highlightValue(null);
                if (onKLineHighLightListener != null) {
                    onKLineHighLightListener.onKLineHighLight(mData, 0, false);
                }
                mastr[0] = "MA5 " + mData.getKLineDatas().get(mData.getKLineDatas().size() - 1).ma1;
                mastr[1] = " MA10 " + mData.getKLineDatas().get(mData.getKLineDatas().size() - 1).ma2;
                mastr[2] = " MA30 " + mData.getKLineDatas().get(mData.getKLineDatas().size() - 1).ma3;

                zbstr[0] = Consts.kxzbLabel[mKLineIndex];
                zbstr[1] = "";
                zbstr[2] = "";
            }
        });
        //主图触摸高亮线事件
        combinedchart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

                combinedchart2.highlightValues(new Highlight[]{h});

                mastr[0] = "MA5 " + mData.getKLineDatas().get(e.getXIndex()).ma1;
                mastr[1] = " MA10 " + mData.getKLineDatas().get(e.getXIndex()).ma2;
                mastr[2] = " MA30 " + mData.getKLineDatas().get(e.getXIndex()).ma3;

                dragzbdata(e.getXIndex());

                if (onKLineHighLightListener != null) {
                    onKLineHighLightListener.onKLineHighLight(mData, e.getXIndex(), true);
                }
            }

            @Override
            public void onNothingSelected() {
                combinedchart2.highlightValue(null);
                if (onKLineHighLightListener != null) {
                    onKLineHighLightListener.onKLineHighLight(mData, 0, false);
                }
                mastr[0] = "MA5 " + mData.getKLineDatas().get(mData.getKLineDatas().size() - 1).ma1;
                mastr[1] = " MA10 " + mData.getKLineDatas().get(mData.getKLineDatas().size() - 1).ma2;
                mastr[2] = " MA30 " + mData.getKLineDatas().get(mData.getKLineDatas().size() - 1).ma3;

                zbstr[0] = Consts.kxzbLabel[mKLineIndex];
                zbstr[1] = "";
                zbstr[2] = "";
            }
        });
    }

    /**
     * 设置画线数据
     *
     * @param mData
     */
    public void setData(final DataParse mData) {
        this.mData = mData;
        this.mKLineIndex = mData.getKLineindex();
        kLineDatas = mData.getKLineDatas();
        xAxisK.setXLabels(mData.getXValuesLabel());
        xAxisK2.setXLabels(mData.getXValuesLabel());
//        Logs.e("###数据总个数"+kLineDatas.size());
        if (kLineDatas.size() == 0) {
            combinedchart.setNoDataText("暂无数据");
            combinedchart2.setNoDataText("暂无数据");
            return;
        }

        zbstr[0] = Consts.kxzbLabel[mKLineIndex];
        zbstr[1] = "";
        zbstr[2] = "";

        String unit = MyUtils.getVolUnit(mData.getVolmax());
        if(Consts.kxzbName[mKLineIndex].equals("成交量")) {
            int u = 1;
            if ("万手".equals(unit)) {
                u = 4;
            } else if ("亿手".equals(unit)) {
                u = 8;
            }
            axisLeftK2.setValueFormatter(new VolFormatter((int) Math.pow(10, u)));
            axisLeftK2.setAxisMinValue(0);
        }else{
            axisLeftK2.resetValueFormatter();
            axisLeftK2.resetAxisMaxValue();
            axisLeftK2.resetAxisMinValue();
        }

        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<BarEntry> barEntries = new ArrayList<>();//成交量数据
        ArrayList<CandleEntry> candleEntries = new ArrayList<>();//蜡烛线数据
        ArrayList<Entry> line5Entries = new ArrayList<>();//MA数据
        ArrayList<Entry> line10Entries = new ArrayList<>();
        ArrayList<Entry> line30Entries = new ArrayList<>();
        ArrayList<Entry> lineEntries1 = new ArrayList<>();//指标线数据
        ArrayList<Entry> lineEntries2 = new ArrayList<>();
        ArrayList<Entry> lineEntries3 = new ArrayList<>();
        List<Integer> listcolor1 = new ArrayList<>();//主图蜡烛线颜色
        List<Integer> listcolor2 = new ArrayList<>();//副图颜色
        for (int i = 0; i < mData.getKLineDatas().size(); i++) {
            xVals.add(mData.getKLineDatas().get(i).date + "");
            //蜡烛数据
            candleEntries.add(new CandleEntry(i, mData.getKLineDatas().get(i).high, mData.getKLineDatas().get(i).low, mData.getKLineDatas().get(i).open, mData.getKLineDatas().get(i).close));
            if (mData.getKLineDatas().get(i).close > mData.getKLineDatas().get(i).open) {
                listcolor1.add(i, ContextCompat.getColor(context, R.color.up_color));
            } else {
                listcolor1.add(i, ContextCompat.getColor(context, R.color.down_color));
            }
            //MA均线数据
            if(mData.getKLineDatas().get(i).ma1 != INVALID_VALUE)
                line5Entries.add(new Entry(mData.getKLineDatas().get(i).ma1, i));
            if(mData.getKLineDatas().get(i).ma2 != INVALID_VALUE)
                line10Entries.add(new Entry(mData.getKLineDatas().get(i).ma2, i));
            if(mData.getKLineDatas().get(i).ma3 != INVALID_VALUE)
                line30Entries.add(new Entry(mData.getKLineDatas().get(i).ma3, i));
            if (i == mData.getKLineDatas().size() - 1) {
                mastr[0] = "MA5 " + mData.getKLineDatas().get(i).ma1;
                mastr[1] = " MA10 " + mData.getKLineDatas().get(i).ma2;
                mastr[2] = " MA30 " + mData.getKLineDatas().get(i).ma3;
            }

            //指标数据
            if(Consts.kxzbName[mKLineIndex].equals("成交量")) {
                if (mData.getKLineDatas().get(i).close > mData.getKLineDatas().get(i).open) {
                    barEntries.add(new BarEntry(mData.getKLineDatas().get(i).vol, i, true));
                } else {
                    barEntries.add(new BarEntry(mData.getKLineDatas().get(i).vol, i, false));
                }
            }else if(Consts.kxzbName[mKLineIndex].equals("MACD")){
                if(mData.getKLineDatas().get(i).macd1 != INVALID_VALUE)
                    lineEntries1.add(new Entry(mData.getKLineDatas().get(i).macd1, i));
                if(mData.getKLineDatas().get(i).macd2 != INVALID_VALUE)
                    lineEntries2.add(new Entry(mData.getKLineDatas().get(i).macd2, i));
                if(mData.getKLineDatas().get(i).macd3 != INVALID_VALUE)
                    barEntries.add(new BarEntry(mData.getKLineDatas().get(i).macd3, i, false));
                if (mData.getKLineDatas().get(i).macd3 > 0) {
                    listcolor2.add(i, ContextCompat.getColor(context, R.color.up_color));
                } else {
                    listcolor2.add(i, ContextCompat.getColor(context, R.color.down_color));
                }
            }else if(Consts.kxzbName[mKLineIndex].equals("KDJ")){
                if(mData.getKLineDatas().get(i).kdj1 != INVALID_VALUE)
                    lineEntries1.add(new Entry(mData.getKLineDatas().get(i).kdj1, i));
                if(mData.getKLineDatas().get(i).kdj2 != INVALID_VALUE)
                    lineEntries2.add(new Entry(mData.getKLineDatas().get(i).kdj2, i));
                if(mData.getKLineDatas().get(i).kdj3 != INVALID_VALUE)
                    lineEntries3.add(new Entry(mData.getKLineDatas().get(i).kdj3, i));
            }else if(Consts.kxzbName[mKLineIndex].equals("RSI")){
                if(mData.getKLineDatas().get(i).rsi1 != INVALID_VALUE)
                    lineEntries1.add(new Entry(mData.getKLineDatas().get(i).rsi1, i));
                if(mData.getKLineDatas().get(i).rsi2 != INVALID_VALUE)
                    lineEntries2.add(new Entry(mData.getKLineDatas().get(i).rsi2, i));
                if(mData.getKLineDatas().get(i).rsi3 != INVALID_VALUE)
                    lineEntries3.add(new Entry(mData.getKLineDatas().get(i).rsi3, i));
            }else if(Consts.kxzbName[mKLineIndex].equals("WR")){
                if(mData.getKLineDatas().get(i).wr1 != INVALID_VALUE)
                    lineEntries1.add(new Entry(mData.getKLineDatas().get(i).wr1, i));
                if(mData.getKLineDatas().get(i).wr2 != INVALID_VALUE)
                    lineEntries2.add(new Entry(mData.getKLineDatas().get(i).wr2, i));
            }else if(Consts.kxzbName[mKLineIndex].equals("BOLL")){
                if(mData.getKLineDatas().get(i).boll1 != INVALID_VALUE)
                    lineEntries1.add(new Entry(mData.getKLineDatas().get(i).boll1, i));
                if(mData.getKLineDatas().get(i).boll2 != INVALID_VALUE)
                    lineEntries2.add(new Entry(mData.getKLineDatas().get(i).boll2, i));
                if(mData.getKLineDatas().get(i).boll3 != INVALID_VALUE)
                    lineEntries3.add(new Entry(mData.getKLineDatas().get(i).boll3, i));
            }else if(Consts.kxzbName[mKLineIndex].equals("DMA")){
                if(mData.getKLineDatas().get(i).dma1 != INVALID_VALUE)
                    lineEntries1.add(new Entry(mData.getKLineDatas().get(i).dma1, i));
                if(mData.getKLineDatas().get(i).dma2 != INVALID_VALUE)
                    lineEntries2.add(new Entry(mData.getKLineDatas().get(i).dma2, i));
            }else if(Consts.kxzbName[mKLineIndex].equals("AROON")){
                if(mData.getKLineDatas().get(i).aroon1 != INVALID_VALUE)
                    lineEntries1.add(new Entry(mData.getKLineDatas().get(i).aroon1, i));
                if(mData.getKLineDatas().get(i).aroon2 != INVALID_VALUE)
                    lineEntries2.add(new Entry(mData.getKLineDatas().get(i).aroon2, i));
            }else if(Consts.kxzbName[mKLineIndex].equals("CCI")){
                if(mData.getKLineDatas().get(i).cci != INVALID_VALUE)
                    lineEntries1.add(new Entry(mData.getKLineDatas().get(i).cci, i));
            }else if(Consts.kxzbName[mKLineIndex].equals("SAR")){
                if(mData.getKLineDatas().get(i).sar != INVALID_VALUE)
                    lineEntries1.add(new Entry(mData.getKLineDatas().get(i).sar, i));
                if (mData.getKLineDatas().get(i).sar > mData.getKLineDatas().get(i).open) {
                    listcolor2.add(i, ContextCompat.getColor(context, R.color.down_color));
                } else {
                    listcolor2.add(i, ContextCompat.getColor(context, R.color.up_color));
                }
            }

        }
        //蜡烛线数据
        CandleDataSet candleDataSet = new CandleDataSet(candleEntries, "KLine");
        candleDataSet.setDrawHorizontalHighlightIndicator(false);
        candleDataSet.setHighlightEnabled(true);
        candleDataSet.setHighLightColor(ContextCompat.getColor(context, R.color.highlight));
        candleDataSet.setValueTextSize(8f);
        candleDataSet.setValueTextColor(ContextCompat.getColor(context, R.color.highlight));
        candleDataSet.setDrawValues(false);
        candleDataSet.setColors(listcolor1);
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
        //指标数据
        if(Consts.kxzbName[mKLineIndex].equals("成交量")) {
            //成交量数据
            BarDataSet barDataSet = new BarDataSet(barEntries, "成交量");
            barDataSet.setBarSpacePercent(25); //bar空隙
            barDataSet.setHighlightEnabled(true);
            barDataSet.setHighLightAlpha(255);
            barDataSet.setHighLightColor(ContextCompat.getColor(context, R.color.highlight));
            barDataSet.setDrawValues(false);
            barDataSet.setColors(listcolor1);
            BarData barData = new BarData(xVals, barDataSet);

            CombinedData combinedData2 = new CombinedData(xVals);
            combinedData2.setData(barData);
            combinedchart2.setData(combinedData2);
        }else if(Consts.kxzbName[mKLineIndex].equals("MACD")){
            //MACD
            BarDataSet barDataSet = new BarDataSet(barEntries, "MACD");
            barDataSet.setBarSpacePercent(25); //bar空隙
            barDataSet.setHighlightEnabled(true);
            barDataSet.setHighLightAlpha(255);
            barDataSet.setHighLightColor(ContextCompat.getColor(context, R.color.highlight));
            barDataSet.setDrawValues(false);
            barDataSet.setColors(listcolor2);
            BarData barData = new BarData(xVals, barDataSet);

            ArrayList<ILineDataSet> sets1 = new ArrayList<>();
            sets1.add(setIndexLine(lineEntries1,ContextCompat.getColor(context, R.color.kline_linebg1),false));
            sets1.add(setIndexLine(lineEntries2,ContextCompat.getColor(context, R.color.kline_linebg2),false));
            LineData lineData1 = new LineData(xVals, sets1);

            CombinedData combinedData2 = new CombinedData(xVals);
            combinedData2.setData(barData);
            combinedData2.setData(lineData1);
            combinedchart2.setData(combinedData2);
        }else if(Consts.kxzbName[mKLineIndex].equals("BOLL")){
            //BOLL
            ArrayList<ILineDataSet> sets1 = new ArrayList<>();
            sets1.add(setIndexLine(lineEntries1,ContextCompat.getColor(context, R.color.kline_linebg1),false));
            sets1.add(setIndexLine(lineEntries2,ContextCompat.getColor(context, R.color.kline_linebg2),false));
            sets1.add(setIndexLine(lineEntries3,ContextCompat.getColor(context, R.color.kline_linebg3),false));
            LineData lineData1 = new LineData(xVals, sets1);

            //蜡烛线数据
            CandleDataSet candleDataSet1 = new CandleDataSet(candleEntries, "KLine");
            candleDataSet1.setDrawHorizontalHighlightIndicator(false);
            candleDataSet1.setHighlightEnabled(true);
            candleDataSet1.setHighLightColor(ContextCompat.getColor(context, R.color.highlight));
            candleDataSet1.setDrawValues(false);
            candleDataSet1.setColors(listcolor1);
            candleDataSet1.setShadowWidth(1f);
            candleDataSet1.setAxisDependency(YAxis.AxisDependency.LEFT);
            candleDataSet1.setShowCandleBar(false);
            CandleData candleData1 = new CandleData(xVals, candleDataSet1);

            CombinedData combinedData2 = new CombinedData(xVals);
            combinedData2.setData(candleData1);
            combinedData2.setData(lineData1);
            combinedchart2.setData(combinedData2);
        }else if(Consts.kxzbName[mKLineIndex].equals("SAR")){
            //SAR
            ScatterDataSet scatterDataSet = new ScatterDataSet(lineEntries1, "SAR");
            scatterDataSet.setDrawValues(false);
            scatterDataSet.setHighlightEnabled(false);
            scatterDataSet.setColors(listcolor2);
            scatterDataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
            scatterDataSet.setScatterShapeSize(5f);
            scatterDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

            ArrayList<IScatterDataSet> sets1 = new ArrayList<>();
            sets1.add(scatterDataSet);
            ScatterData scatterData = new ScatterData(xVals, sets1);

            //蜡烛线数据
            CandleDataSet candleDataSet1 = new CandleDataSet(candleEntries, "KLine");
            candleDataSet1.setDrawHorizontalHighlightIndicator(false);
            candleDataSet1.setHighlightEnabled(true);
            candleDataSet1.setHighLightColor(ContextCompat.getColor(context, R.color.highlight));
            candleDataSet1.setDrawValues(false);
            candleDataSet1.setColors(listcolor1);
            candleDataSet1.setShadowWidth(1f);
            candleDataSet1.setAxisDependency(YAxis.AxisDependency.LEFT);
            candleDataSet1.setShowCandleBar(false);
            CandleData candleData1 = new CandleData(xVals, candleDataSet1);

            CombinedData combinedData2 = new CombinedData(xVals);
            combinedData2.setData(candleData1);
            combinedData2.setData(scatterData);
            combinedchart2.setData(combinedData2);
        }else {//其他指标线
            ArrayList<ILineDataSet> sets1 = new ArrayList<>();
            sets1.add(setIndexLine(lineEntries1,ContextCompat.getColor(context, R.color.kline_linebg1),true));
            sets1.add(setIndexLine(lineEntries2,ContextCompat.getColor(context, R.color.kline_linebg2),false));
            sets1.add(setIndexLine(lineEntries3,ContextCompat.getColor(context, R.color.kline_linebg3),false));
            LineData lineData1 = new LineData(xVals, sets1);

            //暂时解决副图只有纯指标曲线时无法与主图对齐，故放一组柱状数据进去，使之能与主图的蜡烛数据对齐
            ArrayList<BarEntry> barEntries1 = new ArrayList<>();
            BarDataSet barDataSet = new BarDataSet(barEntries1, "");
            BarData barData = new BarData(xVals, barDataSet);

            CombinedData combinedData2 = new CombinedData(xVals);
            combinedData2.setData(lineData1);
            combinedData2.setData(barData);
            combinedchart2.setData(combinedData2);
        }


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
//设置MA线数据
    private LineDataSet setMaLine(int ma, ArrayList<Entry> lineEntries) {
        LineDataSet lineDataSetMa = new LineDataSet(lineEntries, "ma" + ma);
        if (ma == 5) {
            lineDataSetMa.setHighlightEnabled(true);
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
    //设置指标线数据
    private LineDataSet setIndexLine(ArrayList<Entry> lineEntries,int color,boolean isHighlightEnabled) {
        LineDataSet lineDataSet = new LineDataSet(lineEntries, "line");
        if (isHighlightEnabled) {
            lineDataSet.setHighlightEnabled(true);
            lineDataSet.setDrawHorizontalHighlightIndicator(false);
            lineDataSet.setHighLightColor(ContextCompat.getColor(context, R.color.highlight));
        } else {/*此处必须得写*/
            lineDataSet.setHighlightEnabled(false);
        }
        lineDataSet.setDrawValues(false);
        lineDataSet.setColor(color);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        return lineDataSet;
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


    private void dragzbdata(int dataSetIndex){
        if(Consts.kxzbName[mKLineIndex].equals("MACD")) {
            zbstr[0] = "DIFF:"+mData.getKLineDatas().get(dataSetIndex).macd1;
            zbstr[1] = " DEA:"+mData.getKLineDatas().get(dataSetIndex).macd2;
            zbstr[2] = " MACD:"+mData.getKLineDatas().get(dataSetIndex).macd3;
        }else if(Consts.kxzbName[mKLineIndex].equals("KDJ")) {
            zbstr[0] = "K:"+mData.getKLineDatas().get(dataSetIndex).kdj1;
            zbstr[1] = " D:"+mData.getKLineDatas().get(dataSetIndex).kdj2;
            zbstr[2] = " J:"+mData.getKLineDatas().get(dataSetIndex).kdj3;
        }else if(Consts.kxzbName[mKLineIndex].equals("RSI")) {
            zbstr[0] = "RSI6:"+mData.getKLineDatas().get(dataSetIndex).rsi1;
            zbstr[1] = " RSI12:"+mData.getKLineDatas().get(dataSetIndex).rsi2;
            zbstr[2] = " RSI24:"+mData.getKLineDatas().get(dataSetIndex).rsi3;
        }else if(Consts.kxzbName[mKLineIndex].equals("WR")) {
            zbstr[0] = "WR1:"+mData.getKLineDatas().get(dataSetIndex).wr1;
            zbstr[1] = " WR2:"+mData.getKLineDatas().get(dataSetIndex).wr2;
            zbstr[2] = "";
        }else if(Consts.kxzbName[mKLineIndex].equals("BOLL")) {
            zbstr[0] = "UPPER:"+mData.getKLineDatas().get(dataSetIndex).boll1;
            zbstr[1] = " MID:"+mData.getKLineDatas().get(dataSetIndex).boll2;
            zbstr[2] = " LOWER:"+mData.getKLineDatas().get(dataSetIndex).boll3;
        }else if(Consts.kxzbName[mKLineIndex].equals("DMA")) {
            zbstr[0] = "DDD:"+mData.getKLineDatas().get(dataSetIndex).dma1;
            zbstr[1] = " AMA:"+mData.getKLineDatas().get(dataSetIndex).dma2;
            zbstr[2] = "";
        }else if(Consts.kxzbName[mKLineIndex].equals("AROON")) {
            zbstr[0] = "UP:"+mData.getKLineDatas().get(dataSetIndex).aroon1;
            zbstr[1] = " DOWN:"+mData.getKLineDatas().get(dataSetIndex).aroon2;
            zbstr[2] = "";
        }else if(Consts.kxzbName[mKLineIndex].equals("CCI")) {
            zbstr[0] = "CCI:"+mData.getKLineDatas().get(dataSetIndex).cci;
            zbstr[1] = "";
            zbstr[2] = "";
        }else if(Consts.kxzbName[mKLineIndex].equals("SAR")) {
            zbstr[0] = "SAR:"+mData.getKLineDatas().get(dataSetIndex).sar;
            zbstr[1] = "";
            zbstr[2] = "";
        }
    }
    /**
     * 高亮线出现后的回调接口
     */
    public interface OnKLineHighLightListener {
        // TODO: Update argument type and name
        void onKLineHighLight(DataParse Data, int dataSetIndex, boolean isvisibility);
    }

    public void setOnKLineHighLightListener(OnKLineHighLightListener onKLineHighLightListener) {
        this.onKLineHighLightListener = onKLineHighLightListener;
    }
}

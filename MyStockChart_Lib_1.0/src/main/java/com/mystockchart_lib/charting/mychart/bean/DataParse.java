package com.mystockchart_lib.charting.mychart.bean;

import android.util.SparseArray;

import com.eno.base.utils.TCRS;
import com.mystockchart_lib.charting.mychart.utils.Consts;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 画线数据填充对应的数据体封装
 */
public class DataParse {
    private ArrayList<MinutesBean> minutesDatas = new ArrayList<>();//分时采样点数据
    private ArrayList<KLineBean> klineDatas = new ArrayList<>();//K线采样点数据
    private String name;
    private String code = "sz002081";
    private float baseValue;//昨收
    private float permaxmin;//最高最低值偏离中间价格的幅度，价格显示区间
    private float volmax = 0f;//成交量的最大值
    private int stockExchange;//股票的市场
    private int presicion = 2;//小数位数精度
    private int classid;//股票类别
    private int mKLineindex = 0;//K线指标类型
    private SparseArray<String> fivedayLabels = new SparseArray<>();;//5日时间坐标轴label
    private int decreasingColor;//下跌的颜色值
    private int increasingColor;//上涨的颜色值

    private SparseArray<String> xValuesLabel = new SparseArray<>();//时间坐标轴label


    public void parseMinutes(JSONObject object) {
        JSONArray jsonArray = object.optJSONObject("data").optJSONObject(code).optJSONObject("data").optJSONArray("data");
        String date = object.optJSONObject("data").optJSONObject(code).optJSONObject("data").optString("date");
        if (date.length() == 0) {
            return;
        }
/*数据解析依照自己需求来定，如果服务器直接返回百分比数据，则不需要客户端进行计算*/
        baseValue = (float) object.optJSONObject("data").optJSONObject(code).optJSONObject("qt").optJSONArray(code).optDouble(4);
        int count = jsonArray.length();
        for (int i = 0; i < count; i++) {
            String[] t = jsonArray.optString(i).split(" ");/*  "0930 9.50 4707",*/
            MinutesBean minutesData = new MinutesBean();
            minutesData.time = t[0].substring(0, 2) + ":" + t[0].substring(2);
            minutesData.cjprice = Float.parseFloat(t[1]);
            if (i != 0) {
                String[] pre_t = jsonArray.optString(i - 1).split(" ");
                minutesData.cjnum = Integer.parseInt(t[2]) - Integer.parseInt(pre_t[2]);
                minutesData.cjmoney = minutesData.cjnum * minutesData.cjprice + minutesDatas.get(i - 1).cjmoney;
                minutesData.avprice = (minutesData.cjmoney) / Integer.parseInt(t[2]);
            } else {
                minutesData.cjnum = Integer.parseInt(t[2]);
                minutesData.avprice = minutesData.cjprice;
                minutesData.cjmoney = minutesData.cjnum * minutesData.cjprice;
            }
            minutesData.cha = minutesData.cjprice - baseValue;
            minutesData.per = (minutesData.cha / baseValue);
            double cha = minutesData.cjprice - baseValue;
            if (Math.abs(cha) > permaxmin) {
                permaxmin = (float) Math.abs(cha);
            }
            volmax = Math.max(minutesData.cjnum, volmax);
            minutesDatas.add(minutesData);
        }

        if (permaxmin == 0) {
            permaxmin = baseValue * 0.02f;
        }
    }

    /**
     * 解析分时的TCRS结果集
     *
     * @param tcrs
     */
    public void parseMinutes(TCRS[] tcrs) {
        name = tcrs[0].toString("name");
        code = tcrs[0].toString("code");
        baseValue = tcrs[0].getFloat2("zrsp");//昨收价
        int lotsize = tcrs[0].getInt("lotsize");//每手数
        stockExchange = tcrs[0].getInt("exchid");//市场
        presicion = tcrs[0].getInt("precision");//小数位数精度
        classid = tcrs[0].getInt("classid");//股票类别
        float per1 = tcrs[0].getFloat2("zgcj") - baseValue;
        float per2 = tcrs[0].getFloat2("zdcj") - baseValue;
        permaxmin = Math.max(Math.abs(per1), Math.abs(per2))+baseValue * 0.002f;//价格显示区间

        tcrs[1].moveFirst();
        while (!tcrs[1].IsEof()) {
            MinutesBean minutesData = new MinutesBean();
            String time = tcrs[1].toString("fbtm");
            if (time.length() < 4) {
                time = "0" + time;
            }
            minutesData.time = time.substring(0, 2) + ":" + time.substring(2);
            minutesData.cjprice = tcrs[1].getFloat2("zjcj");
            minutesData.avprice = tcrs[1].getFloat2("cjjj");
            minutesData.cjnum = tcrs[1].getLong("cjsl") / lotsize;
            minutesData.cjmoney = tcrs[1].getLong("cjje");
            minutesData.cha = minutesData.cjprice - baseValue;
            minutesData.per = (minutesData.cha / baseValue);
            volmax = Math.max(minutesData.cjnum, volmax);
            minutesDatas.add(minutesData);

            tcrs[1].moveNext();
        }
        if (permaxmin == 0) {
            permaxmin = baseValue * 0.02f;
        }
    }

    /**
     * 解析5日分时的TCRS结果集
     *
     * @param tcrs
     */
    public void parseFiveDayMinutes(TCRS[] tcrs) {

        String day[] = new String[5];
        int i = 0;
        tcrs[0].moveFirst();
        while (!tcrs[0].IsEof()) {
            day[i] = tcrs[0].toString(0).substring(
                    tcrs[0].toString(0).length() - 4,
                    tcrs[0].toString(0).length() - 2)
                    + "-"
                    + tcrs[0].toString(0).substring(
                    tcrs[0].toString(0).length() - 2);
            i++;
            tcrs[0].moveNext();
        }
        fivedayLabels.put(0,"");
        fivedayLabels.put(60,day[4]);
        fivedayLabels.put(120,"");
        fivedayLabels.put(180,day[3]);
        fivedayLabels.put(241,"");
        fivedayLabels.put(300,day[2]);
        fivedayLabels.put(362,"");
        fivedayLabels.put(420,day[1]);
        fivedayLabels.put(483,"");
        fivedayLabels.put(540,day[0]);
        fivedayLabels.put(604,"");

        tcrs[5].moveFirst();
        baseValue = tcrs[5].getFloat2("zjcj");//昨收价

        TCRS details = tcrs[5];
        details.mergeTCRS(tcrs[4], false);
        details.mergeTCRS(tcrs[3], false);
        details.mergeTCRS(tcrs[2], false);
        details.mergeTCRS(tcrs[1], false);

        details.moveFirst();
        while (!details.IsEof()) {
            MinutesBean minutesData = new MinutesBean();
            String time = details.toString("fbtm");
            if (time.length() < 4) {
                time = "0" + time;
            }
            minutesData.time = time.substring(0, 2) + ":" + time.substring(2);
            minutesData.cjprice = details.getFloat2("zjcj");
            minutesData.avprice = details.getFloat2("cjjj");
            minutesData.cjnum = details.getLong("cjsl");
            minutesData.cjmoney = details.getLong("cjje");
            minutesData.cha = minutesData.cjprice - baseValue;
            minutesData.per = (minutesData.cha / baseValue);
            permaxmin = Math.max(Math.abs(minutesData.cha), permaxmin);
            volmax = Math.max(minutesData.cjnum, volmax);
            minutesDatas.add(minutesData);

            details.moveNext();
        }
        permaxmin = permaxmin+baseValue * 0.002f;//价格显示区间
        if (permaxmin == 0) {
            permaxmin = baseValue * 0.02f;
        }
    }

    public void parseKLine(JSONObject obj) {
        ArrayList<KLineBean> kLineBeans = new ArrayList<>();
        JSONObject data = obj.optJSONObject("data").optJSONObject(code);
        JSONArray list = data.optJSONArray("day");
        if (list != null) {
            int count = list.length();
            for (int i = 0; i < count; i++) {
                JSONArray dayData = list.optJSONArray(i);
                KLineBean kLineData = new KLineBean();
                kLineBeans.add(kLineData);
                kLineData.date = dayData.optString(0);
                kLineData.open = (float) dayData.optDouble(1);
                kLineData.close = (float) dayData.optDouble(2);
                kLineData.high = (float) dayData.optDouble(3);
                kLineData.low = (float) dayData.optDouble(4);
                kLineData.vol = (float) dayData.optDouble(5);
                volmax = Math.max(kLineData.vol, volmax);
                xValuesLabel.put(i, kLineData.date);
            }
        }
        klineDatas.addAll(kLineBeans);
    }

    /**
     * 解析K线的TCRS结果集
     *
     * @param tcrs
     */
    public void parseKLine(TCRS[] tcrs) {
        name = tcrs[0].toString("name");
        code = tcrs[0].toString("code");
        int lotsize = tcrs[0].getInt("lotsize");//每手数
        stockExchange = tcrs[0].getInt("exchid");//市场
        presicion = tcrs[0].getInt("precision");//小数位数精度
        classid = tcrs[0].getInt("classid");//股票类别
        volmax = tcrs[0].getLong("maxvol")/lotsize;//最大成交量

        tcrs[1].moveFirst();
        int i = 0;
        while (!tcrs[1].IsEof()) {
            KLineBean kLineData = new KLineBean();
            String date = tcrs[1].toString("hqda");
            String time = tcrs[1].toString("fbtm");

            if(!time.equalsIgnoreCase("0")){
                if(time.length()<4){
                    time = "0"+time;
                }
                date = date.substring(date.length()-4,date.length()-2)+"-"+date.substring(date.length()-2)+" "+time.substring(time.length()-4,time.length()-2)+":"+time.substring(time.length()-2);
            }else{
                date = date.substring(date.length()-8,date.length()-4)+"-"+date.substring(date.length()-4,date.length()-2)+"-"+date.substring(date.length()-2);
            }

            kLineData.date = date;
            kLineData.open = tcrs[1].getFloat2("jrkp");
            kLineData.close = tcrs[1].getFloat2("zrsp");
            kLineData.high = tcrs[1].getFloat2("zgcj");
            kLineData.low = tcrs[1].getFloat2("zdcj");
            kLineData.vol = tcrs[1].getLong("cjsl")/lotsize;
            kLineData.cjmoney = tcrs[1].getLong("cjje");
            kLineData.zaverj = tcrs[1].getFloat2("zjcj");
            kLineData.cha = kLineData.zaverj - kLineData.open;
            kLineData.per = kLineData.cha/kLineData.open;
            try {
                kLineData.ma1 = (float)tcrs[1].getDouble("MA3");
                kLineData.ma2 = (float)tcrs[1].getDouble("MA2");
                kLineData.ma3 = (float)tcrs[1].getDouble("MA1");
            } catch (Exception e) {
                e.printStackTrace();
            }

            xValuesLabel.put(i, kLineData.date);
            //取指标数据
            if(Consts.kxzbName[mKLineindex].equals("MACD")){
                kLineData.macd1 = (float)tcrs[1].getDouble("MACD3");
                kLineData.macd2 = (float)tcrs[1].getDouble("MACD2");
                kLineData.macd3 = (float)tcrs[1].getDouble("MACD1");
            }else if(Consts.kxzbName[mKLineindex].equals("KDJ")){
                kLineData.kdj1 = (float)tcrs[1].getDouble("KDJ3");
                kLineData.kdj2 = (float)tcrs[1].getDouble("KDJ2");
                kLineData.kdj3 = (float)tcrs[1].getDouble("KDJ1");
            }else if(Consts.kxzbName[mKLineindex].equals("RSI")){
                kLineData.rsi1 = (float)tcrs[1].getDouble("RSI3");
                kLineData.rsi2 = (float)tcrs[1].getDouble("RSI2");
                kLineData.rsi3 = (float)tcrs[1].getDouble("RSI1");
            }else if(Consts.kxzbName[mKLineindex].equals("WR")){
                kLineData.wr1 = (float)tcrs[1].getDouble("WR2");
                kLineData.wr2 = (float)tcrs[1].getDouble("WR1");
            }else if(Consts.kxzbName[mKLineindex].equals("BOLL")){
                kLineData.boll1 = (float)tcrs[1].getDouble("BOLL3");
                kLineData.boll2 = (float)tcrs[1].getDouble("BOLL2");
                kLineData.boll3 = (float)tcrs[1].getDouble("BOLL1");
            }else if(Consts.kxzbName[mKLineindex].equals("DMA")){
                kLineData.dma1 = (float)tcrs[1].getDouble("DMA2");
                kLineData.dma2 = (float)tcrs[1].getDouble("DMA1");
            }else if(Consts.kxzbName[mKLineindex].equals("AROON")){
                kLineData.aroon1 = (float)tcrs[1].getDouble("AROON2");
                kLineData.aroon2 = (float)tcrs[1].getDouble("AROON1");
            }else if(Consts.kxzbName[mKLineindex].equals("CCI")){
                kLineData.cci = (float)tcrs[1].getDouble("CCI1");
            }else if(Consts.kxzbName[mKLineindex].equals("SAR")){
                kLineData.sar = (float)tcrs[1].getDouble("SAR1");
            }

            klineDatas.add(kLineData);

            i++;
            tcrs[1].moveNext();
        }
    }

    /**
     * 获取股票名称
     *
     * @return
     */
    public String getStockName() {
        return name;
    }

    /**
     * 获取股票代码
     *
     * @return
     */
    public String getStockCode() {
        return code;
    }

    /**
     * 获取股票市场
     *
     * @return
     */
    public int getStockExchange() {
        return stockExchange;
    }

    /**
     * 获取股票代码+市场
     *
     * @return
     */
    public String getCodeAndStockExchange() {
        return code + "." + stockExchange;
    }

    /**
     * 获取小数精度位
     *
     * @return
     */
    public int getPresicion() {
        return presicion;
    }

    /**
     * 获取股票类别
     *
     * @return
     */
    public int getClassId() {
        return classid;
    }

    /**
     * 获取昨收价格
     *
     * @return
     */
    public float getZrsp() {
        return baseValue;
    }

    /**
     * 获取最低价格
     *
     * @return
     */
    public float getMin() {
        return baseValue - permaxmin;
    }

    /**
     * 获取最高价格
     *
     * @return
     */
    public float getMax() {
        return baseValue + permaxmin;
    }

    /**
     * 获取最高价格的幅度（百分数）
     *
     * @return
     */
    public float getPercentMax() {
        return permaxmin / baseValue;
    }

    /**
     * 获取最低价格的幅度（百分数）
     *
     * @return
     */
    public float getPercentMin() {
        return -getPercentMax();
    }

    /**
     * 获取最大成交量
     *
     * @return
     */
    public float getVolmax() {
        return volmax;
    }

    /**
     * 获取分时采样点数据
     *
     * @return
     */
    public ArrayList<MinutesBean> getMinutesDatas() {
        return minutesDatas;
    }

    /**
     * 获取K线采样点数据
     *
     * @return
     */
    public ArrayList<KLineBean> getKLineDatas() {
        return klineDatas;
    }

    /**
     * 获取时间轴的label
     *
     * @return
     */
    public SparseArray<String> getXValuesLabel() {
        return xValuesLabel;
    }

    /**
     * 获取五日分时的时间轴的label
     *
     * @return
     */
    public SparseArray<String> getFiveDayLabel() {
        return fivedayLabels;
    }

    /**
     * 设置K线指标
     *
     * @return
     */
    public void setKLineindex(int mKLineindex) {
        this.mKLineindex = mKLineindex;
    }
    /**
     *获取K线指标
     *
     * @return
     */
    public int getKLineindex() {
        return mKLineindex;
    }
}

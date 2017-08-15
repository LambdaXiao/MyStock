package com.android.mystock.ui.marketpages.bean;


import android.annotation.SuppressLint;

import com.android.mystock.common.utils.Utils;
import com.android.mystock.ui.marketpages.HqUtils;

import java.text.DecimalFormat;

/**
 * 行情数据bean
 */
@SuppressLint("ParcelCreator")
public class BeanHqMaket extends Stock {

    private String newPrice; //最新价格
    private float value_newPrice;

    private String zdf;//涨跌幅
    private float value_zdf;//涨跌幅

    private String zde;//涨跌额
    private float value_zde;//涨跌额

    private String zrsp;//昨日收盘
    private float value_zrsp;//昨日收盘

    private String jrkp;//今日开盘
    private float value_jrkp;//今日开盘

    private String cjl;//成交量
    private long value_cjl;

    private String cje;//成交额
    private long value_cje;

    private String hsl;//换手率
    private float value_hsl;

    private long value_zsz;//总市值
    private String zsz;//总市值

    private float value_zg;//最高
    private String zg;//最高

    private float value_zd;//最低
    private String zd;//最低

    private float value_zt;//涨停
    private String zt;//涨停

    private float value_dt;//跌停
    private String dt;//跌停

    private long value_wp;//外盘
    private String wp;//外盘

    private long value_np;//内盘
    private String np;//内盘

    private float value_wb;//委比
    private String wb;//委比

    private float value_lb;//量比
    private String lb;//量比

    private float value_junj;//均价
    private String junj;//均价

    private float value_zhf;//振幅
    private String zhf;//振幅

    private int state = 0; // -1跌 0平 1涨
    private int status;
    private int order;//默认排序

    /**
     * 所有数据填充完毕后初始化一下这个方法
     */
//    public void init()
//    {
//
////        if(value_cjl==0 && value_newPrice==0 && value_zdf==0)
////        {
////            setNewPrice("--");
////            setZdf("停牌");
////            setZde("停牌");
////            setCjl("停牌");
////        }
//
//
//    }
//***************************************************************************************************

    /**
     * 获取最近成交价格
     *
     * @return
     */
    public void setValue_newPrice(float value_newPrice) {

        this.value_newPrice = value_newPrice;
        if (value_newPrice == 0) {
            setNewPrice("--");
        } else {
            if (getPrecision() != 0) {
                DecimalFormat decimalFormat = HqUtils.precisionToString(getPrecision());
                String price = decimalFormat.format(value_newPrice);//format 返回的是字符串
                setNewPrice(price);
            }
        }
    }

    public float getValue_newPrice() {
        return value_newPrice;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

//***************************************************************************************************

    /**
     * 设置涨跌幅
     *
     * @param value_zdf
     */
    public void setValue_zdf(float value_zdf) {
        this.value_zdf = value_zdf;
        DecimalFormat decimalFormat = HqUtils.precisionToString(2);
        String zdfFormatStr = decimalFormat.format(value_zdf);//format 返回的是字符串
        setZdf(zdfFormatStr + "%");

        if (value_zdf > 0) {
            setState(1);
        } else if (value_zdf < 0) {
            setState(-1);
        } else {
            setState(0);
        }
    }

    public float getValue_zdf() {
        return value_zdf;
    }

    public String getZdf() {
        return zdf;
    }

    public void setZdf(String zdf) {
        this.zdf = zdf;
    }
//***************************************************************************************************

    /**
     * 设置涨跌额
     *
     * @param value_zde
     */
    public void setValue_zde(float value_zde) {
        this.value_zde = value_zde;
        DecimalFormat decimalFormat = HqUtils.precisionToString(getPrecision());
        String zde = decimalFormat.format(value_zde);//format 返回的是字符串
        setZde(zde);
    }

    public float getValue_zde() {
        return value_zde;
    }

    public String getZde() {
        return zde;
    }

    public void setZde(String zde) {
        this.zde = zde;
    }
//***************************************************************************************************

    /**
     * 昨日收盘价
     * @param value_zrsp
     */
    public void setValue_zrsp(float value_zrsp) {
        this.value_zrsp = value_zrsp;
        DecimalFormat decimalFormat = HqUtils.precisionToString(getPrecision());
        String zrsp = decimalFormat.format(value_zrsp);//format 返回的是字符串
        setZrsp(zrsp);
    }

    public float getValue_zrsp() {
        return value_zrsp;
    }

    public String getZrsp() {
        return zrsp;
    }

    public void setZrsp(String zrsp) {
        this.zrsp = zrsp;
    }
//***************************************************************************************************

    /**
     * 今日开盘价
     * @param value_jrkp
     */
    public void setValue_jrkp(float value_jrkp) {
        this.value_jrkp = value_jrkp;
        DecimalFormat decimalFormat = HqUtils.precisionToString(getPrecision());
        String jrkp = decimalFormat.format(value_jrkp);//format 返回的是字符串
        setJrkp(jrkp);
    }

    public float getValue_jrkp() {
        return value_jrkp;
    }

    public String getJrkp() {
        return jrkp;
    }

    public void setJrkp(String jrkp) {
        this.jrkp = jrkp;
    }
//***************************************************************************************************

    /**
     * 设置换手率
     *
     * @param value_hsl
     */
    public void setValue_hsl(float value_hsl) {
        this.value_hsl = value_hsl;
        DecimalFormat decimalFormat = HqUtils.precisionToString(2);
        String hslFormatStr = decimalFormat.format(value_hsl);//format 返回的是字符串
        setHsl(hslFormatStr + "%");
    }

    public float getValue_hsl() {
        return value_hsl;
    }

    public String getHsl() {
        return hsl;
    }

    public void setHsl(String hsl) {
        this.hsl = hsl;
    }
//***************************************************************************************************

    /**
     * 成交量
     *
     * @param value_cjl
     */
    public void setValue_cjl(long value_cjl) {
        this.value_cjl = value_cjl;
        setCjl(Utils.formatLongNumber(value_cjl));//格式化cjl
    }

    public long getValue_cjl() {
        return value_cjl;
    }

    public String getCjl() {
        return cjl;
    }

    public void setCjl(String cjl) {
        this.cjl = cjl;
    }
//***************************************************************************************************

    /**
     * 成交额
     *
     * @param value_cje
     */
    public void setValue_cje(long value_cje) {
        this.value_cje = value_cje;
        setCje(Utils.formatLongNumber(value_cje));//格式化cje
    }

    public long getValue_cje() {
        return value_cje;
    }

    public String getCje() {
        return cje;
    }

    public void setCje(String cje) {
        this.cje = cje;
    }
//***************************************************************************************************

    /**
     * 设置总市值
     *
     * @param value_zsz
     */
    public void setValue_zsz(long value_zsz) {
        this.value_zsz = value_zsz;
        setZsz(Utils.formatLongNumber(value_zsz));
    }

    public long getValue_zsz() {
        return value_zsz;
    }

    public String getZsz() {
        return zsz;
    }

    public void setZsz(String zsz) {
        this.zsz = zsz;
    }

//***************************************************************************************************

    /**
     * 最高
     *
     * @param value_zg
     */
    public void setValue_zg(float value_zg) {
        this.value_zg = value_zg;
        DecimalFormat decimalFormat = HqUtils.precisionToString(getPrecision());
        String zg = decimalFormat.format(value_zg);//format 返回的是字符串
        setZg(zg);
    }

    public float getValue_zg() {
        return value_zg;
    }

    public String getZg() {
        return zg;
    }

    public void setZg(String zg) {
        this.zg = zg;
    }
//***************************************************************************************************

    /**
     * 最低
     *
     * @param value_zd
     */
    public void setValue_zd(float value_zd) {
        this.value_zd = value_zd;
        DecimalFormat decimalFormat = HqUtils.precisionToString(getPrecision());
        String zd = decimalFormat.format(value_zd);//format 返回的是字符串
        setZd(zd);
    }

    public float getValue_zd() {
        return value_zd;
    }

    public String getZd() {
        return zd;
    }

    public void setZd(String zd) {
        this.zd = zd;
    }
//***************************************************************************************************

    /**
     * 涨停价
     *
     * @param value_zt
     */
    public void setValue_zt(float value_zt) {
        this.value_zt = value_zt;
        DecimalFormat decimalFormat = HqUtils.precisionToString(getPrecision());
        String zt = decimalFormat.format(value_zt);//format 返回的是字符串
        setZt(zt);
    }

    public float getValue_zt() {
        return value_zt;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }
//***************************************************************************************************

    /**
     * 跌停价
     *
     * @param value_dt
     */
    public void setValue_dt(float value_dt) {
        this.value_dt = value_dt;
        DecimalFormat decimalFormat = HqUtils.precisionToString(getPrecision());
        String dt = decimalFormat.format(value_dt);//format 返回的是字符串
        setDt(dt);
    }

    public float getValue_dt() {
        return value_dt;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }
//***************************************************************************************************

    /**
     * 内盘
     *
     * @param value_np
     */
    public void setValue_np(long value_np) {
        this.value_np = value_np;
        setNp(Utils.formatLongNumber(value_np));
    }

    public long getValue_np() {
        return value_np;
    }

    public String getNp() {
        return np;
    }

    public void setNp(String np) {
        this.np = np;
    }
//***************************************************************************************************

    /**
     * 外盘
     *
     * @param value_wp
     */
    public void setValue_wp(long value_wp) {
        this.value_wp = value_wp;
        setWp(Utils.formatLongNumber(value_wp));
    }

    public long getValue_wp() {
        return value_wp;
    }

    public String getWp() {
        return wp;
    }

    public void setWp(String wp) {
        this.wp = wp;
    }
//***************************************************************************************************

    /**
     * 委比
     *
     * @param value_wb
     */
    public void setValue_wb(float value_wb) {
        this.value_wb = value_wb;
        DecimalFormat decimalFormat = HqUtils.precisionToString(getPrecision());
        String wb = decimalFormat.format(value_wb);//format 返回的是字符串
        setWb(wb);
    }

    public float getValue_wb() {
        return value_wb;
    }

    public String getWb() {
        return wb;
    }

    public void setWb(String wb) {
        this.wb = wb;
    }
//***************************************************************************************************

    /**
     * 量比
     *
     * @param value_lb
     */
    public void setValue_lb(float value_lb) {
        this.value_lb = value_lb;
        DecimalFormat decimalFormat = HqUtils.precisionToString(getPrecision());
        String lb = decimalFormat.format(value_lb);//format 返回的是字符串
        setLb(lb);
    }

    public float getValue_lb() {
        return value_lb;
    }

    public String getLb() {
        return lb;
    }

    public void setLb(String lb) {
        this.lb = lb;
    }
//***************************************************************************************************

    /**
     * 均价
     *
     * @param value_junj
     */
    public void setValue_junj(float value_junj) {
        this.value_junj = value_junj;
        DecimalFormat decimalFormat = HqUtils.precisionToString(getPrecision());
        String junj = decimalFormat.format(value_junj);//format 返回的是字符串
        setJunj(junj);
    }

    public float getValue_junj() {
        return value_junj;
    }

    public String getJunj() {
        return junj;
    }

    public void setJunj(String junj) {
        this.junj = junj;
    }
//***************************************************************************************************

    /**
     * 设置振幅
     *
     * @param value_zhf
     */
    public void setValue_zhf(float value_zhf) {
        this.value_zhf = value_zhf;
        DecimalFormat decimalFormat = HqUtils.precisionToString(2);
        String zhfFormatStr = decimalFormat.format(value_zhf);//format 返回的是字符串
        setZhf(zhfFormatStr + "%");
    }

    public float getValue_zhf() {
        return value_zhf;
    }

    public String getZhf() {
        return zhf;
    }

    public void setZhf(String zhf) {
        this.zhf = zhf;
    }
//***************************************************************************************************
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;

//        if( status==-3 ||(value_cjl==0 && value_newPrice==0 && value_zdf==0))
//        {
//            setNewPrice("--");
//            setZdf("停牌");
//            setZde("停牌");
//            setCjl("停牌");
//
//        }

        if (value_cjl == 0 && value_newPrice == 0 && value_zdf == 0) {
            setNewPrice("--");
            setZdf("--");
            setZde("--");
            setCjl("--");
        }
    }
//***************************************************************************************************

    /**
     * 设置涨跌状态
     *
     * @param state
     */
    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

//***************************************************************************************************
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
//***************************************************************************************************
}

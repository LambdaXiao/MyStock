package com.android.mystock.ui.marketpages.mvp.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.mystock.data.consts.Consts;
import com.android.mystock.data.consts.ENOStyle;
import com.android.mystock.ui.homepages.mvp.presenter.I_Presenter;
import com.android.mystock.ui.homepages.mvp.view.I_View_Base;
import com.android.mystock.ui.marketpages.HqUtils;
import com.android.mystock.ui.marketpages.bean.BeanCJMX;
import com.android.mystock.ui.marketpages.bean.BeanHqMaket;
import com.android.mystock.ui.marketpages.mvp.model.HQService;
import com.android.mystock.ui.marketpages.mvp.view.I_View_5D;
import com.android.mystock.ui.marketpages.mvp.view.I_View_5FS;
import com.android.mystock.ui.marketpages.mvp.view.I_View_CJMX;
import com.android.mystock.ui.marketpages.mvp.view.I_View_FS;
import com.android.mystock.ui.marketpages.mvp.view.I_View_FSKX;
import com.android.mystock.ui.marketpages.mvp.view.I_View_HS;
import com.android.mystock.ui.marketpages.mvp.view.I_View_KX;
import com.android.mystock.ui.marketpages.mvp.view.I_View_MyStockList;
import com.android.mystock.ui.marketpages.mvp.view.I_View_ZS;
import com.eno.base.utils.TCRS;
import com.mystockchart_lib.charting.mychart.bean.DataParse;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 行情部分数据业务封装
 */
public class HQPresenterImpl implements I_Presenter {

    public Context context;
    private static final String FIELD = "1|2|3|4|6|7|8|11|16|17|18|20|21|27|96";// 行情列表正常请求的字段
    private int mKLineIndex = 0;// 默认指标为成交量
    private int mKLinePeriod = 0; // 默认周期为日线
    public  int mKLineRehabilitation = 0;// 0不复权 1前复权

    private I_View_ZS p_zs;//指数接口
    private I_View_HS p_hs;//沪深排行榜接口
    private I_View_FSKX p_fskx_base;//k线分时的基础数据部分
    private I_View_5D p_5D;//5档行情
    private I_View_MyStockList p_myStockList;//自选股
    private I_View_CJMX p_cjmx;//成交明细
    private I_View_FS p_fs;//分时
    private I_View_5FS p_5fs;//5日分时
    private I_View_KX p_kx;//K线

    /**
     * 构造函数
     *
     * @param context
     */
    public HQPresenterImpl(Context context) {
        this.context = context;
    }

    /**
     * 实现接口发送请求方法
     * 每一个页面请求方法
     * 带入I_P_Base基类 因为Activity中实现的接口类也是继承这个基类
     * 所以可以联系起来
     */
    public boolean sendRequest(I_View_Base presenter) {

        if (presenter instanceof I_View_ZS) {
            p_zs = (I_View_ZS) presenter;
            // 上证指数 、深圳成指、创业板指、沪深300、 中小板指、A股指数、B股指数、上证380、上证180、上证50、 成份A指、成份B指、深证100R、
            String zsStockStr = "000001.2|399001.1|399006.1|399300.1|399005.1|000002.2|000003.2|000009.2|000010.2|000016.2|399002.1|399003.1|399004.1|399007.1|399008.1";
            String params = "field="+FIELD+"&code="+zsStockStr;

            HQService.sendRequestOptional(context,params, new HQService.OnLoadDataListener() {
                @Override
                public void onSuccess(TCRS[] tcrs) {
                    parseZsTcrs(tcrs);
                }

                @Override
                public void onFailure(int errorId, String msg) {
                    p_zs.errorMessage(errorId,msg);
                }
            });
            Log.e("tag", "-------------------指数功能号：191.4----------------------------");
        }
        //沪深排行榜请求
        else if (presenter instanceof I_View_HS) {
            p_hs = (I_View_HS) presenter;
            // 上证指数 、深圳成指、创业板指
            String zsStockStr = "000001.2|399001.1|399006.1";
            String params = "field="+FIELD+"&code="+zsStockStr;

            HQService.sendRequestOptional(context,params, new HQService.OnLoadDataListener() {
                @Override
                public void onSuccess(TCRS[] tcrs) {
                    parseZsFromHsTcrs(tcrs);
                }

                @Override
                public void onFailure(int errorId, String msg) {
                    p_hs.errorMessage(errorId,msg);
                }
            });
            //涨幅榜
            if(p_hs.getVisibilityZfb()){

                HQService.sendRequestSort(context,"type=21&direct=1", new HQService.OnLoadDataListener() {
                    @Override
                    public void onSuccess(TCRS[] tcrs) {
                        parseRankTcrs(0,tcrs);
                    }

                    @Override
                    public void onFailure(int errorId, String msg) {
                        p_hs.errorMessage(errorId,msg);
                    }
                });
            }
            //跌幅榜
            if(p_hs.getVisibilityDfb()){

                HQService.sendRequestSort(context,"type=21&direct=0", new HQService.OnLoadDataListener() {
                    @Override
                    public void onSuccess(TCRS[] tcrs) {
                        parseRankTcrs(1,tcrs);
                    }

                    @Override
                    public void onFailure(int errorId, String msg) {
                        p_hs.errorMessage(errorId,msg);
                    }
                });
            }
            //换手率榜
            if(p_hs.getVisibilityHsl()){

                HQService.sendRequestSort(context,"type=27&direct=1", new HQService.OnLoadDataListener() {
                    @Override
                    public void onSuccess(TCRS[] tcrs) {
                        parseRankTcrs(2,tcrs);
                    }

                    @Override
                    public void onFailure(int errorId, String msg) {
                        p_hs.errorMessage(errorId,msg);
                    }
                });
            }
            //成交额榜
            if(p_hs.getVisibilityCje()){

                HQService.sendRequestSort(context,"type=18&direct=1", new HQService.OnLoadDataListener() {
                    @Override
                    public void onSuccess(TCRS[] tcrs) {
                        parseRankTcrs(3,tcrs);
                    }

                    @Override
                    public void onFailure(int errorId, String msg) {
                        p_hs.errorMessage(errorId,msg);
                    }
                });
            }
        }
        //分时K线基础查询
        else if (presenter instanceof I_View_FSKX) {//判断presenter是否是I_P_BankQuery的实例
            p_fskx_base = (I_View_FSKX) presenter;
            String field = "1|2|3|4|6|7|8|11|12|13|14|15|16|17|18|20|21|22|23|24|25|26|27|29|30|32|33|35|36|97|98|99|100|101|102|106|202|204|205|206|207";// 正常请求的字段
            String myStockStr = p_fskx_base.getStockCode();
            String params = "field="+field+"&code="+myStockStr;
            //股票代码不为空才发送请求
            if (!TextUtils.isEmpty(myStockStr)) {

                HQService.sendRequestOptional(context,params, new HQService.OnLoadDataListener() {
                    @Override
                    public void onSuccess(TCRS[] tcrs) {
                        fskxBase(tcrs);
                    }

                    @Override
                    public void onFailure(int errorId, String msg) {

                    }
                });
            }
            Log.e("tag", "-------------------分时K线基础：31000.4----------------------------");
        }

        //自选股
        else if (presenter instanceof I_View_MyStockList) {
            p_myStockList = (I_View_MyStockList) presenter;

            String myStockStr = p_myStockList.getStockCodes();
            String params = "field="+FIELD+"&code="+myStockStr;
            //股票代码不为空才发送请求
//            if (!TextUtils.isEmpty(myStockStr)) {

                HQService.sendRequestOptional(context,params, new HQService.OnLoadDataListener() {
                    @Override
                    public void onSuccess(TCRS[] tcrs) {
                        myStocktList(tcrs);
                    }

                    @Override
                    public void onFailure(int errorId, String msg) {
                        p_myStockList.errorMessage(errorId,msg);
                    }
                });
//            }
            Log.e("tag", "-------------------自选股功能号：31000 4----------------------------" + myStockStr);
        }

        //五档行情
        else if (presenter instanceof I_View_5D) {
            p_5D = (I_View_5D) presenter;

            String myStockStr = p_5D.getStockCode();
            String myExclude = p_5D.getExclude();
            String field = "1|2|3|4|8|11|13|14|15|16|17|18|20|21|22|23|24|25|26|27|28|29|30|31|32|33|35|36|38|39|50|51|52|53|54|55|56|57|58|59|60|61|70|71|72|73|74|75|76|77|78|79|80|81|201|202";
            String params = "field="+field+"&code="+myStockStr+"&exclude=" + myExclude;

            //股票代码不为空才发送请求
            if (!TextUtils.isEmpty(myStockStr)) {

                HQService.sendRequestOptional(context,params, new HQService.OnLoadDataListener() {
                    @Override
                    public void onSuccess(TCRS[] tcrs) {
                        wdhq(tcrs);
                    }

                    @Override
                    public void onFailure(int errorId, String msg) {

                    }
                });
            }
            Log.e("tag", "-------------------自选股功能号：31000 4----------------------------" + myStockStr);
        }

        //成交明细
        else if (presenter instanceof I_View_CJMX) {
            p_cjmx = (I_View_CJMX) presenter;

            int teg = 1;
            int offset = 0;
            int count = 20;
            int direct = p_cjmx.direct(); // 暂时不需要修改
            String time = p_cjmx.getTime();//获取时间
            if (!TextUtils.isEmpty(time)) {
                if (direct == 1)//代表取time时间后的数据
                {
                    teg = 2;
                } else {
                    count = 0;
                }
            }
            StringBuffer sbf = new StringBuffer();
            sbf.append("time=" + time + "&");
            sbf.append("direct=" + direct + "&");
            sbf.append("count=" + count + "&");
            sbf.append("offset=" + offset + "&");
            sbf.append("code=" + p_cjmx.getCode());
            String url = sbf.toString();

            final int finalTeg = teg;
            HQService.sendRequestDetails(context,url, new HQService.OnLoadDataListener() {
                @Override
                public void onSuccess(TCRS[] tcrs) {
                    cjmx(tcrs, finalTeg);
                }

                @Override
                public void onFailure(int errorId, String msg) {

                }
            });

            Log.e("tag", "-------------------行情列表功能 功能号：191.5----------------------------");
        }
        //分时请求
        else if (presenter instanceof I_View_FS) {
            p_fs = (I_View_FS) presenter;

            String code = p_fs.getStockCode();
            StringBuffer sbf = new StringBuffer();
            sbf.append("code=" + code + "&");
            sbf.append("date=0&");
            sbf.append("time=0&");
            sbf.append("freq=" + Consts.FSDATA_FREG + "&");
            sbf.append("close_xxld=0&");
//            if (m_hasDJ) {//是否大盘叠加
//                sbf.append("addup=1");
//            }
            String url = sbf.toString();

            HQService.sendRequestFs(context,url, new HQService.OnLoadDataListener() {
                @Override
                public void onSuccess(TCRS[] tcrs) {
                    DataParse mData = new DataParse();
                    mData.parseMinutes(tcrs);
                    p_fs.onResponseData(mData);
                }

                @Override
                public void onFailure(int errorId, String msg) {

                }
            });

            Log.e("tag", "-------------------分时请求 功能号：31000.1----------------------------");
        }
        //5日分时请求
        else if (presenter instanceof I_View_5FS) {
            p_5fs = (I_View_5FS) presenter;

            String code = p_5fs.getStockCode();
            StringBuffer sbf = new StringBuffer();
            sbf.append("code=" + code);

            String url = sbf.toString();

            HQService.sendRequest5Fs(context,url, new HQService.OnLoadDataListener() {
                @Override
                public void onSuccess(TCRS[] tcrs) {
                    DataParse mData = new DataParse();
                    mData.parseFiveDayMinutes(tcrs);
                    p_5fs.onResponseData(mData);
                }

                @Override
                public void onFailure(int errorId, String msg) {

                }
            });

            Log.e("tag", "-------------------5日分时请求 功能号：31000.11----------------------------");
        }
        //K线请求
        else if (presenter instanceof I_View_KX) {
            p_kx = (I_View_KX) presenter;

            String code = p_kx.getStockCode();
            mKLinePeriod = p_kx.getKLinePeriod();
            mKLineIndex = p_kx.getKLineIndex();
            mKLineRehabilitation = p_kx.getKLineRehabilitation();
            StringBuffer sbf = new StringBuffer();
            if (code != null) {
                sbf.append("code=" + code + "&");
            }
            sbf.append("type=" + Consts.TYPE_VALE[mKLinePeriod] + "&");
            sbf.append("count=" + Consts.count + "&");
            String tempStr = "";
            if (Consts.kxzb[mKLineIndex] != null && !"".equals(Consts.kxzb[mKLineIndex])) {
                tempStr = ":" + Consts.kxzb[mKLineIndex];
            }
            sbf.append("recover=" + mKLineRehabilitation + "&");
            sbf.append("techname=USEMA|3|5|10|30"+tempStr+"&");
            sbf.append("date=0&");
            sbf.append("direct=1");

            String url = sbf.toString();

            HQService.sendRequestKx(context,url, new HQService.OnLoadDataListener() {
                @Override
                public void onSuccess(TCRS[] tcrs) {
                    DataParse mData = new DataParse();
                    mData.setKLineindex(mKLineIndex);
                    mData.parseKLine(tcrs);
                    p_kx.onResponseData(mData);
                }

                @Override
                public void onFailure(int errorId, String msg) {

                }
            });

            Log.e("tag", "-------------------K线请求 功能号：31000.2----------------------------");
        }
        return false;
    }
/*
指数页数据
 */
    private void parseZsTcrs(TCRS[] tcrs){
        TCRS rs = tcrs[0];
        rs.moveFirst();
        ArrayList<BeanHqMaket> list1 = new ArrayList<BeanHqMaket>();
        ArrayList<BeanHqMaket> list2 = new ArrayList<BeanHqMaket>();
        ArrayList<BeanHqMaket> list3 = new ArrayList<BeanHqMaket>();
        int n = 0;
        while (!rs.IsEof()) {
            BeanHqMaket stock = new BeanHqMaket();
            stock.setPrecision(rs.getInt("precision"));
            stock.setStockName(rs.toString("name"));
            stock.setStockCode(rs.toString("code"));
            stock.setValue_newPrice(rs.getFloat2("zjcj"));
            stock.setValue_zdf(rs.getFloat2("zdf"));
            stock.setValue_zde(rs.getFloat2("zhd"));
            stock.setMaketID(rs.getInt("exchid"));
            if(n<6){
                list1.add(stock);
            }else if(n<12){
                list2.add(stock);
            }else if(n<18){
                list3.add(stock);
            }
            n++;
            rs.moveNext();
        }
        p_zs.onResponse(list1,list2,list3);
    }

    /*
    沪深中的指数数据
    */
    private void parseZsFromHsTcrs(TCRS[] tcrs){
        TCRS rs = tcrs[0];
        rs.moveFirst();
        ArrayList<BeanHqMaket> list1 = new ArrayList<BeanHqMaket>();
        while (!rs.IsEof()) {
            BeanHqMaket stock = new BeanHqMaket();
            stock.setPrecision(rs.getInt("precision"));
            stock.setStockName(rs.toString("name"));
            stock.setStockCode(rs.toString("code"));
            stock.setValue_newPrice(rs.getFloat2("zjcj"));
            stock.setValue_zdf(rs.getFloat2("zdf"));
            stock.setValue_zde(rs.getFloat2("zhd"));
            stock.setMaketID(rs.getInt("exchid"));
            list1.add(stock);

            rs.moveNext();
        }
        p_hs.onResponseZs(list1);
    }

    /*
    沪深中的排行榜数据
    */
    private void parseRankTcrs(int flag,TCRS[] tcrs){
        TCRS rs = tcrs[0];
        rs.moveFirst();
        ArrayList<BeanHqMaket> list1 = new ArrayList<BeanHqMaket>();
        while (!rs.IsEof()) {
            BeanHqMaket stock = new BeanHqMaket();
            stock.setPrecision(rs.getInt("precision"));
            stock.setStockName(rs.toString("name"));
            stock.setStockCode(rs.toString("code"));
            stock.setValue_newPrice(rs.getFloat2("zjcj"));
            stock.setValue_zdf(rs.getFloat2("zdf"));
            stock.setValue_zde(rs.getFloat2("zhd"));
            stock.setMaketID(rs.getInt("exchid"));
            stock.setValue_hsl(rs.getFloat2("hsl"));
            stock.setValue_cje(rs.getLong("cjje"));
            list1.add(stock);

            rs.moveNext();
        }
        if(flag == 0) {
            p_hs.onResponseZfb(list1);//回调涨幅榜数据
        }else if(flag == 1){
            p_hs.onResponseDfb(list1);//回调跌幅榜数据
        }else if(flag == 2){
            p_hs.onResponseHsl(list1);//回调换手率榜数据
        }else if(flag == 3){
            p_hs.onResponseCje(list1);//回调成交额榜数据
        }
    }
    /**
     * 分时K线页面基础数据
     *
     * @param tcrs
     */
    private void fskxBase(TCRS[] tcrs) {

        TCRS rs = tcrs[0];
        rs.moveFirst();
        BeanHqMaket stock = new BeanHqMaket();

        stock.setPrecision(rs.getInt("precision"));
        stock.setStockName(rs.toString("name"));
        stock.setStockCode(rs.toString("code"));
        stock.setLotsize(rs.getInt("lotsize"));
        stock.setValue_newPrice(rs.getFloat2("zjcj"));
        stock.setValue_zdf(rs.getFloat2("zdf"));
        stock.setValue_zde(rs.getFloat2("zhd"));
        stock.setMaketID(rs.getInt("exchid"));
        stock.setValue_hsl(rs.getFloat2("hsl"));
        stock.setValue_cje(rs.getLong("cjje"));
        stock.setValue_cjl(rs.getLong("cjsl"));
        stock.setValue_zrsp(rs.getFloat2("zrsp"));
        stock.setValue_jrkp(rs.getFloat2("jrkp"));
        stock.setValue_np(rs.getLong("np"));
        stock.setValue_wp(rs.getLong("wp"));
        stock.setValue_wb(rs.getFloat2("wb"));
        stock.setValue_lb(rs.getFloat2("lb"));
        stock.setValue_zt(rs.getFloat2("zt"));
        stock.setValue_dt(rs.getFloat2("dt"));
        stock.setValue_zg(rs.getFloat2("zgcj"));
        stock.setValue_zd(rs.getFloat2("zdcj"));
        stock.setValue_junj(rs.getFloat2("cjjj"));
        stock.setValue_zhf(rs.getFloat2("zhf"));

        p_fskx_base.onResponseStock(stock);


    }

    /**
     * 成交明细
     *
     * @param tcrs
     */
    private void cjmx(TCRS[] tcrs, int teg) {
        // 0_[3](direct)买卖方向  1_[3](fbtm)采样时间  2_[6](zjcj)最近  3_[5](cjsl)成交数量  4_[5](cjje)成交金额  5_[3](tcount)成交笔数
        // 1   1459   27.92   2600   72592   0

        float jrkp = tcrs[0].getFloat2("jrkp");//今日开盘
        int lotSize = tcrs[0].getInt("lotsize");

        TCRS rs1 = tcrs[1];
        List<BeanCJMX> mList = new ArrayList<BeanCJMX>();

        DecimalFormat decimalFormat = HqUtils.precisionToString(2);

        rs1.moveFirst();
        while (!rs1.IsEof()) {
            //成交时间
            String time = rs1.toString("fbtm");

            if (time.length() == 3) {
                time = "0" + time;
            }
            if (time.length() > 2) {
                time = time.substring(0, 2) + ":" + time.substring(2);
            } else {
                time = "00:00";
            }

            //价格
            float zjcj = rs1.getFloat2("zjcj");
            String zjcjStr = "<font color=\"" + HqUtils.getHQColor(jrkp, zjcj) + "\">" + decimalFormat.format(zjcj) + "</font>";

            //成交量和买卖方向
            boolean direct = rs1.getBoolean(0);//买卖方向
            String directStr = direct ? "B" : "S";

            String cjlStr = "";
            int cjsl = rs1.getInt("cjsl") / lotSize;//手
            if (direct) {
                cjlStr = "<font color=\"" + ENOStyle.hq_up + "\">" + cjsl + "B</font>";
            } else {
                cjlStr = "<font color=\"" + ENOStyle.hq_down + "\">" + cjsl + "S</font>";
            }

            BeanCJMX beanCJMX = new BeanCJMX();
            beanCJMX.setTime("<font >" + time + "</font>");
            beanCJMX.setPrice(zjcjStr);
            beanCJMX.setCjl(cjlStr);
            beanCJMX.setTimeValue(rs1.toString("fbtm"));
            mList.add(beanCJMX);
            rs1.moveNext();
        }

        if (teg == 1) {
            p_cjmx.onResponse(mList);
        } else {
            p_cjmx.onResponseMore(mList);

        }

    }


    /**
     * 五档行情
     *
     * @param tcrs
     */
    private void wdhq(TCRS[] tcrs) {
        // Field=  0_[4](exchid)交易所代码 1_[4](classid)商品类型 2_[101](name)名称 1_[101](code)代码  2_[4](lotsize)每手  3_[6](zrsp)昨收  4_[6](jrkp)今开
        // 5_[6](zgcj)最高  6_[6](zdcj)最低  7_[6](zjcj)最近  8_[5](cjsl)成交数量  9_[5](cjje)成交金额  10_[6](zhd)涨跌
        // 11_[6](zdf)涨跌幅度  12_[6](zhf)震幅  13_[6](averj)核算价  14_[6](zt)涨停价  15_[6](dt)跌停价  16_[6](syl)市盈率
        // 17_[6](hsl)换手率  18_[5](zl)增量  19_[6](wb)委比  20_[6](lb)量比  21_[6](m5zdf)5分钟涨跌  22_[6](cjjj)成交均价
        // 23_[5](lastupd)最后更新  24_[5](np)内盘  25_[5](wp)外盘  26_[5](ze)增额  27_[5](zc)增仓
        // 28_[6](bj1)买价1 29_[5](bl1)买量1  30_[6](bj2)买价2  31_[4](bl2)买量2  32_[6](bj3)买价3  33_[4](bl3)买量3
        //34_[6](bj4)买价4 35_[4](bl4)买量4  36_[6](bj5)买价5  37_[4](bl5)买量5  38_[6](bj6)买价6  39_[4](bl6)买量6
        // 40_[6](sj1)卖价1 41_[5](sl1)卖量1  42_[6](sj2)卖价2  43_[4](sl2)卖量2  44_[6](sj3)卖价3  45_[4](sl3)卖量3
        // 46_[6](sj4)卖价 47_[4](sl4)卖量4  48_[6](sj5)卖价5  49_[4](sl5)卖量5  50_[6](sj6)卖价6  51_[4](sl6)卖量6
        // 52_[4](precision)小数精度
        // 53_[4](lotsize)每
        // p_fskx_base.onResponseBase();

        if (tcrs[0].getRecords() > 0) {
            DecimalFormat decimalFormat = HqUtils.precisionToString(tcrs[0].getInt("precision"));


            float jrkp = tcrs[0].getFloat2("jrkp");

            float bj1 = tcrs[0].getFloat2("bj1");
            float bj2 = tcrs[0].getFloat2("bj2");
            float bj3 = tcrs[0].getFloat2("bj3");
            float bj4 = tcrs[0].getFloat2("bj4");
            float bj5 = tcrs[0].getFloat2("bj5");

            float sel1 = tcrs[0].getFloat2("sj1");
            float sel2 = tcrs[0].getFloat2("sj2");
            float sel3 = tcrs[0].getFloat2("sj3");
            float sel4 = tcrs[0].getFloat2("sj4");
            float sel5 = tcrs[0].getFloat2("sj5");

            String dt = "<font color=\"#27782E\">" + decimalFormat.format(tcrs[0].getFloat2("dt")) + "</font>";
            String zt = "<font color=\"#7F2D16\">" + decimalFormat.format(tcrs[0].getFloat2("zt")) + "</font>";
            p_5D.onResponseDTZT(dt, zt);

            String market = tcrs[0].toString("exchid");
            String name = tcrs[0].toString("name");
            String type = tcrs[0].toString("classid");

            p_5D.onResponse(market, name, type);

            float zrsp = tcrs[0].getFloat2("zrsp");//昨收
            float zjcj = tcrs[0].getFloat2("zjcj");//现价

            String zs = "<font color=\"#DAA520\">" + decimalFormat.format(zrsp) + "</font>";//昨收
            String jk = "<font color=\"" + HqUtils.getHQColor(zrsp, jrkp) + "\">" + jrkp + "</font>";//今开
            String xj = "<font color=\"" + HqUtils.getHQColor(zrsp, zrsp) + "\">" + decimalFormat.format(zjcj) + "</font>";//现价

            p_5D.onResponseZSJK(zs, xj);

            String bj1Str = decimalFormat.format(bj1);
            String bj2Str = decimalFormat.format(bj2);
            String bj3Str = decimalFormat.format(bj3);
            String bj4Str = decimalFormat.format(bj4);
            String bj5Str = decimalFormat.format(bj5);

            String sel1Str = decimalFormat.format(sel1);
            String sel2Str = decimalFormat.format(sel2);
            String sel3Str = decimalFormat.format(sel3);
            String sel4Str = decimalFormat.format(sel4);
            String sel5Str = decimalFormat.format(sel5);

            String buy1 = "<font color=\"" + HqUtils.getHQColor(jrkp, bj1) + "\">" + bj1Str + "</font>";
            String buy2 = "<font color=\"" + HqUtils.getHQColor(jrkp, bj2) + "\">" + bj2Str + "</font>";
            String buy3 = "<font color=\"" + HqUtils.getHQColor(jrkp, bj3) + "\">" + bj3Str + "</font>";
            String buy4 = "<font color=\"" + HqUtils.getHQColor(jrkp, bj4) + "\">" + bj4Str + "</font>";
            String buy5 = "<font color=\"" + HqUtils.getHQColor(jrkp, bj5) + "\">" + bj5Str + "</font>";

            String sell1 = "<font color=\"" + HqUtils.getHQColor(jrkp, sel1) + "\">" + sel1Str + "</font>";
            String sell2 = "<font color=\"" + HqUtils.getHQColor(jrkp, sel2) + "\">" + sel2Str + "</font>";
            String sell3 = "<font color=\"" + HqUtils.getHQColor(jrkp, sel3) + "\">" + sel3Str + "</font>";
            String sell4 = "<font color=\"" + HqUtils.getHQColor(jrkp, sel4) + "\">" + sel4Str + "</font>";
            String sell5 = "<font color=\"" + HqUtils.getHQColor(jrkp, sel5) + "\">" + sel5Str + "</font>";

            p_5D.onResponse5D(buy1, buy2, buy3, buy4, buy5, sell1, sell2, sell3, sell4, sell5);
            int bl1 = tcrs[0].getInt("bl1") / 100;
            int bl2 = tcrs[0].getInt("bl2") / 100;
            int bl3 = tcrs[0].getInt("bl3") / 100;
            int bl4 = tcrs[0].getInt("bl4") / 100;
            int bl5 = tcrs[0].getInt("bl5") / 100;

            int sl1 = tcrs[0].getInt("sl1") / 100;
            int sl2 = tcrs[0].getInt("sl2") / 100;
            int sl3 = tcrs[0].getInt("sl3") / 100;
            int sl4 = tcrs[0].getInt("sl4") / 100;
            int sl5 = tcrs[0].getInt("sl5") / 100;

            String buy1L = "<font color=\"#DAA520\">" + bl1 + "</font>";
            String buy2L = "<font color=\"#DAA520\">" + bl2 + "</font>";
            String buy3L = "<font color=\"#DAA520\">" + bl3 + "</font>";
            String buy4L = "<font color=\"#DAA520\">" + bl4 + "</font>";
            String buy5L = "<font color=\"#DAA520\">" + bl5 + "</font>";

            String sell1L = "<font color=\"#DAA520\">" + sl1 + "</font>";
            String sell2L = "<font color=\"#DAA520\">" + sl2 + "</font>";
            String sell3L = "<font color=\"#DAA520\">" + sl3 + "</font>";
            String sell4L = "<font color=\"#DAA520\">" + sl4 + "</font>";
            String sell5L = "<font color=\"#DAA520\">" + sl5 + "</font>";
            p_5D.onResponse5DL(buy1L, buy2L, buy3L, buy4L, buy5L, sell1L, sell2L, sell3L, sell4L, sell5L);
        }

    }


    /**
     * 自选股
     *
     * @param tcrs
     */
    public void myStocktList(TCRS[] tcrs) {

        ArrayList<BeanHqMaket> list = new ArrayList<BeanHqMaket>();
        TCRS rs = tcrs[0];
        rs.moveFirst();
        int i = 0;
        while (!rs.IsEof()) {
            BeanHqMaket beanHqMaket = new BeanHqMaket();
            beanHqMaket.setStockCode(rs.toString("code"));//股票代码
            beanHqMaket.setStockName(rs.toString("name"));//股票名称
            beanHqMaket.setMaketID(rs.getInt("exchid"));//股票市场
            beanHqMaket.setClassId(rs.getInt("classid"));//股票类型
            beanHqMaket.setPrecision(rs.getInt("precision"));//小数点精度
            beanHqMaket.setLotsize(rs.getInt("lotsize"));//每首
            beanHqMaket.setStockCodeex(rs.toString("codeex"));

            beanHqMaket.setValue_newPrice(rs.getFloat2("zjcj"));//最近成交

            beanHqMaket.setValue_zdf(rs.getFloat2("zdf"));//涨跌幅

            beanHqMaket.setValue_zde(rs.getFloat2("zhd"));//涨跌

            beanHqMaket.setValue_cjl(rs.getLong("cjsl"));//成交数量

            beanHqMaket.setValue_zsz(rs.getLong("cjsl"));
            beanHqMaket.setStatus(rs.getInt("status"));
            list.add(beanHqMaket);

            rs.moveNext();

        }
        p_myStockList.onResponse(list);//回调
    }



}

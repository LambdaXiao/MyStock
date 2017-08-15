package com.android.mystock.data.consts;

/**
 * 定义APP的一些全局常量
 */
public class Consts {

    public static final int port = 8002;   //服务器端口
    public static final String defaultIp = "106.37.173.34";//测试服务器地址210.74.1.90，172.16.101.65
    public static final int LUA_NUMBE = 300;//lua服务功能号300，310

    //*****************************分时常量定义*****************************************************

    public final static int FSDATA_FREG = 1;// 分时频率
    // 各行情最大采样点数
    public static final int MAXCYD_SH_SZ = 240; // 深沪A股 交易4小时
    public static final int MAXCYD_HK = 330;// 港股 交易5个半小时
    public static final int MAXCYD_CF = 270;// 中金所 上午2小时15分钟，下午2小时15分钟
    public static final int MAXCYD_SHEF = 215;// 上海期交所 上午2小时15分钟，下午80分钟
    public static final int MAXCYD_DCE_CZCE = 225;// 大连-郑州期交所,上午2小时15分钟，下午90分钟

    /* 各市场ID */
    public static final int MARKET_SZ = 1; // '深交所', 'SZ',
    // '930-1130,1300-1500', '深圳证券交易所
    // SZSE'
    public static final int MARKET_SH = 2; // '上交所', 'SH',
    // '930-1130,1300-1500', '上海证券交易所
    // SSE'
    public static final int MARKET_HK = 6;// '港交所', 'HK', '930-1200,1330-1600',
    // '香港交易所 HKEX'
    public static final int MARKET_SB = 5;// 三板
    public static final int MARKET_CF = 4;// '中金所', 'CF', '915-1130,1300-1515',
    // '中国金融期货交易所 CFFEX'
    public static final int MARKET_SHFE = 7;// '上海期交所', 'SHFE',
    // '900-1015,1030-1130,1330-1410,1420-1500',
    // '上海期货交易所 SHFE'
    public static final int MARKET_DCE = 8;// '大连期交所', 'DCE',
    // '900-1015,1030-1130,1330-1500',
    // '大连期货交易所 DCE'
    public static final int MARKET_CZCE = 9;// '郑州期交所', 'CZCE',
    // '900-1015,1030-1130,1330-1500',
    // '郑州期货交易所 CZCE'

    //*************************K线常量定义**************************************************

    /**
     * K线分类(05分钟数据)
     */
    public static final int KX_5MIN = 0x101;
    /**
     * K线分类(15分钟数据)
     */
    public static final int KX_15MIN = 0x103;
    /**
     * K线分类(30分钟数据)
     */
    public static final int KX_30MIN = 0x106;
    /**
     * K线分类(60分钟数据)
     */
    public static final int KX_60MIN = 0x10C;
    /**
     * K线分类(日K线数据)
     */
    public static final int KX_DAY = 0x201;
    /**
     * K线分类(周K线数据)
     */
    public static final int KX_WEEK = 0x301;
    /**
     * K线分类(月K线数据)
     */
    public static final int KX_MONTH = 0x401;

    public static final int KX_TYPE_KEY[] = { KX_DAY,KX_WEEK, KX_MONTH,KX_5MIN, KX_15MIN,KX_30MIN, KX_60MIN };
    public static final int TYPE_VALE[] = { 513, 769, 1025, 257, 259, 262, 268 };

    public static String kxzb[] = {"","USEMACD|3|12|26|9","USEKDJ|3|9|3|3","USERSI|3|6|12|24","USEWR|2|10|6","USEBOLL|1|20","USEDMA|3|10|50|10","USEAROON|1|14","USECCI|1|14","USESAR|2|2|2"};
    public static String kxzbName[] = {"成交量","MACD","KDJ","RSI","WR","BOLL","DMA","AROON","CCI","SAR"};
    public static String kxzbLabel[] = {"VOL","MACD(12,26,9)","KDJ(9,3,3)","RSI(6,12,24)","WR(10,6)","BOLL(20)","DMA(10,50,10)","AROON(14)","CCI(14)","SAR(2,2)"};

    public static int count = 200;// 默认K线请求数据行数
    public static int fqtype = 0;// 0不复权 1前复权






}

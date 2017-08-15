package com.android.mystock.ui.marketpages.bean;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * 股票代码实体类
 */
public class Stock implements Cloneable, Parcelable {

    public final static int DISABLE = 1;//股票停牌
    public final static int NORMAL = 0;//股票正常


    public final static int SZ = 1;//深圳交易所
    public final static int SH = 2;//上海交易所
    //public final static int HK=3;//香港交易所
    public final static int HK = 6;//香港交易所
    public final static int ZGJRQH = 4;//中国金融期货交易所
    public final static int SB = 5;//三板市场
    public final static int NJXH = 6;//南京现货


    public final static int GOODS_INDEX = 2;    // 指数
    public final static int GOODS_STOCK = 3;                // 股票
    public final static int GOODS_FUND = 4;                    // 基金
    public final static int GOODS_WARRANT = 5;                // 权证
    public final static int GOODS_BOND = 6;                // 债券
    public final static int GOODS_EXCHANGE = 7;            // 汇率
    public final static int GOODS_FUTURES = 8;                // 期货
    public final static int GOODS_WAREHOUSE = 9;            // 现货

    // 第二层子类
    // 股票
    public final static int GOODS_STK_A = 100;                // A股
    public final static int GOODS_STK_B = 101;                // B股
    public final static int GOODS_STK_S = 110;              // 创业板
    public final static int GOODS_STK_HKM = 108;            // 香港主板
    public final static int GOODS_STK_HKS = 109;            // 香港创业板
    // 基金
    public final static int GOODS_FUND_CLOSE = 102;            // 封闭式基金
    public final static int GOODS_FUND_OPEN = 103;            // 开放式基金
    // 债券
    public final static int GOODS_BOND_QZ = 104;                // 企债
    public final static int GOODS_BOND_GZ = 105;                // 国债
    public final static int GOODS_BOND_HG = 106;                // 回购
    public final static int GOODS_BOND_KZ = 107;                // 可转债

    // 第三层子类
    public final static int GOODS_STK_A_ZXB = 1001;             // 深圳A股中小板
    public final static int GOODS_STK_A_ST = 1002;              // ST股票，涨跌停限制为5%
    public final static int GOODS_STK_A_NEW = 1003;              // 新股上市首日，无涨跌停限制


    private String StockCode; //股票代码
    private String StockName; //股票名称
    private String stockCodeex;
    private int maketID;    //股票市场
    private int classId = -1;    //股票类别
    private int isDisable;   //是否停用
    private int precision = 2; //小数点精度
    private int lotsize;//每手

    public int getLotsize() {
        return lotsize;
    }

    public void setLotsize(int lotsize) {
        this.lotsize = lotsize;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public String getStockCode() {
        return StockCode;
    }

    public void setStockCode(String stockCode) {
        StockCode = stockCode;
    }

    public String getStockName() {
        return StockName;
    }

    public void setStockName(String stockName) {
        StockName = stockName;
    }

    public int getMaketID() {
        return maketID;
    }

    public void setMaketID(int maketID) {
        this.maketID = maketID;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getIsDisable() {
        return isDisable;
    }

    public void setIsDisable(int isDisable) {
        this.isDisable = isDisable;
    }

    /**
     * 获取带市场股票代码
     *
     * @return
     */
    public String getStockCodeex() {

        if (stockCodeex == null) {
            if (getMaketID() != 0) {
                stockCodeex = getStockCode() + "." + getMaketID();
            } else {
                stockCodeex = getStockCode();
            }

        }
        return stockCodeex;
    }

    public void setStockCodeex(String stockCodeex) {
        this.stockCodeex = stockCodeex;
    }

    /**
     * 返回带市场的股票代码
     *
     * @return
     */
    public String getStockCodeAndMaket() {

        if (maketID == 0) {
            return StockCode;
        }

        return StockCode + "." + maketID;
    }

    public Object clone() {
        Stock stock = null;
        try {
            stock = (Stock) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return stock;
    }

    public static final Creator<Stock> CREATOR =
            new Creator<Stock>() {

                @Override
                public Stock createFromParcel(Parcel arg0) {
                    Stock stock = new Stock();
                    stock.setStockCode(arg0.readString());
                    stock.setStockName(arg0.readString());
                    stock.setStockCodeex(arg0.readString());
                    stock.setMaketID(arg0.readInt());
                    stock.setClassId(arg0.readInt());
                    stock.setIsDisable(arg0.readInt());
                    stock.setLotsize(arg0.readInt());
                    stock.setPrecision(arg0.readInt());
                    return stock;
                }

                @Override
                public Stock[] newArray(int arg0) {
                    // TODO Auto-generated method stub
                    return null;
                }
            };

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(StockCode);
        dest.writeString(StockName);
        dest.writeString(stockCodeex);
        dest.writeInt(maketID);
        dest.writeInt(classId);
        dest.writeInt(isDisable);
        dest.writeInt(precision);
        dest.writeInt(lotsize);
    }

}

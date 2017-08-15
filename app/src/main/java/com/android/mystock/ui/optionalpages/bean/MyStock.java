package com.android.mystock.ui.optionalpages.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xmf on 16/4/5.
 * emil: 693559916@qq.com
 *自选股分组
 */
public class MyStock implements Cloneable,Parcelable{
    private String StockCode; //股票代码
    private String StockName; //股票名称
    private int  maketID;    //股票市场
    private int classId ;    //股票类别
    private int state;//当前状态 0表示正常 1表示待删



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
    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }
    /**
     * 返回带市场的股票代码
     * @return
     */
    public String getStockCodeAndMaket()
    {

        return StockCode+"."+maketID;

    }

    public Object clone(){
        MyStock stock = null;
        try {
            stock = (MyStock)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return stock;
    }

    public static final Creator<MyStock> CREATOR =
            new Creator<MyStock>(){

                @Override
                public MyStock createFromParcel(Parcel arg0) {
                    MyStock stock =new MyStock() ;
                    stock.setStockCode(arg0.readString()) ;
                    stock.setStockName(arg0.readString()) ;
                    stock.setMaketID(arg0.readInt());
                    stock.setClassId(arg0.readInt()) ;
                    stock.setState(arg0.readInt()) ;
                    return stock;
                }

                @Override
                public MyStock[] newArray(int arg0) {
                    // TODO Auto-generated method stub
                    return null;
                }} ;

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(StockCode);
        dest.writeString(StockName);
        dest.writeInt(maketID) ;
        dest.writeInt(classId) ;
        dest.writeInt(state) ;
    }

}

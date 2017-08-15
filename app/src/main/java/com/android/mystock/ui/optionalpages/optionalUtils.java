package com.android.mystock.ui.optionalpages;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.android.mystock.common.utils.ToastUtils;
import com.android.mystock.data.database.DBHelper;
import com.android.mystock.data.database.DataBaseManager;
import com.android.mystock.ui.optionalpages.bean.MyStock;


/**
 * 自选股增，删，查方法
 */
public class optionalUtils {

    public static DataBaseManager db;
    /**
     * 查询股票是否在自选股中
     *
     * @param code
     * @return
     */
    public static boolean queryCode(Context context,String code) {
        boolean isExit = false;
        if (db == null) {
            db = DataBaseManager.getInstance(context);
        }
        if (code == null) {
            return isExit;
        }

        String args[] = null;
        if (code.indexOf(".") > -1) {
            args = code.split("\\.");
        }
        if (args == null || args.length < 2) {
            return isExit;
        }
        String sql = "select * from " + DBHelper.TABLENAME3
                + " where stockCode= ? and maketID= ? and selfGroup=?";
        Cursor cursor = null;
        try {
            cursor = db.queryData2Cursor(sql, new String[] { args[0], args[1],
                    "0" });
            isExit = cursor.moveToFirst();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return isExit;
    }

    // 删除股票
    public static boolean deleteStock(Context context,String code) {
        boolean isDeleted = false;
        String args[] = null;
        if (db == null) {
            db = DataBaseManager.getInstance(context);
        }
        if (code == null) {
            return isDeleted;
        }

        if (code.indexOf(".") > -1) {
            args = code.split("\\.");
        }
        if (args == null || args.length < 2) {
            return isDeleted;
        }

        String whereString = "stockCode= ?"
                + " and  maketID = ? and selfGroup=?";

        int id = db.deleteData(DBHelper.TABLENAME3, whereString, new String[] {
                args[0], args[1], "0" });
        if (id > 0) {
            isDeleted = true;
            ToastUtils.showShort(context,"删除成功！");
        }
        return isDeleted;
    }

    // 添加股票
    public static boolean addStock(Context context,MyStock mStock) {
        boolean isAdd = false;
        if (db == null) {
            db = DataBaseManager.getInstance(context);
        }
        if (mStock == null) {
            return isAdd;
        }
        String code = mStock.getStockCodeAndMaket();
        if (!queryCode( context,code)) {
            ContentValues values = new ContentValues();
            values.put("stockCode", mStock.getStockCode());
            values.put("stockName", mStock.getStockName());
            values.put("maketID", mStock.getMaketID());
            values.put("stockClass", mStock.getClassId());
            values.put("state", 0);
            values.put("stockOrder", 0);
            values.put("selfGroup", 0);
            values.put("updateTime", System.currentTimeMillis());
            Long l = db.insertData(DBHelper.TABLENAME3, values);
            String whereString = "id" + "=" + l;
            values.clear();
            values.put("stockOrder", l);
            db.updataData(DBHelper.TABLENAME3, values, whereString, null);
            values.clear();
            isAdd = true;
            ToastUtils.showShort(context,"添加成功！");
        } else {
            ToastUtils.showShort(context,"已经在自选股列表了！");
        }

        return isAdd;
    }
}

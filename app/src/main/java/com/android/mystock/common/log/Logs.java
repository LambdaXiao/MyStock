package com.android.mystock.common.log;

import android.util.Log;

import com.eno.base.utils.TCRS;

/**
 * Log统一管理类
 *
 */
public class Logs {

    private Logs() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化
    private static final String TAG = "Log：";

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        if (isDebug)
            Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (isDebug)
            Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (isDebug)
            Log.e(TAG, msg);
    }

    public static void v(String msg) {
        if (isDebug)
            Log.v(TAG, msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    /**
     * 打印结果集数组
     *
     * @param tcrs  结果集数组
     * @param field 某个字段
     * @param isALL 是否全部打印
     */
    public static void printTCRS(TCRS[] tcrs, String field, boolean isALL) {

        if (isDebug) {


            for (int e = 0; e < tcrs.length; e++) {
                Logs.e(e + "结果集个数size=" + tcrs.length);
                if (tcrs[e] != null) {


                    tcrs[e].moveFirst();
                    Logs.e("row=" + tcrs[e].getRecords() + "  " + tcrs[e].getRecordInfo());

                    int fieldNum = tcrs[e].getFields();
                    String fieldStr = "";
                    for (int i = 0; i < fieldNum; i++) {
                        fieldStr += "  " + i + "_[" + tcrs[e].getField(i).fieldType
                                + "]" + "(" + tcrs[e].getField(i).fieldName + ")"
                                + tcrs[e].getField(i).FieldDesc;
                    }
                    Logs.e("Field=" + fieldStr);

                    int k = 0;
                    while (!tcrs[e].IsEof()) {
                        String temp = "";
                        for (int j = 0; j < fieldNum; j++) {
                            if (isALL) {
                                temp += " {" + j + "}  " + tcrs[e].toString(j);
                            }

                            //if (field == null || field.equals(tcrs[e].getField(j).fieldName)) {
                            //	Log.v(k + "___" + j + "", (tcrs[e].toString(j) + "\t"));
                            //}
                        }

                        Logs.e(k + "___" + temp);
                        k++;
                        tcrs[e].moveNext();
                    }
                    tcrs[e].moveFirst();
                }
            }


        }
    }


}
package com.android.mystock.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.android.mystock.common.utils.AppUtils;


/**
 * 所有SharedPreferences的存储与获取
 */
public class Setting {

    //所有SharedPreferences的key值统一在这里面
    private static final String AUTOREFRESH         = "AutoRefreshTime"; //定时刷新时间
    private static final String MYSTOCK_FIEL_NAME   = "myStock";//存储自选股的key
    private static final String SHOW_USER_GUIDE 	= "ShowUserGuideSwitch";//是否显示引导页
    private static final String DATE_TIME_STOCKS    = "date_time_stocks";//同步股票字典时间
    private static final String SESSION             = "CURRENT_SESSION";//session串
    private static final String NIGHT_MODE             = "night_mode";//夜间模式


    /**
     * 设置行情数据定时刷新时间
     * @param context
     * @return
     */
    public static void setAutoRefreshTime(Context context, int value){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = prefs.edit();
        editor.putInt(AUTOREFRESH, value);
        editor.commit();
    }

    /**
     * 获取行情数据定时刷新时间
     * @param context
     * @return
     */
    public static int getAutoRefreshTime(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(AUTOREFRESH, 5000);//默认5秒
    }

    /**
     * 设置自选股
     * @param context
     * @return
     */
    public static void setMyStock(Context context, String value){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = prefs.edit();
        editor.putString(MYSTOCK_FIEL_NAME, value);
        editor.commit();
    }

    /**
     * 获取自选股
     * @param context
     * @return
     */
    public static String getMyStock(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(MYSTOCK_FIEL_NAME, "");
    }

    /**
     * 设置最新版本的引导页是否已经显示
     * @param context
     * @return
     */
    public static void setShowUserGuideSwitch(Context context, Boolean value){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = prefs.edit();
        editor.putBoolean(SHOW_USER_GUIDE + AppUtils.getVersionName(context), value);
        editor.commit();
    }

    /**
     * 判断最新版本的引导页是否已经显示
     * @param context
     * @return
     */
    public static Boolean getShowUserGuideSwitch(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(SHOW_USER_GUIDE + AppUtils.getVersionName(context), true);
    }

    /**
     * 设置同步股票字典的时间
     * @param context
     * @return
     */
    public static void setSyncTime(Context context, String value){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = prefs.edit();
        editor.putString(DATE_TIME_STOCKS, value);
        editor.commit();
    }

    /**
     * 获取同步股票字典的时间
     * @param context
     * @return
     */
    public static String getSyncTime(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(DATE_TIME_STOCKS, "");
    }

    /**
     * 设置Session
     * @param context
     * @return
     */
    public static void setSession(Context context, String value){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = prefs.edit();
        editor.putString(SESSION, value);
        editor.commit();
    }

    /**
     * 获取Session
     * @param context
     * @return
     */
    public static String getSession(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(SESSION, "");
    }

    /**
     * 设置夜间模式
     * @param context
     * @return
     */
    public static void setNightMode(Context context, Boolean value){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = prefs.edit();
        editor.putBoolean(NIGHT_MODE, value);
        editor.commit();
    }

    /**
     * 获取夜间模式
     * @param context
     * @return
     */
    public static Boolean getNightMode(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(NIGHT_MODE, true);
    }

}

package com.android.mystock.common.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 跟App相关的辅助类
 */
public class AppUtils {

    private AppUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * [获取应用程序版本号信息]
     *
     * @param context
     * @return 当前应用的版本号
     */
    public static int getVersionCode(Context context) {
        int verCode = 0;
        try {
            verCode = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return verCode;
    }

    public static String getPhoneInformation(Context context) {
        //单应用内存大小
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int memClass = activityManager.getMemoryClass();

        String result = "手机型号:"+ PhoneInfoUtils.getPhoneModel() + "\n" + "手机系统:" + PhoneInfoUtils.getPhoneSystemVersion() + "\n"
                + "屏幕分辨率:" + PhoneInfoUtils.screenHeight + "x"+ PhoneInfoUtils.screenWidth+ "\n" + "手机内存:" + PhoneInfoUtils.getMemoryInfo() + "\n"
                + "单应用内存大小:" + memClass + "M\n" + "应用版本号:" + AppUtils.getVersionCode(context) + "\n"
                + "应用版本名:" + AppUtils.getVersionName(context) + "\n";

        return result;
    }
}

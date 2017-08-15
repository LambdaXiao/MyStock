package com.android.mystock.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 获取手机配置信息
 */

public class PhoneUtils {
    public static final String TAG = "PhoneUtils";

    public static String getDeviceSeries() {
        Build bd = new Build();
        if (bd.MODEL != null) {
            return bd.MODEL;
        }
        return "";
    }

    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    public static boolean isTabletDevice(Context mContext) {
        TelephonyManager telephony = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        int type = telephony.getPhoneType();
        if (type == TelephonyManager.PHONE_TYPE_NONE) {

            return true;
        }
        return false;
    }

    public static String getMac(Context mContext) {
        WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    public static boolean hasInternet(Context mContext) {
        boolean flag;
        if (((ConnectivityManager) mContext.getSystemService(
                Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null)
            flag = true;
        else
            flag = false;
        return flag;
    }

    /**
     * 获取设备id；
     * 获取用户设备的IMEI，通过IMEI和mac来唯一的标识用户。
     * ACCESS_WIFI_STATE(必须)	获取用户设备的mac地址，
     * <p/>
     * 在平板设备上，无法通过IMEI标示设备，我们会将mac地址作为用户的唯一标识
     * 在模拟器中运行时，IMEI返回总是000000000000000。
     *
     * @param context
     * @return
     */
    public static String getUUID(Context context) {
        if (context == null) {
            return "";
        }
        final TelephonyManager tm = (TelephonyManager) context.
                getSystemService(Context.TELEPHONY_SERVICE);
        String tmDeviceId = "";
        String androidId = "";
        String mac = "";

        String serial = Build.SERIAL; //12位；
        String time = Build.TIME + "";//13位；

        if (tm != null) {
            tmDeviceId = "" + tm.getDeviceId();
        }

        androidId = "" + Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
//        mac = "" + getMacAdress(context);

        Log.e("@@@", "tmDeviceId=" + tmDeviceId + ",\nandroidId=" + androidId
                + ",\nmac=" + mac);

        long uuidParam2;

        uuidParam2 = (long) (androidId.hashCode() << 32 | (serial + time).hashCode());

        UUID deviceUuid = new UUID(tmDeviceId.hashCode(), uuidParam2);
        //生成32位的识别码；
        String uniqueId = deviceUuid.toString();
        String encryption = encryption(uniqueId);
        Log.e("@@@", "encryption====>" + encryption);
        return encryption;
    }

    public static String encryption(String plainText) {
        String re_md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i = 0;

            StringBuilder sb = new StringBuilder("");
            for (int offset = 0, len = b.length; offset < len; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(i));
            }
            re_md5 = sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }

    /**
     * 获取CPU名字
     *
     * @return
     */
    public static String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);

            for (int i = 0; i < array.length; i++) {

            }
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 验证手机号码
     * 电信号段:133/153/180/181/189/177；              1  3578   01379
     * 联通号段:130/131/132/155/156/185/186/145/176；  1  34578  01256
     * 移动号段：134/135/136/137/138/139/150/151/152/157/158/159/182/183/184/187/188/147/178。
     * <p/>
     * 13      0123456789
     * 14       57
     * 15       012356789
     * 17       678
     * 18       0123456789
     *
     * @param phoneNum
     * @return
     */
    public static boolean isMobilePhone(String phoneNum) {
        if (isNotEmpty(phoneNum)) {
            //11位；
            Pattern p = Pattern.compile("^((13[0-9])|(14[5,7])|(15[0,1,2,3,5,6,7,8,9])|(17[6,7,8])|(18[0-9]))\\d{8}$");
            boolean m = p.matcher(phoneNum).matches();
            Log.e("@@@", "手机号验证===>" + m);
            return m;
        }
        return false;
    }

    public static boolean isNotEmpty(String text) {
        if (!TextUtils.isEmpty(text)) {
            return true;
        }
        return false;
    }
}

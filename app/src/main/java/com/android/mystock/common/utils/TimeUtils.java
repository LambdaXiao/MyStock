package com.android.mystock.common.utils;

import java.util.Calendar;

/*
 * 格式化日期
 */
public class TimeUtils {

    /**
     * YYYY-MM-DD
     * @param date
     * @return
     */
    public static String DateFormate(String date) {
        String d = null;
        if (date != null && date.length() >= 8) {
            d = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
        } else {
            d = "--";
        }
        return d;
    }

    public static String TimeFormate(int t) {
        String te = "00" + t;
        te = te.substring(te.length() - 2, te.length());
        return te;
    }

    /**
     * 获取当前时间
     *
     * @param type
     * @return type=1,YYYY-MM-DD HH:MM ;type=2,HH:MM; type=3,YYYY-MM-DD ;
     */
    public static String getCurrentTime(int type) {
        String str = "";
        // TimeZone defaultZone = TimeZone.getTimeZone("GMT+8");
        // TimeZone defaultZone = TimeZone.getDefault();
        // if( defaultZone.getRawOffset() != 28800000 )
        // defaultZone = TimeZone.getTimeZone("GMT+8");
        // Calendar ca = Calendar.getInstance(defaultZone);
        Calendar ca = Calendar.getInstance();
        int yy = ca.get(Calendar.YEAR);
        int hh = ca.get(Calendar.HOUR_OF_DAY);
        int mm = ca.get(Calendar.MINUTE);
        int mom = ca.get(Calendar.MONTH) + 1;
        int dd = ca.get(Calendar.DAY_OF_MONTH);
        int second = ca.get(Calendar.SECOND);
        switch (type) {
            case 1:
                str = yy + "-" + TimeFormate(mom) + "-" + TimeFormate(dd) + " "
                        + TimeFormate(hh) + ":" + TimeFormate(mm);// +":"+format(ss);
                break;
            case 2:
                str = TimeFormate(hh) + ":" + TimeFormate(mm);
                break;
            case 3:
                str = yy + "-" + TimeFormate(mom) + "-" + TimeFormate(dd);
                break;
            case 4:
                str = TimeFormate(hh) + ":" + TimeFormate(mm) + ":"
                        + TimeFormate(second);
                break;
        }

        return str;
    }

    /**
     * 时间格式化；
     *
     * @param timeIns 单位为：秒；
     * @return 格式："x天xx小时xx秒分xx秒";
     */
    public static int[] timeFormat(int timeIns) {
        int dayMs = 24 * 3600;
        int hourMs = 3600;
        int minuteMs = 60;

        int lastDays = 0;
        int lastHours = 0;
        int lastminutes = 0;
        int lastMs = 0;

//        StringBuilder sb = new StringBuilder();
        //天数；
        if (timeIns > dayMs) {
            lastDays = timeIns / dayMs;
            timeIns = timeIns % dayMs;
        }

        //小时；
        if (timeIns > hourMs) {
            lastHours = timeIns / hourMs;
            timeIns = timeIns % hourMs;
        }

        //分钟；
        if (timeIns > minuteMs) {
            lastminutes = timeIns / minuteMs;
            timeIns = timeIns % minuteMs;
        }

        //秒；
        lastMs = timeIns;
//        sb.append(lastDays + "天" + lastHours + "小时" + lastminutes + "分" + lastMs + "秒");
        int[] dhms = new int[4];
        dhms[0] = lastDays;
        dhms[1] = lastHours;
        dhms[2] = lastminutes;
        dhms[3] = lastMs;
        return dhms;
    }
}

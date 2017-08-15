package com.android.mystock.common.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.TypedValue;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 工具类
 */
public class Utils {

    /**
     * EditTextView 获取小数点后几位
     *
     * @param editText 控件
     * @param s        内容
     * @param i        位数
     */
    public static void limitDecimals(EditText editText, CharSequence s, int i) {
        //处理输入为小数点的后三位
        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > i) {
                s = s.toString().subSequence(0, s.toString().indexOf(".") + i + 1);
                editText.setText(s);
                editText.setSelection(s.length());//光标位置
            }
        }
        if (s.toString().trim().substring(0).equals(".")) {
            s = "0" + s;
            editText.setText(s);
            editText.setSelection(i);
        }

        if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {//beginIndex - 开始处的索引（包括）。endIndex - 结束处的索引（不包括）。
                editText.setText(s.subSequence(0, 1));
                editText.setSelection(1);
                return;
            }
        }
    }


    /**
     * 获取两位小数
     *
     * @param values
     * @return
     */
    public static String getDecimals(Object values) {
        if (values != null && !"".equals(values)) {
            String value = values.toString();
            if (isNumber(value)) {
                BigDecimal bd = new BigDecimal(value);
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_EVEN);
                return bd.toString();
            } else {
                return value;
            }
        } else {
            return "--";
        }
    }

    /**
     * 获取n位小数
     *
     * @param values
     * @return
     */
    public static String getDecimals(Object values,int n) {
        if (values != null && !"".equals(values)) {
            String value = values.toString();
            if (isNumber(value)) {
                BigDecimal bd = new BigDecimal(value);
                bd = bd.setScale(n, BigDecimal.ROUND_HALF_EVEN);
                return bd.toString();
            } else {
                return value;
            }
        } else {
            return "--";
        }
    }

    public static boolean isNumber(String str) {
        String number = "0123456789-.";
        int len = str.length();
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (number.indexOf(String.valueOf(c)) == -1) {
                return false;
            }
        }
        return true;

    }


    /**
     * 是否为PY字符串
     *
     * @param code
     * @return
     */
    public static boolean isPYCode(String code) {
        boolean bRtn = true;
        for (int i = 0; i < code.length(); i++) {
            if (Character.isDigit(code.charAt(i))) {
                bRtn = false;
                break;
            }
        }
        return bRtn;
    }

    /**
     * 是否带市场
     *
     * @param code
     * @return
     */
    public static boolean isMaketCode(String code) {
        boolean bRtn = false;
        if (code != null && code.indexOf(".") > -1) {
            bRtn = true;
        }

        return bRtn;
    }


    /**
     * 获取当前分辨率下指定单位对应的像素大小（根据设备信息） px,dip,sp -> px
     * <p/>
     * Paint.setTextSize()单位为px
     * <p/>
     * 代码摘自：TextView.setTextSize()
     *
     * @param unit TypedValue.COMPLEX_UNIT_*
     * @param size
     * @return
     */
    public static float getRawSize(Context context, int unit, float size) {
        Context c = context;
        Resources r;

        if (c == null)
            r = Resources.getSystem();
        else
            r = c.getResources();

        return TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param context
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param context
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static DecimalFormat formatter = new DecimalFormat("####.##");

    /**
     * 将原始数据除以10000所得的结果
     **/
    public static String fromat2Myriad(String pSrcVal) {
        return formatter.format(Double.parseDouble(pSrcVal) / 10000.0);
    }

    /**
     * 将给定的数字保留两位小数返回
     **/
    public static double format2Double(double pSrcVal) {
        return (double) Math.round(pSrcVal * 100) / 100.0;
    }

    /**
     * 将给定的数字变成小数点后两位的字符串
     **/
    public static String format(double pSrcVal) {
        return formatter.format(pSrcVal);
    }

    /**
     * 将原始数据除以10000所得的结果
     **/
    public static String fromat2Myriad(double pSrcVal) {
        return formatter.format(pSrcVal / 10000.0);
    }

    /**
     * 把比较大的数字转换成以“万或者亿”为单位
     *
     * @param value
     * @return
     */
    public static String formatLongNumber(float value) {
        String style = "###.##";

        if (value > 100000000) {
            style = "###.##亿";
            value = value / 100000000;
        } else if (value > 10000) {
            style = "###.##万";
            value = value / 10000;
        }

        formatter.applyPattern(style);
        String v = formatter.format(value);

        return v;
    }

    /**
     * 把比较大的数字转换成以“万或者亿”为单位
     *
     * @param value
     * @return
     */
    public static String ENODoubleToMoney(String value, int local) {
        if (!isNumber(value))
            return value;
        double d = Double.valueOf(value);
        String style = "###.##";
        local = 0;
        switch (local) {
            case 0:
                if (d > 1000000000) {
                    style = "###.##B";
                    d = d / 1000000000;
                } else if (d > 1000000) {
                    style = "###.##M";
                    d = d / 1000000;
                } else if (d > 1000) {
                    style = "###.##K";
                    d = d / 1000;
                }
                break;
            default:
                if (d > 100000000) {
                    style = "###.##亿";
                    d = d / 100000000;
                } else if (d > 10000) {
                    style = "###.##万";
                    d = d / 10000;
                }
                break;
        }

        formatter.applyPattern(style);
        String v = formatter.format(d);

        return v;
    }

    /**
     * 将图片压缩成二进制数组
     *
     * @param bitmap
     * @return
     */
    public byte[] getBitmapByte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    /**
     * 将二进制数组转化为图片
     *
     * @param temp
     * @return
     */
    public Bitmap getBitmapFromByte(byte[] temp) {
        if (temp != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            return bitmap;
        } else {
            return null;
        }
    }

    /**
     * 放大图片
     *
     * @param bitmap
     * @param width    放大到最大的宽度，以dip为单位
     * @param height   放大到最高的高度，以dip为单位
     * @param mContext
     * @return
     */
    public static Bitmap zoomInImage(Bitmap bitmap, int width, int height,
                                     Context mContext) {

        width = dip2px(mContext, width);
        height = dip2px(mContext, height);

        // Display dispaly=

        int widthImage = bitmap.getWidth();
        int heightImage = bitmap.getHeight();

        float scaleWidth = Math.max((float) width / widthImage, 1.0f);
        float scaleHeight = Math.max(1.0f, (float) height / heightImage);

        scaleWidth = Math.min(scaleWidth, 20.0f);
        scaleHeight = Math.min(scaleHeight, 20.0f);

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleWidth);
        // Logger.d("**********8  widthImage="+widthImage+"   heightImage="+heightImage
        // +
        // " width="+width+"   height="+height+"  scaleHeight="+scaleHeight
        // +"   scaleWidth="+scaleWidth) ;
        // Logger.d("原始：bitmap.getWidth()="+bitmap.getWidth()+" bitmap.getHeight()="+
        // bitmap.getHeight());
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, widthImage, heightImage,
                matrix, true);
        // Logger.d("放大后：bitmap.getWidth()="+bitmap.getWidth()+" bitmap.getHeight()="+
        // bitmap.getHeight());
        return bitmap;
    }

    /**
     * 格式化涨跌幅
     *
     * @param zdf
     * @return
     */
    public static String formatZDF(String zdf) {
        if (zdf == null && !"".equals(zdf))
            return "";
        if (!isNumber(zdf))
            return zdf;
        else {
            zdf = get5LenString(zdf);
            float f = Float.valueOf(zdf);
            if (f > 0) {
                zdf = "+" + zdf + "%";
            } else {
                zdf = zdf + "%";
            }

        }
        return zdf;
    }

    /**
     * 格式化成带符号的数字
     *
     * @param zdf
     * @return
     */
    public static String formatSignNum(String zdf) {
        if (zdf == null && !"".equals(zdf))
            return "";
        if (!isNumber(zdf))
            return zdf;
        else {
            float f = Float.valueOf(zdf);
            if (f > 0) {
                zdf = get5LenString(zdf);
                zdf = "+" + zdf;
            }

        }
        return zdf;
    }

    /**
     * 格式化数字，列如：将-123432423423.134 格式化为 -123,432,423,423.134
     *
     * @param d
     * @return
     */
    public static String formatMoney(Double d) {
        // d=123345.12945;
        BigDecimal bg = new BigDecimal(d);
        // Logger.d("Utils   formatMoney   bg="+ bg);
        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        // Logger.d("Utils   formatMoney   f1="+ bg);
        formatter.applyPattern(",###.00");
        String str = formatter.format(f1);

        if (".00".equals(str))
            str = "0.00";
        return str;

    }

    /**
     * 格式化数字，列如：将-123432423423.134 格式化为 -123,432,423,423.134
     *
     * @param value
     * @return
     */
    public static String formatMoney(String value) {
        if (!isNumber(value))
            return value;
        Double d = Double.valueOf(value);
        BigDecimal bg = new BigDecimal(d);
        // Logger.d("Utils   formatMoney   bg="+ bg);
        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        // Logger.d("Utils   formatMoney   f1="+ bg);
        formatter.applyPattern(",###.00");
        String str = formatter.format(f1);

        if (".00".equals(str))
            str = "0.00";
        return str;

    }

    /**
     * 格式化数字，列如：将-123432423423.134 格式化为 -123,432,423,423.134
     *
     * @param value
     * @return
     */
    public static String formatNumStyle(String value) {
        // d=123345.12945;
        if (!isNumber(value))
            return value;
        Double d = Double.valueOf(value);
        BigDecimal bg = new BigDecimal(d);
        // Logger.d("Utils   formatMoney   bg="+ bg);
        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        // Logger.d("Utils   formatMoney   f1="+ bg);
        formatter.applyPattern(",###");
        String str = formatter.format(f1);

        if (".".equals(str))
            str = "0";
        return str;

    }

    /**
     * 格式化数字，列如：将-123432423423.134 格式化为 -123,432,423,423.134
     *
     * @param d
     * @return
     */
    public static String formatNumStyle(Double d) {

        // Double d = Double.valueOf(value) ;
        BigDecimal bg = new BigDecimal(d);
        // Logger.d("Utils   formatMoney   bg="+ bg);
        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        // Logger.d("Utils   formatMoney   f1="+ bg);
        formatter.applyPattern(",###");
        String str = formatter.format(f1);

        if (".".equals(str))
            str = "0";
        return str;

    }

    public static String get5LenString(String value) {
        String str = "N/A";
        if (value == null)
            return str;
        if (!isNumber(value.trim())) {
            return value.trim();
        }

        if (value.indexOf(".") < 0) {
            return value.trim();
        }

        Double d = Double.valueOf(value);

        BigDecimal bd = new BigDecimal(value);

        if (d >= 100) {
            bd = bd.setScale(1, BigDecimal.ROUND_HALF_EVEN);
        } else if (d > 10) {
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        } else if (d >= 0) {
            bd = bd.setScale(3, BigDecimal.ROUND_HALF_EVEN);
        } else {
            bd = bd.setScale(3, BigDecimal.ROUND_HALF_EVEN);
        }

        str = bd.toString();
        return str;
    }

    /**
     * @param value 所传的股票代码，
     * @return value的长度小于5时，返回长度为5的股票代码
     */
    public static String get5LenCode(String value) {
        String insertString = "00000";
        int length = 0; // 需要补0的位数
        if (value.length() < 5) {
            length = 5 - value.length();
        }
        StringBuffer sbf = new StringBuffer();
        sbf.append(value);
        sbf.insert(0, insertString, 0, length); // str的长度不满13位时，前面补零
        return sbf.toString();
    }

    /**
     * 去中文全角
     *
     * @param str
     * @return
     */
    public static String trimString(String str) {

        str = str.replaceAll("　", "");

        return str;
    }

    /**
     * 判斷是否低於 api 16的版本
     *
     * @return 低於16返回 false，否則返回 ，true
     */
    public static boolean isLowerAPI() {

        int api = android.os.Build.VERSION.SDK_INT;


        if (api > 15) {
            return false;
        }

        return true;
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    public static String string2Json(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    /*
     *  将行情市场1 2 转为00 10
     */
    public static String getmarket(String exchid, String classid) {
        // 代码	交易市场
        // 00	深圳A
        // 01	深圳B
        // 10	上海A
        // 11	上海B
        // 13	上交所沪港通板块
        // 02	三板
        // 20	股转系统（A）
        // 21	股转系统（B）

        if (exchid.equals("1")) {
            if (classid.equals("100") || classid.equals("103") || classid.equals("1001") || classid.equals("110")) {
                return "00";
            } else if (classid.equals("101")) {
                return "01";
            } else return "00";
        } else if (exchid.equals("2")) {
            if (classid.equals("100") || classid.equals("103") || classid.equals("95")) {
                return "10";
            } else if (classid.equals("101")) {
                return "11";
            } else return "10";
        } else if (exchid.equals("6")) {
            return "13";
        } else if (exchid.equals("5")) {//三板--现在后台返回的市场是5--20--股转系统(A)
            if (classid.equals("100")) {
                return "20";
            } else if (classid.equals("101")) {
                return "21";
            }
            return "20";
        }
        return "";
    }

    /*
     *  将行情市场00 10 转为深A 沪A
	 */
    public static String markettransfer(String exchid) {
        if (exchid == null) {
            return "";
        } else {
            if (exchid.equals("00")) {
                return "深A";
            } else if (exchid.equals("01")) {
                return "深B";
            } else if (exchid.equals("10")) {
                return "沪A";
            } else if (exchid.equals("11")) {
                return "沪B";
            } else if (exchid.equals("13")) {
                return "沪港通";
            } else if (exchid.equals("02")) {
                return "三板";
            } else if (exchid.equals("20")) {
                return "股转A";
            } else if (exchid.equals("21")) {
                return "股转B";
            }
        }
        return "";
    }


}
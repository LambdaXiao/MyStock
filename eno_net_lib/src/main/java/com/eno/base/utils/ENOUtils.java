package com.eno.base.utils;

import com.eno.base.enocoder.Hex;
import com.eno.base.enocoder.MD5;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

/**
 * 
 * @author 	xmf 
 *  @see  2013-4-24 下午1:32:48 包名com.eno.utils 工程名称EnoNet_xyzq
 *  @version 1.0
 */
public class ENOUtils {

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String trim(String str) {
		String temp = "";
		for (int i = 0; i < str.length() - 1; i++) {
			String ch = str.substring(i, i + 1);
			if (!ch.equals(""))
				temp += ch;
		}
		return temp;
	}

	public static String formatLongNumber(Long L) {
		int temp = String.valueOf(L).length();
		String value = "";
		Long i;
		if (temp > 8) {
			i = L / 100000000;
			value = String.valueOf(i) + "亿";

		} else if (temp > 4) {

			i = L / 10000;
			value = String.valueOf(i) + "万";
		}

		return value;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkOrderNumber(String str) {
		String number = "0123456789";
		for (int i = 0; i < str.length(); i++) {
			if (number.indexOf(str.charAt(i)) == -1) {
				return false;
			}
		}
		return true;
	}

	private final static String[] STRHEX;
	static { // 生成静态十六进制字符串数组
		STRHEX = new String[256];
		char[] cBytes = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		char[] cTemp = new char[2];
		for (int i = 0; i < 16; i++) {
			cTemp[0] = cBytes[i];
			for (int j = 0; j < 16; j++) {
				cTemp[1] = cBytes[j];
				STRHEX[i * 16 + j] = new String(cTemp);
			}
		}
	}

	/**
	 * 把一个ascii字符串转换成bytes.
	 * 
	 * @param s
	 *            ascii格式的字符串
	 * @return 转换后的byte数组
	 */
	public static byte[] ascstr2Bytes(String s) {
		int nLen = s.length();
		byte[] ret = new byte[nLen];
		for (int i = 0; i < nLen; i++)
			ret[i] = (byte) s.charAt(i);
		return ret;
	}

	/**
	 * 把一个ascii字符串转换成bytes.
	 * 
	 * @param s
	 *            ascii格式的字符串
	 * @param bytes
	 *            转换后存贮的数组
	 * @param nOffset
	 *            从数组的第nOffset位置开始转
	 * @param nMaxLen
	 *            数组允许的最大长度.-1代表不限长
	 * @return 转换后的byte数组
	 */
	public static int ascstr2Bytes(String s, byte[] bytes, int nOffset,
			int nMaxLen) {
		int nLen = s.length();
		if (nMaxLen > 0 && nLen > nMaxLen)
			nLen = nMaxLen;
		for (int i = 0; i < nLen; i++)
			bytes[nOffset + i] = (byte) s.charAt(i);
		return nLen;
	}

	/**
	 * 把一个byte数据转换成全ascii格式的字符串.
	 * 
	 * @param bytes
	 *            要转换的byte数组
	 * @return 转换后字符串.
	 */
	public static String bytes2Ascstr(byte[] bytes) {
		String ret = null;
		int nLen = bytes.length;
		char[] s = new char[nLen];
		int i;
		for (i = 0; i < nLen; i++) {
			if (bytes[i] == 0)
				break;
			s[i] = (char) bytes[i];
		}
		return new String(s, 0, i);
	}

	/**
	 * 把一个byte数据转换成全ascii格式的字符串.
	 * 
	 * @param bytes
	 *            要转换的byte数组
	 * @param offset
	 *            从第几个位置开始转
	 * @param len
	 *            要转换的长度
	 * @return 转换后字符串.
	 */
	public static String bytes2Ascstr(byte[] bytes, int offset, int len) {
		char[] s = new char[len];
		int i;
		for (i = 0; i < len; i++) {
			s[i] = (char) bytes[offset + i];
			if (s[i] == 0)
				break;
		}
		return new String(s, 0, i);
	}

	public static int bytes2AscstrLen(byte[] bytes, int offset) {
		int nPos = offset;
		while (bytes[nPos++] != 0)
			;
		return nPos - offset;
	}

	/**
	 * 把一个unicode字符串转换成bytes.
	 * 
	 * @param s
	 *            unicode格式的字符串
	 * @return 转换后的byte数组
	 */
	public static byte[] unistr2Bytes(String s) {
		int nLen = s.length();
		byte[] ret = new byte[nLen * 2];
		int nDPos = 0;
		char c;
		for (int i = 0; i < nLen; i++) {
			c = s.charAt(i);
			ret[nDPos++] = (byte) (c & 0xff);
			ret[nDPos++] = (byte) (c >>> 8 & 0xff);
		}
		return ret;
	}

	public static float byteArraytoFloat(byte[] b) {
		// 4 bytes
		int accum = 0;
		for (int shiftBy = 0; shiftBy < 4; shiftBy++) {
			accum |= (b[shiftBy] & 0xff) << shiftBy * 8;
		}
		return Float.intBitsToFloat(accum);
	}

	/**
	 * 把一个unicode字符串转换成bytes.
	 * 
	 * @param s
	 *            unicode格式的字符串
	 * @param bytes
	 *            转换后存贮的数组
	 * @param nOffset
	 *            从数组的第nOffset位置开始转
	 * @param nMaxLen
	 *            数组允许的最大长度.-1代表不限长
	 * @return 转换后的byte数组
	 */
	public static int unistr2Bytes(String s, byte[] bytes, int nOffset,
			int nMaxLen) {
		int nLen = s.length();
		if (nMaxLen > 0 && nLen > nMaxLen / 2)
			nLen = nMaxLen / 2;
		char c;
		int nDPos = nOffset;
		for (int i = 0; i < nLen; i++) {
			c = s.charAt(i);
			bytes[nDPos++] = (byte) (c & 0xff);
			bytes[nDPos++] = (byte) (c >>> 8 & 0xff);
		}
		return nLen * 2;
	}

	/**
	 * 把一个byte数据转换成全unicode格式的字符串.
	 * 
	 * @param bytes
	 *            要转换的byte数组
	 * @return 转换后字符串.
	 */
	public static String bytes2Unistr(byte[] bytes) {
		int nLen = bytes.length / 2;
		char[] cs = new char[nLen];
		int nSPos = 0;
		char c;
		int i;
		for (i = 0; i < nLen; i++) {
			cs[i] = (char) (bytes[nSPos++] & 0xFF);
			c = (char) (bytes[nSPos++] & 0xFF);
			c <<= 8;
			cs[i] += c;
			if (cs[i] == 0)
				break;
		}
		return (new String(cs, 0, i));
	}

	/**
	 * 把一个byte数据转换成全unicode格式的字符串.
	 * 
	 * @param bytes
	 *            要转换的byte数组
	 * @param offset
	 *            从第几个位置开始转
	 * @param len
	 *            要转换的长度
	 * @return 转换后字符串.
	 */
	public static String bytes2Unistr(byte[] bytes, int offset, int len) {
		int nLen = len / 2;
		char[] cs = new char[nLen];
		int nSPos = offset;
		char c;
		int i;
		for (i = 0; i < nLen; i++) {
			cs[i] = (char) (bytes[nSPos++] & 0xFF);
			c = (char) (bytes[nSPos++] & 0xFF);
			c <<= 8;
			cs[i] += c;
			if (cs[i] == 0)
				break;
		}
		return (new String(cs, 0, i));
	}

	/**
	 * 从字节数组中指定的位置读取一个Short类型的数据。
	 * 
	 * @param b
	 *            字节数组
	 * @param pos
	 *            指定的开始位置
	 * @return 一个Short类型的数据
	 */
	public static short bytes2Short(byte[] b, int pos) {
		int val = 0;
		val = b[pos + 1] & 0xFF; // 锟斤拷锟街斤拷锟斤拷荩锟斤拷锟??锟斤拷前锟斤拷锟斤拷纸凇锟??
		val = val << 8;
		val |= b[pos] & 0xFF;
		return (short) val;
	}

	/**
	 * 从字节数组中指定的位置读取一个Integer类型的数据。
	 * 
	 * @param b
	 *            字节数组
	 * @param pos
	 *            指定的开始位置
	 * @return 一个Integer类型的数据
	 */
	public static int bytes2Integer(byte[] b, int pos) {
		int val = 0;
		val = b[pos + 3] & 0xff; // 多字节数据，低8位在前面的字节。
		val <<= 8;
		val |= b[pos + 2] & 0xff;
		val <<= 8;
		val |= b[pos + 1] & 0xff;
		val <<= 8;
		val |= b[pos] & 0xff;
		return val;
	}

	public static long bytes2Long(byte[] b, int pos) {
		long buffer = 0;
		buffer = b[pos + 7] & 0xff;
		for (int i = 6; i >= 0; i--) {
			buffer <<= 8;
			buffer |= b[pos + i] & 0xff;
		}
		return buffer;
	}

	/**
	 * 将一个short 类型的数据转换为字节并存入到指定的字节数组指定的位置。
	 * 
	 * @param b
	 *            字节数组
	 * @param pos
	 *            指定的开始位置
	 * @param val
	 *            short类型的数据。
	 */
	public static void short2Bytes(byte[] b, int pos, short val) {
		b[pos + 1] = (byte) (val >>> 8 & 0xff);
		b[pos] = (byte) (val & 0xff);
	}

	/**
	 * 将一个int类型的数据转换为字节并存入到指定的字节数组指定的位置。
	 * 
	 * @param b
	 *            字节数组
	 * @param pos
	 *            指定的开始位置
	 * @param val
	 *            int类型的数据。
	 */
	public static void integer2Bytes(byte[] b, int pos, int val) {
		b[pos + 3] = (byte) (val >>> 24 & 0xff); // 锟斤拷位锟斤拷前
		b[pos + 2] = (byte) (val >>> 16 & 0xff);
		b[pos + 1] = (byte) (val >>> 8 & 0xff);
		b[pos] = (byte) (val & 0xff);
	}

	/**
	 * 根据decimal指定的小数点位数，把num转换为浮点型的字符串
	 * 
	 * @param num
	 *            待转换的整型数
	 * @param decimal
	 *            小数点位数
	 * @return 浮点型的字符串
	 */
	public static String getFloatString(int num, int decimal) {
		if (decimal < 0)
			return "";

		String strOrg = String.valueOf(Math.abs(num));
		int nDecPos = strOrg.length() - decimal;
		if (nDecPos <= 0) {
			strOrg = getRepeatString('0', 1 - nDecPos) + strOrg;
			nDecPos = 1;
		}

		if (num < 0)
			return ("-" + strOrg.substring(0, nDecPos) + "." + strOrg
					.substring(nDecPos));
		else
			return (strOrg.substring(0, nDecPos) + "." + strOrg
					.substring(nDecPos));
	}

	/**
	 * 从float_string中, 返回指定有效位数的字符串
	 * 
	 * @param strFloat
	 *            float数据类型的字符串
	 * @param size
	 *            有效位数
	 * @return
	 */
	public static String getNumberString(String strFloat, int size) {
		if (strFloat != null && size > 0 && strFloat.length() > size) {
			String temp = strFloat.substring(0, size);
			if (temp.charAt(size - 1) == '.') {
				temp = size > 1 ? temp.substring(0, size - 1) : "0";
			}
			return temp;
		}

		return strFloat;
	}

	/**
	 * 返回一个重复的字符串
	 * 
	 * @param c
	 * @param repeat
	 * @return
	 */
	public static String getRepeatString(char c, int repeat) {
		if (repeat < 1)
			return "";

		char[] temp = new char[repeat];
		for (int i = 0; i < repeat; i++)
			temp[i] = c;

		return new String(temp);
	}

	/**
	 * 取得当前日期。
	 * 
	 * @return 字符串形式的当前日期（年、月、日）。
	 */
	public static final int getCurrentTime() {
		// TimeZone defaultZone = TimeZone.getTimeZone("GMT+8");
		// TimeZone defaultZone = TimeZone.getDefault();
		// if( defaultZone.getRawOffset() != 28800000 )
		// defaultZone = TimeZone.getTimeZone("GMT+8");
		// Calendar rightNow = Calendar.getInstance(defaultZone);
		Calendar rightNow = Calendar.getInstance();
		int year = rightNow.get(Calendar.YEAR);
		int month = rightNow.get(Calendar.MONTH) + 1;
		int day = rightNow.get(Calendar.DATE);
		return (year * 10000 + month * 100 + day);
	}

	/**
	 * 把一个字节数组的内容转码为可打印输出的字符串，类似于：02 03 1A 00
	 * 
	 * @param data
	 *            准备输出的字节数组
	 * @param line
	 *            要打印的行号（每一行输出8个字节），开始行为０
	 * @param direct
	 *            打印方向，正向为true
	 */
	public static final String dumpBytes(byte[] data, int line, boolean direct) {
		if (data == null)
			return "null";
		if (data.length == 0)
			return "";

		int lineBytes = 8;
		int totalLines = data.length / lineBytes;
		if ((data.length % lineBytes) > 0)
			totalLines++;
		int currLine = line % totalLines;
		if (!direct)
			currLine = totalLines - currLine - 1;

		int beginPos = currLine * lineBytes;
		int endPos = beginPos + lineBytes;
		String strRet = "";
		for (int i = beginPos; i < data.length && i < endPos; i++) {
			strRet = strRet + " ";
			int c = data[i] < 0 ? data[i] + 256 : data[i];
			strRet = strRet + STRHEX[c];
		}
		return strRet;
	}

	/**
	 * 把一个字节数组的内容转码为可打印输出的字符串，类似于：02031A00
	 * 
	 * @param data
	 *            准备输出的字节数组
	 */
	public static final String dumpBytes(byte[] data) {
		if (data == null)
			return "null";
		if (data.length == 0)
			return "";

		String strRet = "";
		for (int i = 0; i < data.length; i++) {
			int c = data[i] < 0 ? data[i] + 256 : data[i];
			strRet = strRet + STRHEX[c];
		}
		return strRet;
	}

	public static String getURLParam(String strURL, String strParamName) {
		String strRtn = null;
		if (strURL != null) {
			int nPos = strURL.indexOf(strParamName + "=");
			if (nPos > -1) {
				nPos += strParamName.length() + 1;
				int nEndPos = strURL.indexOf('&', nPos);
				if (nEndPos > -1)
					strRtn = strURL.substring(nPos, nEndPos);
				else
					strRtn = strURL.substring(nPos);
			}
		}
		return strRtn;
	}

	public static int getURLParam(String strURL, String strParamName,
			int nDefValue) {
		int nRtn = nDefValue;
		String strRtn = getURLParam(strURL, strParamName);
		if (strRtn != null && !strRtn.equals("")) {
			try {
				nRtn = Integer.parseInt(strRtn);
			} catch (Exception e) {
			}
		}
		return nRtn;
	}

	public static boolean getURLParam(String strURL, String strParamName,
			boolean nDefValue) {
		String temp = getURLParam(strURL, strParamName);
		if (temp != null && !temp.equals("")) {
			return temp.equals("true");
		} else
			return nDefValue;
	}

	/**
	 * 替换字符串函数 String strSource - 源字符串 String strFrom　 - 要替换的子串 String strTo　　 -
	 * 替换为的字符串
	 */
	public static String replaceAll(String strSource, String strFrom,
			String strTo) {
		// 如果要替换的子串为空，则直接返回源串
		if (strFrom == null || strFrom.equals(""))
			return strSource;
		String strDest = "";
		// 要替换的子串长度
		int intFromLen = strFrom.length();
		int intPos;
		// 循环替换字符串
		while ((intPos = strSource.indexOf(strFrom)) != -1) {
			// 获取匹配字符串的左边子串
			strDest = strDest + strSource.substring(0, intPos);
			// 加上替换后的子串
			strDest = strDest + strTo;
			// 修改源串为匹配子串后的子串
			strSource = strSource.substring(intPos + intFromLen);
		}
		// 加上没有匹配的子串
		strDest = strDest + strSource;
		// 返回
		return strDest;
	}

	public static String replaceParam(String strURL, String strParam,
			String strValue) {
		int tmp;
		int beginPos = strURL.indexOf(strParam);
		int sizeParam = strParam.length();
		String rplString;
		while (beginPos > -1) {
			if (strURL.charAt(beginPos + sizeParam) == '='
					&& (strURL.charAt(beginPos - 1) == '&' || strURL
							.charAt(beginPos - 1) == '?')) {
				int endPos = strURL.indexOf('&', beginPos);
				if (endPos == -1) {
					rplString = strURL.substring(beginPos);
				} else {
					rplString = strURL.substring(beginPos, endPos);
				}
				if (strValue == null || strValue.equals("")) {
					if (endPos == -1) {
						return replaceAll(strURL, strURL.charAt(beginPos - 1)
								+ rplString, "");
					} else {
						return replaceAll(strURL, rplString + "&", "");
					}
				} else {
					return replaceAll(strURL, rplString, strParam + "="
							+ strValue);
				}
			}
			beginPos = strURL.indexOf(strParam, beginPos + sizeParam);
		}
		if (strValue != null && !strValue.equals("")) {
			return strURL + "&" + strParam + "=" + strValue;
		}
		return strURL;
	}

	/**
	 * Makes a long from two integers (treated unsigned).
	 * 
	 * @param lo
	 *            lower 32bits
	 * @param hi
	 *            higher 32bits
	 * @return the built long
	 */
	public final static long makeLong(int lo, int hi) {
		return (((long) hi << 32) | ((long) lo & 0x00000000ffffffffL));
	}

	/**
	 * Gets the lower 32 bits of a long.
	 * 
	 * @param val
	 *            the long integer
	 * @return lower 32 bits
	 */
	public final static int longLo32(long val) {
		return (int) val;
	}

	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Gets the higher 32 bits of a long.
	 * 
	 * @param val
	 *            the long integer
	 * @return higher 32 bits
	 */
	public final static int longHi32(long val) {
		return (int) (val >>> 32);
	}

	/**
	 * Gets bytes from an array into an integer.
	 * 
	 * @param buf
	 *            where to get the bytes
	 * @param ofs
	 *            index from where to read the data
	 * @return the 32bit integer
	 */
	public final static int byteArrayToInt(byte[] buf, int ofs) {
		return (buf[ofs] << 24) | ((buf[ofs + 1] & 0x0ff) << 16)
				| ((buf[ofs + 2] & 0x0ff) << 8) | (buf[ofs + 3] & 0x0ff);
	}

	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Converts an integer to bytes, which are put into an array.
	 * 
	 * @param value
	 *            the 32bit integer to convert
	 * @param buf
	 *            the target buf
	 * @param ofs
	 *            where to place the bytes in the buf
	 */
	public final static void intToByteArray(int value, byte[] buf, int ofs) {
		buf[ofs] = (byte) ((value >>> 24) & 0x0ff);
		buf[ofs + 1] = (byte) ((value >>> 16) & 0x0ff);
		buf[ofs + 2] = (byte) ((value >>> 8) & 0x0ff);
		buf[ofs + 3] = (byte) value;
	}

	public static int getSign(String strValue) {
		char chTemp = strValue.charAt(0);

		int nRtn = 0;
		if (chTemp == '-')
			nRtn = -1;
		else if (chTemp >= '0' && chTemp <= '9') {
			int nLen = strValue.length();
			for (int i = 0; i < nLen; i++) {
				chTemp = strValue.charAt(i);
				if (chTemp != '0' && chTemp != '.') {
					nRtn = 1;
					break;
				}
			}
		} else {
			if (strValue.indexOf('买') != -1)
				nRtn = 1;
			else if (strValue.indexOf('卖') != -1)
				nRtn = -1;
		}
		return nRtn;
	}

	public static String getUniString(byte[] data, int nOffset) {
		int nLen = bytes2Integer(data, nOffset);
		if (data[nOffset + 3 + nLen] == '\0')
			nLen--;
		return bytes2Unistr(data, nOffset + 4, nLen);
	}

	public static String str2MD5(String s) {
		byte[] source;
		byte[] out = new byte[16];
		MD5 md5obj = new MD5();
		source = s.getBytes();
		md5obj.update(source, 0, source.length);
		md5obj.doFinal(out, 0);
		return Hex.encode(out);

	}

	public static String[] split(String original, String regex) {
		// 取子串的起始位置
		int startIndex = 0;
		// 将结果数据先放入Vector中
		Vector v = new Vector();
		// 返回的结果字符串数组
		String[] str = null;
		// 存储取子串时起始位置
		int index = 0;
		// 获得匹配子串的位置
		startIndex = original.indexOf(regex);
		// 如果起始字符串的位置小于字符串的长度，则证明没有取到字符串末尾。
		// -1代表取到了末尾
		while (startIndex < original.length() && startIndex != -1) {
			String temp = original.substring(index, startIndex);
			// 取子串
			v.addElement(temp);
			// 设置取子串的起始位置
			index = startIndex + regex.length();
			// 获得匹配子串的位置
			startIndex = original.indexOf(regex, startIndex + regex.length());
		}
		// 取结束的子串
		v.addElement(original.substring(index + 1 - regex.length()));
		// 将Vector对象转换成数组
		str = new String[v.size()];
		for (int i = 0; i < v.size(); i++) {
			str[i] = (String) v.elementAt(i);
		}
		v = null;
		return str;
	}

	public static int[] sort(int[] v) {
		for (int i = v.length - 1; i > 0; i--) {
			for (int j = 0; j < i; j++) {
				if (v[j] > v[j + 1]) {
					int temp = v[j];
					v[j] = v[j + 1];
					v[j + 1] = temp;
				}
			}
		}
		return v;
	}

	// 锟斤拷byte[] 转锟斤拷锟斤拷 float
	public static float byteToFloat(byte[] b) {
		// 4 bytes
		// int val = byteToInt(b);
		int val = bytes2Integer(b, 0);
		return Float.intBitsToFloat(val);
	}

	public static byte[] floatToByte(float number) {
		int num = Float.floatToIntBits(number);
		// return intToByte(num);
		byte[] b = new byte[4];
		integer2Bytes(b, 0, num);
		return b;
	}

	public static byte[] LongToByte(Long val, int pos) {

		byte[] b = new byte[8];
		b[pos + 7] = (byte) (val >>> 56 & 0xff);
		b[pos + 6] = (byte) (val >>> 48 & 0xff);
		b[pos + 5] = (byte) (val >>> 40 & 0xff);
		b[pos + 4] = (byte) (val >>> 32 & 0xff);
		b[pos + 3] = (byte) (val >>> 24 & 0xff);
		b[pos + 2] = (byte) (val >>> 16 & 0xff);
		b[pos + 1] = (byte) (val >>> 8 & 0xff);
		b[pos] = (byte) (val & 0xff);
		return b;
	}

	public static byte[] doubleToByte(double d) {
		byte[] b = new byte[8];
		long l = Double.doubleToLongBits(d);
		for (int i = 0; i < b.length; i++) {
			b[i] = new Long(l).byteValue();
			l = l >> 8;
		}
		return b;
	}

	public static double byteToDouble(byte[] b) {
		if (b.length != 8)
			System.out.println("位数不足8位");
		long l;
		l = b[0];
		l &= 0xff;
		l |= ((long) b[1] << 8);
		l &= 0xffff;
		l |= ((long) b[2] << 16);
		l &= 0xffffff;
		l |= ((long) b[3] << 24);
		l &= 0xffffffffl;
		l |= ((long) b[4] << 32);
		l &= 0xffffffffffl;

		l |= ((long) b[5] << 40);
		l &= 0xffffffffffffl;
		l |= ((long) b[6] << 48);
		l &= 0xffffffffffffffl;

		l |= ((long) b[7] << 56);

		Double d = Double.longBitsToDouble(l);
		// d=(double)Math.round(d*1000)/1000;
		return d;
	}

	/**
	 * 锟斤拷int锟斤拷锟斤拷锟斤拷锟阶拷锟斤拷锟絪hort
	 * 
	 * @param i
	 * @return
	 */
	public static short[] convertToShort(int i) {
		short[] a = new short[2];
		a[0] = (short) (i & 0x0000ffff); // 锟斤拷锟斤拷锟酵的碉拷位取锟斤拷,
		a[1] = (short) (i >> 16); // 锟斤拷锟斤拷锟酵的革拷位取锟斤拷.
		return a;
	}

	public static int convertToInt(short[] a) {
		return ((a[1] << 16)) | (a[0] & 0x0000ffff); // 锟斤拷&锟斤拷锟斤拷锟斤拷员锟街ぷ拷锟斤拷锟斤拷锟斤拷莩锟斤拷缺锟斤拷锟??位!
	}

	/**
	 * 格式化数量单位
	 * 
	 * @param data
	 * @param lotSize
	 * @return
	 */
	public static String formatQuoteData(double data, int lotSize) {
		double temp = data / lotSize;

		return formatQuoteData(temp) ;
	}

	
	
	
	
	
	
//	/**
//	 * 格式化数量单位
//	 * 
//	 * @param data
//	 * @param precision
//	 * @return
//	 */
//	public static String formatQuoteData(double data, int precision) {
//		String str = "";
//		String precisionStr = "";
//			
//		for (int i = 0; i < precision; i++) {
//			precisionStr = precisionStr + "0";
//		}
//		if (precision > 1) {
//			precisionStr = "." + precisionStr;
//		}
//
//		if (data >= 100000000)// 大于亿
//		{
//			double temp = data / 100000000;
//			
//			
//			DecimalFormat df1 = new DecimalFormat("####" + precisionStr);
//			str = df1.format(temp) + "亿";
//
//		} else if (data > 10000)// 大于万
//		{
//			double temp = data / 10000;
//			DecimalFormat df1 = new DecimalFormat("####" + precisionStr);
//			str = df1.format(temp) + "万";
//
//		} else {
//			DecimalFormat df1 = new DecimalFormat("####");
//			str = df1.format(data);
//		}
//
//		return str;
//
//	}
//
//	
	
	
	/**
	 * 格式化数量单位
	 *
	 * @return
	 */
	public static String formatQuoteData(double sdata) {
		String str = "";
	
		double data=Math.abs(sdata);
		
		
		if (data >= 100000000)// 大于亿
		{
			double temp = data / 100000000;
			if(temp<10)
			{
				
				str =String.format("%1$.2f亿",temp);

			}else if(temp<100)
			{
				str =String.format("%1$.1f亿",temp);

			}else 
			{
				str =String.format("%1$.0f亿",temp);
				
			}
				

		} else if (data > 100000)// 大于十万
		{
			double temp = data / 10000;
			if(temp<100)
			{
				str =String.format("%1$.1f万",temp);
			}else
			{
				str =String.format("%1$.0f万",temp);
			}		
		
		} else {
			
			str =String.format("%1$.0f",data);
		}

		if(sdata<0)
		{
			str="-"+str;
		}
		
		
		return str;

	}


	
	public static String FormatQuoteData(String aData, boolean aMoney) {
		String aDes = "";
		String cUnit = null;
		int nLength = aData.length();
		if ((!aMoney) && (nLength > 2)) {
			nLength -= 2; // 成交量首先变单位为手
		}
		if (nLength >= 9) // 亿为单位
		{
			nLength -= 6; // 2位小数
			cUnit = "亿";
		} else if (nLength >= 6) // 万为单位
		{
			nLength -= 2; // 2位小数
			cUnit = "万";
		}
		// 做四舍五入
		if (nLength < aData.length()) {
			char c = aData.charAt(nLength);
			if (c >= '5') {
				int tmp = Integer.parseInt(aData.substring(0, nLength)) + 1;
				aDes = tmp + "";
			} else
				aDes = aData.substring(0, nLength);
		} else {
			aDes = aData.substring(0, nLength); // 原始数据
		}
		// 加入小数点及单位
		if (cUnit != null) {
			aDes = aDes.substring(0, nLength - 2) + "."
					+ aDes.substring(nLength - 2);
			aDes += cUnit;
		}
		return aDes;
	}

	/**
	 * 
	 * @param day
	 * @param dt
	 * @return
	 */
	public static int getWeekOfDate(String day, SimpleDateFormat dt) {
		Date date = null;
		try {
			date = dt.parse(day);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (date == null) {
			return -1;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}
		return w;
	}

	/**
	 * 
	 * @param nTime
	 * @return
	 */
	public static int Time4Min(int nTime) {
		int nRtn = nTime / 100 * 60 + nTime % 100;
		return nRtn;
	}

	/**
	 * 
	 * @param nTime
	 * @return
	 */
	public static int Min4Time(int nTime) {
		int nRtn = nTime / 60 * 100 + nTime % 60;

		return nRtn;
	}

	public static void main(String[] args) {
		// ReadFile r = ReadFile.getInstance();
		// byte[] data = r.readFile("/pubkey.bin");
		// System.out.println(data.length);
		// String a = "test";
		// String b = null;
		// b =a ;
		// a = null;
		// System.out.println( b );
		// long num = 330592;
		// byte [] b = long2Bytes(num);

		// byte [] b = {96,11,5,0,0,0,0,0};
		// System.out.println( "** " + bytes2Long(b,0));
		// Double.doubleToLongBits(arg0)

	}


	
	public static void ENOLog(String msg)
	{
		System.out.println("==============="+msg);
	}
}
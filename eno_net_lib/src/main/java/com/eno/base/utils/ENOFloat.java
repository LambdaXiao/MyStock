package com.eno.base.utils;

/**
 * <p>
 * Title: ENOFloat类
 * </p>
 * <p>
 * Description: 通过一个整形数,对浮点对象进行封装.注:只适合小一点的浮点数
 * </p>
 * <p>
 * Copyright: Copyright ENO(c) 2004
 * </p>
 * <p>
 * Company: ENO
 * </p>
 * 
 * @author cheng-dn
 * @version 1.1
 */

public class ENOFloat {
	public ENOFloat() {
		m_nUnit = 0;
		m_nValue = 0;
	}

	public ENOFloat(int nValue, int nUnit) {
		SetValue(nValue, nUnit);
	}

	public ENOFloat(ENOFloat nVal) {
		SetValue(nVal);
	}

	public ENOFloat(int nFloat) {
		m_nUnit = nFloat % 4;
		if (m_nUnit < 0) {
			m_nUnit = 4 + m_nUnit;
		}
		m_nValue = (nFloat - m_nUnit) / 4;
	}

	public void SetValue(int nValue, int nUnit) {
		m_nValue = nValue;
		m_nUnit = nUnit;
		if (m_nUnit > MAX_UNIT)
			m_nUnit = MAX_UNIT;
	}

	public void SetValue(ENOFloat nVal) {
		m_nValue = nVal.m_nValue;
		m_nUnit = nVal.m_nUnit;
	}

	public static int toENOFloat(double ftValue, int nUnit) {
		if (nUnit > MAX_UNIT - 1 || nUnit < 0)
			nUnit = 0;
		double dbRtn = ftValue * UNIT[nUnit];
		if (dbRtn >= 0)
			dbRtn += 0.5;
		else
			dbRtn -= 0.5;
		int nRtn = (int) dbRtn;
		nRtn *= 4;
		nRtn += nUnit;
		return nRtn;
	}

	/**
	 * 两个浮点数比较
	 */
	public static final int compare(ENOFloat val1, ENOFloat val2) {
		int nRtn = 0;
		if (val1.m_nValue > val2.m_nValue)
			nRtn = 1;
		else if (val1.m_nValue < val2.m_nValue)
			nRtn = -1;
		return nRtn;
	}

	/**
	 * 加运算
	 */
	public static final ENOFloat add(ENOFloat val1, ENOFloat val2) {
		int nValue = val1.m_nValue + val2.m_nValue;
		return new ENOFloat(nValue, val1.m_nUnit);
	}

	/**
	 * 减运算
	 */
	public static final ENOFloat sub(ENOFloat val1, ENOFloat val2) {
		int nValue = val1.m_nValue - val2.m_nValue;
		return new ENOFloat(nValue, val1.m_nUnit);
	}

	/**
	 * 乘运算
	 */
	public static final ENOFloat mul(ENOFloat val1, int val2) {
		return new ENOFloat(val1.m_nValue * val2, val1.m_nUnit);
	}

	public static final ENOFloat mul(ENOFloat val1, ENOFloat val2) {
		return new ENOFloat(val1.m_nValue * val2.m_nValue / UNIT[val2.m_nUnit],
				val1.m_nUnit);
	}

	/**
	 * 除运算
	 */
	public static final ENOFloat div(ENOFloat val1, int val2) {
		return new ENOFloat(val1.m_nValue / val2, val1.m_nUnit);
	}

	public static final ENOFloat div(ENOFloat val1, ENOFloat val2) {

		int value1 = val1.m_nValue * val1.m_nUnit;
		int value2 = val2.m_nValue * val2.m_nUnit;

		if (value2 != 0)
			return new ENOFloat(value1 / value2, val1.m_nUnit);
		else
			return new ENOFloat(value1, val1.m_nUnit);
	}

	/**
	 * 绝对值
	 */
	public static final ENOFloat abs(ENOFloat ftValue) {
		if (ftValue.m_nValue < 0)
			return new ENOFloat((0 - ftValue.m_nValue), ftValue.m_nUnit);
		else
			return new ENOFloat(ftValue.m_nValue, ftValue.m_nUnit);
	}

	/**
	 * 最大值
	 */
	public static final ENOFloat max(ENOFloat val1, ENOFloat val2) {
		if (val1.m_nValue > val2.m_nValue)
			return new ENOFloat(val1);
		else
			return new ENOFloat(val2);
	}

	/**
	 * 最小值
	 */
	public static final ENOFloat min(ENOFloat val1, ENOFloat val2) {
		if (val1.m_nValue < val2.m_nValue)
			return new ENOFloat(val1);
		else
			return new ENOFloat(val2);
	}

	public String toMoney() {
		String strRtn = toString();
		if (this.m_nUnit == 1)
			strRtn += "万";
		else if (this.m_nUnit == 2)
			strRtn += "亿";
		return strRtn;
	}

	/**
	 * toString().
	 */
	public String toString() {
		StringBuffer strRtn = new StringBuffer(16);
		strRtn.append(m_nValue);
		int nLen = strRtn.length();
		int nPos = 0;
		if (m_nValue < 0)
			nPos = 1;
		if (m_nUnit > 0) {
			if ((nLen - nPos) > m_nUnit)
				strRtn.insert(nLen - m_nUnit, '.');
			else {
				strRtn.insert(nPos, "0.");
				while ((nLen - nPos) < m_nUnit) {
					strRtn.insert(nPos + 2, '0');
					nLen++;
				}
			}
		}
		return strRtn.toString();
	}

	public int getUnit() {
		return UNIT[m_nUnit];
	}

	public int m_nValue;
	public int m_nUnit;
	public static final int UNIT[] = { 1, 10, 100, 1000, 10000, 100000 };
	public static final int MAX_UNIT = 5;
}

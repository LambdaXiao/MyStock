package com.eno.net.enotc;

import com.eno.base.enocoder.base64;
import com.eno.base.utils.ENOUtils;

import java.util.Random;

/**
 * 获取KEY
 * @author 	xmf 
 *  @see  2013-11-11 下午5:13:14 包名com.eno.net.enotc 工程名称EnoNet
 *  @version 1.0
 */
public  class ENO_buildKey   {

	/** 通信密匙 */
	public static String m_bfKey;
	
	/**
	 * 得到动态生成的交易密钥，如果还没有存在与缓存中，就动态产生一份。
	 *            。
	 * @return 字节数组形式表示的交易密钥。
	 */
	public static String initKey() {

		if (m_bfKey == null) {
			byte[] buf = new byte[16];
			Random ran = new Random(Runtime.getRuntime().freeMemory());
			for (int i = 0; i < 16; i++)
				buf[i] = base64.GET_BASE64_CHAR(ran.nextInt());
			m_bfKey = ENOUtils.bytes2Ascstr(buf);
		}
		return m_bfKey;
	}


}

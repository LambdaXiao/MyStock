package com.eno.net.base;
import com.eno.net.channel.ENO_Channel;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
/**
 *连接抽象类
 * @author 	xmf 
 *  @see  2013-6-29 上午10:03:36 包名com.eno.net.socket.base 工程名称EnoNet_lxzq
 *  @version 1.0
 */
public abstract class ENO_AbstractChannel implements ENO_Channel  {

	protected long lastActTime = 0;//最后一次收到消息的时间
	protected int netOutTime = 10000;// 连接超时时间
	protected String m_addr;//ip地址
	protected int m_port;//端口	

	protected boolean m_fKeepAlive;

	private ByteArrayOutputStream m_baos = new ByteArrayOutputStream();

	protected String m_strErrMsg = "";
	protected int m_nContentLength = 0; // 当前需要要接收的数据长度

	private byte[] m_recvBuff = new byte[2048];

	private int m_recvLen = 0;

	protected HttpURLConnection m_conn = null;

	protected DataInputStream m_is = null;

	protected boolean isConnected = false;// 连接是否正常

	protected DataOutputStream m_os = null;

	byte[] byRtn = null;

	
	
	
	public long getLastActTime() {
		return lastActTime;
	}

	public void setLastActTime(long lastActTime) {
		this.lastActTime = lastActTime;
	}
	
	
	
	
	/**
	 * 发送心跳
	 */
	public void sendHeartbeat() throws IOException
	{
		
		
	}

	
	
	/**
	 * 获取超时时间
	 * 
	 * @return
	 */
	public int getNetOutTime() {
		return netOutTime;
	}

	/**
	 * 设置超时时间
	 * 
	 * @param netOutTime
	 */
	public void setNetOutTime(int netOutTime) {
		this.netOutTime = netOutTime;
	}



	/**
	 * 当getData()返回空时,通过getErrMsg()取到错误信息.
	 * 
	 * @return
	 */
	public String getErrMsg() {
		return m_strErrMsg;
	}

	/**
	 * 设置错误信息
	 * 
	 * @param strErrMsg
	 */
	public void setErrMsg(String strErrMsg) {
		m_strErrMsg = strErrMsg;
	}



	/**
	 * Add request properties for the configuration, profiles, and locale of
	 * this system.
	 */
	protected void setRequestHeaders(HttpURLConnection c) {

		String ua = "ENO android Client";
		try {

			c.setRequestProperty("User-Agent", ua);

		} catch (Exception e) {
		}
		String locale = System.getProperty("microedition.locale");
		if (locale != null) {
			try {
				c.setRequestProperty("Content-Language", locale);

			} catch (Exception e) {
			}
		}
	}

	/***************************************************************************
	 * 单字节读取 DataInputStream 中的数据
	 * 
	 * @return 字节数组
	 */
	protected byte[] readByteArrayFromStream() {
		// reportNetMsg("使用单字节读取的方式来进行");
		//Logger.v("使用单字节读取的方式来进行");
		m_baos.reset();
		try {
			m_recvLen = 0;
			while (true) {
				// 有些机种不支持批量读取数据，使用单字节读取的方式来进行
				int chr = m_is.read();

				if (chr > -1) {
					m_recvBuff[m_recvLen++] = (byte) chr;
					if (m_recvLen == m_recvBuff.length) {
						m_baos.write(m_recvBuff, 0, m_recvLen);
						m_recvLen = 0;
					}
				} else {

					if (m_recvLen > 0) {
						m_baos.write(m_recvBuff, 0, m_recvLen);
						m_recvLen = 0;
					}
					break;
				}
			}
		} catch (Exception e) {
			m_strErrMsg += "readByteArrayFromStream exception.\r\n";

		}
		return m_baos.toByteArray();
	}

	
	/**
	 * 获取头部标识工具类
	 * @param header
	 * @param key
	 * @return
	 */
	public String getHeaderField(String header, String key) {
		if (header == null || key == null)
			return null;
		header = header.toUpperCase();
		key = key.toUpperCase();

		int clIndex = header.indexOf(key);
		try {
			byte requst[] = header.getBytes();
			if (clIndex != -1) { // 从clIndex+1起至\r\n
				StringBuffer sb = new StringBuffer();

				for (int i = (clIndex + key.length() + 1);; i++) {
					if (requst[i] != (byte) 13 && requst[i] != (byte) 10) {
						sb.append((char) requst[i]);
					} else
						break;
				}
				return sb.toString(); // 值
			}
		} catch (Exception e) {
		}
		return null;
	}

	public String getaddr() {
		return m_addr;
	}

	public void setAddr(String addr) {
		this.m_addr = addr;
	}

	public int getport() {
		return m_port;
	}

	public void setPort(int port) {
		this.m_port = port;
	}


}

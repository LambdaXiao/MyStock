package com.eno.net.enotc;
import com.eno.base.utils.ENOUtils;
import com.eno.net.channel.ENO_Channel;
import com.eno.net.codec.ENO_Decoder;
import com.eno.net.messages.ENO_NetParseException;
import com.eno.net.worke.ENO_NetWorke;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * 解码
 * 
 * @author xmf
 * @see  2013-6-27 下午12:54:57 包名com.eno.net.twogo.push.messages
 *      工程名称EnoNet_lxzq
 * @version 1.0
 */
public class ENO_HttpResponseDecoder implements ENO_Decoder {
	private byte crlf13 = (byte) 13; // '\r'
	private byte crlf10 = (byte) 10; // '\n'
	private byte ENOFlag = 1;// 返回标志
	private int ENOPackage = -1;// 默认是-1因为推送回来的数据id为0.为了区分开来
	private byte Encrypt = 0x00;// 是否加密36;
	private byte Compress = 0x00;// 是否压缩18；
	private  long dataSize=0;//数据大小
	private ByteArrayOutputStream chunkedData = new ByteArrayOutputStream();
	private boolean isEnd = true;// 读完头后，流是否还有数据可读
	private boolean isChunked = false;
	protected int m_nContentLength = 0; // 当前需要要接收的数据长度
	// protected boolean m_fProxyMode;
	private DataInputStream m_is;
	/**加解密接口*/
	private I_ENODataEncoder m_encoder; 
	
	/**
	 * 初始化的内容
	 */
	private void init()
	{
		dataSize=0;	
	}
	
	@Override
	public byte[] decode(ENO_NetWorke worke, ENO_Channel channel)  throws Exception {
		// TODO Auto-generated method stub
		init();
		if(channel==null)
		{
			System.out.println("channel为空");
		}
		m_is = (DataInputStream) channel.getResponse();
	
		byte[] resultData = null;
		m_nContentLength = 0;
		// reportNetMsg("接收成功。开始解析数据.....");
		// 开始解析数据包
		ReadSocketHeader();// 读包头并为 m_nContentLength 赋值。
		if (isEnd) {
			return null;
		}
		// reportNetMsg("读完包头：contentLength = " + m_nContentLength);
		if (m_nContentLength > 0) {

			resultData = new byte[(int) m_nContentLength];
			int actual = 0;
			int bytesread = 0;
			int maxPerRead = 256;

			while ((bytesread < m_nContentLength) && (actual != -1)) {
				if ((m_nContentLength - bytesread) > 256) {
					maxPerRead = 256;
				} else {
					maxPerRead = m_nContentLength - bytesread;
				}

				actual = m_is.read(resultData, bytesread, maxPerRead);
				
			
				if (actual == -1) {
					// isConnected = false;
					throw  new ENO_NetParseException();
				}

				bytesread += actual;
			}

		} else if (isChunked) {

			resultData = getchunkedResponse();

		} else {

			resultData = null;

		}
		
		dataSize=resultData.length;	
			
		resultData=Decode(resultData,Encrypt,Compress);
		
		
		return resultData;
	}

	/***************************************************************************
	 * 这个方法处理返回的是chunked的数据
	 * 
	 * @return
	 */
	public byte[] getchunkedResponse() throws IOException {
		chunkedData.reset();
		System.out.println("处理chunked包数据");
		int chunkedSize = 0;
		boolean readFrist = true;// 是不是第一次在包头后读chuck包
		int i = 0;
		// 循环连续读取chunk包
		do {
			i++;
			int chunkcrlf;
			int chunkcrlfNum = 0; // 已经连接的回车换行数 crlfNum=4为头部结束
			StringBuffer temp = new StringBuffer();
			
				// 从现在起，读chunk包的大小。
				while ((chunkcrlf = m_is.read()) != -1) // 读取头部
				{
					if (chunkcrlf == crlf13 || chunkcrlf == crlf10) {
						chunkcrlfNum++;
					}
					temp = temp.append((char) chunkcrlf); // byte数组相加
					// 去掉HTTP包头后。整个通信chunk包结构是：chunk大小\r\n内容\r\n包大小\r\n内容、、、、、
					if (readFrist == true && chunkcrlfNum == 2) {
						break;
					} else if (readFrist == false && chunkcrlfNum == 4) {
						break;
					}
				}
			
			
			
			try {
				// 本次包的大小为：(十六进制)

				chunkedSize = Integer.parseInt(temp.toString().trim(), 16);

				// reportNetMsg("第" + i + "个chunked包的大小：" + chunkedSize );
				// 接着块读取本次包内容
				if (chunkedSize != 0) {
					int readed = 0; // 已经读取数
					int available = 0;// input.available(); //可读数
					while (readed < chunkedSize) {
						available = chunkedSize - readed; // size-readed--剩余数
						if (available > 256)
							available = 256; // size-readed--剩余数
						byte[] buffer = new byte[available];
						int reading = m_is.read(buffer);
						chunkedData.write(buffer, 0, reading);
						readed += reading; // 已读字符
					}
				}
				// 第一个chunk包读完
				readFrist = false;
			} catch (Exception e) {

				e.printStackTrace();

				return null;
			}

		} while (chunkedSize != 0);
		// reportNetMsg("chunked包的个数：" + i );
		// 返回读得的内容
		return chunkedData.toByteArray();
	}

	/***************************************************************************
	 * 读取返回的 包头信息。得到包头内 Content-Length的长度 如果包头内没有 Content-Length 的信息。那么
	 * DataInputStream 也把头信息读完了。接下来就让readByteArrayFromStream方法读后面的数据
	 */
	private void ReadSocketHeader()  throws Exception   // 读取头部 并获得大小
	{
		int crlf = 0;
		int crlfNum = 0; // 已经连接的回车换行数 crlfNum=4为头部结束
		StringBuffer socketHead = new StringBuffer(); // 用于保存包头内容


			while (true) // 读取头部
			{
				if ((crlf = m_is.read()) != -1) {
					if (crlf == crlf13 || crlf == crlf10) {
						crlfNum++;
					} else {
						crlfNum = 0;
					} // 不是则清
					socketHead = socketHead.append((char) crlf); // byte数组相加
					if (crlfNum == 4) {
						isEnd = false;// 读完头后还有数据可读。
						break;
					}
				} else {

					throw  new ENO_NetParseException();
					
				}
			}
		 
		// System.out.println("整个包头：" + socketHead.toString());
		String tempStr = (new String(socketHead)).toUpperCase();
		String resp = "200 OK";
		int reIndex = tempStr.indexOf(resp);
		if (reIndex == -1) {
			isEnd = true;
			return;
		}
		// 判断包是不是以chunked返回的。Transfer-Encoding: chunked
		String chu = "TRANSFER-ENCODING: CHUNKED";
		int chIndex = tempStr.indexOf(chu);
		if (chIndex != -1) {
			isChunked = true;
			return;
		}
		String key = "CONTENT-LENGTH:";
		String value = getHeaderField(tempStr, key);
		// System.out.println("CONTENT-LENGTH:_______________________"+value+"   "+chIndex);

		if (value != null) {
			m_nContentLength = Integer.parseInt(value); // 正式的HTML文件的大小
		} else {
			// m_strErrMsg += "得到content-length时发生异常！";
		}

		String temp = getHeaderField(tempStr, "ETag");

		//Logger.v("包头数据：" + temp);
		if (temp == null) {
			ENOPackage = -1;
		}

		String tmpValue[];
		if (temp != null && !"".equals(temp)) {
			if (temp.indexOf(",") > 0) {
				tmpValue = ENOUtils.split(temp, ",");
				ENOFlag = Byte.parseByte(tmpValue[0].trim());
				//Logger.v("ENOFlag=" + ENOFlag);

				ENOPackage = Integer.parseInt(tmpValue[1].trim());
				Compress = Byte.parseByte(tmpValue[2].trim());
				Encrypt = Byte.parseByte(tmpValue[3].trim());

			} else {
				// begin 为noki6270增加新的解包方式，没有逗号的字符串（不压缩通讯）
				//Logger.v("______________noki6270________________");
				ENOFlag = Byte.parseByte(temp.substring(0, 1));
				String temp1 = temp.substring(temp.length() - 2);
				if (Integer.parseInt(temp1) == 0) {
					ENOPackage = Integer.parseInt(temp.substring(1,
							temp.length() - 2));
					Encrypt = Byte.parseByte("0");
				} else {
					ENOPackage = Integer.parseInt(temp.substring(1,
							temp.length() - 3));
					Encrypt = Byte.parseByte(temp1);
				}

				// end 为noki6270增加新的解包方式，没有逗号的字符串
			}
		} else {
			ENOFlag = -1;
			Compress = 0;
			Encrypt = 0;
		}
		temp = null;
		tmpValue = null;
	}

	/**
	 * 获取头部标识工具类
	 * 
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
			e.printStackTrace();
		}
		return null;
	}

	
	
	
	
	/**
	 * 解密数据包
	 * 
	 * @param data
	 * @param encrypt
	 * @param compress
	 * @return
	 * @throws Exception
	 */
	private byte[] Decode(byte[] data,byte encrypt,byte compress) throws Exception {
		if(m_encoder==null)
		{
			
		 m_encoder = new I_ENODataEncoder(); //初始化加解密接口
		}
		// 如果需要解压缩或解密，我们先把它做了
		if (m_encoder != null
				&& encrypt != I_ENOBodyEncoder.ENCODE_NONE) {
			data = m_encoder.encryptData(data, encrypt,ENO_buildKey.m_bfKey, false);
		}
		if (data == null)
			throw new Exception("数据解码错误1。");

		if (m_encoder != null
				&& compress != I_ENOBodyEncoder.ENCODE_NONE) {
			data = m_encoder.compressData(data, compress, false);
		}

		if (data == null)
			throw new Exception("数据解压缩错误2。");
		return data;
	}
	
	
	public byte getENOFlag() {
		return ENOFlag;
	}

	public void setENOFlag(byte eNOFlag) {
		ENOFlag = eNOFlag;
	}

	public int getENOPackage() {
		return ENOPackage;
	}

	public void setENOPackage(int eNOPackage) {
		ENOPackage = eNOPackage;
	}

	public byte getEncrypt() {
		return Encrypt;
	}

	public void setEncrypt(byte encrypt) {
		Encrypt = encrypt;
	}

	public byte getCompress() {
		return Compress;
	}

	public void setCompress(byte compress) {
		Compress = compress;
	}
	
	public long getDataSize() {
		return dataSize;
	}

	public void setDataSize(long dataSize) {
		this.dataSize = dataSize;
	}
	
	
}
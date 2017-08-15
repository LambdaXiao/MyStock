package com.eno.net.enotc;
import com.eno.base.enocoder.base64;
import com.eno.net.codec.ENO_Encoder;

import java.util.Map;



/**
 * 编码
 * @author 	xmf 
 *  @see  2013-6-27 下午12:54:57 包名com.eno.net.twogo.push.messages 工程名称EnoNet_lxzq
 *  @version 1.0
 */
public  class  ENO_HttpRequestEncoder implements ENO_Encoder{

	/**加解密接口*/
	private static I_ENODataEncoder m_encoder; 
	
	private  long dataSize=0;//数据大小

	private int ENOPackage=0;
	
	private int Encrypt=0;
	private int Compress=0; 
	/**
	 * 初始化
	 */
	private void init()
	{
		dataSize=0;//数据大小	
		
	}
	
	
	@Override
	public byte[] encode(Map<String, Object> map) {
		// TODO Auto-generated method stub	
		init();
		String  m_strCmd = "";
			
	      String url=(String) map.get("url");
		  ENOPackage = (Integer) map.get("packageid");
		  Encrypt =(Integer) map.get("encrypt");// 是否加密36;
		  Compress = (Integer) map.get("compress");// 是否压缩18；
		

	    if(Compress>0)
	    {
	    	url=url+"&supportCompress="+Compress;
	    }
		
	    url += "&tc_packageid=" + ENOPackage;// 每个请求的唯一ID
	  
		
				
		
		if(Encrypt>0)
		{
		StringBuffer sbTCUrl = new StringBuffer();
		StringBuffer sbUrl = new StringBuffer();
		sbUrl.append("&");
		String strToken;
		int nFromIndex = 0;
		int nToIndex;

		while ((nToIndex = url.indexOf("&", nFromIndex)) != -1) {
			strToken = url.substring(nFromIndex, nToIndex);
			if (strToken.toLowerCase().startsWith("tc_")) {
				sbTCUrl.append(strToken);
				sbTCUrl.append('&');
			} else {
				sbUrl.append(strToken);
				sbUrl.append('&');
			}
			nFromIndex = nToIndex + 1;
		}
		strToken = url.substring(nFromIndex);
		if (strToken.toLowerCase().startsWith("tc_")) {
			sbTCUrl.append(strToken);
		} else {
			sbUrl.append(strToken);
		}
		m_strCmd += sbTCUrl.toString() + "&TC_ENCRYPT=" + Encrypt;	
		
		byte[] bySrcData = sbUrl.toString().getBytes();
		//Logger.v("加密前：", sbUrl.toString());
		m_strCmd += "&TC_REQLENGTH="
				+ bySrcData.length
				+ "&TC_REQDATA="
				+ Encode(bySrcData, ENO_buildKey.m_bfKey, (byte)Compress,
						(byte)Encrypt);
		//Logger.v("加密请求串：", m_strCmd);
	} else {
		m_strCmd = url;
		//Logger.v("请求串：", m_strCmd);
	}
			
		dataSize=m_strCmd.getBytes().length;
		
		return m_strCmd.getBytes();
	}
	
	/**
	 * 加密请求串
	 * 
	 * @param data
	 * @param szEncryptKey
	 * @param compressType
	 * @param encodeType
	 * @return
	 */
	private String Encode(byte[] data, String szEncryptKey, byte compressType,
			byte encodeType) {
		String strRtn = null;
		if(m_encoder==null)
		{
		 m_encoder = new I_ENODataEncoder(); //初始化加解密接口
		}

		byte[] temp = data;
		// 如果需要加密和压缩，我们先把它做了
		if (m_encoder!= null && temp != null
				&& compressType != I_ENOBodyEncoder.ENCODE_NONE) {
			temp = m_encoder
					.compressData(temp, compressType, true);
		}
		if (m_encoder != null && temp != null
				&& encodeType != I_ENOBodyEncoder.ENCODE_NONE) {
			temp = m_encoder.encryptData(temp, encodeType,
					szEncryptKey, true);
		}
		if (temp != null && temp.length > 0) {
			strRtn = new String(base64.encode(temp));
		}
		return strRtn;
	}
	
	
	
	public long getDataSize() {
		return dataSize;
	}
	public void setDataSize(long dataSize) {
		this.dataSize = dataSize;
	}
	
	
	public int getENOPackage() {
		return ENOPackage;
	}


	public void setENOPackage(int eNOPackage) {
		ENOPackage = eNOPackage;
	}


	public int getEncrypt() {
		return Encrypt;
	}


	public void setEncrypt(int encrypt) {
		Encrypt = encrypt;
	}


	public int getCompress() {
		return Compress;
	}


	public void setCompress(int compress) {
		Compress = compress;
	}
}
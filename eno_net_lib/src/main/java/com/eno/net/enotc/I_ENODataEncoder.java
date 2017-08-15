package com.eno.net.enotc;
import com.eno.base.compress.ENOCompress;
import com.eno.base.enocoder.BlowfishEasy;


/**
 * 解析数据
 */
public class I_ENODataEncoder implements I_ENOBodyEncoder
{
  public I_ENODataEncoder()
  {
  }

  /**
   * 压缩的实现接口
   * @param isEncode 是否进行编码
   * @param encodeType 编码解码的种类
   * @return 压缩或解压完成的数据内容
   */
  public byte[] compressData(byte[] source, byte encodeType, boolean isEncode)
  {
//    if (m_comp == null)
////    	m_comp = new ENOCompressOld();
//    	m_comp = new ENOCompress();
    return ENOCompress.CompressStream(source,isEncode);
    
//	return source;
  }

  /**
   * 加密的实现接口
   * @param isEncode 是否进行编码
   * @param encodeType 编码解码的种类
   * @param encryptKey 加解密KEY
   * @return 加密或解密完成的数据内容
   */
 public byte[] encryptData(byte[] source, byte encodeType, String encryptKey, boolean isEncode)
  {
  	if (m_bf == null)
  	
  		m_bf = new BlowfishEasy(encryptKey,0);

		byte[] byRtn = null;
		if (isEncode)
		{
			byRtn = m_bf.encodeData(source);
		}
	  else
	 	{
		  
	 		byRtn = m_bf.decodeData(source);
		}	
  	return byRtn;
  }

  private BlowfishEasy m_bf;
//  private ENOCompressOld m_comp;
//  private ENOCompress m_comp;
}

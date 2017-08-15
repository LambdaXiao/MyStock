package com.eno.base.compress;

import com.eno.base.jzlib.ZInputStream;
import com.eno.base.utils.ENOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * JZlib是Zlib的JAVA版本，Zlib用于压缩。压缩分九级（-1为默认压缩比 9为最高压缩比 0为不压缩 1为快速压缩）。
 * @author Administrator
 *
 */
public class ENOCompress {

	 /**
	   * 根据参数fIsCompress进行压缩或解压操作.
	   * 当fIsCompress= true,时进行压缩;当为false时,解压
	   */
	  public static byte[] CompressStream(byte lpSource[], boolean fIsCompress) {
	    //初始化变量
//	    m_lpInStr = lpSource; //当前处理串
	    int m_nInSize = lpSource.length; //待处理串总长度

	    if (m_nInSize == 0)
	      return null;

	    if (fIsCompress){ //压缩
//	    	ByteArrayOutputStream out=new ByteArrayOutputStream();
//		    ZOutputStream zOut=new ZOutputStream(out, JZlib.Z_BEST_COMPRESSION);
//		    DataOutputStream objOut=new DataOutputStream(zOut);
//		    try {
//				objOut.write(lpSource);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			return out.toByteArray();
	    	return lpSource;
	    }
	    else{ //解压
//	    	ENOSystem.println("解压前的大小" + (lpSource.length-4) );
	    	int sourceLen = ENOUtils.bytes2Integer(lpSource, 0);
//	    	ENOSystem.println("原来大小： " + sourceLen);
			byte [] compData = new byte[lpSource.length-4];
			System.arraycopy(lpSource, 4, compData, 0, compData.length );
	    	ByteArrayInputStream in=new ByteArrayInputStream(compData);
		    ZInputStream zIn=new ZInputStream(in);
		    //DataInputStream objIn=new DataInputStream(zIn);
		    byte[] resultData = null;
		    
			try {
				resultData = new byte[sourceLen];
				int actual = 0;
				int bytesread = 0;
				int maxPerRead = 256;
				while ((bytesread < sourceLen) && (actual != -1)) {
					if ((sourceLen - bytesread) > 256)
						maxPerRead = 256;
					else
						maxPerRead = sourceLen - bytesread;
					//actual = objIn.read(resultData, bytesread,maxPerRead);
					actual = zIn.read(resultData, bytesread,maxPerRead);
					bytesread += actual;
				}
			} catch (IOException e) {
				return null;
			}
			return resultData;
	    }
	    
	  }
	  
//	  public static void main(String[] args){
//		    try{
//		    	String hello="Hello World!";
//		    	byte [] b = hello.getBytes();
//		    	byte [] comp = ENOCompress.CompressStream(b, true);
//		    	
//		    }
//		    catch (Exception e){
//		      e.printStackTrace();
//		    }
//	  }
}

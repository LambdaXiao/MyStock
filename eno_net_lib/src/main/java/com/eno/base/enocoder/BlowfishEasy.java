
/*
 * Copyright 1997-2006 Markus Hahn 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eno.base.enocoder;
import com.eno.base.utils.ENOUtils;

/**
 * <p>Title: BlowfishEasy 类</p>
 * <p>Description: 对Blowfish算法封装,更易使用,且加入检验</p>
 * <p>Copyright: Copyright ENO(c) 2006</p>
 * <p>Company: ENO</p>
 * @author cheng-dn
 * @version 1.1
 */
public class BlowfishEasy
{
	BlowfishCBC bfc;
  private long m_cbciv;
  
	/**
	 * Constructor to use string data as the key.
	 * @param passw the password, usually gained by String.toCharArray()
	 */
	public BlowfishEasy(
		String strKey,long cbciv)
	{
/*
		int i, c;
		MD5 sh = null;
		byte[] hash;
    byte[] passw = strKey.getBytes();

		// hash down the password to a 160bit key, using SHA-1

		//sh = new SHA1();
    sh = new MD5();
		for (i = 0, c = passw.length; i < c; i++)
		{
			sh.update(passw[i]);
		}

		//sh.finalize();

		// setup the encryptor (using a dummy IV for now)

		hash = new byte[sh.getDigestSize()];
		sh.doFinal(hash, 0);
    m_cbciv = cbciv;
		this.bfc = new BlowfishCBC(hash, 0, hash.length);
*/
    byte[] hash = strKey.getBytes();
		m_cbciv = cbciv;
		this.bfc = new BlowfishCBC(hash, 0, hash.length);
	}


	public byte[] encodeData(byte[] data)
	{
		int i, pos, datalen;
		char achar;
		byte padval;
		byte[] buf;
		byte[] new_cbciv_buf;

		datalen = data.length;
    int nCRCData = (int)CRC.calcCRC(data);
		buf = new byte[(datalen & ~7) + 16];
    ENOUtils.integer2Bytes(buf,0,datalen);
    ENOUtils.integer2Bytes(buf,4,nCRCData);
    
    System.arraycopy(data,0,buf,8,data.length);
		padval = (byte) (buf.length - datalen);
		pos = 8+datalen;
    while (pos < buf.length)
		{
			buf[pos++] = padval;
		}
		this.bfc.setCBCIV(m_cbciv);
		this.bfc.encrypt(buf, 8, buf, 8, buf.length-8);
    return buf;
	}

	
	
	
	
	public byte[] decodeData(byte[] data)
	{
    int nSrcLen = ENOUtils.bytes2Integer(data,0);
    int nCRCData = ENOUtils.bytes2Integer(data,4); 
    this.bfc.setCBCIV(m_cbciv);
    this.bfc.decrypt(data, 8, data, 8, data.length-8);
    if (nSrcLen > 0 && nSrcLen <= data.length-8)
    {
      int nNewCRC = (int)CRC.calcCRC(data,8,nSrcLen);
      if (nNewCRC == nCRCData)
      {
        byte[] bRtn = new byte[nSrcLen];
        System.arraycopy(data,8,bRtn,0,nSrcLen);
        return bRtn;
      }
    }
    return null;
	}

	///////////////////////////////////////////////////////////////////////////

	/**
	 * Destroys (clears) the encryption engine, after that the instance is not
	 * valid anymore.
	 */
	public void destroy()
	{
		this.bfc.cleanUp();
	}
}

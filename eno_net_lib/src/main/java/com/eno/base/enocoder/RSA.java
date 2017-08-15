package com.eno.base.enocoder;

// RSA Encryption Implementation.
// Copyright (C) 2003 Eugene Luzgin, eugene@luzgin.com
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either version 2
// of the License, or any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
// See the GNU General Public License for more details
// at http://www.gnu.org/licenses/gpl.txt

import com.eno.base.utils.ENOUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class RSA
{
  class public_key {
    public vlong m;
    public vlong e;

    public public_key() {
      m = new vlong();
      e = new vlong();
    }

    public vlong encrypt(vlong plain) {
      return monty.modexp(plain, e, m);
    }
  };
  public_key iPubKey;
  public int iKeyLength;

        class KEYHEAD {
          public byte fileTag[]; // "ENOKEY"
          public int keyLength; // 钥匙的长度(字节数)
          public int dataLen; // 数据总长度
          public byte md5[]; // 数据MD5校验值
          public static final int KEYHEAD_LENGTH = 30;

          public KEYHEAD() {
            fileTag = new byte[6];
            md5 = new byte[16];
          }

          public boolean check(byte data[], int len) {
            if (len < KEYHEAD_LENGTH) {
              return false;
            }

            if (data[0] != 'E' ||
                data[1] != 'N' ||
                data[2] != 'O' ||
                data[3] != 'K' ||
                data[4] != 'E' ||
                data[5] != 'Y') {
              return false;
            }

            // check MD5
            byte digest[] = new byte[16];
            MD5 md5obj = new MD5();
            md5obj.update(data, KEYHEAD_LENGTH, len - KEYHEAD_LENGTH);
            md5obj.doFinal(digest, 0);
            for (int i = 0; i < 16; i++) {
              if (data[14 + i] != digest[i]) {
                return false;
              }
            }

            return true;
          }
        };

        public RSA()
	{
	}

      // 加载公钥数据
        public boolean loadPublicKey( InputStream in) {
          // 打开指定的文件
          byte[] byData = new byte[256];
          int nRtn = 0;
          try {
            //InputStream in = this.getClass().getResourceAsStream("/pubkey.bin");
            nRtn = in.read(byData);
            in.close();
          }
          catch (Exception e) {
            e.printStackTrace();
            return false;
          }

          KEYHEAD header = new KEYHEAD();
          if (!header.check(byData, nRtn)) {
            return false;
          }

          // Key的长度(Bytes)
  				iKeyLength = ENOUtils.bytes2Integer(byData, 6);

          // 数据格式正确，对整块数据进行左移1位
          int nSize = nRtn - KEYHEAD.KEYHEAD_LENGTH;
          int nOff = KEYHEAD.KEYHEAD_LENGTH;

          byte bit0 = (byte) ( ( (byData[nOff] & 0x80) >> 7) & 0x01); // 第一个字节的7位被移到了最后一个字节的0位
          byte bit7;
          for (int j = nSize - 1; j >= 0; j--) {
            bit7 = (byte) ( ( (byData[j + nOff] & 0x80) >> 7) & 0x01);
            byData[j + nOff] = (byte) ( (byData[j + nOff] << 1) | bit0);
            bit0 = bit7;
          }

          // Ok, 初始化iExponent和iModulus
          iPubKey = new public_key();

          int nWords = ENOUtils.bytes2Integer(byData, nOff);
          nOff += 4;
          nSize -= 4;
          iPubKey.e.load(byData, nOff, nWords * 4);
          nOff += nWords * 4;
          nSize -= nWords * 4;

          nWords = ENOUtils.bytes2Integer(byData, nOff);
          nOff += 4;
          nSize -= 4;
          iPubKey.m.load(byData, nOff, nWords * 4);
          nOff += nWords * 4;
          nSize -= nWords * 4;

          return true;
        }

  /**
   *
   * @param bIn
   * @return
     */
    public byte[] encode(byte[] bIn)
    {
      // 为了防止nDataLength不是4的整数，我们在pInData之前加上一个DWORD来表示nDataLength
      byte bInData[] = new byte[4 + (bIn.length + 3) / 4 * 4];
      ENOUtils.integer2Bytes(bInData, 0, bIn.length);
      System.arraycopy(bIn, 0, bInData, 4, bIn.length);

    	// 开始加密处理：当nSize超过了m_nBlockSize时，我们需要分块来处理
    	int nMaxSize = iKeyLength - 4;  // 每一块最大可处理的数据
    	int nOff = 0;
    	int nTotalSize = bInData.length;
	    ByteArrayOutputStream arrOut = new ByteArrayOutputStream();
    	while (nTotalSize > 0) {
         vlong inL = new vlong();
         int nBlockSize = nTotalSize>nMaxSize? nMaxSize : nTotalSize;
         inL.load(bInData, nOff, nBlockSize);
         nOff += nBlockSize;
         nTotalSize -= nBlockSize;
         vlong outL = iPubKey.encrypt(inL);
	       byte[] bOutData = outL.getBytes();
         arrOut.write(bOutData, 0, bOutData.length);
      }
      
      return arrOut.toByteArray();
    }
}



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
 * <p>Title: BlowfishCBC 类</p>
 * <p>Description: Blowfish加密算法(CBC模式)</p>
 * <p>Copyright: Copyright ENO(c) 2004</p>
 * <p>Company: ENO</p>
 * @author cheng-dn
 * @version 1.1
 */
public class BlowfishCBC extends BlowfishECB
{

	// the CBC IV
	int iv_lo;
	int iv_hi;

	///////////////////////////////////////////////////////////////////////////

	/**
	 * Gets the current CBC IV.
	 * @return current CBC IV
	 */
	public long getCBCIV()
	{
		return ENOUtils.makeLong(iv_lo, iv_hi);
	}

	///////////////////////////////////////////////////////////////////////////

	/**
	 * Sets the current CBC IV (for cipher resets).
	 * @param lNewCBCIV the new CBC IV
	 */
	public void setCBCIV(
		long lNewCBCIV)
	{
		iv_hi = ENOUtils.longHi32(lNewCBCIV);
		iv_lo = ENOUtils.longLo32(lNewCBCIV);
	}

	///////////////////////////////////////////////////////////////////////////

	/**
	 * Sets the current CBC IV (for cipher resets).
	 * @param newCBCIV the new CBC IV
	 * @param ofs where to start reading the IV
	 */
	public void setCBCIV(
		byte[] newCBCIV,
		int ofs)
	{
		iv_hi = ENOUtils.byteArrayToInt(newCBCIV, ofs);
		iv_lo = ENOUtils.byteArrayToInt(newCBCIV, ofs + 4);
	}

	///////////////////////////////////////////////////////////////////////////

	/**
	 * Constructor, uses a zero CBC IV.
	 * @param key key material, up to MAXKEYLENGTH bytes
	 * @param ofs where to start reading the key
	 * @param len size of the key in bytes
	 */
	public BlowfishCBC(
		byte[] key,
		int ofs,
		int len)
	{
		super(key, ofs, len);

		iv_hi = iv_lo = 0;
	}

	///////////////////////////////////////////////////////////////////////////

	/**
	 * Constructor to define the CBC IV.
	 * @param key key material, up to MAXKEYLENGTH bytes
	 * @param ofs where to start reading the key
	 * @param len size of the key in bytes
	 * @param init_cbciv the CBC IV
	 */
	public BlowfishCBC(
		byte[] key,
		int ofs,
		int len,
		long init_cbciv)
	{
		super(key, ofs, len);

		setCBCIV(init_cbciv);
	}

	///////////////////////////////////////////////////////////////////////////

	/**
	 * Constructor to define the CBC IV.
	 * @param key key material, up to MAXKEYLENGTH bytes
	 * @param ofs where to start reading the key
	 * @param len size of the key in bytes
	 * @param init_cbciv the CBC IV
	 * @param iv_ofs where to start reading the IV
	 */
	public BlowfishCBC(
		byte[] key,
		int ofs,
		int len,
		byte[] init_cbciv,
		int iv_ofs)
	{
		super(key, ofs, len);

		setCBCIV(init_cbciv, iv_ofs);
	}

	///////////////////////////////////////////////////////////////////////////

	/**
	 * see blowfishj.BlowfishECB#cleanUp()
	 */
	public void cleanUp()
	{
		iv_hi = iv_lo = 0;
		super.cleanUp();
	}

	///////////////////////////////////////////////////////////////////////////

	/**
	 * @see blowfishj.BlowfishECB#encrypt(byte[], int, byte[], int, int)
	 */
	public int encrypt(
		byte[] inbuf,
		int inpos,
		byte[] outbuf,
		int outpos,
		int len)
	{
		// same speed tricks than in the ECB variant ...

		len -= len % BLOCKSIZE;

		int c = inpos + len;

		int[] pbox = this.pbox;
		int pbox00 = pbox[0];
		int pbox01 = pbox[1];
		int pbox02 = pbox[2];
		int pbox03 = pbox[3];
		int pbox04 = pbox[4];
		int pbox05 = pbox[5];
		int pbox06 = pbox[6];
		int pbox07 = pbox[7];
		int pbox08 = pbox[8];
		int pbox09 = pbox[9];
		int pbox10 = pbox[10];
		int pbox11 = pbox[11];
		int pbox12 = pbox[12];
		int pbox13 = pbox[13];
		int pbox14 = pbox[14];
		int pbox15 = pbox[15];
		int pbox16 = pbox[16];
		int pbox17 = pbox[17];

		int[] sbox1 = this.sbox1;
		int[] sbox2 = this.sbox2;
		int[] sbox3 = this.sbox3;
		int[] sbox4 = this.sbox4;

		int iv_hi = this.iv_hi;
		int iv_lo = this.iv_lo;

		int hi, lo;

		while (inpos < c)
		{
			hi  =  inbuf[inpos++] << 24;
			hi |= (inbuf[inpos++] << 16) & 0x0ff0000;
			hi |= (inbuf[inpos++] <<  8) & 0x000ff00;
			hi |=  inbuf[inpos++]        & 0x00000ff;

			lo  =  inbuf[inpos++] << 24;
			lo |= (inbuf[inpos++] << 16) & 0x0ff0000;
			lo |= (inbuf[inpos++] <<  8) & 0x000ff00;
			lo |=  inbuf[inpos++]        & 0x00000ff;

			// extra step: chain with IV

			hi ^= iv_hi;
			lo ^= iv_lo;

			hi ^= pbox00;
			lo	^= (((sbox1[hi >>> 24] + sbox2[(hi >>> 16) & 0x0ff]) ^ sbox3[(hi >>> 8) & 0x0ff]) + sbox4[hi & 0x0ff]) ^ pbox01;
			hi	^= (((sbox1[lo >>> 24] + sbox2[(lo >>> 16) & 0x0ff]) ^ sbox3[(lo >>> 8) & 0x0ff]) + sbox4[lo & 0x0ff]) ^ pbox02;
			lo	^= (((sbox1[hi >>> 24] + sbox2[(hi >>> 16) & 0x0ff]) ^ sbox3[(hi >>> 8) & 0x0ff]) + sbox4[hi & 0x0ff]) ^ pbox03;
			hi	^= (((sbox1[lo >>> 24] + sbox2[(lo >>> 16) & 0x0ff]) ^ sbox3[(lo >>> 8) & 0x0ff]) + sbox4[lo & 0x0ff]) ^ pbox04;
			lo	^= (((sbox1[hi >>> 24] + sbox2[(hi >>> 16) & 0x0ff]) ^ sbox3[(hi >>> 8) & 0x0ff]) + sbox4[hi & 0x0ff]) ^ pbox05;
			hi	^= (((sbox1[lo >>> 24] + sbox2[(lo >>> 16) & 0x0ff]) ^ sbox3[(lo >>> 8) & 0x0ff]) + sbox4[lo & 0x0ff]) ^ pbox06;
			lo	^= (((sbox1[hi >>> 24] + sbox2[(hi >>> 16) & 0x0ff]) ^ sbox3[(hi >>> 8) & 0x0ff]) + sbox4[hi & 0x0ff]) ^ pbox07;
			hi	^= (((sbox1[lo >>> 24] + sbox2[(lo >>> 16) & 0x0ff]) ^ sbox3[(lo >>> 8) & 0x0ff]) + sbox4[lo & 0x0ff]) ^ pbox08;
			lo	^= (((sbox1[hi >>> 24] + sbox2[(hi >>> 16) & 0x0ff]) ^ sbox3[(hi >>> 8) & 0x0ff]) + sbox4[hi & 0x0ff]) ^ pbox09;
			hi	^= (((sbox1[lo >>> 24] + sbox2[(lo >>> 16) & 0x0ff]) ^ sbox3[(lo >>> 8) & 0x0ff]) + sbox4[lo & 0x0ff]) ^ pbox10;
			lo	^= (((sbox1[hi >>> 24] + sbox2[(hi >>> 16) & 0x0ff]) ^ sbox3[(hi >>> 8) & 0x0ff]) + sbox4[hi & 0x0ff]) ^ pbox11;
			hi	^= (((sbox1[lo >>> 24] + sbox2[(lo >>> 16) & 0x0ff]) ^ sbox3[(lo >>> 8) & 0x0ff]) + sbox4[lo & 0x0ff]) ^ pbox12;
			lo	^= (((sbox1[hi >>> 24] + sbox2[(hi >>> 16) & 0x0ff]) ^ sbox3[(hi >>> 8) & 0x0ff]) + sbox4[hi & 0x0ff]) ^ pbox13;
			hi	^= (((sbox1[lo >>> 24] + sbox2[(lo >>> 16) & 0x0ff]) ^ sbox3[(lo >>> 8) & 0x0ff]) + sbox4[lo & 0x0ff]) ^ pbox14;
			lo	^= (((sbox1[hi >>> 24] + sbox2[(hi >>> 16) & 0x0ff]) ^ sbox3[(hi >>> 8) & 0x0ff]) + sbox4[hi & 0x0ff]) ^ pbox15;
			hi	^= (((sbox1[lo >>> 24] + sbox2[(lo >>> 16) & 0x0ff]) ^ sbox3[(lo >>> 8) & 0x0ff]) + sbox4[lo & 0x0ff]) ^ pbox16;

			lo ^= pbox17;

			outbuf[outpos++] = (byte)(lo >>> 24);
			outbuf[outpos++] = (byte)(lo >>> 16);
			outbuf[outpos++] = (byte)(lo >>>  8);
			outbuf[outpos++] = (byte) lo;

			outbuf[outpos++] = (byte)(hi >>> 24);
			outbuf[outpos++] = (byte)(hi >>> 16);
			outbuf[outpos++] = (byte)(hi >>>  8);
			outbuf[outpos++] = (byte) hi;

			// (the encrypted block becomes the new IV)

			iv_hi = lo;
			iv_lo = hi;
		}

		this.iv_hi = iv_hi;
		this.iv_lo = iv_lo;

		return len;
	}

	///////////////////////////////////////////////////////////////////////////

	/**
	 * @see blowfishj.BlowfishECB#decrypt(byte[], int, byte[], int, int)
	 */
	public int decrypt(
		byte[] inbuf,
		int inpos,
		byte[] outbuf,
		int outpos,
		int len)
	{
		len -= len % BLOCKSIZE;

		int nC = inpos + len;

		int[] pbox = this.pbox;
		int pbox00 = pbox[0];
		int pbox01 = pbox[1];
		int pbox02 = pbox[2];
		int pbox03 = pbox[3];
		int pbox04 = pbox[4];
		int pbox05 = pbox[5];
		int pbox06 = pbox[6];
		int pbox07 = pbox[7];
		int pbox08 = pbox[8];
		int pbox09 = pbox[9];
		int pbox10 = pbox[10];
		int pbox11 = pbox[11];
		int pbox12 = pbox[12];
		int pbox13 = pbox[13];
		int pbox14 = pbox[14];
		int pbox15 = pbox[15];
		int pbox16 = pbox[16];
		int pbox17 = pbox[17];

		int[] sbox1 = this.sbox1;
		int[] sbox2 = this.sbox2;
		int[] sbox3 = this.sbox3;
		int[] sbox4 = this.sbox4;

		int iv_hi = this.iv_hi;
		int iv_lo = this.iv_lo;

		int tmp_hi, tmp_lo;

		int hi, lo;

		while (inpos < nC)
		{
			hi  =  inbuf[inpos++] << 24;
			hi |= (inbuf[inpos++] << 16) & 0x0ff0000;
			hi |= (inbuf[inpos++] <<  8) & 0x000ff00;
			hi |=  inbuf[inpos++]        & 0x00000ff;

			lo  =  inbuf[inpos++] << 24;
			lo |= (inbuf[inpos++] << 16) & 0x0ff0000;
			lo |= (inbuf[inpos++] <<  8) & 0x000ff00;
			lo |=  inbuf[inpos++]        & 0x00000ff;

			// (save the current block, it will become the new IV)
			tmp_hi = hi;
			tmp_lo = lo;

			hi ^= pbox17;
			lo	^= (((sbox1[hi >>> 24] + sbox2[(hi >>> 16) & 0x0ff]) ^ sbox3[(hi >>> 8) & 0x0ff]) + sbox4[hi & 0x0ff]) ^ pbox16;
			hi	^= (((sbox1[lo >>> 24] + sbox2[(lo >>> 16) & 0x0ff]) ^ sbox3[(lo >>> 8) & 0x0ff]) + sbox4[lo & 0x0ff]) ^ pbox15;
			lo	^= (((sbox1[hi >>> 24] + sbox2[(hi >>> 16) & 0x0ff]) ^ sbox3[(hi >>> 8) & 0x0ff]) + sbox4[hi & 0x0ff]) ^ pbox14;
			hi	^= (((sbox1[lo >>> 24] + sbox2[(lo >>> 16) & 0x0ff]) ^ sbox3[(lo >>> 8) & 0x0ff]) + sbox4[lo & 0x0ff]) ^ pbox13;
			lo	^= (((sbox1[hi >>> 24] + sbox2[(hi >>> 16) & 0x0ff]) ^ sbox3[(hi >>> 8) & 0x0ff]) + sbox4[hi & 0x0ff]) ^ pbox12;
			hi	^= (((sbox1[lo >>> 24] + sbox2[(lo >>> 16) & 0x0ff]) ^ sbox3[(lo >>> 8) & 0x0ff]) + sbox4[lo & 0x0ff]) ^ pbox11;
			lo	^= (((sbox1[hi >>> 24] + sbox2[(hi >>> 16) & 0x0ff]) ^ sbox3[(hi >>> 8) & 0x0ff]) + sbox4[hi & 0x0ff]) ^ pbox10;
			hi	^= (((sbox1[lo >>> 24] + sbox2[(lo >>> 16) & 0x0ff]) ^ sbox3[(lo >>> 8) & 0x0ff]) + sbox4[lo & 0x0ff]) ^ pbox09;
			lo	^= (((sbox1[hi >>> 24] + sbox2[(hi >>> 16) & 0x0ff]) ^ sbox3[(hi >>> 8) & 0x0ff]) + sbox4[hi & 0x0ff]) ^ pbox08;
			hi	^= (((sbox1[lo >>> 24] + sbox2[(lo >>> 16) & 0x0ff]) ^ sbox3[(lo >>> 8) & 0x0ff]) + sbox4[lo & 0x0ff]) ^ pbox07;
			lo	^= (((sbox1[hi >>> 24] + sbox2[(hi >>> 16) & 0x0ff]) ^ sbox3[(hi >>> 8) & 0x0ff]) + sbox4[hi & 0x0ff]) ^ pbox06;
			hi	^= (((sbox1[lo >>> 24] + sbox2[(lo >>> 16) & 0x0ff]) ^ sbox3[(lo >>> 8) & 0x0ff]) + sbox4[lo & 0x0ff]) ^ pbox05;
			lo	^= (((sbox1[hi >>> 24] + sbox2[(hi >>> 16) & 0x0ff]) ^ sbox3[(hi >>> 8) & 0x0ff]) + sbox4[hi & 0x0ff]) ^ pbox04;
			hi	^= (((sbox1[lo >>> 24] + sbox2[(lo >>> 16) & 0x0ff]) ^ sbox3[(lo >>> 8) & 0x0ff]) + sbox4[lo & 0x0ff]) ^ pbox03;
			lo	^= (((sbox1[hi >>> 24] + sbox2[(hi >>> 16) & 0x0ff]) ^ sbox3[(hi >>> 8) & 0x0ff]) + sbox4[hi & 0x0ff]) ^ pbox02;
			hi	^= (((sbox1[lo >>> 24] + sbox2[(lo >>> 16) & 0x0ff]) ^ sbox3[(lo >>> 8) & 0x0ff]) + sbox4[lo & 0x0ff]) ^ pbox01;

			lo ^= pbox00;

			// extra step: unchain

			hi ^= iv_lo;
			lo ^= iv_hi;

			outbuf[outpos++] = (byte)(lo >>> 24);
			outbuf[outpos++] = (byte)(lo >>> 16);
			outbuf[outpos++] = (byte)(lo >>>  8);
			outbuf[outpos++] = (byte) lo;

			outbuf[outpos++] = (byte)(hi >>> 24);
			outbuf[outpos++] = (byte)(hi >>> 16);
			outbuf[outpos++] = (byte)(hi >>>  8);
			outbuf[outpos++] = (byte) hi;

			// (now set the new IV)
			iv_hi = tmp_hi;
			iv_lo = tmp_lo;
		}

		this.iv_hi = iv_hi;
		this.iv_lo = iv_lo;

		return len;
	}
}

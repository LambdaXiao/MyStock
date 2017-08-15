/**
 * A simple class the hold and calculate the CRC for sanity checking of the
 * data.
 *
 * @author Keiron Liddle
 */
package com.eno.base.enocoder;
/**
 * <p>Title: CRC检验类</p>
 * <p>Description: CRC算法</p>
 * <p>Copyright: Copyright ENO(c) 2004</p>
 * <p>Company: ENO</p>
 * @author cheng-dn
 * @version 1.1
 */
class CRC
{
	private static int[] crc_table = make_crc_table();

  /** Make the table for a fast CRC. */
  private static int[] make_crc_table ()
  {
    int[] crc_table = new int[256];
    for (int n = 0; n < 256; n++)
    {
      int c = n;
      for (int k = 8;  --k >= 0; )
      {
        if ((c & 1) != 0)
          c = 0xedb88320 ^ (c >>> 1);
        else
          c = c >>> 1;
      }
      crc_table[n] = c;
    }
    return crc_table;
  }

	private static int update_crc(byte[] buf,int off,int len){   
		int c = 0xffffffff;   
		int n;   
		for (n = off; n < len + off;n++){   
	    c = crc_table[(c^buf[n])&0xff]^(c >>> 8);   
		}   
		return c;   
	}   

	public static int calcCRC(byte[] data)
	{   
		return update_crc(data,0,data.length)^0xffffffff;   
	}
	
	public static int calcCRC(byte[] data,int off,int len)
	{   
		return update_crc(data,off,len)^0xffffffff;   
	}
}

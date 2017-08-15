package com.eno.base.enocoder;

/**
 * <p>Title: base64 类</p>
 * <p>Description: 对于base64算法进行的封装</p>
 * <p>Copyright: Copyright ENO(c) 2004</p>
 * <p>Company: ENO</p>
 * @author cheng-dn
 * @version 1.1
 */
public class  base64
{
  static private final int  BASELENGTH         = 255;
  static private final int  LOOKUPLENGTH       = 64;
  static private final int  TWENTYFOURBITGROUP = 24;
  static private final int  EIGHTBIT           = 8;
  static private final int  SIXTEENBIT         = 16;
  static private final int  SIXBIT             = 6;
  static private final int  FOURBYTE           = 4;
  static private final int  SIGN               = -128;
  static private final byte PAD                = (byte) '=';
  static private byte [] base64Alphabet       = new byte[BASELENGTH];
  static private byte [] lookUpBase64Alphabet = new byte[LOOKUPLENGTH];
  //static private final Log log = LogSource.getInstance("org.apache.commons.util.Base64");

  static
  {
      for (int i = 0; i < BASELENGTH; i++ )
      {
          base64Alphabet[i] = -1;
      }
      for (int i = 'Z'; i >= 'A'; i--)
      {
          base64Alphabet[i] = (byte) (i - 'A');
      }
      for (int i = 'z'; i>= 'a'; i--)
      {
          base64Alphabet[i] = (byte) (i - 'a' + 26);
      }
      for (int i = '9'; i >= '0'; i--)
      {
          base64Alphabet[i] = (byte) (i - '0' + 52);
      }

      base64Alphabet['+']  = 62;
      base64Alphabet['/']  = 63;

      for (int i = 0; i <= 25; i++ )
          lookUpBase64Alphabet[i] = (byte) ('A' + i);

      for (int i = 26,  j = 0; i <= 51; i++, j++ )
          lookUpBase64Alphabet[i] = (byte) ('a'+ j);

      for (int i = 52,  j = 0; i <= 61; i++, j++ )
          lookUpBase64Alphabet[i] = (byte) ('0' + j);

      lookUpBase64Alphabet[62] = (byte) '+';
      lookUpBase64Alphabet[63] = (byte) '/';
  }
	/**
	 * 判断一个字串是否为 Base64编码
	 * @return 是或否
	 * @param isValidString 要验证的字符串.
	 */
  public static boolean isBase64( String isValidString )
  {
      return isArrayByteBase64(isValidString.getBytes());
  }
	/**
	 * 判断一个字符是否为Base64.
	 * @return 是或否
	 * @param octect 要验证的字符
	 */
  public static boolean isBase64( byte octect )
  {
      //shall we ignore white space? JEFF??
      return (octect == PAD || base64Alphabet[octect] != -1);
  }
	
	/**
	 * 判断一个byte[]是否为base64.
	 * @return 是或否
	 * @param arrayOctect 要判定的数组.
	 */
  public static boolean isArrayByteBase64( byte[] arrayOctect )
  {
      int length = arrayOctect.length;
      if (length == 0)
      {
          // shouldn't a 0 length array be valid base64 data?
          // return false;
          return true;
      }
      for (int i=0; i < length; i++)
      {
          if ( !base64.isBase64(arrayOctect[i]) )
              return false;
      }
      return true;
  }

  /**
   * 编码一个二进制数组成Base64.
   *
   * @param binaryData 要编码的数据.
   * @return 编码后的二进制数据.
   */
  public static byte[] encode( byte[] binaryData )
  {
      int      lengthDataBits    = binaryData.length*EIGHTBIT;
      int      fewerThan24bits   = lengthDataBits%TWENTYFOURBITGROUP;
      int      numberTriplets    = lengthDataBits/TWENTYFOURBITGROUP;
      byte     encodedData[]     = null;


      if (fewerThan24bits != 0)
      {
          //data not divisible by 24 bit
          encodedData = new byte[ (numberTriplets + 1 ) * 4 ];
      }
      else
      {
          // 16 or 8 bit
          encodedData = new byte[ numberTriplets * 4 ];
      }

      byte k = 0, l = 0, b1 = 0, b2 = 0, b3 = 0;

      int encodedIndex = 0;
      int dataIndex   = 0;
      int i           = 0;
      //log.debug("number of triplets = " + numberTriplets);
      for ( i = 0; i<numberTriplets; i++ )
      {
          dataIndex = i*3;
          b1 = binaryData[dataIndex];
          b2 = binaryData[dataIndex + 1];
          b3 = binaryData[dataIndex + 2];

          //log.debug("b1= " + b1 +", b2= " + b2 + ", b3= " + b3);

          l  = (byte)(b2 & 0x0f);
          k  = (byte)(b1 & 0x03);

          encodedIndex = i * 4;
          byte val1 = ((b1 & SIGN)==0)?(byte)(b1>>2):(byte)((b1)>>2^0xc0);
          byte val2 = ((b2 & SIGN)==0)?(byte)(b2>>4):(byte)((b2)>>4^0xf0);
          byte val3 = ((b3 & SIGN)==0)?(byte)(b3>>6):(byte)((b3)>>6^0xfc);

          encodedData[encodedIndex]   = lookUpBase64Alphabet[ val1 ];
          //log.debug( "val2 = " + val2 );
          //log.debug( "k4   = " + (k<<4) );
          //log.debug(  "vak  = " + (val2 | (k<<4)) );
          encodedData[encodedIndex+1] =
              lookUpBase64Alphabet[ val2 | ( k<<4 )];
          encodedData[encodedIndex+2] =
              lookUpBase64Alphabet[ (l <<2 ) | val3 ];
          encodedData[encodedIndex+3] = lookUpBase64Alphabet[ b3 & 0x3f ];
      }

      // form integral number of 6-bit groups
      dataIndex    = i*3;
      encodedIndex = i*4;
      if (fewerThan24bits == EIGHTBIT )
      {
          b1 = binaryData[dataIndex];
          k = (byte) ( b1 &0x03 );
          //log.debug("b1=" + b1);
          //log.debug("b1<<2 = " + (b1>>2) );
          byte val1 = ((b1 & SIGN)==0)?(byte)(b1>>2):(byte)((b1)>>2^0xc0);
          encodedData[encodedIndex]     = lookUpBase64Alphabet[ val1 ];
          encodedData[encodedIndex + 1] = lookUpBase64Alphabet[ k<<4 ];
          encodedData[encodedIndex + 2] = PAD;
          encodedData[encodedIndex + 3] = PAD;
      }
      else if (fewerThan24bits == SIXTEENBIT)
      {

          b1 = binaryData[dataIndex];
          b2 = binaryData[dataIndex +1 ];
          l = (byte) (b2 & 0x0f);
          k = (byte) (b1 & 0x03);

          byte val1 = ((b1 & SIGN) == 0)?(byte)(b1>>2):(byte)((b1)>>2^0xc0);
          byte val2 = ((b2 & SIGN) == 0)?(byte)(b2>>4):(byte)((b2)>>4^0xf0);

          encodedData[encodedIndex]     = lookUpBase64Alphabet[ val1 ];
          encodedData[encodedIndex + 1] =
              lookUpBase64Alphabet[ val2 | ( k<<4 )];
          encodedData[encodedIndex + 2] = lookUpBase64Alphabet[ l<<2 ];
          encodedData[encodedIndex + 3] = PAD;
      }

      return encodedData;
  }

  /**
   * 解码Base64数据
   *
   * @param binaryData 要解码的Base64数据
   * @return 解码后数据.
   */
  public static byte[] decode( byte[] base64Data )
  {
      // handle the edge case, so we don't have to worry about it later
      if(base64Data.length == 0) { return new byte[0]; }

      int      numberQuadruple    = base64Data.length/FOURBYTE;
      byte     decodedData[]      = null;
      byte     b1=0,b2=0,b3=0, b4=0, marker0=0, marker1=0;

      // Throw away anything not in base64Data

      int encodedIndex = 0;
      int dataIndex    = 0;
      {
          // this sizes the output array properly - rlw
          int lastData = base64Data.length;
          // ignore the '=' padding
          while (base64Data[lastData-1] == PAD)
          {
              if (--lastData == 0)
              {
                  return new byte[0];
              }
          }
          decodedData = new byte[ lastData - numberQuadruple ];
      }

      for (int i = 0; i < numberQuadruple; i++)
      {
          dataIndex = i * 4;
          marker0   = base64Data[dataIndex + 2];
          marker1   = base64Data[dataIndex + 3];

          b1 = base64Alphabet[base64Data[dataIndex]];
          b2 = base64Alphabet[base64Data[dataIndex +1]];

          if (marker0 != PAD && marker1 != PAD)
          {
              //No PAD e.g 3cQl
              b3 = base64Alphabet[ marker0 ];
              b4 = base64Alphabet[ marker1 ];

              decodedData[encodedIndex]   = (byte)(  b1 <<2 | b2>>4 ) ;
              decodedData[encodedIndex + 1] =
                  (byte)(((b2 & 0xf)<<4 ) |( (b3>>2) & 0xf) );
              decodedData[encodedIndex + 2] = (byte)( b3<<6 | b4 );
          }
          else if (marker0 == PAD)
          {
              //Two PAD e.g. 3c[Pad][Pad]
              decodedData[encodedIndex]   = (byte)(  b1 <<2 | b2>>4 ) ;
          }
          else if (marker1 == PAD)
          {
              //One PAD e.g. 3cQ[Pad]
              b3 = base64Alphabet[ marker0 ];

              decodedData[encodedIndex]   = (byte)(  b1 <<2 | b2>>4 );
              decodedData[encodedIndex + 1] =
                  (byte)(((b2 & 0xf)<<4 ) |( (b3>>2) & 0xf) );
          }
          encodedIndex += 3;
      }
      return decodedData;
  }
	public static final byte GET_BASE64_CHAR(int index)
	{
		if (index < 0)
			return lookUpBase64Alphabet[(0-index % 64)];
		else
			return lookUpBase64Alphabet[index % 64];
	}
}
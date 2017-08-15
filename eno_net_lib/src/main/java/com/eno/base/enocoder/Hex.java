package com.eno.base.enocoder;
/**
 * <p>Title: Hex 类</p>
 * <p>Description: 十六进制格式编码类,用于把byte数组编码成一个字串.</p>
 * <p>Copyright: Copyright ENO(c) 2004</p>
 * <p>Company: ENO</p>
 * @author cheng-dn
 * @version 1.1
 */

public class Hex 
{
  public Hex()
  {
  }

  public static void main(String[] args)
  {
    Hex hex = new Hex();
  }

  /**
   * 对byte数组进行编码.
   */
  public static  String encode(byte abyte0[])
  {
    StringBuffer stringbuffer = new StringBuffer();
    for(int i = 0; i < abyte0.length; i++)
    {
      String s = Integer.toHexString(abyte0[i] & 0xff);
      if(s.length() < 2)
        stringbuffer.append("0");
      stringbuffer.append(s);
    }

    return stringbuffer.toString();
  }

  private static byte hexTo(char ch)
  {
    byte chRtn = 0;
    if (ch >= '0' && ch <='9')
      chRtn = (byte)(ch - '0');
    else if (ch >='a' && ch <='f')
      chRtn = (byte)(ch -'a' + 10);
    else if (ch >='A' && ch <='F')
      chRtn = (byte)(ch -'A' + 10);
    return chRtn;
  }

  /**
   * 对字串buff进行解码,生成一个byte 数组.
   */
  public static byte[] decode(String buff)
  {
    int nSize = buff.length()/2;
    byte[] rtn = new byte[nSize];
    char chTemp;
    for(int i = 0; i < nSize; i++)
    {
      chTemp = buff.charAt(2*i);
      rtn[i] = (byte)(hexTo(chTemp) << 4);
      chTemp = buff.charAt(2*i+1);
      rtn[i] +=hexTo(chTemp);
    }
    return rtn;
  }
}
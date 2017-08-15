package com.eno.net.enotc;

/**
 * 加密压缩解析数据接口
 */
public abstract interface I_ENOBodyEncoder {
  // 这里定义一些加解密和解压缩的种类
  public static final byte ENCODE_NONE = (byte) 0x00;
  public static final byte ENCODE_COMPRESS = (byte) 0x11;
  public static final byte ENCODE_ENCRYPT = (byte) 0x21;
  public static final byte ENCODE_ENCODER = (byte) 0x22;

  /**
   * 压缩的实现接口
   * @param isEncode 是否进行编码
   * @param encodeType 编码解码的种类
   * @return 压缩或解压完成的数据内容
   */
  abstract byte[] compressData(byte[] source, byte encodeType, boolean isEncode);

  /**
   * 加密的实现接口
   * @param isEncode 是否进行编码
   * @param encodeType 编码解码的种类
   * @param encryptKey 密钥
   * @return 加密或解密完成的数据内容
   */
  abstract byte[] encryptData(byte[] source, byte encodeType, String encryptKey, boolean isEncode);
}
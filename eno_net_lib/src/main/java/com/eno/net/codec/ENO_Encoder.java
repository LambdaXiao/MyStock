package com.eno.net.codec;
import java.util.Map;


/**
 * 编码
 * @author 	xmf 
 *  @see 创建时间 2013-6-27 下午12:54:57 包名com.eno.net.twogo.push.messages 工程名称EnoNet_lxzq 
 *  @version 1.0
 */
public  interface  ENO_Encoder {
	
	
	  public  byte[] encode(Map<String, Object> map);

}
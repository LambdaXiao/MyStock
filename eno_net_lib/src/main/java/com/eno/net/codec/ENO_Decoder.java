package com.eno.net.codec;
import com.eno.net.channel.ENO_Channel;
import com.eno.net.worke.ENO_NetWorke;


/**
 * 解码
 * @author 	xmf 
 *  @see 创建时间 2013-6-27 下午12:54:57 包名com.eno.net.twogo.push.messages 工程名称EnoNet_lxzq 
 *  @version 1.0
 */
public  interface  ENO_Decoder {
	
   
	  public  byte[] decode(ENO_NetWorke worke, ENO_Channel channel)throws Exception;
		
}
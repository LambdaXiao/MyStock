package com.eno.net.messages;

import com.eno.net.codec.ENO_Decoder;


/**
 * 网络通讯接口
 * @author 	xmf 
 *  @see 创建时间 2013-6-27 上午11:28:22 包名com.eno.net.twogo.push.messages 工程名称EnoNet_lxzq 
 *  @version 1.0
 */
public  interface ENO_NetMessageSocketResponse extends ENO_NetMessage{
	

	public  void netParse(ENO_Decoder enoDecoder, byte[] data);


}
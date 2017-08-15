package com.eno.net.messages;
import java.util.Map;

import com.eno.net.codec.ENO_Decoder;
import com.eno.net.codec.ENO_Encoder;

/**
 * 
 * @author 	xmf 
 *  @see 创建时间 2013-6-27 下午12:54:57 包名com.eno.net.twogo.push.messages 工程名称EnoNet_lxzq 
 *  @version 1.0
 */
public  interface ENO_NetMessageHttp extends ENO_NetMessage {
	
    public  Map<String, Object>  netSend();
	public  void netParse(ENO_Encoder encoder, ENO_Decoder decoder, byte[] data);
    public void timeOutNotify();
 
}
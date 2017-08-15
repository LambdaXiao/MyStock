package com.eno.net.socket;

import java.io.IOException;

import com.eno.net.channel.ENO_Channel;

/**
 * 
 * @author 	xmf 
 *  @see 创建时间 2013-6-27 下午12:54:57 包名com.eno.net.twogo.push.messages 工程名称EnoNet_lxzq 
 *  @version 1.0
 */
public abstract  interface  ENO_SocketChannel extends  ENO_Channel{
	

	public void sendHeartbeat()throws IOException;
}
package com.eno.net.messages;


/**
 * 
 * @author 	xmf 
 *  @see 创建时间 2013-6-27 下午12:54:57 包名com.eno.net.twogo.push.messages 工程名称EnoNet_lxzq 
 *  @version 1.0
 */
public abstract  interface  ENO_NetMessage {
	   
	public abstract void netError(int errorIndex, String msg);
	public abstract void netNotifier();
	
}
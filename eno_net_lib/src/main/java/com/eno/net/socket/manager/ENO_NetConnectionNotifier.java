package com.eno.net.socket.manager;

import com.eno.net.channel.ENO_Channel;


/**
 * 长连接管理类回调接口
 * @author 	xmf 
 *  @see 创建时间 2013-2-18 下午5:36:36 包名com.eno.net.push.base 工程名称EnoNet_xyzq 
 *  @version 1.0
 */
public  interface ENO_NetConnectionNotifier
{
	
  abstract boolean onConnectSusseed(ENO_Channel con);
  
  abstract boolean onConnectError(int errorNumber, ENO_Channel con);
  
}
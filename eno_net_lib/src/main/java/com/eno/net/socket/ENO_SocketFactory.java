package com.eno.net.socket;

import com.eno.net.channel.ENO_Channel;
import com.eno.net.channel.ENO_ChannelFactory;
/**
 * socket连接工厂
 * @author 	xmf 
 *  @see 创建时间 2013-7-22 上午11:28:22 包名com.eno.net.socket 工程名称EnoNet 
 *  @version 1.0
 */
public  class ENO_SocketFactory extends ENO_ChannelFactory{

	@Override
	public ENO_Channel create(String addr,int port) {
		// TODO Auto-generated method stub
		return new ENO_SocketConn(addr,port);
	}
	
}

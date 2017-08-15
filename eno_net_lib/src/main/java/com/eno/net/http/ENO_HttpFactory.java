package com.eno.net.http;

import com.eno.net.channel.ENO_Channel;
import com.eno.net.channel.ENO_ChannelFactory;

public  class ENO_HttpFactory extends ENO_ChannelFactory{

	@Override
	public ENO_Channel create(String addr,int port) {
		// TODO Auto-generated method stub
		return new ENOHttpConn();
	}
	
}

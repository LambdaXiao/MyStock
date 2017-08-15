package com.eno.net.channel;

public  abstract class ENO_ChannelFactory {
	public abstract ENO_Channel create(String addr,int port);
}

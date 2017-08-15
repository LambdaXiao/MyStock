package com.eno.net.worke;

import com.eno.net.enotc.ENO_HttpRequestEncoder;
import com.eno.net.enotc.ENO_HttpResponseDecoder;

public  class ENO_WorkeHttpFactory extends ENO_WorkeFactory {

	@Override
	public ENO_NetWorkeHttp create() {
		// TODO Auto-generated method stub
		ENO_NetWorkeHttp netWorke=new ENO_NetWorkeHttp();
		netWorke.setDecoder(new ENO_HttpResponseDecoder());
		netWorke.setEncoder(new ENO_HttpRequestEncoder());	
		return netWorke;
	}

	
}

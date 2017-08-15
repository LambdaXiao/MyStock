package com.eno.net.worke;

import com.eno.net.enotc.ENO_HttpRequestEncoder;
import com.eno.net.enotc.ENO_HttpResponseDecoder;

public class ENO_WorkeLongTimeFactory extends ENO_WorkeFactory {
	@Override
	public ENO_NetWorkeLongTime create() {
		// TODO Auto-generated method stub
		ENO_NetWorkeLongTime netWorke=new ENO_NetWorkeLongTime();
		netWorke.setDecoder(new ENO_HttpResponseDecoder());
		netWorke.setEncoder(new ENO_HttpRequestEncoder());	
		return netWorke;
	}

}

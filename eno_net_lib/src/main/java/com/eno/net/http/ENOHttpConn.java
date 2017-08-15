package com.eno.net.http;
import java.io.IOException;

import com.eno.net.base.ENO_AbstractChannel;

/*
 * 用HTTP的方式进行通信 还未实现
 */
public class ENOHttpConn extends ENO_AbstractChannel implements ENO_HttpChannel{

	@Override
	public boolean sendRequest(byte[] data) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public byte[] getResponse() throws IOException  {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean connect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isConnect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	



}

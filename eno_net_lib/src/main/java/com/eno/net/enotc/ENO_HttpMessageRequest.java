package com.eno.net.enotc;

import com.eno.net.codec.ENO_Decoder;
import com.eno.net.codec.ENO_Encoder;
import com.eno.net.messages.ENO_NetMessageHttp;

import java.util.HashMap;
import java.util.Map;

/**
 * 伊诺网络messageRequest封装
 * 
 * @author xmf
 * @see  2013-7-17 下午2:08:53 包名com.eno.net.socket.face 工程名称EnoNet_lxzq
 * @version 1.0
 */
public abstract class ENO_HttpMessageRequest implements ENO_NetMessageHttp {

	private  Map<String, Object>  map;	
	public ENO_HttpMessageRequest(final String url) {
		map=new HashMap<String, Object>();
		map.put("url", url);	
	}

	public ENO_HttpMessageRequest(final int packageid, final String url) {
		
		map=new HashMap<String, Object>();
		map.put("url", url);
		map.put("packageid",packageid);
	}

	public ENO_HttpMessageRequest(final int packageid, final int compress,
			final int encrypt, final String url) {
		map=new HashMap<String, Object>();
		map.put("url", url);
		map.put("packageid",packageid);
		map.put("compress",compress);
		map.put("encrypt",encrypt);
		
	}

	@Override
	public abstract void netError(int errorIndex, String msg);
	
		// TODO Auto-generated method stub


	@Override
	public abstract void netNotifier();
		// TODO Auto-generated method stub
	
	@Override
	public  Map<String, Object> netSend() {
		// TODO Auto-generated method stub
		return map;
	}

	@Override
	public abstract void netParse(ENO_Encoder encoder, ENO_Decoder decoder, byte[] data);
		// TODO Auto-generated method stub
	

	@Override
	public abstract void timeOutNotify();
		// TODO Auto-generated method stub



}

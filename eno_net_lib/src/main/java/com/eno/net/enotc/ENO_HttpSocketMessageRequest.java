package com.eno.net.enotc;

import com.eno.net.messages.ENO_NetMessageSocketRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 伊诺网络messageRequest封装
 * 
 * @author xmf
 * @see  2013-7-17 下午2:08:53 包名com.eno.net.socket.face 工程名称EnoNet_lxzq
 * @version 1.0
 */
public abstract class ENO_HttpSocketMessageRequest implements ENO_NetMessageSocketRequest {

	private  Map<String, Object>  map;	
	public ENO_HttpSocketMessageRequest(final String url) {
		map=new HashMap<String, Object>();
		map.put("url", url);	
	}

	public ENO_HttpSocketMessageRequest(final int packageid, final String url) {
		
		map=new HashMap<String, Object>();
		map.put("url", url);
		map.put("packageid",packageid);
	}

	public ENO_HttpSocketMessageRequest(final int packageid, final int compress,
			final int encrypt, final String url) {
		map=new HashMap<String, Object>();
		map.put("url", url);
		map.put("packageid",packageid);
		map.put("compress",compress);
		map.put("encrypt",encrypt);
		
	}

	@Override
	public abstract void netError(int errorIndex, String msg);



	@Override
	public abstract void netNotifier();

	@Override
	public  Map<String, Object>  netSend() {
		// TODO Auto-generated method stub
			
		return map;
	}

	@Override
	public abstract void timeOutNotfy();


	
}

package com.eno.net.channel;

import java.io.IOException;

/**
 * 实现连接管道
 * @author 	xmf 
 *  @see 创建时间 2013-6-27 下午12:54:57 包名com.eno.net.twogo.push.messages 工程名称EnoNet_lxzq 
 *  @version 1.0
 */
public   interface  ENO_Channel {
	
	/**
	 * 发送请求
	 * 
	 * @param message
	 * @return
	 */
	public  boolean sendRequest(byte[] data) throws Exception;

	/**
	 * 获取返回数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public  Object getResponse() throws IOException;

	/**
	 * 建立连接
	 */
	public  boolean connect();

	/**
	 * 连接是否正常
	 * @return
	 */
	public  boolean isConnect();

	/**
	 * 关闭连接
	 */
	public  void close();

}
package com.eno.net.worke;

import com.eno.net.channel.ENO_Channel;
import com.eno.net.codec.ENO_Decoder;
import com.eno.net.codec.ENO_Encoder;
import com.eno.net.messages.ENO_NetMessage;


/**
 * 连接工作线程基类
 * @author 	xmf 
 *  @see  2013-7-17 上午11:47:13 包名com.eno.net.worke 工程名称EnoNet_lxzq
 *  @version 1.0
 */
public abstract class ENO_NetWorke   {
 
	public static final int NET_STATE0=0;//正常
	public static final int NET_STATE1=1;//网络故障。
	public static final int NET_STATE2=2;//数据非法，请稍后重试。
	public static final int NET_STATE3=4;//数据编码错误
	public static final int NET_STATE4=5;//session超时
	public int CurrentState=NET_STATE0;

	protected ENO_Channel enoChannel;//连接通道
	protected ENO_Encoder encoder;// 发送请求编码器
	protected ENO_Decoder decoder;// 接收数据解码器
	

	
	public abstract void sendRequest(ENO_NetMessage message,ENO_Channel enoChannel);
	
	
	public abstract void setParseResponse(ENO_NetMessage message);


	
	public ENO_Channel getEnoChannel() {
		return enoChannel;
	}
	public void setEnoChannel(ENO_Channel enoChannel) {
		this.enoChannel = enoChannel;
	}
	public ENO_Encoder getEncoder() {
		return encoder;
	}
	public void setEncoder(ENO_Encoder encoder) {
		this.encoder = encoder;
	}
	public ENO_Decoder getDecoder() {
		return decoder;
	}
	public void setDecoder(ENO_Decoder decoder) {
		this.decoder = decoder;
	}
	
	
	
	


}
package com.eno.net.worke;
import com.eno.net.channel.ENO_Channel;
import com.eno.net.messages.ENO_NetMessage;
import com.eno.net.messages.ENO_NetMessageSocketRequest;
import com.eno.net.messages.ENO_NetMessageSocketResponse;
import com.eno.net.socket.ENO_SocketConn;

import java.io.IOException;

/**
 * 工作线程类
 * 
 * @author xmf
 * @see  2013-7-16 下午7:07:02 包名com.eno.net.worke 工程名称EnoNet_lxzq
 * @version 1.0
 */
public class ENO_NetWorkeLongTime extends ENO_NetWorke {
	private Thread procThread = null;
	private ENO_NetMessageSocketResponse enoMessage;

	/**
	 * 开始监听收到的数据
	 */
	public void startDataListenting() {
	
		if(procThread==null || !procThread.isAlive())
		{
		Runnable task = new Runnable() {
       
			public void run()// TODO Auto-generated method stub
			{
				do {
					byte[] bytes = null;
					try {
						bytes = decoder.decode(ENO_NetWorkeLongTime.this,
								enoChannel);

					} catch (IOException e) {
						//e.printStackTrace();
						//Logger.v("监听关闭");
						enoChannel.close();
						((ENO_SocketConn) enoChannel).setLastActTime(0);
						break;
					} catch (Exception e) {
						enoMessage.netError(0, "网络错误！");
						e.printStackTrace();
						
						((ENO_SocketConn) enoChannel).setLastActTime(0);
						break;
					}

					//Logger.v("监听");
					if (!enoChannel.isConnect()) {
						// lastActTime=0;
						((ENO_SocketConn) enoChannel).setLastActTime(0);
						//Logger.v("监听关闭");
						break;
					}
					if (enoMessage != null) {
						enoMessage.netParse(decoder, bytes);
					}

				} while (true);

			}
		 
		};

		procThread = new Thread(task);
		procThread.setPriority(Thread.MAX_PRIORITY);
		procThread.start();
		}
	}

	

	
	class ENO_NetThreadRequest implements Runnable {

		private ENO_NetMessageSocketRequest request;
		private ENO_NetWorkeLongTime worke;

		public ENO_NetThreadRequest(ENO_NetWorkeLongTime worke,
				ENO_NetMessage message, ENO_Channel channel) {
			this.worke = worke;
			this.request = (ENO_NetMessageSocketRequest)message;

		}

		@Override
		public void run() {

			if (!enoChannel.isConnect()) {
				enoChannel.close();
			}
			try {
				//Logger.v("请求串：" + request.netSend());

				byte[] bytes = worke.getEncoder().encode(request.netSend());

				enoChannel.sendRequest(bytes);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				request.netError(0, "请求发送失败！");
			}

		}
	}
	
	
//	/**
//	 * 开始监听收到的数据
//	 */
//	public void sendRequest(int index,String url) {
//		Log.v("ENO", url);
//		ENO_HttpMessageRequest request = new ENO_HttpMessageRequest(index, 0, 0,
//				url);
//		ENO_NetThreadRequest requestThreadPool = new ENO_NetThreadRequest(this,
//				request, enoChannel);
//		// Thread t1 = new Thread(requestThreadPool);
//		// t1.start();
//
//		ENO_NetThreadPool.getInstance().execute(requestThreadPool);
//
//	}



	@Override
	public void setParseResponse(ENO_NetMessage message) {
		this.enoMessage = (ENO_NetMessageSocketResponse) message;
		
	}

	@Override
	public void sendRequest(ENO_NetMessage message, ENO_Channel channel) {
		// TODO Auto-generated method stub
		enoChannel=	channel;
		ENO_NetThreadRequest requestThreadPool = new ENO_NetThreadRequest(this,
				message, enoChannel);
		// Thread t1 = new Thread(requestThreadPool);
		// t1.start();

		ENO_NetThreadPool.getInstance().execute(requestThreadPool);

	}

}
package com.eno.net.socket;
import com.eno.net.base.ENO_AbstractChannel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * socketHTTP协议 
 * @author 	xmf 
 *  @see  2013-7-19 上午11:57:27 包名com.eno.net.socket 工程名称EnoNet_lxzq
 *  @version 1.0
 */
public class ENO_SocketConn extends ENO_AbstractChannel  implements ENO_SocketChannel  {

	private Socket socket = null;
	
	public ENO_SocketConn() {
	
		m_fKeepAlive = true;
	}
	
	
	
	
	public ENO_SocketConn(String addr, int port) {
		 m_addr=addr;//ip地址
		 m_port=port;//端口
		m_fKeepAlive = true;
	}


	public Socket getSocket() {
		return socket;
	}

	

	/**
	 * 连接到m_addr,m_port.
	 * 
	 * @return 成功与否标志?
	 */
	public synchronized boolean connect() {	
		isConnected = false;
		 close();//建立连接前先彻底关闭当前连接
			  String addr = m_addr;
		      int port = m_port;
			try {
				//Logger.v("网络连接类型 socket：" + addr + "   " + port);
							
				InetSocketAddress socketAddress = new InetSocketAddress(addr,
						port);
				
				
				socket= new Socket();
				socket.connect(socketAddress, netOutTime);
				socket.setKeepAlive(true);
				isConnected=true;

			} catch (UnknownHostException e1) {
				e1.printStackTrace();
				isConnected = false;
			} catch (IOException e1) {
				e1.printStackTrace();
				isConnected = false;
			}
				
		return isConnected;
	}


	/**
	 * 取服务端响应的数据.
	 * 
	 * @return
	 * @throws IOException
	 */
	public  DataInputStream getResponse()  throws IOException {
		lastActTime = System.currentTimeMillis();// 更新最后一次请求的时间
		
		if (m_is == null) {

				m_is = new DataInputStream(socket.getInputStream());

		}
		
	   return m_is;
	}
	private StringBuffer requestHead;
	@Override
	public  synchronized boolean  sendRequest(byte[] byData)throws Exception {
			
		if (m_os == null) {

			m_os = new DataOutputStream(socket.getOutputStream());

		}
		//socket.setTcpNoDelay(true);
		
			// 设置请求头
			 requestHead = new StringBuffer();
			if (byData == null)
				requestHead.append("GET /");
			else
				requestHead.append("POST /");
		
			requestHead.append(" HTTP/1.1\r\n");			
			
			// 加其它的请求头
			requestHead
					.append("Content-Type: application/x-www-form-urlencoded\r\n");
			requestHead.append("Accept: image/png, */*\r\n");
			requestHead.append("Connection: Keep-Alive\r\n");

			requestHead.append("User-Agent: ENO android Client\r\n");
			requestHead.append("Content-Language: "
					+ System.getProperty("microedition.locale") + "\r\n");

			if (byData != null) {
				requestHead.append("Content-Length:" + byData.length);
				requestHead.append("\r\n");
			}
			// 结束Http Header
			requestHead.append("\r\n");
		  
		
			m_os.write(requestHead.toString().getBytes());
		if (byData != null) {		
			m_os.write(byData);
			m_os.flush();
		}

	return true;

	}

	
	@Override
	public void sendHeartbeat() throws IOException
	{
		this.getSocket().sendUrgentData(0xFF);	
	}
	

	@Override
	public boolean isConnect() {
		if (socket!=null && !socket.isClosed() && socket.isConnected()) {
			return true;
		} else {

			return false;

		}
	}

	@Override
	public void close() {
		try {
			if (socket != null) {
				socket.close();
			}
			if (m_is != null) {
				m_is.close();
				m_is = null;
			}

			if (m_os != null) {
				m_os.close();
				m_os = null;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}

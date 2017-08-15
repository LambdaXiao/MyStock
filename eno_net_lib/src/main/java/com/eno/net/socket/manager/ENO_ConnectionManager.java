package com.eno.net.socket.manager;

import com.eno.net.base.ENO_AbstractChannel;
import com.eno.net.channel.ENO_Channel;
import com.eno.net.socket.ENO_SocketConn;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 长连接管理类
 * 
 * @author xmf
 * @see  2013-6-27 下午2:29:57 包名com.eno.net.socket.manager 工程名称EnoNet_lxzq
 * @version 1.0
 */
public class ENO_ConnectionManager {
	/**
	 * 心跳周期（单位：毫秒）
	 */
	private long activeCycle = 30000;// 心跳间隔

	private static final int SCAN_CYCLE = 3000;// 扫描间隔

	private  Timer timer;// 定时器
	private  TimerTask task;// 线程

	private boolean isNormal = true;

	private int errorNumber = 0;

	/**
	 * 存放产生的长连接
	 */
	private Set<ENO_AbstractChannel> pool = Collections
			.synchronizedSet(new HashSet<ENO_AbstractChannel>());

	private ENO_NetConnectionNotifier mNetNotifier;

	public ENO_NetConnectionNotifier getNetNotifier() {
		return mNetNotifier;
	}

	public void setNetNotifier(ENO_NetConnectionNotifier netNotifier) {
		mNetNotifier = netNotifier;

	}

	public ENO_ConnectionManager() {
		init();
	}

	/**
	 * 参数初始化
	 */
	public void init() {
		isNormal = true;
		errorNumber = 0;
		activeCycle = 20000;// 心跳间隔
	}

	private void beginRun() {
		//Logger.v("长连接开始监听———————————————————ENO_ConnectionManager—————————————————————————");

		if(task!=null)
		{
			task.cancel();	
		}
    	task = new TimerTask() {

				public void run() {
					
					 Thread current = Thread.currentThread();  
//				        System.out.println(current.getPriority());  
//				        System.out.println(current.getName());  
//				        System.out.println(current.activeCount());  
//				        System.out.println(current.getId());  
//				        System.out.println(current.getThreadGroup());  
//				        System.out.println(current.getStackTrace());  
//				        System.out.println(current.hashCode());  
//				        System.out.println(current.toString());  
					//Logger.v("_____ENO_ConnectionManager____"+current.getId()+"  "+current.getName());
					long time = System.currentTimeMillis();
					int i = 0;
					for (ENO_AbstractChannel con : pool) {
						i++;
						boolean isSuccess = con.isConnect();
						//Logger.v("isConnect------------------------------------------"+isSuccess);
						if (!isNormal) {
							return;
						}

						long sTime = con.getLastActTime();

						if ((sTime + activeCycle) < time) {
							//Logger.v("第" + i + "个连接的心跳=" + activeCycle + "   "
							//		+ time);
							try {
								con.sendHeartbeat();
								con.setLastActTime(time);
							} catch (Exception ex) {
								//Logger.v("心跳失败------------------------------------------");
								isSuccess = false;
								con.close();
							}

						}

						if (!isSuccess) {
							try {
								//Logger.v("正在连接.....");
								boolean isConnect = con.connect();

								if (isConnect) {
									//Logger.v("恭喜，连接成功！");
									errorNumber = 0;
									if (mNetNotifier != null)
										if (!mNetNotifier.onConnectSusseed(con))// 如果返回false
										{
											return;
										}

								} else {
									if (errorNumber < Integer.MAX_VALUE) {
										errorNumber++;
									}
									if (mNetNotifier != null) {
										if (!mNetNotifier.onConnectError(
												errorNumber, con))// 如果返回
										{
											isNormal = false;
										}
									}
								}

							} catch (Exception e) {
								// TODO: handle exception

								if (errorNumber < Integer.MAX_VALUE)
									errorNumber++;
								if (mNetNotifier != null) {
									if (!mNetNotifier.onConnectError(
											errorNumber, con))// 如果返回
									{
										isNormal = false;
									}
								}
								e.printStackTrace();

							}
						}

					}

				}

				// }
			};
		
	
			timer = new Timer();
			timer.schedule(task, 0, SCAN_CYCLE);	

	}

	/**
	 * 停止长连接维护
	 */
	public void stop() {
		//Logger.v("_______________________监听取消__________________________");
		try {
			if (timer != null)
				timer.cancel();
			timer = null;
			if (task != null) {
				task.cancel();
				task = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 开始维护长连接
	 */
	public void start() {
		if (timer != null)
			timer.cancel();
		beginRun();
	}

	/**
	 * 创建连接
	 * 
	 * @param host
	 * @param port
	 * @return
	 * @throws IOException
	 */
	public ENO_Channel createSocketConnection(String host, int port) {
		ENO_SocketConn conn = new ENO_SocketConn(host, port);
		isNormal = true;
		addConnection(conn);
		return conn;
	}

	/**
	 * 添加连接
	 * 
	 * @param conn
	 */
	public void addConnection(ENO_AbstractChannel conn) {
		isNormal = true;
		pool.add(conn);
	}

	/**
	 * 从管理器里删除一个连接
	 * 
	 * @param sconn
	 */
	public void removeConnection(ENO_AbstractChannel sconn) {
		pool.remove(sconn);
		sconn.close();// 关闭这个连接
		if (pool.size() < 1) {
			stop();
		}

	}

	/**
	 * 清空连接池
	 */
	public void removeAllConnection() {
		stop();
		pool.clear();

	}

	/**
	 * 获取连接池
	 */
	public Set<ENO_AbstractChannel> getConnectionPool() {
		return pool;
	}

	/**
	 * 添加连接池
	 * 
	 * @param pool
	 */
	public void setConnectionPool(Set<ENO_AbstractChannel> pool) {
		this.pool = pool;
	}

	/**
	 * 关闭所有连接然后心跳会尝试重连
	 */
	public void onRestart() {
		for (ENO_AbstractChannel con : pool) {
			con.close();
		}
	}

	public long getActiveCycle() {
		return activeCycle;
	}

	public void setActiveCycle(long activeCycle) {
		this.activeCycle = activeCycle;
	}

}
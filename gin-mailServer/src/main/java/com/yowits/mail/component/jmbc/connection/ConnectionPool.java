package com.yowits.mail.component.jmbc.connection;

import java.util.ArrayList;
import java.util.List;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class ConnectionPool implements JMBCPool {

	private JavaMailSender connection;
	// 连接池活动状态
	private Boolean isActive = false;
	// 记录创建的总的连接数
	//TODO:static?
	private static int contActive;

	// 空闲连接
	private static List<JavaMailSender> freeConnection = new ArrayList<>();
	// 活动连接
	private static List<JavaMailSender> activeConnection = new ArrayList<>();

	// 将线程和连接绑定，保证事务能统一执行
	private static ThreadLocal<JavaMailSender> threadLocal = new ThreadLocal<>();
	
	public ConnectionPool() {}

	public ConnectionPool(JavaMailSender connection) {
		this.connection = connection;
		init(this.connection);
		cheackPool();
	}

	private void init(JavaMailSender connection) {
		this.isActive = true;
		freeConnection.add(connection);
	}

	@Override
	public synchronized JavaMailSender getConnection() {
//		Queue<JavaMailSender> queue = new LinkedList<JavaMailSender>();
//		queue.poll();//取得数据并删除
//		queue.offer(null);//往队列添加数据q
		
		JavaMailSender conn = null;
		if(CollectionUtils.isEmpty(freeConnection)){
			freeConnection.addAll(activeConnection);
			activeConnection = new ArrayList<JavaMailSender>();
		}
		// 判断是否超过最大连接数限制
		if (freeConnection.size() > 0) {
			conn = freeConnection.get(0);
			if (conn != null) {
				threadLocal.set(conn);
			}
			freeConnection.remove(0);
		}

		if (isValid(conn)) {
			activeConnection.add(conn);    
			contActive++;
		}
		return conn;

	}

	/**
	 * 判断连接是否可用 jian.liu 下午11:30:18
	 * 
	 * @param conn
	 * @return
	 */
	private boolean isValid(JavaMailSender conn) {
		try {
			if (conn == null) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 获取当前连接
	 */
	@Override
	public JavaMailSender getCurrentConnecton() {
		// 默认线程里面取
		JavaMailSender conn = threadLocal.get();
		if (!isValid(conn)) {
			conn = getConnection();
		}
		return conn;
	}

	/**
	 * 释放连接
	 */
	@Override
	public synchronized void releaseConn(JavaMailSender conn) throws Exception {
		if (isValid(conn)) {
			freeConnection.add(conn);
			activeConnection.remove(conn);
			contActive--;
			threadLocal.remove();
			// 唤醒所有正待等待的线程，去抢连接
			notifyAll();
		}

	}

	@Override
	public void destroy() {
		freeConnection = null;
		activeConnection = null;
		isActive = false;
		contActive = 0;

	}

	@Override
	public boolean isActive() {
		return isActive;
	}

	@Override
	public void cheackPool() {

	}

}

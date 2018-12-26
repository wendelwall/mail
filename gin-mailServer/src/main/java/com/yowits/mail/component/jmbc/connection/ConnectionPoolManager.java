package com.yowits.mail.component.jmbc.connection;

import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.yowits.mail.component.mail.service.MailProperties;
import com.yowits.mail.constan.dto.Constans;

/**
 * 
 * @author jian.liu 下午2:21:20
 */
//@Component
@Slf4j
public class ConnectionPoolManager {

	private Map<String, JavaMailSender> pools = new Hashtable<>();
	
	private ConnectionPoolManager() {
//		init();
	}

	public static ConnectionPoolManager getInstance() {return Singtonle.instance;}

	private static class Singtonle {private static ConnectionPoolManager instance = new ConnectionPoolManager();}

	/**
	 * init jian.liu 下午6:39:27
	 */
	public void init(Map<String, String> slaves) {
		if(CollectionUtils.isEmpty(slaves)) return;
		slaves.forEach((key,value)-> {
					MailProperties properties = Constans.PROPERTIES_MAP
							.get(Constans.PROPERTIES_KEY);
					if (properties == null) return;
					JavaMailSenderImpl sender = new JavaMailSenderImpl();
					sender.setHost(properties.getHost());
					if (properties.getPort() != null) sender.setPort(properties.getPort());
					sender.setUsername(key);
					sender.setPassword(value);
					sender.setDefaultEncoding(properties.getDefaultEncoding());

					if (!properties.getProperties().isEmpty()) {
						Properties prop = new Properties();
						prop.putAll(properties.getProperties());
						sender.setJavaMailProperties(prop);
					}

					// pools.put(item, sender);
					ConnectionPool pool = new ConnectionPool(sender);
					log.info("Info:Init connection successed ->{}", pool);
				});

	}

	/**
	 * 获得连接,根据连接池名字 获得连接 jian.liu 下午6:39:09
	 * 
	 * @return
	 */
	public JavaMailSender getConnection() {
		JMBCPool pool = new ConnectionPool();
		JavaMailSender conn = pool.getConnection();
		// if(pools.size()>0 && pools.containsKey(poolName))
		// conn = getPool(poolName).getConnection();
		// else LOG.info("Error:Can't find this connecion pool ->{}",poolName);
		return conn;
	}

	/**
	 * 关闭，回收连接 jian.liu 下午6:39:16
	 * 
	 * @param conn
	 */
	public void close(JavaMailSender conn) {
		JMBCPool pool = new ConnectionPool();
		try {
			if (pool != null) {
				pool.releaseConn(conn);
			}
		} catch (Exception e) {
			log.info("jmbc poll aready destory...");
			e.printStackTrace();
		}
	}

	// 清空连接池
	public void destroy() {
		JMBCPool pool = new ConnectionPool();
		if (pool != null) {
			pool.destroy();
		}
	}

}

package com.yowits.mail.component.jmbc.connection;

import org.springframework.mail.javamail.JavaMailSender;

/**
 * JMBC连接池
 * @author jian.liu
 * 下午11:17:03
 */
public interface JMBCPool {

	/**
	 * 获得连接  
	 * jian.liu
	 * 下午11:17:27
	 * @return
	 */
    public abstract JavaMailSender getConnection();  

    /**
     * 获得当前连接  
     * jian.liu
     * 下午11:17:41
     * @return
     */
    public abstract JavaMailSender getCurrentConnecton();  

    /**
     * 回收连接  
     * jian.liu
     * 下午11:18:04
     * @param conn
     * @throws Exception
     */
    public abstract void releaseConn(JavaMailSender conn) throws Exception;  
    
    /**
     * 销毁
     * jian.liu
     * 下午11:18:16
     */
    public abstract void destroy();  
    

    /**
     * 连接池是活动状态
     * jian.liu
     * 下午11:18:26
     * @return
     */
    public abstract boolean isActive();  

    /**
     * 连接池是活动状态
     * jian.liu
     * 下午11:18:35
     */
    public abstract void cheackPool();  
	
	
}

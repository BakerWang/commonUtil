package com.cdeledu.application.log;

import com.cdeledu.application.log.dialect.ApacheCommonsLogFactory;
import com.cdeledu.application.log.dialect.JdkLogFactory;
import com.cdeledu.application.log.dialect.Log4jLogFactory;
import com.cdeledu.application.log.dialect.Slf4jLogFactory;

/**
 * @类描述: 日志工厂类
 * @创建者: 独泪了无痕
 * @创建日期: 2016年1月21日 下午7:40:11
 * @版本: V1.0
 * @since: JDK 1.7
 * @see <a href="">TODO(连接内容简介)</a>
 */
public abstract class LogFactory {
	private String logFramworkName;

	public LogFactory(String logFramworkName) {
		this.logFramworkName = logFramworkName;
	}

	/**
	 * 获得日志对象
	 * 
	 * @param name
	 *            日志对象名
	 * @return 日志对象
	 */
	public abstract Log getLog(String name);

	/**
	 * 获得日志对象
	 * 
	 * @param clazz
	 *            日志对应类
	 * @return 日志对象
	 */
	public abstract Log getLog(Class<?> clazz);

	private static volatile LogFactory currentLogFactory;
	private static final Object lock = new Object();

	/**
	 * @return 当前使用的日志工厂
	 */
	public static LogFactory getCurrentLogFactory() {
		if (null == currentLogFactory) {
			synchronized (lock) {
				if (null == currentLogFactory) {
					currentLogFactory = detectLogFactory();
				}
			}
		}
		return currentLogFactory;
	}

	/**
	 * 
	 * 自定义日志实现
	 * 
	 * @param logFactory
	 *            日志工厂类
	 * 
	 */
	public static void setCurrentLogFactory(LogFactory logFactory) {
		logFactory.getLog(LogFactory.class).debug("Custom Use %s Logger.", logFactory.logFramworkName);
		currentLogFactory = logFactory;
	}

	/**
	 * 获得日志对象
	 * 
	 * @param name
	 *            日志对象名
	 * @return 日志对象
	 */
	public static Log get(String name) {
		return getCurrentLogFactory().getLog(name);
	}

	/**
	 * 获得日志对象
	 * 
	 * @param clazz
	 *            日志对应类
	 * @return 日志对象
	 */
	public static Log get(Class<?> clazz) {
		return getCurrentLogFactory().getLog(clazz);
	}

	/**
	 * @return 获得调用者的日志
	 */
	public static Log get() {
		return get(new Exception().getStackTrace()[1].getClassName());
	}

	/**
	 * @return 获得调用者的调用者的日志（用于内部辗转调用）
	 */
	public static Log indirectGet() {
		return get(new Exception().getStackTrace()[2].getClassName());
	}

	/**
	 * 
	 * 决定日志实现
	 * 
	 * @return 日志实现类
	 * 
	 */
	private static LogFactory detectLogFactory() {
		LogFactory logFactory;
		try {
			logFactory = new Slf4jLogFactory(true);
			logFactory.getLog(LogFactory.class).debug("Use %s Logger As Default.", logFactory.logFramworkName);
		} catch (Throwable e) {
			try {
				logFactory = new Log4jLogFactory();
				logFactory.getLog(LogFactory.class).debug("Use %s Logger As Default.", logFactory.logFramworkName);
			} catch (Throwable e2) {
				try {
					logFactory = new ApacheCommonsLogFactory();
					logFactory.getLog(LogFactory.class).debug("Use %s Logger As Default.", logFactory.logFramworkName);
				} catch (Throwable e4) {
					logFactory = new JdkLogFactory();
					logFactory.getLog(LogFactory.class).debug("Use %s Logger As Default.", logFactory.logFramworkName);
				}
			}
		}

		return logFactory;
	}
}

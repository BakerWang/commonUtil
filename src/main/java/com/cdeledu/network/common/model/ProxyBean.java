package com.cdeledu.network.common.model;

/**
 * @类描述: 代理服务器 相关参数设置
 * @创建者: 独泪了无痕
 * @创建日期: 2015年9月15日 下午4:57:56
 * @版本: V1.0
 * @since: JDK 1.7
 */
public class ProxyBean {
	// 设置http访问要使用的代理服务器的地址
	private String host;
	// 设置http访问要使用的代理服务器的端口
	private int port = 80;
	// 设置http访问要使用的代理服务器的用户名
	private String userName;
	// 设置http访问要使用的代理服务器的密码
	private String password;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}

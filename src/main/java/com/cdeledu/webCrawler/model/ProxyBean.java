package com.cdeledu.webCrawler.model;

/**
 * @类描述: ip地址代理器
 * @创建者: 皇族灬战狼
 * @创建时间: 2016年8月17日 下午9:08:21
 * @版本: V1.0
 * @since: JDK 1.7
 */
public class ProxyBean {
	/** ----------------------------------------------------- Fields start */
	// 代理IP
	private String proxyIP;
	// 代理接口
	private int porxyPort;

	/** ----------------------------------------------------- Fields end */
	public ProxyBean() {
	}

	public ProxyBean(String proxyIP, int porxyPort) {
		this.proxyIP = proxyIP;
		this.porxyPort = porxyPort;
	}

	public String getProxyIP() {
		return proxyIP;
	}

	public void setProxyIP(String proxyIP) {
		this.proxyIP = proxyIP;
	}

	public int getPorxyPort() {
		return porxyPort;
	}

	public void setPorxyPort(int porxyPort) {
		this.porxyPort = porxyPort;
	}
}

package com.cdeledu.network.cookie;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

public class CookieUtilHelper {
	private static Map<String, String> cookies = new ConcurrentHashMap<String, String>();

	/**
	 * @方法:获得某个网站的Cookie信息
	 * @创建人:独泪了无痕
	 * @param host
	 *            网站Host
	 * @return Cookie字符串
	 */
	public static String get(String host) {
		return cookies.get(host);
	}

	/**
	 * @方法:将某个网站的Cookie放入Cookie池
	 * @创建人:独泪了无痕
	 * @param host
	 *            网站Host
	 * @param cookie
	 *            Cookie字符串
	 */
	public static void put(String host, String cookie) {
		cookies.put(host, cookie);
	}

	/**
	 * 
	 * @Title: isValidPort
	 * @Description: 是否为有效的端口
	 * @param port
	 *            端口号
	 * @return 是否有效
	 */
	public static boolean isValidPort(int port) {
		// 有效端口是0～65535
		return port >= 0 && port <= 0xFFFF;
	}

	/**
	 * 
	 * @Title：addCookie
	 * @Description：添加Cookie
	 * @param response
	 * @param key
	 * @param value
	 * @param domain
	 * @param path
	 * @param age
	 * @return：void 返回类型
	 */
	public static void addCookie(HttpServletResponse response, String key, String value, String domain, String path,
			int age) {
		Cookie cookie = new Cookie(key, value);
		cookie.setDomain(domain);
		cookie.setPath(path);
		cookie.setMaxAge(age);
		response.addCookie(cookie);
	}

	/**
	 * 
	 * @Title：getCookie
	 * @Description：从request中获取Cookie
	 * @param request
	 * @param key
	 * @return
	 * @return：Cookie 返回类型
	 */
	public static Cookie getCookie(HttpServletRequest request, String key) {
		Cookie[] cookies = request.getCookies();
		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].getName().equals(key)) {
				return cookies[i];
			}
		}
		return null;
	}

	/**
	 * 
	 * @Title：buildCurrentURL
	 * @Description：生成当前URL
	 * @param request
	 * @return
	 * @return：String 返回类型
	 */
	public static String buildCurrentURL(HttpServletRequest request) {

		StringBuffer url = new StringBuffer("");
		if (request.getScheme().startsWith("http"))
			url.append("http://");
		else
			url.append("https://");
		url.append(request.getHeader("host"));

		if (request.getServerPort() != 80)
			url.append(":" + request.getServerPort());

		url.append(request.getRequestURI());

		if (StringUtils.isNotEmpty(request.getQueryString())) {
			url.append("?");
			url.append(request.getQueryString());
		}
		return url.toString();
	}
}

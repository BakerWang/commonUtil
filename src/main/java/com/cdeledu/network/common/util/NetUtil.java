package com.cdeledu.network.common.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import com.cdeledu.exception.RuntimeExceptionHelper;

/**
 * @类描述: 网络相关工具
 * @创建者: 独泪了无痕
 * @创建日期: 2016年1月27日 下午11:03:23
 * @版本: V1.0
 * @since: JDK 1.7
 * @see <a href="">TODO(连接内容简介)</a>
 */
public class NetUtil {
	public final static String LOCAL_IP = "127.0.0.1";

	/**
	 * @方法:检测本地端口可用性
	 * @创建人:独泪了无痕
	 * @param port
	 * @return 是否可用
	 */
	public static boolean isUsableLocalPort(int port) {
		if (!isValidPort(port)) {
			// 给定的IP未在指定端口范围中
			return false;
		}
		try {
			new Socket(LOCAL_IP, port).close();
			// socket链接正常，说明这个端口正在使用
			return false;
		} catch (Exception e) {
			return true;
		}
	}

	/**
	 * @方法:是否为有效的端口
	 * @创建人:独泪了无痕
	 * @param port
	 * @return
	 */
	public static boolean isValidPort(int port) {
		// 有效端口是0～65535
		return port >= 0 && port <= 0xFFFF;
	}

	/**
	 * @方法:获得本机的IP地址列表
	 * @创建人:独泪了无痕
	 * @return
	 */
	public static Set<String> localIpv4s() {
		Enumeration<NetworkInterface> networkInterfaces = null;
		try {
			networkInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			throw new RuntimeExceptionHelper(e.getMessage(), e);
		}

		if (networkInterfaces == null) {
			throw new RuntimeExceptionHelper("Get network interface error!");
		}

		final HashSet<String> ipSet = new HashSet<String>();

		while (networkInterfaces.hasMoreElements()) {
			final NetworkInterface networkInterface = networkInterfaces.nextElement();
			final Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
			while (inetAddresses.hasMoreElements()) {
				final InetAddress inetAddress = inetAddresses.nextElement();
				if (inetAddress != null && inetAddress instanceof Inet4Address) {
					ipSet.add(inetAddress.getHostAddress());
				}
			}
		}

		return ipSet;
	}
}

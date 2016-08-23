package com.cdeledu.application.system;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;

import com.cdeledu.application.commons.model.MemInfo;

import sun.misc.BASE64Encoder;

/**
 * @类描述: 服务器工具，用来读取服务器相关信息
 * @创建者: 独泪了无痕
 * @创建日期: 2016年1月23日 上午12:43:52
 * @版本: V1.0
 * @since: JDK 1.7
 * @see <a href="">TODO(连接内容简介)</a>
 */
public class ServerUtils {
	// 获取内存信息的脚本
	public final static String MEM_SCRIPT = "free -m | grep Mem | awk '{print $2\"~\"$3\"~\"$4}'";
	// 获取CPU信息的脚本
	public final static String CPU_SCRIPT = "top -b -n 1 | grep Cpu | awk '{print $2}' | cut -f 1 -d \"u\"";

	/**
	 * @方法:获取服务器内网ip
	 * @创建人:独泪了无痕
	 * @return
	 */
	public static String getIp() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			return addr.getHostAddress();
		} catch (UnknownHostException e) {
			return "unknown";
		}
	}
	
	/**
	 * 运行shell
	 * @param script
	 * @return 执行结果
	 */
	private static String runShell(String script) {
		InputStream ins = null;
		try {
			String[] cmd = { "/bin/sh", "-c", script };

			// 执行liunx命令
			Process process = Runtime.getRuntime().exec(cmd);
			// 获取执行完后的结果
			ins = process.getInputStream();
			// 转为string类型分析执行结果
			return IOUtils.toString(ins).trim();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(ins);
		}
		return null;
	}

	/**
	 * @方法:获取服务器内存信息
	 * @创建人:独泪了无痕
	 * @return
	 */
	public static MemInfo getMemInfo() {
		String info = runShell(MEM_SCRIPT);
		if (StringUtils.isBlank(info)) {
			return null;
		}
		// 返回的信息以k最为单位
		String details[] = info.split("~");
		if (details.length < 3) {
			return null;
		}
		// 转换类型
		int total = Integer.parseInt(details[0]);
		int used = Integer.parseInt(details[1]);
		int free = Integer.parseInt(details[2]);

		return new MemInfo(total, used, free);
	}

	/**
	 * @方法描述: 读取Tomcat的运行状态
	 * @创建者: 皇族灬战狼
	 * @创建时间: 2016年4月29日 下午3:22:51
	 * @param serverHost
	 *            tomcat 地址
	 * @param serverPort
	 *            tomcat 端口
	 * @param userName
	 *            管理用户名
	 * @param passWord
	 *            管理用户密码
	 * @return
	 */
	public static String getTomcateRuntimeInfo(String serverHost, Integer serverPort, String userName, String passWord) {
		String result = "";
		URL url = null;
		BufferedReader breader = null;
		StringBuffer temp = new StringBuffer();
		Document document = null;
		
		if(StringUtils.isEmpty(serverHost)){
			return result;
		}
		if(null == serverPort || StringUtils.isEmpty(String.valueOf(serverPort))){
			serverPort=80;
		}
		try {
			String tomcatUrl = "http://%s:%s/manager/status?XML=true";
			url = new URL(String.format(tomcatUrl, serverHost, serverPort));
			String userPassword = userName + ":" + passWord;
			String encoding = new BASE64Encoder().encode(userPassword.getBytes());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Authorization", "Basic " + encoding);
			breader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = "";
			while ((line = breader.readLine()) != null) {
				temp.append(line);
			}
			// 将字符串转化为XML的Document
			document = DocumentHelper.parseText(temp.toString());
			result = document.asXML();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IOUtils.closeQuietly(breader);
		}
		return result;
	}
	public static void main(String[] args) {
		getTomcateRuntimeInfo("localhost", 80, "admin", "admin");
	}
}

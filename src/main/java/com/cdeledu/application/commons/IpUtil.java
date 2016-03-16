package com.cdeledu.application.commons;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cdeledu.application.regex.RegexUtil;
import com.cdeledu.network.common.UrlHelper;
import com.cdeledu.network.tcp.HttpURLConnHelper;

import net.sf.json.JSONObject;

/**
 * 
 * @title : IpUtil
 * 
 * @author : 独泪了无痕
 * 
 * @方法描述 :IP地址工具类
 * 
 */
public class IpUtil {
	/*--------------------------私有方法 start-------------------------------*/
	/** 新浪Ip查询接口 */
	private final static String SINA_URL = "http://int.dpool.sina.com.cn/iplookup/iplookup.php";
	/** 淘宝Ip查询接口 */
	private final static String TAO_BAO_URL = "http://ip.taobao.com/service/getIpInfo.php?ip=%S";
	/** API调试工具--接口地址 */
	private final static String IP_LOOKUP_SERVICE = "http://apis.baidu.com/apistore/iplookupservice/iplookup?ip=%s";
	private static Map<String, String> headerMap = new HashMap<String, String>();
	static {
		headerMap.put("apikey", ConfigUtil.getApiStoreAkValue());
	}

	/*--------------------------私有方法 end-------------------------------*/
	/*--------------------------公有方法 start-------------------------------*/
	/**
	 * 
	 * @Title：getIpAddr
	 * @Description：获取客户端的IP地址
	 * @param request
	 * @return
	 * @return：String 返回类型
	 */
	public static String getClientIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip.equals("0:0:0:0:0:0:0:1")) {
			ip = "127.0.0.1";
		}
		// 多级反向代理检测
		if (ip != null && ip.split(",").length > 1) {
			ip = ip.split(",")[0];
		}
		return ip;
	}

	/**
	 * 
	 * @Title：getRealIp
	 * @Description：本机IP
	 * @return
	 * @throws SocketException
	 * @return：String 返回类型
	 */
	public static String getRealIp() throws SocketException {
		String localip = null;// 本地IP，如果没有配置外网IP则返回它
		String netip = null;// 外网IP

		Enumeration<NetworkInterface> netInterfaces = NetworkInterface
				.getNetworkInterfaces();

		InetAddress ip = null;
		boolean finded = false;// 是否找到外网IP

		while (netInterfaces.hasMoreElements() && !finded) {
			NetworkInterface ni = netInterfaces.nextElement();
			Enumeration<InetAddress> address = ni.getInetAddresses();
			while (address.hasMoreElements()) {
				ip = address.nextElement();
				if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
						&& ip.getHostAddress().indexOf(":") == -1) {// 外网IP
					netip = ip.getHostAddress();
					finded = true;
					break;
				} else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
						&& ip.getHostAddress().indexOf(":") == -1) {// 内网IP
					localip = ip.getHostAddress();
				}
			}
		}

		if (netip != null && !"".equals(netip)) {
			return netip;
		} else {
			return localip;
		}
	}

	/**
	 * 
	 * @Title：getIpInfoBySinaUrl
	 * @Description：新浪接口(IP值为空时获取本地)
	 * @param ip
	 * @return <ul>
	 *         <li>ret : 有且仅当tet的值是1才有效</li>
	 *         <li>country: 国家</li>
	 *         <li>province: 省份</li>
	 *         <li>city: 城市</li>
	 *         <li>district: 区域</li>
	 *         <li>ISP: 运营商</li>
	 *         <li>type: 类型</li>
	 *         <li>desc : 其他</li>
	 *         </ul>
	 * @return：String 返回类型
	 */
	public static String getIpInfoBySinaUrl(String ip) {
		if (RegexUtil.isIp(ip)) {
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("format", "json");
			paramsMap.put("ip", ip);

			String url = UrlHelper.formatParameters(SINA_URL, paramsMap);

			return HttpURLConnHelper.sendGetRequest(url, null);
		}
		return "";
	}

	/**
	 * 
	 * @Title：getIpInfoByTaoBaoUrl
	 * @Description：淘宝ip查询接口
	 * @param ip
	 * @return <ul>
	 *         <li>code : 0：成功，1：失败</li>
	 *         <li>country: 国家</li>
	 *         <li>area: 区域</li>
	 *         <li>region: 省份</li>
	 *         <li>city: 城市</li>
	 *         <li>region: 县</li>
	 *         <li>ISP: 运营商</li>
	 *         <li>type: 类型</li>
	 *         </ul>
	 * @return：String 返回类型
	 */
	public static String getIpInfoByTaoBaoUrl(String ip) {
		String url = String.format(TAO_BAO_URL, ip);
		return HttpURLConnHelper.sendGetRequest(url, null);
	}

	/**
	 * 
	 * @Title：iplookup
	 * @Description：IP地址查询接口
	 * @param ip
	 * @return 根据提供ip地址就能得到IP相关的信息
	 * @return：JSONObject 返回类型
	 */
	public static JSONObject iplookup(String ip) {
		String url = String.format(IP_LOOKUP_SERVICE, ip);
		String result = HttpURLConnHelper.sendGetRequest(url, headerMap);
		// 转化为JSON类
		JSONObject json = JSONObject.fromObject(result);
		JSONObject _array = null;

		// 得到错误码并判断
		if (json.getInt("errNum") == 0) {
			// 根据需要取得数据
			_array = json.getJSONObject("retData");
		}
		return _array;
	}

	public static void main(String[] args) {
		String ip = "219.232.43.228";
		System.out.println(iplookup(ip));

	}
	/*--------------------------公有方法 end-------------------------------*/
}

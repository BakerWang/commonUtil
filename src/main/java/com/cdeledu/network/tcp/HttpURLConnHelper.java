package com.cdeledu.network.tcp;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;

import com.cdeledu.apache.collection.MapUtilHelper;
import com.cdeledu.exception.ExceptionHelper;
import com.cdeledu.exception.RuntimeExceptionHelper;
import com.cdeledu.network.common.UrlHelper;
import com.cdeledu.network.common.model.BasicAuthenticator;
import com.cdeledu.network.common.model.HttpMethod;
import com.cdeledu.network.common.model.ProxyBean;

/**
 * @描述:
 * 		<ul>
 *      <li>HttpURLConnection模拟HTTP请求网页内容</li>
 *      <li>Https协议工具类:封装了采用HttpURLConnection发送HTTP请求的GET\POST方法</li>
 *      <li>get请求可以获取静态页面，也可以把参数放在URL字串后面</li>
 *      <li>post的参数不是放在URL字串里面，而是放在http请求的正文内</li>
 *      </ul>
 * @author: 独泪了无痕
 * @date: 2015年6月14日 下午2:18:58
 * @version: V1.2
 */
public class HttpURLConnHelper {
	// protected  static Logger logger = Logger.getLogger(this.);
	 protected static Logger log=Logger.getLogger(HttpURLConnHelper.class.getName());  
	/** 异常原因 */
	private static String IO_EXCEPTION_MEG = "Url无法正常连接,请检查是否网络连接正常";
	// 定义数据分隔线
	private static final String BOUNDARY = "---------7d4a6d158c9";
	private static final String POST_HTTP = HttpMethod.POST.getValue();
	private static final String GET_HTTP = HttpMethod.GET.getValue();

	/*--------------------------私有方法 start-------------------------------*/
	/** 转码 */
	private static String UnicodeToString(String str) {
		Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			char ch = (char) Integer.parseInt(matcher.group(2), 16);
			str = str.replace(matcher.group(1), ch + "");
		}
		return str;
	}

	/** HttpURLConnection的Post、Get访问请求参数设置 */
	private static HttpURLConnection initConnection(String url, String RequestMethod, boolean isproxy,
			ProxyBean proxyBean) {
		URLConnection urlConn = null;
		HttpURLConnection httpUrlConnection = null;
		if(StringUtils.isBlank(url)) {
			throw new RuntimeExceptionHelper("请求的URL不能为空");
		}
		try {
			// 打开HttpURLConnection
			URL realUrl = new URL(url);
			if (isproxy) {
				Proxy proxy = new Proxy(Proxy.Type.HTTP,
						new InetSocketAddress(proxyBean.getHost(), proxyBean.getPort()));

				Authenticator.setDefault(new BasicAuthenticator(proxyBean.getUserName(), proxyBean.getPassword()));
				urlConn = realUrl.openConnection(proxy);
			} else {
				urlConn = realUrl.openConnection();
			}

			/** 设置http头通用的请求属性 */
			urlConn.setRequestProperty(HttpHeaders.ACCEPT, "*/*");
			urlConn.setRequestProperty(HttpHeaders.CONNECTION, "Keep-Alive");
			// 设置 HttpURLConnection的字符编码,从而避免出现乱码
			urlConn.setRequestProperty(HttpHeaders.ACCEPT_CHARSET, "utf-8");
			// 浏览页面的访问者在用什么操作系统(包括版本号)、浏览器(包括版本号)等
			urlConn.setRequestProperty(HttpHeaders.USER_AGENT,
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

			httpUrlConnection = (HttpURLConnection) urlConn;

			boolean caches = true;
			/** 发送POST请求必须设置如下所示 */
			if ("POST".equalsIgnoreCase(RequestMethod)) {
				/**
				 * 1.设置是否向httpUrlConnection输出,默认情况下是false<BR/>
				 * 2.是否打开输出流 true|false,表示向服务器写数据
				 */
				httpUrlConnection.setDoOutput(true);
				/**
				 * 1.设置是否从httpUrlConnection读入,默认情况下是true<br/>
				 * 2.是否打开输入流true|false,表示从服务器获取数据
				 */
				httpUrlConnection.setDoInput(true);
				/**
				 * 1.是否缓存true|false<BR/>
				 * 2.Post 请求不能使用缓存
				 */
				caches = false;
			}
			// 是否缓存true|false
			httpUrlConnection.setUseCaches(caches);
			// 设置连接超时时间，单位毫秒
			httpUrlConnection.setConnectTimeout(100000);
			// 设置读取数据超时时间，单位毫秒
			httpUrlConnection.setReadTimeout(100000);

			// 设置 HttpURLConnection的请求方式-->POST|GET,默认是GET
			if (POST_HTTP.equalsIgnoreCase(RequestMethod)) {
				httpUrlConnection.setRequestMethod(POST_HTTP);
			} else if (GET_HTTP.equalsIgnoreCase(RequestMethod)) {
				httpUrlConnection.setRequestMethod(GET_HTTP);
			}

		} catch (Exception e) {
			ExceptionHelper.catchHttpUtilException(e, null);
		}

		/** 参数配置必须要在connect之前完成 */
		return httpUrlConnection;
	}

	/** 利用 HttpURLConnection发送代理服务器的POST()方法的请求 */
	private static String sendProxyRequest(String url, String parameters, boolean isproxy, ProxyBean proxy) {
		String result = "";// 响应内容
		HttpURLConnection httpConn = null;
		OutputStream outStrm = null;
		InputStream is = null;

		if (StringUtils.isEmpty(url)) {
			ExceptionHelper.getExceptionHint("HttpURLConnHelper", "sendPostRequest", "targetUrl不能为空!");
		}

		try {
			httpConn = initConnection(url, POST_HTTP, isproxy, proxy);

			// 建立实际的连接
			httpConn.connect();

			if (StringUtils.isNoneBlank(parameters)) {
				// 现在通过输出流对象构建对象输出流对象，以实现输出可序列化的对象。
				outStrm = httpConn.getOutputStream();

				// 向对象输出流写出数据，这些数据将存到内存缓冲区中
				outStrm.write(parameters.getBytes("UTF-8"));
				// 刷新对象输出流，将任何字节都写入潜在的流中(此处为ObjectOutputStream)
				outStrm.flush();
				// 关闭流对象
				outStrm.close();
			}

			// HTTP 状态码(只有是200的时候才说明请求成功,其余皆失败)
			if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				// 将返回的输入流转换成字符串
				is = httpConn.getInputStream();
				int size = is.available();
				byte[] jsonBytes = new byte[size];
				is.read(jsonBytes);
				result = new String(jsonBytes, "UTF-8");
			} else {
				log.log(Level.ALL, IO_EXCEPTION_MEG);
				throw new RuntimeException(IO_EXCEPTION_MEG);
				
			}

			System.out.println("---> get to: " + url);
			System.out.println("---> data is: " + parameters);
			System.out.println("---> back data is: " + result);
			
		} catch (Exception e) {
			ExceptionHelper.catchHttpUtilException(e, url);
		} finally {
			// 释放资源
			IOUtils.closeQuietly(outStrm);
			IOUtils.closeQuietly(is);
			IOUtils.close(httpConn);
		}
		return result;
	}

	/*--------------------------私有方法 end-------------------------------*/
	/*--------------------------公有方法 start-------------------------------*/
	/**
	 * @Title: sendGetRequest
	 * @Description: 利用 HttpURLConnection 模拟发送http get方法的请求
	 * @param url
	 *            发送请求的URL(服务器地址)
	 * @return
	 */
	public static String sendGetRequest(String url) {
		return sendGetRequest(url, null);
	}

	/**
	 * @Title：sendGetRequest
	 * @Description：利用 HttpURLConnection 模拟发送http get方法的请求
	 * @param url
	 *            发送请求的URL(服务器地址)
	 * @param paramsMap
	 *            参数位置在 urlParam 类型 请求参数
	 * @param headerMap
	 *            参数位置在 header 类型 请求参数
	 * @return
	 * @return：String 返回类型
	 */
	public static String sendGetRequest(String url, Map<String, String> headerMap) {
		String result = "";
		HttpURLConnection httpConn = null;
		BufferedReader reader = null;

		if (StringUtils.isEmpty(url)) {
			ExceptionHelper.getExceptionHint("HttpURLConnHelper", "sendGetRequest", "targetUrl不能为空!");
		}

		try {

			httpConn = initConnection(url, HttpMethod.GET.getValue(), false, null);
			if (MapUtilHelper.isNotEmpty(headerMap)) {
				for (Map.Entry<String, String> entry : headerMap.entrySet()) {
					httpConn.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}

			// 打开连接端口,建立实际的连接
			httpConn.connect();

			// HTTP 状态码(只有是200的时候才说明请求成功,其余皆失败)
			if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				// 定义 BufferedReader输入流来读取URL的响应
				reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "utf-8"));
				String line;
				StringBuffer sb = new StringBuffer();

				while ((line = reader.readLine()) != null) {
					String str = UnicodeToString(line);
					sb.append(str);
				}

				result = sb.toString();

			} else {
				throw new RuntimeException(IO_EXCEPTION_MEG);
			}
		} catch (Exception e) {
			ExceptionHelper.catchHttpUtilException(e, url);
		} finally {
			// 释放资源
			IOUtils.closeQuietly(reader);
			IOUtils.close(httpConn);
		}

		return result;
	}

	/**
	 * @方法名称: sendPostRequest
	 * @方法描述:利用 HttpURLConnection 模拟发送http POST方法的请求
	 * @param url
	 *            发送请求的 URL(服务器地址)
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPostRequest(String url) {
		return sendProxyRequest(url, null, false, null);
	}

	/**
	 * @方法名称: sendPostRequest
	 * @方法描述:利用 HttpURLConnection 模拟发送http POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL(服务器地址)
	 * @param isproxy
	 *            是否使用代理模式
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPostRequest(String url, String parameters) {
		return sendProxyRequest(url, parameters, false, null);
	}

	/**
	 * @Title: sendPostRequest
	 * @Description: 利用 HttpURLConnection 模拟发送http POST方法的请求
	 * @param url
	 *            发送请求的 URL(服务器地址)
	 * @param paramsMap
	 *            请求参数
	 * @return
	 */
	public static String sendPostRequest(String url, Map<String, String> paramsMap) {
		String parameters = UrlHelper.formatParameters(paramsMap);
		return sendPostRequest(url, parameters);
	}

	/**
	 * @Title: sendPostRequest
	 * @Description: 利用 HttpURLConnection 模拟发送http POST方法的请求
	 * @param url
	 *            发送请求的 URL
	 * @param parameters
	 *            请求参数
	 * @param host
	 *            http访问要使用的代理服务器的地址
	 * @param port
	 *            http访问要使用的代理服务器的端口
	 * @param userName
	 *            http访问要使用的代理服务器的用户名
	 * @param password
	 *            http访问要使用的代理服务器的密码
	 * @return
	 */
	public static String sendPostRequest(String url, String parameters, String host, int port, String userName,
			String password) {
		ProxyBean proxy = new ProxyBean();
		proxy.setHost(host);
		proxy.setPort(port);
		proxy.setUserName(userName);
		proxy.setPassword(password);
		return sendProxyRequest(url, parameters, true, proxy);
	}

	/**
	 * 
	 * @Title: FileUpload
	 * @Description: 通过HTTP协议向指定的网络地址发送文件
	 * @author: 独泪了无痕
	 * @param url
	 *            设置要请求的服务器地址，即上传文件的地址
	 * @param file
	 * @return
	 */
	public static String FileUpload(String url, File file) {
		if (!file.exists())
			return null;
		HttpURLConnection conn = null; // URL连结对象。
		BufferedReader br = null; // 请求后的返回信息的读取对象。
		OutputStream out = null;
		DataInputStream datain = null;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setUseCaches(false);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod(POST_HTTP);
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);

			out = new DataOutputStream(conn.getOutputStream());
			// 定义最后数据分隔线
			byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			/**
			 * 设置文件数据
			 */
			StringBuilder sb = new StringBuilder();
			sb.append("--");
			sb.append(BOUNDARY);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
			/**
			 * /获取文件的上传类型
			 */
			sb.append("Content-Type:application/octet-stream\r\n\r\n");

			byte[] data = sb.toString().getBytes();
			out.write(data);
			datain = new DataInputStream(new FileInputStream(file));
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = datain.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			out.write(end_data);
			datain.close();
			out.flush();
			out.close();

			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

		} catch (FileNotFoundException fe) {
			InputStream err = ((HttpURLConnection) conn).getErrorStream();
			if (err == null)
				br = new BufferedReader(new InputStreamReader(err));
		} catch (IOException ie) {
			ie.printStackTrace();
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(datain);
		}

		/**
		 * 返回提示信息
		 */
		StringBuffer response = new StringBuffer();
		String line;
		try {
			while ((line = br.readLine()) != null)
				response.append(line + "\n");
			br.close();
		} catch (IOException ioe) {
			ioe.getStackTrace();
		} finally {
			IOUtils.closeQuietly(br);
			IOUtils.close(conn);
		}
		return response.toString();
	}

	public static void main(String[] args) {
		String url = "http://www.capec.org.cn/cdel/CAPEC-RM0400025.xml";
		System.out.println(sendGetRequest(url, null));
	}
	/*--------------------------公有方法 end-------------------------------*/
}

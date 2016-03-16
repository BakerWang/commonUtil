package com.cdeledu.network.tcp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.cdeledu.exception.ExceptionHelper;
import com.cdeledu.network.common.model.HttpHeaders;

/**
 * 
 * @ClassName: HttpClientHelper
 * @Description: <ul>
 *               <li>封装了一些采用HttpClient发送HTTP请求的方法</li>
 *               <li>实现与 org.apache.http</li>
 *               <li>使用HttpClient步骤如下</li>
 *               <li>1. 创建HttpClient对象</li>
 *               <li>2. 创建请求方法的实例,并指定请求URL <br/>
 *               如果需要发送GET请求,创建HttpGet对象 <br/>
 *               如果需要发送POST请求,创建HttpPost对象</li>
 *               <li>3. 如果需要发送请求参数<br/>
 *               可调用HttpGet、HttpPost共同的setParams(HetpParams params)方法来添加请求参数 <br/>
 *               对于HttpPost对象而言,也可调用setEntity(HttpEntity entity)方法来设置请求参数</li>
 *               <li>4. 调用HttpClient对象的execute(HttpUriRequest
 *               request)发送请求,该方法返回一个HttpResponse</li>
 *               <li>5. 调用HttpResponse的getAllHeaders()、getHeaders(String
 *               name)等方法可获取服务器的响应头<br/>
 *               调用HttpResponse的getEntity()方法可获取HttpEntity对象,该对象包装了服务器的响应内容</li>
 *               <li>6. 释放连接:无论执行方法是否成功,都必须释放连接</li>
 *               </ul>
 * @author: 独泪了无痕
 * @date: 2015年7月28日 上午9:23:23
 * @version: V1.0
 * @history:
 */
@SuppressWarnings("deprecation")
public class HttpClientHelper {
	/*-------------------------- 私有属性 begin -------------------------------*/
	private HttpClient httpClient = null;
	private static HttpClientHelper instance;
	/** 请求编码，默认使用utf-8 */
	private static String URLCHARSET = "utf-8";

	/*-------------------------- 私有属性 end   -------------------------------*/
	/*-------------------------- 私有方法 begin -------------------------------*/

	private HttpClientHelper() {
		httpClient = new DefaultHttpClient();
	}

	private void setUrlCharset(String urlCharset) {
		URLCHARSET = urlCharset;
	}

	/** 获取调用此方法的所在的类的名称 */
	private static String getClassName() {
		String clazz = Thread.currentThread().getStackTrace()[2].getClassName();
		return clazz.substring(clazz.lastIndexOf(".") + 1);
	}

	/** 获取调用此方法的所在的方法的名称 */
	private static String getMethodName() {
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}

	/** 当HttpClient实例不再需要是，确保关闭connection manager，以释放其系统资源 */
	private void destroyClient() {
		httpClient.getConnectionManager().shutdown();
	}

	/**
	 * 
	 * @Title: sendPostRequestData
	 * @Description: 通过post提交方式获取url指定的资源和数据
	 * @param targetUrl
	 *            服务器地址
	 * @param headersMap
	 *            请求header参数
	 * @param nameValuePairs
	 *            请求参数
	 * @return
	 */
	private String sendPostRequestData(String targetUrl,
			Map<String, String> headersMap, List<NameValuePair> nameValuePairs) {
		String result = null;

		HttpPost httpPost = new HttpPost(targetUrl);

		try {
			if (MapUtils.isNotEmpty(headersMap)) {
				for (String key : headersMap.keySet()) {
					httpPost.setHeader(key, headersMap.get(key));
				}
			}
			if (CollectionUtils.isNotEmpty(nameValuePairs)) {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
						"UTF-8"));
			}

			HttpResponse response = httpClient.execute(httpPost);

			int statusCode = response.getStatusLine().getStatusCode();
			// 请求和响应都成功了
			if (statusCode == 200) {
				// 获取到一个HttpEntity实例
				HttpEntity entity = response.getEntity();
				if (entity == null) {
					return null;
				}
				// 用EntityUtils.toString()这个方法将HttpEntity转换成字符串
				result = EntityUtils.toString(entity, "utf-8");
			} else {
				httpPost.abort();
				throw new RuntimeException("HttpClient,error status code :"
						+ statusCode);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (httpPost != null) {
				httpPost.releaseConnection();
			}
		}
		return result;
	}

	/*-------------------------- 私有方法 end   -------------------------------*/
	/*-------------------------- 公有方法 begin -------------------------------*/
	public HttpClientHelper getInstance() {
		return getInstance(URLCHARSET);
	}

	public HttpClientHelper getInstance(String urlCharset) {
		if (instance == null) {
			instance = new HttpClientHelper();
		}
		if (StringUtils.isBlank(urlCharset)) {
			urlCharset = URLCHARSET;
		}
		// 设置默认的url编码
		instance.setUrlCharset(urlCharset);
		return instance;
	}

	/**
	 * 
	 * @Title: sendGetRequest
	 * @Description: 发送HTTP_GET请求获取url指定的资源和数据
	 * @param targetUrl
	 *            服务器地址
	 * @return 返回响应的文本
	 */
	public String sendGetRequest(String targetUrl) {
		return sendGetRequest(targetUrl, null);
	}

	/**
	 * 
	 * @Title: sendGetRequest
	 * @Description: 带header的get请求
	 * @param targetUrl
	 *            服务器地址
	 * @param headersMap
	 *            添加的请求header信息
	 * @return
	 */
	public String sendGetRequest(String targetUrl,
			Map<String, String> headersMap) {
		String result = null;
		HttpGet httpGet = new HttpGet(targetUrl);
		// 设置响应头信息
		httpGet.addHeader(HttpHeaders.ACCEPT,
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpGet.addHeader(HttpHeaders.CONNECTION, "keep-alive");
		httpGet.addHeader(HttpHeaders.CACHE_CONTROL, "max-age=0");
		httpGet.addHeader(HttpHeaders.USER_AGENT,
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

		try {
			if (MapUtils.isNotEmpty(headersMap)) {
				for (String key : headersMap.keySet()) {
					httpGet.setHeader(key, headersMap.get(key));
				}
			}

			HttpResponse response = httpClient.execute(httpGet);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				if (entity == null) {
					return null;
				}
				result = EntityUtils.toString(entity, "utf-8");
			} else {
				httpGet.abort();
				throw new RuntimeException("HttpClient,error status code :"
						+ statusCode);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// 释放资源
			if (httpGet != null) {
				httpGet.releaseConnection();
			}
			destroyClient();
		}

		return result;
	}

	/**
	 * 
	 * @Title: sendPostRequest
	 * @Description: 发送HTTP_POST请求获取url指定的资源和数据
	 * @param targetUrl
	 *            服务器地址
	 * @return
	 */
	public String sendPostRequest(String targetUrl) {
		return sendPostRequest(targetUrl, null, null);
	}

	/**
	 * 
	 * @Title: sendPostRequest
	 * @Description: 通过post提交方式获取url指定的资源和数据
	 * @param targetUrl
	 *            服务器地址
	 * @param headerMap
	 *            请求header参数
	 * @return
	 */
	public String sendPostRequestHeader(String targetUrl,
			Map<String, String> headerMap) {
		return sendPostRequest(targetUrl, headerMap, null);
	}

	/**
	 * 
	 * @Title: sendPostRequestParam
	 * @Description: 通过post提交方式获取url指定的资源和数据
	 * @param targetUrl
	 *            服务器地址
	 * @param parameterMap
	 *            请求参数
	 * @return
	 */
	public String sendPostRequestParam(String targetUrl,
			Map<String, String> parameterMap) {
		return sendPostRequest(targetUrl, null, parameterMap);
	}

	/**
	 * 
	 * @Title: sendPostRequest
	 * @Description: 通过post提交方式获取url指定的资源和数据
	 * @param targetUrl
	 *            服务器地址
	 * @param headersMap
	 *            请求header参数
	 * @param paramsMap
	 *            请求参数
	 * @return
	 */
	public String sendPostRequest(String targetUrl,
			Map<String, String> headersMap, Map<String, String> paramsMap) {
		// 创建参数队列
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		BasicNameValuePair bnp = null;

		if (MapUtils.isNotEmpty(paramsMap)) {
			for (String key : paramsMap.keySet()) {
				bnp = new BasicNameValuePair(key, paramsMap.get(key));
				formParams.add(bnp);
			}
		}

		return sendPostRequestData(targetUrl, headersMap, formParams);
	}

	/**
	 * 
	 * @Title: sendPostRequestByJsonData
	 * @Description: 通过ContentType 为json的格式进行http传输
	 * @param targetUrl
	 *            远程url
	 * @param content
	 *            传输内容
	 * @return
	 */
	public String sendPostRequestByJsonData(String targetUrl, String content) {
		String result = "";
		HttpPost httpPost = null;

		if (StringUtils.isEmpty(targetUrl)) {
			ExceptionHelper.getExceptionHint(getClassName(), getMethodName(),
					"targetUrl不能为空!");
		}
		try {
			// 发送POST请求:创建一个HttpPost对象,传入目标的网络地址
			httpPost = new HttpPost(targetUrl);
			// 设置响应头信息

			httpPost.addHeader(HttpHeaders.CONNECTION, "keep-alive");
			httpPost.addHeader(HttpHeaders.ACCEPT, "*/*");
			httpPost.addHeader(HttpHeaders.CONTENT_TYPE,
					"application/x-www-form-urlencoded; charset=UTF-8");
			httpPost.addHeader("X-Requested-With", "XMLHttpRequest");
			httpPost.addHeader(HttpHeaders.CACHE_CONTROL, "max-age=0");
			httpPost.addHeader(HttpHeaders.USER_AGENT,
					"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");

			if (StringUtils.isNoneBlank(content)) {
				httpPost.setEntity(new StringEntity(content,
						ContentType.APPLICATION_JSON));
			}

			// 调用HttpClient的execute()方法,并将HttpPost对象传入即可:
			// 服务器所返回的所有信息存储在HttpResponse中
			HttpResponse response = httpClient.execute(httpPost);

			int statusCode = response.getStatusLine().getStatusCode();
			// 请求和响应都成功了
			if (statusCode == 200) {
				// 获取到一个HttpEntity实例
				HttpEntity entity = response.getEntity();
				if (entity == null) {
					return null;
				}
				// 用EntityUtils.toString()这个方法将HttpEntity转换成字符串
				result = EntityUtils.toString(entity, "utf-8");
			} else {
				httpPost.abort();
				throw new RuntimeException("HttpClient,error status code :"
						+ statusCode);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// 释放资源
			if (httpPost != null) {
				httpPost.releaseConnection();
			}
			destroyClient();
		}
		return result;
	}

	/*-------------------------- 公有方法 end   -------------------------------*/
	public static void main(String[] args) {
		String access_token = "ZOSfAFlk32khx-_Yumk7W23QVCwYmWWX_PXKVyRzAqefq4Cmc4hv4lQlY-EAH6QDBdH-2bLRSXeolIYgxYPmbZXYLP3SAighugLfwhEjxAc";
		String GROUP_CREATE_URI = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token="
				+ access_token;
		System.out.println(new HttpClientHelper().sendPostRequestByJsonData(
				GROUP_CREATE_URI, "{\"group\":{\"name\":\"test\"}}"));
	}
}
package com.cdeledu.exception;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

public class ExceptionHelper extends ExceptionUtils {
	/*-------------------------- 私有属性 start -------------------------------*/
	private static String MARKED_WORDS_ONE = "调用%s.%s()方法出现异常,原因是:%s";
	private static String MARKED_WORDS_TWO = "调用%s方法出现异常,原因是:%s";

	/*-------------------------- 私有属性 end   -------------------------------*/
	/*-------------------------- 私有方法 start -------------------------------*/
	/*-------------------------- 私有方法 end   -------------------------------*/
	/*-------------------------- 公有方法 start -------------------------------*/
	/*-------------------------- 公有方法 end   -------------------------------*/

	/**
	 * 
	 * @Title: getExceptionHint
	 * @Description: 异常提示语
	 * @param className
	 *            出现异常所在类
	 * @param methodName
	 *            出现异常所在方法
	 * @param tips
	 *            出现异常的提示
	 */
	public static void getExceptionHint(String className, String methodName,String tips) {
		throw new IllegalArgumentException(String.format(MARKED_WORDS_ONE,className, methodName, tips));
	}

	/**
	 * 
	 * @Title: getExceptionHint
	 * @Description: 异常提示语
	 * @param className
	 *            出现异常所在类
	 * @param tips
	 *            出现异常的提示
	 */
	public static void getExceptionHint(String className, String tips) {
		throw new IllegalArgumentException(String.format(MARKED_WORDS_TWO,
				className, tips));
	}
	
	/**
	 * @方法描述: 抓取异常
	 *        <ul>
	 *        <li>instanceof 是一个简单的二元操作符,是用来判断一个对象是否是一个类的实例,其操作类似于>=、==</li>
	 *        </ul>
	 * @创建者: 独泪了无痕
	 * @修改时间: 2016年2月25日 上午10:41:02
	 * @param e
	 * @param url
	 */
	public static void catchHttpUtilException(Exception e, String url) {

		if (e instanceof NullPointerException) {
			throw new IllegalArgumentException( "请求通信[" + url + "]时空指针异常,堆栈轨迹如下", e);
		}

		/**
		 * 解析异常 <br/>该异常是否是 ParseException 的实例
		 */
		if (e instanceof ParseException) {
			throw new RuntimeException("请求通信[" + url + "]时解析异常,堆栈轨迹如下", e);
		}
		/** 读取超时 */
		if (e instanceof SocketTimeoutException) {
			throw new RuntimeException("请求通信[" + url + "]时读取超时,堆栈轨迹如下", e);
		}
		/** 连接超时 */
		if (e instanceof ConnectTimeoutException) {
			throw new RuntimeException("请求通信[" + url + "]时连接超时,堆栈轨迹如下", e);
		}
		/** 网络异常 */
		if (e instanceof IOException) {
			// 该异常通常是网络原因引起的,如HTTP服务器未启动等
			throw new RuntimeException("请求通信[" + url + "]时网络异常,堆栈轨迹如下", e);
		}

		/** 网络协议 */
		if (e instanceof ClientProtocolException) {
			throw new RuntimeException("请求通信[" + url + "]时网络协议异常,堆栈轨迹如下", e);
		}
		if (e instanceof Exception) {
			throw new RuntimeException("请求通信[" + url + "]时偶遇异常,堆栈轨迹如下", e);
		}
	}
}

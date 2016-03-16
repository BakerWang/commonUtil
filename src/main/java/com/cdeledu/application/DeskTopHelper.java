package com.cdeledu.application;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;

import com.cdeledu.exception.ExceptionHelper;

/**
 * 
 * @ClassName: DeskTopHelper
 * @Description: <ul>
 *               <li>DeskTop类允许一个Java应用程序启动本地的另一个应用程序去处理URI或文件请求</li>
 *               <li>1.启动用户默认的浏览器显示指定的URI链接</li>
 *               <li>2.启动用户默认的邮件客户端发送URI指定的邮件</li>
 *               <li>3.启动一个注册应用程序(本地安装了的应用程序)去打开、编辑或打印一个指定的文件</li>
 *               </ul>
 * @author: 独泪了无痕
 * @date: 2015年8月27日 下午2:12:41
 * @version: V1.0
 * @history:
 */
public class DeskTopHelper {
	/*-------------------------- 私有属性 begin -------------------------------*/
	private static Desktop deskTop = null;;

	/*-------------------------- 私有属性 end   -------------------------------*/
	/*-------------------------- 私有方法 begin -------------------------------*/
	/**
	 * 
	 * @Title：isExists
	 * @Description： <ul>
	 *               <li>1.检查文件是否存在</li>
	 *               <li>2.检查文件路径所指向的是否是文件而不是文件夹</li>
	 *               </ul>
	 * @param file
	 * @param methodName
	 * @param isDirectory
	 * @return：void 返回类型
	 */
	private static void isExists(File file, String methodName,
			boolean isDirectory) {

		if (!file.exists()) {
			ExceptionHelper.getExceptionHint("DeskTopHelper", methodName,
					"没有找到指定的文件，请检查文件路径");
		}

		if (isDirectory && file.isDirectory()) {
			ExceptionHelper.getExceptionHint("DeskTopHelper", methodName,
					"文件格式不正确，请输入正确的文件路径");
		}
	}

	/*-------------------------- 私有方法 end   -------------------------------*/
	/*-------------------------- 公有方法 begin -------------------------------*/
	/**
	 * 
	 * @Title：browse
	 * @Description：使用默认的浏览器打开指定网址网页
	 * @param targetUrl
	 *            指定要打开的网址
	 * @return：void 返回类型
	 */
	public static void browse(String targetUrl) {
		if (StringUtils.isBlank(targetUrl)) {
			ExceptionHelper.getExceptionHint("DeskTopHelper", "browse",
					"targetUrl不能为空!");
		}
		try {
			if (Desktop.isDesktopSupported()) {
				deskTop = Desktop.getDesktop();
			}
			if (deskTop != null)
				deskTop.browse(new URI(targetUrl));
		} catch (IOException ie) {
			ie.printStackTrace();
		} catch (URISyntaxException use) {
			use.printStackTrace();
		}
	}

	/**
	 * 
	 * @Title：open
	 * @Description： <ul>
	 *               <li>打开指定的文件</li>
	 *               <li>与edid()相似,但不同的是open()可以操作文件夹</li>
	 *               </ul>
	 * @param filePath
	 * @return：void 返回类型
	 */
	public static void open(File file) {
		// 当前方法的名称
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		isExists(file, methodName, false);

		try {
			if (Desktop.isDesktopSupported()) {
				deskTop = Desktop.getDesktop();
			}
			if (deskTop != null)
				deskTop.open(file);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * 
	 * @Title：edit
	 * @Description：编辑文件
	 * @param filePath
	 *            所要编辑的文件路径,不能是文件夹的路径
	 * @return：void 返回类型
	 */
	public static void edit(File file) {
		// 当前方法的名称
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		isExists(file, methodName, true);

		try {
			if (Desktop.isDesktopSupported()) {
				deskTop = Desktop.getDesktop();
			}
			if (deskTop != null)
				deskTop.edit(file);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * 
	 * @Title：print
	 * @Description：打印指定的文件
	 * @param filePath
	 * @return：void 返回类型
	 */
	public static void print(File file) {
		// 当前方法的名称
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		isExists(file, methodName, true);

		try {
			if (Desktop.isDesktopSupported()) {
				deskTop = Desktop.getDesktop();
			}
			if (deskTop != null)
				deskTop.print(file);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * 
	 * @Title：mail
	 * @Description：启动用户默认的邮件客户端
	 * @return：void 返回类型
	 */
	public static void mail() {
		try {
			if (Desktop.isDesktopSupported()) {
				deskTop = Desktop.getDesktop();
			}
			if (deskTop != null)
				deskTop.mail();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	/*-------------------------- 公有方法 end   -------------------------------*/
}

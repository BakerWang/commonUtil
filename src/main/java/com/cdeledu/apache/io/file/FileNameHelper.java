package com.cdeledu.apache.io.file;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;

/**
 * 
 * @ClassName: FileNameHelper
 * @Description: 文件路径操作类(继承与org.apache.commons.io.FilenameUtils)
 * @author: 独泪了无痕
 * @date: 2015年8月27日 下午4:26:28
 * @version: V1.0
 * @history:
 */
public class FileNameHelper extends FilenameUtils {
	/*-------------------------- 私有属性 start -------------------------------*/
	/*-------------------------- 私有属性 end   -------------------------------*/
	/*-------------------------- 私有方法 start -------------------------------*/
	/*-------------------------- 私有方法 end   -------------------------------*/
	/*-------------------------- 公有方法 start -------------------------------*/
	/**
	 * 
	 * @Title：getRealPath
	 * @Description：获取服务器的真实文件路径
	 * @param request
	 * @return
	 * @return：String 返回类型
	 */
	public static String getRealPath(HttpServletRequest request) {
		return request.getSession().getServletContext()
				.getRealPath("/../website/uploadFile/")
				+ File.separator;
	}

	/**
	 * 
	 * @Title：getImagePath
	 * @Description：获取服务器的真实图片路径
	 * @param request
	 * @return
	 * @return：String 返回类型
	 */
	public static String getImagePath(HttpServletRequest request) {
		return request.getSession().getServletContext()
				.getRealPath("/../website/images/")
				+ File.separator;
	}
	/*-------------------------- 公有方法 end   -------------------------------*/
}
//
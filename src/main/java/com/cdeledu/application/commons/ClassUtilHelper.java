package com.cdeledu.application.commons;

/**
 * @类描述: 类处理相关工具
 * @创建者: 独泪了无痕
 * @创建日期: 2016年1月23日 上午12:35:50
 * @版本: V1.0
 * @since: JDK 1.7
 * @see <a href="">TODO(连接内容简介)</a>
 */
public class ClassUtilHelper {

	private ClassUtilHelper() {
		// 静态类不可实例化
	}

	/** Class文件扩展名 */
	public static final String CLASS_EXT = ".class";
	/** Jar文件扩展名 */
	public static final String JAR_FILE_EXT = ".jar";
	/** 在Jar中的路径jar的扩展名形式 */
	public static final String JAR_PATH_EXT = ".jar!";
	/** 当Path为文件形式时, path会加入一个表示文件的前缀 */
	public static final String PATH_FILE_PRE = "file:";
}

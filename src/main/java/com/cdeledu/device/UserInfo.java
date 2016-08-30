package com.cdeledu.device;

/**
 * @类描述: 当前用户的信息
 * @创建者: 独泪了无痕
 * @创建日期: 2016年1月24日 下午12:15:20
 * @版本: V1.0
 * @since: JDK 1.7
 */
public class UserInfo {
	/**
	 * @方法:取得当前登录用户的名字(取自系统属性：<code>user.name</code>)
	 * 
	 * @创建人:独泪了无痕
	 * @return
	 */
	public final String getName() {
		return SystemUtil.get(SystemUtil.USER_NAME, false);
	}

	/**
	 * @方法:取得当前登录用户的home目录
	 * 					<p>
	 *                     例如：<code>"/home/admin"</code>
	 *                     </p>
	 * @创建人:独泪了无痕
	 * @return
	 */
	public final String getHomeDir() {
		return SystemUtil.get(SystemUtil.USER_HOME, false);
	}

	/**
	 * @方法:取得当前目录
	 * @创建人:独泪了无痕
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回<code>null</code>
	 */
	public final String getCurrentDir() {
		return SystemUtil.get(SystemUtil.USER_DIR, false);
	}

	/**
	 * @方法:取得临时目录
	 * @创建人:独泪了无痕
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回<code>null</code>
	 */
	public final String getTempDir() {
		return SystemUtil.get(SystemUtil.JAVA_IO_TMPDIR, false);
	}

	/**
	 * @方法:取得当前登录用户的语言设置
	 * @创建人:独泪了无痕
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回<code>null</code>
	 */
	public final String getLanguage() {
		return SystemUtil.get(SystemUtil.USER_LANGUAGE, false);
	}

	/**
	 * @方法:取得当前登录用户的国家或区域设置
	 * @创建人:独泪了无痕
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回<code>null</code>
	 */
	public final String getCountry() {
		String country = SystemUtil.get(SystemUtil.USER_COUNTRY, false);
		String region = SystemUtil.get(SystemUtil.USER_REGION, false);
		return (country == null) ? region : country;
	}

	/**
	 * 将当前用户的信息转换成字符串。
	 * 
	 * @return 用户信息的字符串表示
	 */
	public final String toString() {
		StringBuilder builder = new StringBuilder();

		SystemUtil.append(builder, "User Name:        ", getName());
		SystemUtil.append(builder, "User Home Dir:    ", getHomeDir());
		SystemUtil.append(builder, "User Current Dir: ", getCurrentDir());
		SystemUtil.append(builder, "User Temp Dir:    ", getTempDir());
		SystemUtil.append(builder, "User Language:    ", getLanguage());
		SystemUtil.append(builder, "User Country:     ", getCountry());

		return builder.toString();
	}
}

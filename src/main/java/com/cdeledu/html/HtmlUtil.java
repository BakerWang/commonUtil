package com.cdeledu.html;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * @ClassName: HtmlUtil
 * @Description: HTML工具类
 * @author: 独泪了无痕
 * @date: 2015年9月10日 下午4:39:22
 * @version: V1.0
 * @since: JDK 1.7
 */
public class HtmlUtil {
	public static final String RE_HTML_MARK = "(<.*?>)|(<[\\s]*?/.*?>)|(<.*?/[\\s]*?>)";
	public static final String RE_SCRIPT = "<[\\s]*?script[^>]*?>.*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
	private static final String TAG_SCRIPT_START = "<script";
	private static final String TAG_SCRIPT_END = "</script>";

	private static final String TAG_STYLE_START = "<style";
	private static final String TAG_STYLE_END = "</style>";
	/**
	 * 
	 * @Title: HTMLDecode
	 * @Description: 将HTML规则的字符串转成正常编码的字符串
	 * @param htmlStr
	 *            包含转义符的HTML内容
	 * @return 转换后的字符串
	 */
	public static String htmlDecode(String htmlStr) {
		if (StringUtils.isBlank(htmlStr)) {
			return htmlStr;
		}
		return htmlStr
				.replace("&lt;", "<")
				.replace("&gt;", ">")
				.replace("&amp;", "&")
				.replace("&quot;", "\"")
				.replace("&#39;", "'")
				.replace("<br/>", "\n")
				.replace("&nbsp;", " ") // 替换空格
				.replace("&nbsp;&nbsp;", "\t");// 替换跳格
	}

	/**
	 * @方法:将过滤掉Html标签
	 * @创建人:独泪了无痕
	 * @param content
	 * @return
	 */
	public static String cleanHtmlTag(String content) {
		return content.replaceAll(RE_HTML_MARK, "");
	}

	/**
	 * @方法:只有纯文本可以通过
	 * @创建人:独泪了无痕
	 * @param html
	 * @return
	 */
	public static String getText(String html) {
		if (html == null)
			return null;
		return Jsoup.clean(html, Whitelist.none());
	}
	
	
}

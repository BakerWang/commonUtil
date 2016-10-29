package com.cdeledu.webCrawler.other.crawlerCity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.cdeledu.network.common.util.IpUtilHelper;

/**
 * @类描述: 网络爬虫必要工具类
 * @创建者: 皇族灬战狼
 * @创建时间: 2016年8月19日 下午8:39:23
 * @版本: V1.0
 * @since: JDK 1.7
 */
public class WebCrawler {
	/** ----------------------------------------------------- Fields start */
	/** ----------------------------------------------------- Fields end */
	/**
	 * @方法描述: 通过地址得到document对象
	 * @创建者: 皇族灬战狼
	 * @创建时间: 2016年8月17日 下午5:34:26
	 * @param url
	 * @return
	 */
	public static Document getDocument(String url) {
		try {
			Document document = Jsoup.connect(url).timeout(10000).get();
			// 表示ip被拦截或者其他情况,反正没有获取document对象
			if (null == document || "".equals(document.toString())) {
				IpUtilHelper.setProxyIp(); // 换代理ip
				getDocument(url);// 继续爬取网页
			}
			return document;
		} catch (Exception e) { // 链接超时等其他情况
			e.printStackTrace();
			IpUtilHelper.setProxyIp(); // 换代理ip
			getDocument(url);// 继续爬取网页
		}
		return getDocument(url);
	}
}

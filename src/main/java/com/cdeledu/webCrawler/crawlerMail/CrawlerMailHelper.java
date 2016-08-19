package com.cdeledu.webCrawler.crawlerMail;

import org.apache.commons.lang3.StringUtils;

import com.cdeledu.network.tcp.HttpURLConnHelper;

/**
 * @类描述: 获得物流单号的跟踪信息
 * @创建者: 皇族灬战狼
 * @创建时间: 2016年8月19日 下午8:54:04
 * @版本: V1.0
 * @since: JDK 1.7
 */
public class CrawlerMailHelper {
	/** ----------------------------------------------------- Fields start */
	// 查询的快递公司代码
	// private static final String getComCode =
	// "http://www.kuaidi100.com/autonumber/autoComNum?text=%s";
	// 获得物流单号的跟踪信息
	private static final String getComInfo = "http://www.kuaidi100.com/query?type=%s&postid=%s";

	/** ----------------------------------------------------- Fields end */

	/**
	 * @方法描述: 快递接口
	 * @创建者: 皇族灬战狼
	 * @创建时间: 2016年4月27日 下午4:30:18
	 * @param type
	 *            快递公司代号
	 *            <ul>
	 *            <li>申通:shentong;EMS:ems;顺丰:shunfeng;圆通:yuantong;</li>
	 *            <li>中通:zhongtong;韵达:yunda;天天:tiantian;</li>
	 *            <li>汇通:huitongkuaidi;全峰:quanfengkuaidi</li>
	 *            <li>德邦:debangwuliu;宅急送:zhaijisong</li>
	 *            </ul>
	 * @param postid
	 *            快递单号
	 * @return
	 */
	public static String getKuaidiInfo(String type, String postid) {
		String result = "";
		if (StringUtils.isNotEmpty(type) && StringUtils.isNotEmpty(postid)) {
			String url = String.format(getComInfo, type, postid);
			result = HttpURLConnHelper.sendGetRequest(url);
		}
		return result;
	}
}

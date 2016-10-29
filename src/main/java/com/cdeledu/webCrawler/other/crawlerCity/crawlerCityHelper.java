package com.cdeledu.webCrawler.other.crawlerCity;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.cdeledu.network.common.util.IpUtilHelper;
import com.cdeledu.webCrawler.crawler.type.UserAgentType;

/**
 * @类描述: 利用网络爬虫端抓取行政区域数据
 * @创建者: 皇族灬战狼
 * @创建时间: 2016年8月16日 下午8:08:57
 * @版本: V1.0
 * @since: JDK 1.7
 */
public class crawlerCityHelper {
	/** ----------------------------------------------------- Fields start */
	/** ----------------------------------------------------- Fields end */

	/** ----------------------------------------------- [私有方法] */
	/**
	 * @方法描述: 通过地址得到document对象
	 * @创建者: 皇族灬战狼
	 * @创建时间: 2016年8月17日 下午5:34:26
	 * @param url
	 * @return
	 */
	public static Document getDocument(String url) {
		Connection conn = null;
		Document document = null;
		try {
			conn = Jsoup.connect(url);
			conn.header("User-Agent", UserAgentType.PC_Firefox.getValue());
			conn.timeout(10000);
			document = conn.timeout(10000).get();
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

	/**
	 * @方法描述: 获取省份代码
	 * @return
	 */
	private static String getProvinceCode(String val) {
		if (val.indexOf(".") == -1)
			return val;
		return (String) val.substring(0, val.indexOf("."));
	}

	/**
	 * @方法描述: 将得到的document进行解析 存入数据库
	 * @创建者: 皇族灬战狼
	 * @创建时间: 2016年8月17日 下午5:39:21
	 * @param document
	 */
	private static void analysisDocument(Document document) {
		Elements eles1 = document.getElementsByAttributeValue("class", "provincetr");
		for (Element ele1 : eles1) {
			Elements eles2 = ele1.getElementsByTag("a");
			/**
			 * 省级行政区(省、自治区、直辖市、特别行政区)
			 */
			for (Element ele2 : eles2) {
				System.out.println("	省级行政区 " + ele2.text());
				getProvinceCode(ele2.attr("href"));
				Document doc2 = getDocument(ele2.absUrl("href"));

				Elements eles3 = doc2.getElementsByAttributeValue("class", "citytr");
				/**
				 * 地级行政区划单位(地级市、地区、自治州、盟)
				 */
				for (Element ele3 : eles3) {
					System.out.println("		地级行政区划单位 " + ele3.text());
					Elements eles4 = ele3.getElementsByTag("a");
					if (eles4.toString().trim().equals("")) {// 表示没有a标签
						Elements diffElements = ele3.getElementsByTag("td");
						// 代码
						diffElements.get(0).text();
						// 名称
						diffElements.get(1).text();
						continue;
					}
					Document doc3 = getDocument(eles4.get(1).absUrl("href"));
					// 代码
					eles4.get(0).text();
					// 名称
					eles4.get(1).text();

					Elements eles5 = doc3.getElementsByAttributeValue("class", "countytr");

					/**
					 * 县级行政区划单位(市辖区、县级市、县、自治县、旗、自治旗、特区、林区)
					 */
					for (Element ele5 : eles5) {
						System.out.println("			县级行政区划单位 " + ele5.text());
						Elements eles6 = ele5.getElementsByTag("a");
						if (eles6.toString().trim().equals("")) {// 表示没有a标签
							Elements diffElements = ele5.getElementsByTag("td");
							// 代码
							diffElements.get(0).text();
							// 名称
							diffElements.get(1).text();
							continue;
						}
						// 代码
						eles6.get(0).text();
						// 名称
						eles6.get(1).text();
						Document doc4 = getDocument(eles6.get(1).absUrl("href"));

						Elements eles7 = doc4.getElementsByAttributeValue("class", "towntr");
						/**
						 * 乡级行政区划单位(区公所、街道、镇、乡、民族乡、苏木、民族苏木)
						 */
						for (Element ele7 : eles7) {
							System.out.println("				乡级行政区划单位 " + ele7.text());
							Elements eles8 = ele7.getElementsByTag("a");
							if (eles8.toString().trim().equals("")) {// 表示没有a标签
								Elements diffElements = ele7.getElementsByTag("td");
								// 代码
								diffElements.get(0).text();
								// 名称
								diffElements.get(1).text();
								continue;
							}
							// 代码
							eles8.get(0).text();
							// 名称
							eles8.get(1).text();
							Document doc5 = getDocument(eles8.get(1).absUrl("href"));
							Elements eles9 = doc5.getElementsByAttributeValue("class", "villagetr");

							/**
							 * 村级行政单位(村民委员会、居民委员会、类似村民委员会、类似居民委员)
							 */
							for (Element ele9 : eles9) {
								System.out.println("					村级行政单位 " + ele9.text());
								Elements eles10 = ele9.getElementsByTag("td");
								// 代码
								eles10.get(0).text();
								// 城乡分类代码
								// eles10.get(1).text();
								// 名称
								eles10.get(2).text();
							}
						}
					}
				}
			}
		}
	}

	/** ----------------------------------------------- [私有方法] */
	/**
	 * @方法描述: 利用网络爬虫端抓取数据
	 * @创建者: 皇族灬战狼
	 * @创建时间: 2016年8月17日 下午9:06:22
	 */
	public static void webCrawler() {
		// 初始解析网页地址
		String crawlerUrl = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2015/index.html";
		Document document = getDocument(crawlerUrl);// 得到的document一定是正常的
		analysisDocument(document);
	}

	public static void main(String[] args) {
		webCrawler();
	}
}

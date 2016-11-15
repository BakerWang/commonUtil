package com.cdeledu.webCrawler.other.crawlerCity;

import java.sql.SQLException;
import java.util.ResourceBundle;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.cdeledu.database.DataTableHelper;
import com.cdeledu.network.common.util.IpUtilHelper;
import com.cdeledu.webCrawler.crawler.type.UserAgentType;
import com.cdeledu.webCrawler.crawler.util.WebCrawlerHelper;

/**
 * @类描述: 利用网络爬虫端抓取行政区域数据
 * @创建者: 皇族灬战狼
 * @创建时间: 2016年8月16日 下午8:08:57
 * @版本: V1.0
 * @since: JDK 1.7
 */
public class crawlerCityHelper {
	/** ----------------------------------------------------- Fields start */
	private static ResourceBundle JDBC = ResourceBundle.getBundle("datasource/jdbc");
	private static String dbUrl = JDBC.getString("database.dbUrl");
	private static String dbUserName = JDBC.getString("database.dbUserName");
	private static String dbPassword = JDBC.getString("database.dbPassword");
	private static String jdbcName = JDBC.getString("database.jdbcName");
	/** SQL执行工具 :实例化查询接口 */
	private static QueryRunner runner = null;
	private static DataTableHelper dataSource = null;
	/** SQL执行工具:执行SQL语句 */
	private static String inserSql = "INSERT INTO sys_date_cityInfo(proName,proCode,parentId,proLevel,proType) VALUES ('%s', '%s', %s, '%s','%s')";
	/** ----------------------------------------------------- Fields end */

	static {
		dataSource = DataTableHelper.getInstance(dbUrl, dbUserName, dbPassword, jdbcName);
		try {
			runner = dataSource.getQueryRunner();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

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

	private static void analysisDocument(Document document) throws Exception {
		Elements eles1 = document.getElementsByAttributeValue("class", "provincetr");
		for (Element ele1 : eles1) {
			Elements eles2 = ele1.getElementsByTag("a");
			String proSql = "";
			/**
			 * 省级行政区(省、自治区、直辖市、特别行政区)
			 */
			for (Element ele2 : eles2) {
				Document doc2 = getDocument(ele2.absUrl("href"));
				int key1 = 0;
				String provinceCode = getProvinceCode(ele2.attr("href"));
				String provinceName = ele2.text();

				proSql = String.format(inserSql, provinceName, provinceCode, "0", 1, "");
				key1 = runner.insert(proSql, new ScalarHandler<Long>()).intValue();

				/**
				 * 地级行政区划单位(地级市、地区、自治州、盟)
				 */

				Elements eles3 = doc2.getElementsByAttributeValue("class", "citytr");
				for (Element ele3 : eles3) {
					Elements eles4 = ele3.getElementsByTag("a");
					String citySql = "";
					// 城市代码
					String cityCode = "";
					// 城市名称
					String cityName = "";
					int key2 = 0;

					if (eles4.toString().trim().equals("")) {// 表示没有a标签
						// Elements diffElements = ele3.getElementsByTag("td");
						// cityCode = diffElements.get(0).text();
						// cityName = diffElements.get(1).text();
						continue;
					}

					cityCode = WebCrawlerHelper.removeTheEnd(eles4.get(0).text(), "0");
					cityName = eles4.get(1).text();
					citySql = String.format(inserSql, cityName, cityCode, key1, 2, "");

					key2 = runner.insert(citySql, new ScalarHandler<Long>()).intValue();

					/**
					 * 县级行政区划单位(市辖区、县级市、县、自治县、旗、自治旗、特区、林区)
					 */

					Document doc3 = getDocument(eles4.get(1).absUrl("href"));
					Elements eles5 = doc3.getElementsByAttributeValue("class", "countytr");
					for (Element ele5 : eles5) {
						Elements eles6 = ele5.getElementsByTag("a");
						String countySql = "";
						String countyCode = "";// 县级代码
						String countyName = "";// 县级名称
						int key3 = 0;

						if (eles6.toString().trim().equals("")) {// 表示没有a标签
							// Elements diffElements =
							// ele5.getElementsByTag("td");
							// countyCode = diffElements.get(0).text();
							// countyName = diffElements.get(1).text();
							continue;
						}
						countyCode = WebCrawlerHelper.removeTheEnd(eles6.get(0).text(), "0");
						countyName = eles6.get(1).text();

						countySql = String.format(inserSql, countyName, countyCode, key2, 3, "");
						key3 = runner.insert(countySql, new ScalarHandler<Long>()).intValue();

						/**
						 * 乡级行政区划单位(区公所、街道、镇、乡、民族乡、苏木、民族苏木)
						 */

						Document doc4 = getDocument(eles6.get(1).absUrl("href"));
						Elements eles7 = doc4.getElementsByAttributeValue("class", "towntr");
						for (Element ele7 : eles7) {
							Elements eles8 = ele7.getElementsByTag("a");
							String townSql = "";
							String townCode = "";// 乡级行政区代码
							String townName = "";// 乡级行政区名称
							int key4 = 0;
							if (eles8.toString().trim().equals("")) {// 表示没有a标签
								// Elements diffElements =
								// ele7.getElementsByTag("td");
								// diffElements.get(0).text();
								// diffElements.get(1).text();
								continue;
							}
							townCode = WebCrawlerHelper.removeTheEnd(eles8.get(0).text(), "0");
							townSql = String.format(inserSql, townName, townCode, key3, 4, "");
							key4 = runner.insert(townSql, new ScalarHandler<Long>()).intValue();

							/**
							 * 村级行政单位(村民委员会、居民委员会、类似村民委员会、类似居民委员)
							 */

							Document doc5 = getDocument(eles8.get(1).absUrl("href"));
							Elements eles9 = doc5.getElementsByAttributeValue("class", "villagetr");
							for (Element ele9 : eles9) {
								Elements eles10 = ele9.getElementsByTag("td");
								String villageSql = "";
								// 乡级行政区代码
								String villageCode = eles10.get(0).text();
								// 乡级行政区名称
								String villageName = eles10.get(2).text();
								// 城乡分类代码
								String villageType = eles10.get(1).text();
								villageSql = String.format(inserSql, villageName, villageCode, key4,
										5, villageType);
								runner.insert(villageSql, new ScalarHandler<Long>()).intValue();
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
	public static void webCrawler() throws Exception {
		// 初始解析网页地址
		String crawlerUrl = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2015/index.html";
		Document document = getDocument(crawlerUrl);// 得到的document一定是正常的
		analysisDocument(document);
	}

	public static void main(String[] args) {
		try {
			webCrawler();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

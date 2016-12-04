package com.cdeledu.webCrawler.crawler.imp;


import org.apache.log4j.Logger;

import com.cdeledu.Constant.ConstantHelper;
import com.cdeledu.network.tcp.HttpURLConnHelper;
import com.cdeledu.webCrawler.crawler.bean.CrawlParameter;

public class UrlConnHandler extends CrawlHandler {

	/** ----------------------------------------------------- Fields start */
	protected static Logger logger = Logger.getLogger(UrlConnHandler.class);
	/** ----------------------------------------------------- Fields end */

	/** ----------------------------------------------- [私有方法] */

	/** ----------------------------------------------- [私有方法] */

	public String crawl(String url, CrawlParameter crawlPara) {
		String resource = "";
		try {
			resource = HttpURLConnHelper.sendPostRequest(url, ConstantHelper.GBK);
		} catch (Exception e) {
			logger.error("通过URLConn方式抓取数据出现异常,异常信息如下:",e);
			e.printStackTrace();
		}
		return resource;
	}

	public static void main(String[] args) {
		UrlConnHandler url = new UrlConnHandler();
		CrawlParameter crawlpara = new CrawlParameter();
		String ss = url.crawl("http://www.ctrip.com/", crawlpara);
		System.out.println(ss);
	}
}

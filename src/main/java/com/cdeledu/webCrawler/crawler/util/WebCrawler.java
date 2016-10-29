package com.cdeledu.webCrawler.crawler.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.commons.io.IOUtils;
import org.jsoup.nodes.Document;

/**
 * @类描述: 数据抓取以及解析通用类
 * @创建者: 皇族灬战狼
 * @创建时间: 2016年10月29日 下午3:03:58
 * @版本: V1.0
 * @since: JDK 1.7
 */
public class WebCrawler {
	/** ----------------------------------------------------- Fields start */
	/** ----------------------------------------------------- Fields end */

	/** ----------------------------------------------- [私有方法] */
	/** ----------------------------------------------- [私有方法] */
	/**
	 * @方法描述: TODO(这里用一句话描述这个方法的作用)
	 * @param doc
	 *            Html文档
	 * @param path
	 *            文件路径
	 * @param code
	 *            编码格式
	 * @return filePaht 保存文件路径
	 * @throws IOException
	 */
	public static String saveHtml(Document doc, String path, String code) throws IOException {
		// 文件保存绝对路径
		String filePath = path + File.separator + System.currentTimeMillis() + "_" + doc.title()
				+ ".htm";
		File file = new File(filePath);
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		fos = new FileOutputStream(file);
		osw = new OutputStreamWriter(fos, code);
		// 写入成功
		osw.write(doc.html());
		IOUtils.closeQuietly(osw);
		IOUtils.closeQuietly(fos);
		return filePath;
	}
}

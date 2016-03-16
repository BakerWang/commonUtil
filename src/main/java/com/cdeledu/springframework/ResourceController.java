package com.cdeledu.springframework;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cdeledu.application.commons.ConfigUtil;

/**
 * @类描述:资源(文件的上传、下载)控制类
 * @创建者: 皇族灬战狼
 * @创建时间: 2016年3月5日 下午5:34:22
 * @版本: V1.0
 * @since: JDK 1.7
 */
@Controller
@RequestMapping(value = "resourceController")
public class ResourceController {
	protected Logger logger = LoggerFactory.getLogger(ResourceController.class);
	/**
	 * @方法描述: 上传文件
	 * @创建者: 皇族灬战狼
	 * @创建时间: 2016年3月5日 下午5:37:09
	 * @param request
	 * @param response
	 * @param file
	 *            被上传的文件
	 * @return
	 */
	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
	public String handleFormUpload(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {// 被上传的文件是否是空的
			System.out.println("文件未上传");
		} else {
			System.out.println("文件长度(大小): " + file.getSize());
			System.out.println("文件内容类型: " + file.getContentType());
			System.out.println("文件名称: " + file.getName());
			System.out.println("文件原名: " + file.getOriginalFilename());
			System.out.println("========================================");

		}
		return null;
	}

	/**
	 * @方法描述: 资源下载
	 * @创建者: 皇族灬战狼
	 * @创建时间: 2016年3月5日 下午5:41:20
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/download", method = RequestMethod.POST)
	public String downloadResource(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@RequestParam("path") String dataDirtectory) {
		// 如果用户没有登陆,则跳转到登陆界面
		if (null == session
				|| null == session
						.getAttribute(ConfigUtil.getSessionInfoName())) {
			return "LoginForm";
		}
		return null;
	}
}

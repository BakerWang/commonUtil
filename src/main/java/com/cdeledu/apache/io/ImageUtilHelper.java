package com.cdeledu.apache.io;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.IOUtils;

/**
 * 
 * @ClassName: ImageUtilHelper
 * @Description: 图片处理工具类
 *               <ul>
 *               <li>缩放图像、切割图像、图像类型转换、彩色转黑白、文字水印、图片水印</li>
 *               <li>
 *               http://blog.csdn.net/zhangzhikaixinya/article/details/8459400</li>
 *               <li>图片验证码</li>
 *               <li>http://blog.csdn.net/ruixue0117/article/details/22829557</li>
 *               <li>http://blog.csdn.net/dy511/article/details/6957400</li>
 *               <li>http://www.iteye.com/topic/573456</li>
 *               <li></li>
 *               </ul>
 * @author: 独泪了无痕
 * @date: 2015年7月17日 下午12:31:46
 * @version: V1.0
 */
public class ImageUtilHelper {
	/*-------------------------- 私有属性 begin -------------------------------*/
	/*-------------------------- 私有属性 end   -------------------------------*/
	/*-------------------------- 私有方法 begin -------------------------------*/
	/*-------------------------- 私有方法 end   -------------------------------*/
	/*-------------------------- 公有方法 begin -------------------------------*/
	/** 判断文件是否是图片 */
	public static boolean isImage(File file){
		boolean flag = false;
		ImageInputStream is = null;
		Iterator<ImageReader> iter = null;
		try {
			is = ImageIO.createImageInputStream(file);
			iter = ImageIO.getImageReaders(is);
			if (!iter.hasNext()) {
				flag = false;
			} else {
				flag = true;
			}
		} catch (Exception e) {
			flag = false;
		} finally {
			IOUtils.closeQuietly(is);
		}
		return flag;
	}

	/** 判断文件是否是图片 */
	public static boolean isImage(String filePath) {
		File file = new File(filePath);
		if (!file.exists())
			return false;
		try {
			BufferedImage bi = ImageIO.read(file);
			if (bi == null)
				return false;
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 
	 * @Title：zoomImageByScale
	 * @Description：缩放图像（按比例缩放）
	 * @param srcImageFile
	 *            源图像文件地址
	 * @param result
	 *            缩放后的图像地址
	 * @param scale
	 *            缩放比例
	 * @param flag
	 *            缩放选择:true 放大; false 缩小
	 * @return：void 返回类型
	 */
	public final static void scaleByPercentage(String srcImageFile,
			String result, int scale, boolean flag) {

	}

	/**
	 * 
	 * @Title：scale2
	 * @Description： 缩放图像（按高度和宽度缩放）
	 * @param srcImageFile
	 *            源图像文件地址
	 * @param result
	 *            缩放后的图像地址
	 * @param height
	 *            缩放后的高度
	 * @param width
	 *            缩放后的宽度
	 * @param flag
	 *            比例不对时是否需要补白：true为补白; false为不补白;
	 * @return：void 返回类型
	 */
	public final static void scaleByPixelRate(String srcImageFile,
			String result, int height, int width, boolean flag) {

	}

	/**
	 * 
	 * @Title：cutImageByLocation
	 * @Description：图像切割(按指定起点坐标和宽高切割)
	 * @param srcImageFile
	 *            源图像地址
	 * @param result
	 *            切片后的图像地址
	 * @param x
	 *            目标切片起点坐标X
	 * @param y
	 *            目标切片起点坐标Y
	 * @param width
	 *            目标切片宽度
	 * @param height
	 *            目标切片高度
	 * @return：void 返回类型
	 */
	public final static void cutImageByLocation(String srcImageFile,
			String result, int x, int y, int width, int height) {

	}

	/**
	 * 
	 * @Title：cutImageBy
	 * @Description： 图像切割（指定切片的行数和列数）
	 * @param srcImageFile
	 *            源图像地址
	 * @param descDir
	 *            切片目标文件夹
	 * @param rows
	 *            目标切片行数。默认2，必须是范围 [1, 20] 之内
	 * @param cols
	 *            目标切片列数。默认2，必须是范围 [1, 20] 之内
	 * @return：void 返回类型
	 */
	public final static void cutImageBy(String srcImageFile, String descDir,
			int rows, int cols) {
		if (rows <= 0 || rows > 20)
			rows = 2;// 切片行数
		if (cols <= 0 || cols > 20)
			cols = 2;// 切片列数
	}

	/**
	 * 
	 * @Title：cut
	 * @Description：图像切割（指定切片的宽度和高度）
	 * @param srcImageFile
	 *            源图像地址
	 * @param descDir
	 *            切片目标文件夹
	 * @param destWidth
	 *            目标切片宽度。默认200
	 * @param destHeight
	 *            目标切片高度。默认150
	 * @return：void 返回类型
	 */
	public final static void cut(String srcImageFile, String descDir,
			int destWidth, int destHeight) {
		// 切片宽度
		if (destWidth <= 0)
			destWidth = 200;
		// 切片高度
		if (destHeight <= 0)
			destHeight = 150;
	}

	/**
	 * @Description：图像类型转换：GIF->JPG、GIF->PNG、PNG->JPG、PNG->GIF(X)、BMP->PNG
	 * @param srcImageFile
	 *            源图像地址
	 * @param formatName
	 *            包含格式非正式名称的 String：如JPG、JPEG、GIF等
	 * @param destImageFile
	 *            目标图像地址
	 * @return：void 返回类型
	 */
	public final static void convert(String srcImageFile, String formatName,
			String destImageFile) {

	}

	/**
	 * 
	 * @Title：pressText
	 * @Description：给图片添加文字水印
	 * @param pressText
	 *            水印文字
	 * @param srcImageFile
	 *            源图像地址
	 * @param destImageFile
	 *            目标图像地址
	 * @param fontName
	 *            水印的字体名称
	 * @param fontStyle
	 *            水印的字体样式
	 * @param color
	 *            水印的字体颜色
	 * @param fontSize
	 *            水印的字体大小
	 * @param x
	 *            修正值
	 * @param y
	 *            修正值
	 * @param alpha
	 *            透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 * @return：void 返回类型
	 */
	public final static void pressText(String pressText, String srcImageFile,
			String destImageFile, String fontName, int fontStyle, Color color,
			int fontSize, int x, int y, float alpha) {

	}

	/**
	 * 
	 * @Title：pressImage
	 * @Description：给图片添加图片水印
	 * @param pressImg
	 *            水印图片
	 * @param srcImageFile
	 *            源图像地址
	 * @param destImageFile
	 *            目标图像地址
	 * @param x
	 *            x 修正值。 默认在中间
	 * @param y
	 *            x 修正值。 默认在中间
	 * @param alpha
	 *            透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 * @return：void 返回类型
	 */
	public final static void pressImage(String pressImg, String srcImageFile,
			String destImageFile, int x, int y, float alpha) {

	}
	/*-------------------------- 公有方法 end   -------------------------------*/
}

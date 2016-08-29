package com.cdeledu.message;

import com.cdeledu.apache.lang.StringUtilHelper;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 
 * @类名称 : PinyinUtil
 * @功能说明 : java汉字转拼音操作工具类
 * @创建人: 独泪了无痕
 *
 */
public class PinyinUtil {
	public PinyinUtil() {
		super();
	}

	/*-------------------------- 私有属性 start -------------------------------*/
	/** 创建汉语拼音处理类 */
	private static final HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
	static {
		// 输出设置，大小写，音标方式
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
	}

	/*-------------------------- 私有属性 end   -------------------------------*/
	/*-------------------------- 私有方法 start -------------------------------*/
	/*-------------------------- 私有方法 end   -------------------------------*/
	/*-------------------------- 公有方法 start -------------------------------*/

	/**
	 * 
	 * @方法名称: hanziToPinyin
	 * @方法描述: 将汉字转换成拼音
	 * 
	 * @param hanzi
	 *            转换的汉字
	 * @param separator
	 *            转换之后的拼音之间的分割分,默认是" "
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String hanziToPinyin(String hanzi, String separator) {
		if (StringUtilHelper.isBlank(hanzi)) {
			return null;
		}
		if (StringUtilHelper.isBlank(separator)) {
			separator = " ";
		}
		String pinyingStr = "";
		try {
			pinyingStr = PinyinHelper.toHanyuPinyinString(hanzi, defaultFormat,
					separator);
		} catch (BadHanyuPinyinOutputFormatCombination bhpofc) {
			bhpofc.printStackTrace();
		}
		return pinyingStr;
	}
	/*-------------------------- 公有方法 end   -------------------------------*/
}

package com.cdeledu.apache.poi.excel;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.cdeledu.application.commons.ObjectUtils;

/**
 * 
 * @类名称 : ExcelExportUtil
 * @功能说明 : excel 导出工具类
 * @创建人: 独泪了无痕
 *
 */
public class ExcelExportUtil {
	/*-------------------------- 私有属性 start -------------------------------*/
	/*-------------------------- 私有属性 end   -------------------------------*/
	/*-------------------------- 私有方法 start -------------------------------*/
	/** 单元格样式 */
	private static CellStyle getStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		/**
		 * 创建字体样式
		 */
		Font font = workbook.createFont();
		font.setFontHeight((short) 14);
		font.setFontName("宋体");
		style.setFont(font);

		/**
		 * 创建单元格样式
		 */
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		/**
		 * 设置单元格边框及颜色
		 */
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		return style;
	}

	/*-------------------------- 私有方法 end   -------------------------------*/
	/*-------------------------- 公有方法 start -------------------------------*/
	/**
	 * 
	 * @Title: export
	 * @Description: 基于结果集的导出
	 *               <ul>
	 *               <li>基于结果集的导出</li>
	 *               <li>XSSFWorkbook used for .xslx >= 2007</li>
	 *               <li>HSSFWorkbook used for .xsl 03</li>
	 *               </ul>
	 * @param title
	 * @param cellNames
	 * @param dbList
	 * @return
	 * @throws Exception
	 */
	public static <T> byte[] export(String title, String[][] cellNames, List<T> dbList) throws Exception {
		if (CollectionUtils.isEmpty(dbList) || dbList.size() < 0)
			return null;

		// 创建Excel的工作书册 Workbook,对应到一个excel文档
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 创建Excel的工作sheet,对应到一个excel文档的tab
		HSSFSheet sheet = workbook.createSheet(title);
		// 创建字体样式
		HSSFFont font = workbook.createFont();
		font.setFontName("宋体");
		// 创建单元格样式
		CellStyle style = getStyle(workbook);
		// 创建Excel的sheet的一行
		HSSFRow row = sheet.createRow(0);
		// 设定行的高度
		row.setHeight((short) 500);
		// 创建一个Excel的单元格
		HSSFCell cell = row.createCell(0);
		// 合并单元格(startRow，endRow，startColumn，endColumn)
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, cellNames.length - 1));
		// 给合并后的单元格加上样式
		for (int j = 0; j <= cellNames.length - 1; j++) {
			HSSFCell cell_temp = row.getCell(j);
			if (cell_temp == null) {
				cell_temp = row.createCell(j);
			}
			cell_temp.setCellStyle(style);
		}
		// 给Excel的单元格设置样式和赋值
		cell.setCellStyle(style);
		cell.setCellValue(title);
		HSSFRow rowTitle = sheet.createRow(1);
		for (int i = 0; i < cellNames.length; i++) {
			HSSFCell cellTitle = rowTitle.createCell(i);
			cellTitle.setCellStyle(style);
			// 设置excel列名
			cellTitle.setCellValue(cellNames[i][1]);
		}
		// 自动换行
		style.setWrapText(true);
		int i = 0;
		for (T bd : dbList) {
			row = sheet.createRow(i + 2);
			for (int j = 0; j < cellNames.length; j++) {
				HSSFCell cellvalue = row.createCell(j);
				String value = null;
				Object object = ObjectUtils.getProperty(bd, cellNames[j][0]);
				if (null == object) {
					value = "";
				} else if (object instanceof Date) {
					Date date = (Date) object;
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					value = sdf.format(date);
				} else {
					value = String.valueOf(object);
				}
				if ("str".equals(cellNames[j][2])) {
					cellvalue.setCellValue(value);
					cellvalue.setCellStyle(style);
				} else {
					HSSFDataFormat format = workbook.createDataFormat();
					HSSFCellStyle formatStyle = workbook.createCellStyle();
					// 设置边框
					formatStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
					formatStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
					formatStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
					formatStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
					// 设置字体
					formatStyle.setFont(font);
					if ("amt".equals(cellNames[j][2])) {
						cellvalue.setCellValue(Double.parseDouble(value));
						// 设置cell样式为定制的浮点数格式
						formatStyle.setDataFormat(format.getFormat("#,##0.00"));
					} else if ("datetime".equals(cellNames[j][2])) {
						cellvalue.setCellValue(value);
						// 设置cell样式为定制的日期时间格式
						formatStyle.setDataFormat(format.getFormat("yyyy-MM-dd hh:mm:ss"));
					}
					cellvalue.setCellStyle(formatStyle);
				}
			}
			i++;
		}
		if (i == 0) {
			return null;
		}
		for (int k = 0; k < cellNames.length; k++) {
			// 自动设置列宽
			sheet.autoSizeColumn(k, true);
		}
		// 将生成的Excel放入IO流中
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		// 在内存中把数据写入ByteArrayOutputStream os
		workbook.write(os);
		// 在内存中,得到os的字节数组
		byte[] content = os.toByteArray();
		return content;
	}
	/*-------------------------- 公有方法 end   -------------------------------*/
}

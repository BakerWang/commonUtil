package com.cdeledu.apache.poi.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.cdeledu.exception.ExceptionHelper;

/**
 * 
 * @类名称 : ExcelImportUtil
 * @功能说明 : Excel 导入工具
 * @创建人: 独泪了无痕
 *
 */
public class ExcelImportUtil {
	/*--------------------------私有属性 start -------------------------------*/
	/* Excel 2003 */
	private final static String XLS = "xls";
	/* Excel 2007 */
	private final static String XLSX = "xlsx";

	private static InputStream input = null;
	private static Workbook workBook = null;
	/* 默认的日期格式 */
	private static SimpleDateFormat Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/** 工作表 sheet */
	private static Sheet sheet = null;
	/** 行 row */
	private static Row row = null;
	/** 列 cell */
	private static Cell cell = null;

	/*--------------------------私有属性 end   -------------------------------*/
	/*--------------------------私有方法 start -------------------------------*/
	private static <T> Collection<T> getContent(Workbook workBook, Class<T> clazz) throws Exception {
		Collection<T> result = new ArrayList<T>();

		Field[] fields = clazz.getDeclaredFields();

		// 获取第一页
		sheet = workBook.getSheetAt(0);
		// 获取行数
		int rowMem = sheet.getLastRowNum();

		if (rowMem < 1) {
			return new ArrayList<T>();
		}

		/**
		 * 循环工作表 Sheet 并获取工作表信息
		 */
		// 获取Excel的sheet数量
		Integer sheetNum = workBook.getNumberOfSheets();

		for (int numSheet = 0; numSheet < sheetNum; numSheet++) {
			sheet = workBook.getSheetAt(numSheet);

			if (sheet == null) {
				continue;
			}

			/**
			 * <ul>
			 * <li>循环行Row,获取 行信息</li>
			 * <li>一般情况下,正文内容应该从第二行开始,第一行为表头的标题</li>
			 * </ul>
			 */

			for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
				row = sheet.getRow(rowNum);
				if (row == null) {
					continue;
				}
				T instance = (T) clazz.newInstance();

				// 循环列Cell,获取单元格信息
				for (int cellNum = 0; cellNum < row.getLastCellNum(); cellNum++) {
					cell = row.getCell(cellNum);
					if (cell == null) {
						continue;
					}

					Field Field = fields[cellNum];
					String cellValue = getCellFormatValue(cell);
					Field.setAccessible(true);
					Class<?> type = Field.getType();
					if (type == String.class) {
						if (StringUtils.isNotBlank(cellValue)) {
							Field.set(instance, cellValue);
						}
					} else if (type == double.class || type == Double.class) {
						if (StringUtils.isNotBlank(cellValue)) {
							Field.set(instance, Double.parseDouble(cellValue));
						}
					} else if (type == int.class || type == Integer.class) {
						if (StringUtils.isNotBlank(cellValue)) {
							Field.set(instance, Integer.parseInt(cellValue));
						}
					} else if (type == float.class || type == Float.class) {
						if (StringUtils.isNotBlank(cellValue)) {
							Field.set(instance, Float.parseFloat(cellValue));
						}
					} else if (type == long.class || type == Long.class) {
						if (StringUtils.isNotBlank(cellValue)) {
							Field.set(instance, Long.parseLong(cellValue));
						}
					} else if (type == Date.class) {
						if (StringUtils.isNotBlank(cellValue)) {
							Field.set(instance, Format.parse(cellValue));
						}
					} else {
						Field.set(instance, null);
					}
				}
				result.add(instance);
			}
		}
		return result;
	}

	/**
	 * 
	 * @方法名称: getCellFormatValue
	 * @方法描述: 获取单元格的数据
	 *        <ul>
	 *        <li>获取key的值,针对不同类型获取不同的值</li>
	 *        </ul>
	 * @param cell
	 * @return
	 */
	private static String getCellFormatValue(Cell cell) {
		String cellValue = "";
		if (cell != null) {
			// 判断当前Cell的Type
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BLANK: // 空，不知道何时算空
				cellValue = "";
				break;
			case Cell.CELL_TYPE_BOOLEAN: // 布尔类型
				cellValue = String.valueOf(cell.getBooleanCellValue()).trim();
				break;
			case Cell.CELL_TYPE_FORMULA:// 表达式类型
				cellValue = cell.getCellFormula();
				break;
			case Cell.CELL_TYPE_NUMERIC:// 数值类型
				// 判断当前的cell是否为Date
				if (DateUtil.isCellDateFormatted(cell)) {
					cellValue = Format.format(cell.getDateCellValue());
				} else { // 如果是纯数字
					// 取得当前Cell的数值,默认返回时Integer类型
					// new DecimalFormat("0").format(cell.getNumericCellValue())
					Integer num = new Integer((int) cell.getNumericCellValue());
					cellValue = String.valueOf(num);
				}
				break;
			case Cell.CELL_TYPE_STRING: // 字符串类型
				cellValue = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_ERROR: // 故障
				cellValue = "";
				break;
			default:
				cellValue = "";
			}
		} else {
			cellValue = "";
		}

		return cellValue;
	}

	/*--------------------------私有方法 end   -------------------------------*/
	/*--------------------------公有方法 start -------------------------------*/
	public static <T> Collection<T> excelParse(String excelPath, Class<T> clazz) {
		Collection<T> result = new ArrayList<T>();
		// 如果将要解析的文件路径为空
		if (StringUtils.isBlank(excelPath)) {
			ExceptionHelper.getExceptionHint("ExcelImportUtil", "excelParse", "excelPath不能为空!");
		}

		try {

			// 加载文档
			input = new FileInputStream(new File(excelPath));
			// 文件名的后缀名
			String extensionName = FilenameUtils.getExtension(excelPath);

			/**
			 * <ul>
			 * <li>在解析Excel的时候,判断导入Excel的版本,调用不同的方法</li>
			 * <li>推荐使用WorkbookFactory.create(inputStream)来创建Workbook</li>
			 * <li>在WorkbookFactory.create()函数中,是根据文件类型来分别创建合适的Workbook对象</li>
			 * <li>当然也是可以使用文件后缀名来判断类型</li>
			 * </ul>
			 */
			if (extensionName.toLowerCase().equals(XLS)) {
				// workbook = new HSSFWorkbook(input);
			} else if (extensionName.toLowerCase().equals(XLSX)) {
				// workbook = new XSSFWorkbook(input);
			}
			workBook = WorkbookFactory.create(input);
			result = getContent(workBook, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					input = null;
				}
			}
		}
		return result;
	}

	/*--------------------------公有方法 end   -------------------------------*/
}

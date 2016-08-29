package com.cdeledu.database;

import java.text.MessageFormat;

import com.cdeledu.appConfig.ConfigUtil;

public class JdbcDaoTemplate {
	/*-------------------------- 私有属性 begin -------------------------------*/
	/** 数据库类型 */
	private static final String DATABSE_TYPE_MYSQL = "mysql";
	private static final String DATABSE_TYPE_POSTGRE = "postgresql";
	private static final String DATABSE_TYPE_ORACLE = "oracle";
	/** 分页 语句 */
	// MYsql
	private static final String MYSQL_SQL = "SELECT * FROM ( {0}) sel_tab limit {1},{2}";
	// PostGresql
	private static final String POSTGRE_SQL = "SELECT * FROM ( {0}) sel_tab limit {1} offset {2}";
	// oracle
	private static final String ORACLE_SQL = "SELECT * FROM (select row_.*,rownum rownum_ from ({0}) row_ where rownum <= {1}) where rownum_>{2}";

	/*-------------------------- 私有属性 end   -------------------------------*/
	/*-------------------------- 私有方法 begin -------------------------------*/
	/*-------------------------- 私有方法 end   -------------------------------*/
	/*-------------------------- 公有方法 begin -------------------------------*/
	/**
	 * 
	 * @Title：jeecgCreatePageSql
	 * @Description：按照数据库类型，封装SQL
	 * @param sql
	 * @param page
	 * @param rows
	 * @return
	 * @return：String 返回类型
	 */
	public static String jeecgCreatePageSql(String sql, int page, int rows) {
		int beginNum = (page - 1) * rows;
		String[] sqlParam = new String[3];
		sqlParam[0] = sql;
		sqlParam[1] = beginNum + "";
		sqlParam[2] = rows + "";
		if (ConfigUtil.getJdbcUrl().indexOf(DATABSE_TYPE_MYSQL) != -1) {
			sql = MessageFormat.format(MYSQL_SQL, sqlParam);
		} else if (ConfigUtil.getJdbcUrl().indexOf(DATABSE_TYPE_POSTGRE) != -1) {
			sql = MessageFormat.format(POSTGRE_SQL, sqlParam);
		} else if (ConfigUtil.getJdbcUrl().indexOf(DATABSE_TYPE_ORACLE) != -1) {
			int beginIndex = (page - 1) * rows;
			int endIndex = beginIndex + rows;
			sqlParam[2] = beginIndex + "";
			sqlParam[1] = endIndex + "";
			sql = MessageFormat.format(ORACLE_SQL, sqlParam);
		}
		return sql;
	}
	/*-------------------------- 公有方法 end   -------------------------------*/
}

package com.cdeledu.database;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.cdeledu.apache.collection.ListUtilHelper;
import com.cdeledu.database.model.ColumnInfo;
import com.cdeledu.database.model.DataBaseInfo;
import com.mysql.jdbc.Statement;

/**
 * @描述: 数据库操作工具类
 * @author: 独泪了无痕
 * @date: 2015年6月25日 下午4:22:41
 * @version: V2.0
 * @history:
 */
public class DataTableHelper {
	/** ----------------------------------------------------- Fields start */
	// 属性文件
	private final static String INIT_PATH = "datasource/jdbc";
	// 初始化SQL文件
	private final static String INIT_SQL_PATH = "datasource/initDatabase.sql";
	private static String rootPath = "";
	private static String dbUrl;
	private static String dbUserName;
	private static String dbPassword;
	private static String jdbcName;
	private final static ResourceBundle jdbcBundle;
	private static Connection conn = null;
	/** ----------------------------------------------------- Fields end */
	static {
		jdbcBundle = ResourceBundle.getBundle(INIT_PATH);
		dbUrl = jdbcBundle.getString("database.dbUrl");
		dbUserName = jdbcBundle.getString("database.dbUserName");
		dbPassword = jdbcBundle.getString("database.dbPassword");
		jdbcName = jdbcBundle.getString("database.jdbcName");
		rootPath = DataTableHelper.class.getClassLoader().getResource(INIT_SQL_PATH).getPath();
		try {
			Class.forName(jdbcName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/*--------------------------私有属性 end-------------------------------*/
	/*--------------------------私有方法 start-------------------------------*/
	private DataTableHelper() {
		// 非可实例化类
	}

	/**
	 * @方法描述: 获取数据库连接
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
	}

	/*--------------------------私有方法 end-------------------------------*/
	/*--------------------------共有方法 start-------------------------------*/

	/**
	 * @方法描述: 执行SQL
	 * @param sqlList
	 * @throws SQLException
	 */
	public static void executeSql(List<String> sqlList) throws SQLException {
		conn = getConnection();
		Statement st = (Statement) conn.createStatement();
		for (String sql : sqlList) {
			st.executeUpdate(sql);
		}
		closeAll(conn, null, null, null);
	}

	/**
	 * 
	 * @Title：initDataBase
	 * @Description：根据已经存在SQL文件初始化数据库
	 * @return：void 返回类型
	 */
	public static void initDataBase() {

		// 从SQL文件中读取SQL语句，每行一条，末尾没有分号
		List<String> sqlList = new ArrayList<String>();
		FileInputStream in = null;
		BufferedReader br = null;
		try {
			in = new FileInputStream(rootPath);
			// 指定读取文件时以UTF-8的格式读取
			br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String instring;
			while ((instring = br.readLine()) != null) {
				if (instring.length() != 0) {
					String line = instring.trim();
					sqlList.add(line);
				}
			}
			executeSql(sqlList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(in);
		}
	}

	/**
	 * @方法描述: 关闭数据库连接
	 * @param conn
	 * @param rs
	 * @param st
	 * @param ps
	 * @throws SQLException
	 */
	public static void closeAll(Connection conn, ResultSet rs, Statement st, PreparedStatement ps)
			throws SQLException {
		if (rs != null) {
			rs.close();
		}
		if (st != null) {
			st.close();

		}
		if (ps != null) {
			ps.close();
		}
		if (conn != null) {
			conn.close();
		}
	}

	/**
	 * 
	 * @方法描述:
	 *        <ul>
	 *        <li>列的类型和属性信息的对象</li>
	 *        <li>ResultSetMetaData接口用于获取关于ResultSet对象中列的类型和属性信息的对象</li>
	 *        </ul>
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public static List<ColumnInfo> getTableInfo(String tableName) throws SQLException {
		List<ColumnInfo> resList = null;
		// 如果所要查询的表没有在数据库中表集合中，直接返回null
		if (ListUtilHelper.indexOf(getTableNameByCon(), tableName) < 0) {
			return null;
		}
		if (StringUtils.isNotBlank(tableName)) {
			resList = new ArrayList<ColumnInfo>();
			ColumnInfo info = null;
			PreparedStatement ps = null;
			ResultSet rs = null;

			conn = getConnection();
			String sql = "select * from " + tableName;
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			ResultSetMetaData rsme = rs.getMetaData();

			/** ResultSet对象中的列数 */
			int columnCount = rsme.getColumnCount();
			if (columnCount >= 1) {
				for (int i = 1; i < columnCount; i++) {
					info = new ColumnInfo();
					// 列名称:
					info.setColumnName(rsme.getColumnName(i));
					// 列类型(DB)
					info.setColumnTypeName(rsme.getColumnTypeName(i));
					// 长度
					info.setPrecision(rsme.getPrecision(i));
					// 是否自动编号
					info.setAutoIncrement(rsme.isAutoIncrement(i));
					// 是否可以为空
					boolean result = rsme.isNullable(i) == 0 ? true : false;
					info.setAutoIncrement(result);
					// 是否可以写入
					info.setReadOnly(rsme.isReadOnly(i));
					resList.add(info);
				}
			}
			closeAll(conn, rs, null, ps);
		}
		return resList;
	}

	/**
	 * 
	 * @Title：getDataBaseInfo
	 * @Description：数据库相关属性
	 * @return
	 * @throws SQLException
	 * @return：DataBaseInfo 返回类型
	 */
	public static DataBaseInfo getDataBaseInfo() throws SQLException {
		conn = getConnection();
		DataBaseInfo resultMap = new DataBaseInfo();

		DatabaseMetaData metadata = conn.getMetaData();

		resultMap.setUserName(metadata.getUserName());
		resultMap.setDataBaseUrl(metadata.getURL());
		resultMap.setReadOnly(metadata.isReadOnly());
		resultMap.setProductName(metadata.getDatabaseProductName());
		resultMap.setVersion(metadata.getDatabaseProductName());
		resultMap.setDriverName(metadata.getDriverName());
		resultMap.setDriverVersion(metadata.getDriverVersion());

		ResultSet rs = metadata.getTableTypes();
		List<String> typeInfo = new ArrayList<String>();
		while (rs.next()) {
			typeInfo.add(rs.getString(1));
		}
		resultMap.setTableTypes(typeInfo);

		return resultMap;
	}

	/**
	 * @方法描述: 得到当前数据库下所有的表名
	 * @return
	 * @throws SQLException
	 */
	public static List<String> getTableNameByCon() throws SQLException {
		List<String> resultList = null;
		resultList = new ArrayList<String>();
		conn = getConnection();
		DatabaseMetaData metadata = conn.getMetaData();
		ResultSet rs = metadata.getTables(null, null, null, new String[] { "TABLE" });
		while (rs.next()) {
			/** 表所属用户名: rs.getString(2) */
			resultList.add(rs.getString(3));
		}
		closeAll(conn, null, null, null);

		return resultList;
	}
	/*--------------------------共有方法 end-------------------------------*/
}
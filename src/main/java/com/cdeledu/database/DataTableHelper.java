package com.cdeledu.database;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.cdeledu.apache.collection.ListUtilHelper;
import com.cdeledu.database.model.ColumnInfo;
import com.cdeledu.database.model.DataBaseInfo;
import com.google.common.collect.Lists;
import com.mysql.jdbc.Statement;

/**
 * @类描述:
 *       <ul>
 *       <li>是 Apache 组织提供的一个开源 JDBC工具类库,是轻量级的ORM工具</li>
 *       <li>提供了数据库操作的简单实现,包含增、删、改、查、批量以及事务等操作</li>
 *       <li>ResultSetHandler实现类介绍</li>
 *       <li>①、ArrayHandler:将查询结果的第一行数据,保存到Object数组中</li>
 *       <li>②、ArrayListHandler:将查询的结果,每一行先封装到Object数组中,然后将数据存入List集合</li>
 *       <li>③、BeanHandler:将查询结果的第一行数据,封装到user对象</li>
 *       <li>④、BeanListHandler:将查询结果的每一行封装到user对象,然后再存入List集合</li>
 *       <li>⑤、ColumnListHandler:将查询结果的指定列的数据封装到List集合中</li>
 *       <li>⑥、MapHandler:将查询结果的第一行数据封装到map集合(key==列名,value==列值)</li>
 *       <li>⑦、MapListHandler:将查询结果的每一行封装到map(key==列名,value==列值),再将map集合存入List集合
 *       </li>
 *       <li>⑧、BeanMapHandler:将查询结果的每一行数据,封装到User对象,再存入mao集合中(key==列名,value==列值)
 *       </li>
 *       <li>⑨、KeyedHandler:将查询的结果的每一行数据,封装到map1(key==列名,value==列值
 *       ),然后将map1集合(有多个)存入map2集合(只有一个)</li>
 *       <li>⑩、ScalarHandler:封装类似count、avg、max、min、sum......函数的执行结果,处理单行记录,
 *       只返回结果集第一行中的指定字段,如未指定字段,则返回第一个字段</li>
 *       </ul>
 * @创建者: 皇族灬战狼
 * @创建时间: 2015年6月25日 下午4:22:41
 * @版本: V2.0
 * @since: JDK 1.7
 */
public class DataTableHelper {
	/** ----------------------------------------------------- Fields start */
	private static Logger logger = Logger.getLogger(DataTableHelper.class);
	private String dbUrl = null;
	private String dbUserName = null;
	private String dbPassword = null;
	private String jdbcName = null;
	/** 获取数据源的接口 */
	private DataSource dataSource;
	/** SQL执行工具 :实例化查询接口 */
	private DatabaseMetaData metadata = null;
	private static DataTableHelper instance;
	/** 数据库连接 */
	private Connection conn = null;

	/** ----------------------------------------------------- Fields end */
	/** ----------------------------------------------------- 私有方法 开始 */

	public DataTableHelper(String dbUrl, String dbUserName, String dbPassword, String jdbcName) {
		this.dbUrl = dbUrl;
		this.dbUserName = dbUserName;
		this.dbPassword = dbPassword;
		this.jdbcName = jdbcName;
	}

	/**
	 * @方法描述: 获取数据源并加载相关配置
	 * @return
	 */
	private synchronized DataSource getDataSource() {
		if (null == dataSource) {
			try {
				BasicDataSource dbcpDataSource = new BasicDataSource();
				dbcpDataSource.setUrl(dbUrl);
				dbcpDataSource.setDriverClassName(jdbcName);
				dbcpDataSource.setUsername(dbUserName);
				dbcpDataSource.setPassword(dbPassword);
				dbcpDataSource.setDefaultAutoCommit(true);
				dbcpDataSource.setMaxActive(100);
				dbcpDataSource.setMaxIdle(30);
				dbcpDataSource.setMaxWait(500L);
				dataSource = dbcpDataSource;
			} catch (Exception ex) {
				logger.info("dbcp数据源初始化失败:" + ex.getMessage(), ex);
			}
		}
		return dataSource;
	}

	/**
	 * @方法描述:获取数据源链接
	 * @return
	 * @throws SQLException
	 */
	private synchronized Connection getConnection() throws SQLException {
		if (null == conn) {
			return getDataSource().getConnection();
		}
		return conn;
	}

	/**
	 * @方法描述: 执行SQL
	 * @param sqlList
	 * @throws SQLException
	 */
	private void executeSql(List<String> sqlList) throws SQLException {
		conn = getConnection();
		Statement st = (Statement) conn.createStatement();
		for (String sql : sqlList) {
			st.executeUpdate(sql);
		}
		closeAll(conn, null, st, null);
	}

	/**
	 * @方法描述: 关闭数据库连接
	 * @param conn
	 * @param rs
	 * @param st
	 * @param ps
	 * @throws SQLException
	 */
	private void closeAll(Connection conn, ResultSet rs, Statement st, PreparedStatement ps)
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

	/** ----------------------------------------------------- 私有方法 结束 */

	public static synchronized DataTableHelper getInstance(String dbUrl, String dbUserName,
			String dbPassword, String jdbcName) {
		if (instance == null) {
			instance = new DataTableHelper(dbUrl, dbUserName, dbPassword, jdbcName);
		}
		return instance;
	}

	/**
	 * 
	 * @方法描述: 获取连接
	 * @return
	 * @throws SQLException
	 */
	public QueryRunner getQueryRunner() throws SQLException {
		return new QueryRunner(getDataSource());
	}

	/**
	 * @方法描述: 得到查询记录的条数
	 * @说明 使用ScalarHandler处理单行记录,只返回结果集第一行中的指定字段,如未指定字段,则返回第一个字段
	 * @param sql
	 *            必须为select count(*) from tableName的格式
	 * @return
	 */
	public int getCount(QueryRunner runner, String sql) {
		try {
			return runner.query(sql, new ScalarHandler<Long>()).intValue();
		} catch (SQLException sqlEx) {
			logger.error("数据查询操作异常:", sqlEx);
		}
		return 0;
	}

	/**
	 * @方法描述: 插入新数据
	 * @param sql
	 * @return
	 */
	public int insert(QueryRunner runner, String sql) {
		try {
			return runner.insert(sql, new ScalarHandler<Long>()).intValue();
		} catch (SQLException sqlEx) {
			logger.error("数据保存异常:", sqlEx);
		}
		return 0;
	}

	/**
	 * @方法描述: 得到当前数据库下所有的表名
	 * @return
	 */
	public List<String> getTableName() throws SQLException {
		List<String> tableList = Lists.newArrayList();
		metadata = getConnection().getMetaData();
		ResultSet rs = metadata.getTables(null, null, null, new String[] { "TABLE" });
		while (rs.next()) {
			/** 表所属用户名: rs.getString(2) */
			tableList.add(rs.getString(3));
		}
		return tableList;
	}

	/**
	 * @方法描述: 数据库相关属性
	 * @return
	 */
	public DataBaseInfo getDataBaseInfo() throws SQLException {
		DataBaseInfo resultMap = new DataBaseInfo();
		metadata = getConnection().getMetaData();
		resultMap.setUserName(metadata.getUserName());
		resultMap.setDataBaseUrl(metadata.getURL());
		resultMap.setReadOnly(metadata.isReadOnly());
		resultMap.setProductName(metadata.getDatabaseProductName());
		resultMap.setVersion(metadata.getDatabaseProductName());
		resultMap.setDriverName(metadata.getDriverName());
		resultMap.setDriverVersion(metadata.getDriverVersion());

		ResultSet rs = metadata.getTableTypes();
		List<String> typeInfo = Lists.newArrayList();
		while (rs.next()) {
			typeInfo.add(rs.getString(1));
		}
		resultMap.setTableTypes(typeInfo);

		return resultMap;
	}

	/**
	 * @方法描述: ResultSetMetaData接口用于获取关于ResultSet对象中列的类型和属性信息的对象
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public List<ColumnInfo> getTableInfo(String tableName) throws SQLException {
		List<ColumnInfo> resList = null;
		// 如果所要查询的表没有在数据库中表集合中，直接返回null
		if (ListUtilHelper.indexOf(getTableName(), tableName) < 0) {
			return null;
		}
		if (StringUtils.isNotBlank(tableName)) {
			resList = new ArrayList<ColumnInfo>();
			ColumnInfo info = null;
			PreparedStatement ps = null;
			ResultSet rs = null;

			String sql = "select * from " + tableName;
			ps = getConnection().prepareStatement(sql);
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
		}
		return resList;
	}

	/**
	 * @方法描述:
	 *        <ul>
	 *        <li>根据已经存在SQL文件初始化数据库</li>
	 *        <li>从SQL文件中读取SQL语句，每行一条，末尾没有分号</li>
	 *        </ul>
	 * @param rootPath
	 * 
	 */
	public void initDataBase(String rootPath) {
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
		} catch (Exception exp) {
			logger.error("数据异常:", exp);
		} finally {
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(in);
		}
	}

	public static void main(String[] args) throws SQLException {

	}
}

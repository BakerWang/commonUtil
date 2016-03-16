package com.cdeledu.database;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

public class TxQueryRunner extends QueryRunner {

	public int[] batcn(String sql, Object[][] params) throws SQLException {
		Connection conn = JdbcTransactionHelper.getConnection();
		int[] result = super.batch(conn, sql, params);
		return result;
	}

	public <T> T findForJdbcParam(String sql, ResultSetHandler<T> rsh,
			int page, int rows, Object... params) throws SQLException {
		Connection conn = JdbcTransactionHelper.getConnection();
		sql = JdbcDaoTemplate.jeecgCreatePageSql(sql, page, rows);
		T result = super.query(sql, rsh, params);
		JdbcTransactionHelper.releaseConnection(conn);
		return result;
	}

	public <T> T findObjForJdbc(String sql, ResultSetHandler<T> rsh, int page,
			int rows) throws SQLException {
		sql = JdbcDaoTemplate.jeecgCreatePageSql(sql, page, rows);
		Connection conn = JdbcTransactionHelper.getConnection();
		T result = super.query(sql, rsh);
		JdbcTransactionHelper.releaseConnection(conn);
		return result;
	}

	public int update(String sql) throws SQLException {
		Connection conn = JdbcTransactionHelper.getConnection();
		int result = super.update(conn, sql);
		JdbcTransactionHelper.releaseConnection(conn);
		return result;
	}

	public int update(String sql, Object params) throws SQLException {
		Connection conn = JdbcTransactionHelper.getConnection();
		int result = super.update(conn, sql, params);
		JdbcTransactionHelper.releaseConnection(conn);
		return result;
	}

	public int update(String sql, Object... params) throws SQLException {
		Connection conn = JdbcTransactionHelper.getConnection();
		int result = super.update(conn, sql, params);
		JdbcTransactionHelper.releaseConnection(conn);
		return result;
	}
}

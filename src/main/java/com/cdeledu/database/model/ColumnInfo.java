package com.cdeledu.database.model;

import java.io.Serializable;

/**
 * @类描述: 数据库表的列信息
 * @创建者: 独泪了无痕
 * @创建日期: 2016年1月20日 下午10:45:29
 * @版本: V1.0
 * @since: JDK 1.7
 */
public class ColumnInfo implements Serializable {
	/** ----------------------------------------------------- Fields start */
	private static final long serialVersionUID = 1L;
	// 表名
	private String tableName;
	// 列名称
	private String columnName;
	// 列类型（DB）
	private String columnTypeName;
	// 长度
	private int precision;
	// 是否自动编号
	private boolean isAutoIncrement;
	// 是否可以为空
	private boolean isNullable;
	// 是否可以写入
	private boolean isReadOnly;

	/** ----------------------------------------------------- Fields end */
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnTypeName() {
		return columnTypeName;
	}

	public void setColumnTypeName(String columnTypeName) {
		this.columnTypeName = columnTypeName;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public boolean isAutoIncrement() {
		return isAutoIncrement;
	}

	public void setAutoIncrement(boolean isAutoIncrement) {
		this.isAutoIncrement = isAutoIncrement;
	}

	public boolean isNullable() {
		return isNullable;
	}

	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}

	public boolean isReadOnly() {
		return isReadOnly;
	}

	public void setReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

	@Override
	public String toString() {
		return "ColumnInfo [tableName=" + tableName + ", columnName="
				+ columnName + ", columnTypeName=" + columnTypeName
				+ ", precision=" + precision + ", isAutoIncrement="
				+ isAutoIncrement + ", isNullable=" + isNullable
				+ ", isReadOnly=" + isReadOnly + "]";
	}
}

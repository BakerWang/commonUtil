package com.cdeledu.database.model;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @ClassName: DataBaseInfo
 * @Description: 数据库基础信息
 * @author: 独泪了无痕
 * @date: 2015-8-21 下午11:54:06
 * @version: V1.0
 */
public class DataBaseInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	// 数据库已知的用户
	private String userName;
	// 数据库URL
	private String dataBaseUrl;
	// 是否允许只读
	private boolean isReadOnly;
	// 数据库的产品名称
	private String productName;
	// 数据库的版本
	private String version;
	// 驱动程序的名称
	private String driverName;
	// 驱动程序的版本
	private String driverVersion;
	// 数据库中使用的表类型
	private List<String> tableTypes;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDataBaseUrl() {
		return dataBaseUrl;
	}

	public void setDataBaseUrl(String dataBaseUrl) {
		this.dataBaseUrl = dataBaseUrl;
	}

	public boolean isReadOnly() {
		return isReadOnly;
	}

	public void setReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverVersion() {
		return driverVersion;
	}

	public void setDriverVersion(String driverVersion) {
		this.driverVersion = driverVersion;
	}

	public List<String> getTableTypes() {
		return tableTypes;
	}

	public void setTableTypes(List<String> tableTypes) {
		this.tableTypes = tableTypes;
	}

	@Override
	public String toString() {
		return "DataBaseInfo [userName=" + userName + ", dataBaseUrl="
				+ dataBaseUrl + ", isReadOnly=" + isReadOnly + ", productName="
				+ productName + ", version=" + version + ", driverName="
				+ driverName + ", driverVersion=" + driverVersion
				+ ", tableTypes=" + tableTypes + "]";
	}
}

package com.cdeledu.system.model;

/**
 * @类描述: 内存信息封装.单位为MF
 * @创建者: 独泪了无痕
 * @创建日期: 2016年1月23日 上午12:48:13
 * @版本: V1.0
 * @since: JDK 1.7
 */
public class MemInfo {
	private int total;
	private int used;
	private int free;

	public MemInfo(int total, int used, int free) {
		this.total = total;
		this.used = used;
		this.free = free;
	}

	/**
	 * 返回使用百分比
	 * 
	 * @return String 例子：98.03
	 */
	public double getUsage() {
		return ((double) used / (double) total) * 100;
	}

	/**
	 * 返回未使用百分比
	 * 
	 * @return String 例子：98.03
	 */
	public double getFree() {
		return ((double) free / (double) total) * 100;
	}
}

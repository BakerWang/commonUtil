package com.cdeledu.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * @title : Executor
 * 
 * @author : 独泪了无痕
 * 
 * @方法描述 : 线程池工具
 * 
 */
public class Executor {
	private static ExecutorService executor = Executors.newCachedThreadPool();

	/**
	 * 
	 * @方法名称: newExecutor
	 * @方法描述: 新建一个线程池
	 * 
	 * @param threadSize
	 *            同时执行的线程数大小
	 * @return
	 */
	public static ExecutorService newExecutor(int threadSize) {
		return Executors.newFixedThreadPool(threadSize);
	}

	/**
	 * 
	 * @方法名称: newExecutor
	 * @方法描述: 获得一个新的线程池
	 * 
	 * @return
	 */
	public static ExecutorService newExecutor() {
		return Executors.newCachedThreadPool();
	}

	/**
	 * 
	 * @方法名称: restart
	 * @方法描述: 重启公共线程池
	 *
	 */
	public static void restart() {
		executor.shutdownNow();
		executor = Executors.newCachedThreadPool();
	}
}

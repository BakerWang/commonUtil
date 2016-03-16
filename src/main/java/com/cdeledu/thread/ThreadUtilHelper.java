package com.cdeledu.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.cdeledu.exception.RuntimeExceptionHelper;

/**
 * @类描述: 线程池工具
 * @创建者: 独泪了无痕
 * @创建日期: 2016年1月27日 下午11:32:18
 * @版本: V1.0
 * @since: JDK 1.7
 */
public class ThreadUtilHelper {
	/** ----------------------------------------------------- Fields start */
	private static ExecutorService executor = Executors.newCachedThreadPool();

	/** ----------------------------------------------------- Fields end */
	/**
	 * @方法:直接在公共线程池中执行线程
	 * @创建人:独泪了无痕
	 * @param runnable
	 */
	public static void execute(Runnable runnable) {
		try {
			executor.execute(runnable);
		} catch (Exception e) {
			throw new RuntimeExceptionHelper("Exception when running task!", e);
		}
	}

	/**
	 * 重启公共线程池
	 */
	public static void restart() {
		executor.shutdownNow();
		executor = Executors.newCachedThreadPool();
	}

	/**
	 * @方法:新建一个线程池
	 * @创建人:独泪了无痕
	 * @param threadSize
	 * @return
	 */
	public static ExecutorService newExecutor(int threadSize) {
		return Executors.newFixedThreadPool(threadSize);
	}

	/**
	 * @方法:获得一个新的线程池
	 * @创建人:独泪了无痕
	 * @return
	 */
	public static ExecutorService newExecutor() {
		return Executors.newCachedThreadPool();
	}

	/**
	 * @方法:获得一个新的线程池，只有单个线程
	 * @创建人:独泪了无痕
	 * @return
	 */
	public static ExecutorService newSingleExecutor() {
		return Executors.newSingleThreadExecutor();
	}

	/**
	 * @方法: 挂起当前线程
	 * @创建人:独泪了无痕
	 * @param millis
	 *            挂起的毫秒数
	 * @return 被中断返回false，否则true
	 */
	public static boolean sleep(Long millis) {
		if (millis == null || millis <= 0) {
			return true;
		}

		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}
}

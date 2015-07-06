package com.game.server.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

public class MyBatisFactory {

	public static final Logger logger = Logger.getLogger(MyBatisFactory.class);
	// public static String mybatisConfig = "mybatis-config.xml";
	private SqlSessionFactory sqlSessionFactory = null;

	private ExecutorService executor = Executors
			.newSingleThreadExecutor(new ThreadFactory() {

				@Override
				public Thread newThread(Runnable r) {
					return new Thread(r, "DB-Thread");
				}
			});

	/**
	 * 单例
	 */
	private static MyBatisFactory instance = new MyBatisFactory();

	private MyBatisFactory() {
	}

	public static MyBatisFactory getInstance() {
		return instance;
	}

	public void init(String mybatisConfig) {
		try {
			InputStream ios = Resources.getResourceAsStream(mybatisConfig);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(ios);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("加载mybatis失败");
		}
	}

	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	public synchronized void execute(Runnable task) {
		executor.execute(task);
	}

	public void stop() {
		executor.shutdown();
		try {
			if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
				executor.shutdownNow();
				logger.info("[数据库异步线程]等待60秒关闭");
				if (!executor.awaitTermination(60, TimeUnit.SECONDS))
					logger.error("[数据库异步线程]Pool did not terminate");
			}
		} catch (InterruptedException ie) {
			executor.shutdownNow();
			Thread.currentThread().interrupt();
			logger.info("[数据库异步线程]异常");
		}
		logger.info("[数据库异步线程]关闭成功~~~");
	}

}

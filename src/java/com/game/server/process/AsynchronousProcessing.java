package com.game.server.process;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;

import com.game.server.util.GameUtil;

/**
 * 异步处理
 * 
 * @author nullzZ
 * 
 */
public abstract class AsynchronousProcessing {

	private static final Logger logger = Logger.getLogger("server");
	private int workerExecutorCount = Runtime.getRuntime()
			.availableProcessors() * 2;
	private final Map<Channel, Integer> messageServerThreads = new ConcurrentHashMap<>();
	private ExecutorService[] executorServices;

	public AsynchronousProcessing() {
		executorServices = new ExecutorService[workerExecutorCount];
		for (int i = 0; i < workerExecutorCount; i++) {
			executorServices[i] = Executors
					.newSingleThreadExecutor(new ThreadFactory() {

						@Override
						public Thread newThread(Runnable r) {
							return new Thread(r, "msg-pross-thread");
						}
					});
		}
	}

	public void excute(Channel channel, byte[] msgBytes) {
		MessageTask task = new MessageTask(channel, msgBytes, this);
		ExecutorService executorService = null;
		if (messageServerThreads.containsKey(task.getChannel())) {
			int id = messageServerThreads.get(task.getChannel());
			executorService = executorServices[id];
		} else {
			int id = GameUtil.getRangedRandom(0, workerExecutorCount - 1);
			executorService = executorServices[id];
			messageServerThreads.put(task.getChannel(), id);
		}

		if (executorService != null) {
			executorService.execute(task);
		} else {
			logger.error("消息处理]线程池为空");
		}
	}

	public abstract void handler(Channel channel, byte[] msgBytes);

	public void stop() {
		for (int i = 0; i < workerExecutorCount; i++) {
			executorServices[i].shutdownNow();
			// executorServices[i].shutdown();
			// try {
			// if (!executorServices[i].awaitTermination(60, TimeUnit.SECONDS))
			// {
			// executorServices[i].shutdownNow();
			// if (!executorServices[i].awaitTermination(60,
			// TimeUnit.SECONDS))
			// System.err.println("Pool did not terminate");
			// }
			// } catch (InterruptedException ie) {
			// executorServices[i].shutdownNow();
			// Thread.currentThread().interrupt();
			// }
		}
		logger.info("关闭异步消息处理线程池 ");
	}

	public void deleteChannle2Thread(Channel channel) {
		messageServerThreads.remove(channel);
	}

	public Map<Channel, Integer> getMessageServerThreads() {
		return messageServerThreads;
	}
}

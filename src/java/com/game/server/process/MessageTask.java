package com.game.server.process;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;

public class MessageTask implements Runnable {

	private Channel channel;
	private byte[] msgBytes;
	private static final Logger logger = Logger.getLogger("server");
	private AsynchronousProcessing asynchronousProcessing;

	public MessageTask(Channel channel, byte[] msgBytes,
			AsynchronousProcessing asynchronousProcessing) {
		this.channel = channel;
		this.msgBytes = msgBytes;
		this.asynchronousProcessing = asynchronousProcessing;
	}

	@Override
	public void run() {
		try {
			long now = System.currentTimeMillis();
			asynchronousProcessing.handler(channel, msgBytes);
			logger.debug("消息处理:" + "|操作时间:"
					+ (System.currentTimeMillis() - now));
		} catch (Exception e) {
			logger.error("消息异常:", e);
			e.printStackTrace();
		}
	}

	public Channel getChannel() {
		return channel;
	}

}

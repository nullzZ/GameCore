package com.game.server.net.http;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

public abstract class HttpServer {

	private static final Logger logger = Logger.getLogger(HttpServer.class);
	private SimpleChannelHandler serverHandler;
	private static final int BUFFER_SIZE_RECEIVER = 64 * 1024;
	private static final int BUFFER_SIZE_SEND = 128 * 1024;
	private ChannelGroup channelGroup = new DefaultChannelGroup();
	private ServerBootstrap bootstrap;
	private String ip;
	private short port;
	private int serverId;
	private String name;

	/**
	 * 
	 * @param configFileName
	 *            配置文件
	 * @param workerExecutorCount
	 *            工作线程数
	 * @param serverHandler
	 *            消息处理类
	 * @param actionManager
	 *            action管理类（用sring加载）
	 */
	public HttpServer(String ip, short port, SimpleChannelHandler serverHandler) {
		this.serverHandler = serverHandler;
		this.port = port;
		this.ip = ip;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 启动
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception {
		NioServerSocketChannelFactory nsscf = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());// channel工厂�?
		bootstrap = new ServerBootstrap(nsscf);// 服务器端channel的辅助类 
		bootstrap.setOption("reuseAddress", true);
		bootstrap.setOption("receiveBufferSize", BUFFER_SIZE_RECEIVER);
		bootstrap.setOption("sendBufferSize", BUFFER_SIZE_SEND);
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);

		ChannelPipelineFactory cp = new ChannelPipelineFactory() {

			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
				pipeline.addLast("decoder", new HttpRequestDecoder());
				pipeline.addLast("encoder", new HttpResponseEncoder());
				pipeline.addLast("handler", serverHandler);
				return pipeline;
			}
		};
		bootstrap.setPipeline(cp.getPipeline());
		Channel serverChannel = bootstrap.bind(new InetSocketAddress(port));// 添加到channel组里
		channelGroup.add(serverChannel);

		logger.debug("[启动http服务器]name:" + name + "|serverId:" + serverId
				+ "|ip:" + ip + "|port:" + port);
	}

	public void stop() {
		channelGroup.close().awaitUninterruptibly();
		bootstrap.releaseExternalResources();// 释放资源
		logger.info("http释放监听端口 " + port);
	}

}

package com.game.server.net;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.game.server.net.coder.Decoder;
import com.game.server.net.coder.Encoder;
import com.google.protobuf.AbstractMessageLite.Builder;

public abstract class Client {
	private static final Logger logger = Logger.getLogger(Client.class);
	private SimpleChannelHandler clientHandler;
	private static final int BUFFER_SIZE_RECEIVER = 64 * 1024;
	private static final int BUFFER_SIZE_SEND = 128 * 1024;
	private short port;
	private String ip;
	private Channel clientChannel;
	private ClientBootstrap bootstrap;

	public Client(String ip, short port, SimpleChannelHandler clientHandler) {
		this.ip = ip;
		this.port = port;
		this.clientHandler = clientHandler;
	}

	public void connet() throws Exception {
		NioClientSocketChannelFactory ncscf = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());
		bootstrap = new ClientBootstrap(ncscf);
		bootstrap.setOption("reuseAddress", true);
		bootstrap.setOption("receiveBufferSize", BUFFER_SIZE_RECEIVER);
		bootstrap.setOption("sendBufferSize", BUFFER_SIZE_SEND);
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);

		ChannelPipelineFactory cp = new ChannelPipelineFactory() {

			@Override
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
				pipeline.addLast("decoder", new Decoder());
				pipeline.addLast("handler", clientHandler);
				pipeline.addLast("encoder", new Encoder());

				return pipeline;
			}

		};
		bootstrap.setPipeline(cp.getPipeline());
		ChannelFuture cf = bootstrap.connect(new InetSocketAddress(ip, port));
		clientChannel = cf.getChannel();

	}

	public void stop() {
		clientChannel.close();
		bootstrap.releaseExternalResources();
		logger.info("释放监听端口 " + port);
	}

	public Channel getClientChannel() {
		return clientChannel;
	}

	@SuppressWarnings("rawtypes")
	public void write(Builder message) {
		clientChannel.write(message);
	}

}

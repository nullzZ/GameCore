package com.game.server.action;

import org.jboss.netty.channel.Channel;

import com.google.protobuf.GeneratedMessageLite;

public abstract class ChannelAction<T extends GeneratedMessageLite> extends
		BaseAction<Channel, T> {

	@Override
	public abstract void excute(Channel channel, T requestMessage);

}

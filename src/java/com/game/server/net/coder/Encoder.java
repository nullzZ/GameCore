package com.game.server.net.coder;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.GeneratedMessageLite.Builder;

public class Encoder extends OneToOneEncoder {

	public static final Logger logger = Logger.getLogger(Encoder.class);

	@SuppressWarnings("rawtypes")
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {
		if (!(msg instanceof Builder)) {
			logger.error("[编码]消息体格式不符!!!");
			return null;
		}

		GeneratedMessageLite builderMsg = (GeneratedMessageLite) ((Builder) msg)
				.build();
		byte[] data = builderMsg.toByteArray();
		int dataLenght = data.length;
		ChannelBuffer buff = ChannelBuffers.dynamicBuffer();// 初始化buff
		// buff.writeInt(dataLenght);
		buff.writeBytes(intToByte(dataLenght, 4));
		// buff.writeBytes(bytesReverseOrder(data));
		buff.writeBytes(data);
		logger.debug("[sendByteSize]=====ThreadId:"
				+ Thread.currentThread().getId() + "|dataLenght:" + dataLenght);
		return buff;
	}

	/**
	 * 将 byte数组中的元素倒序排列
	 */
	public static byte[] bytesReverseOrder(byte[] b) {
		int length = b.length;
		byte[] result = new byte[length];
		for (int i = 0; i < length; i++) {
			result[length - i - 1] = b[i];
		}
		return result;
	}

	public static byte[] intToByte(int i, int len) {
		byte[] abyte = null;
		if (len == 1) {
			abyte = new byte[len];
			abyte[0] = (byte) (0xff & i);
		} else {
			abyte = new byte[len];
			abyte[0] = (byte) (0xff & i);
			abyte[1] = (byte) ((0xff00 & i) >> 8);
			abyte[2] = (byte) ((0xff0000 & i) >> 16);
			abyte[3] = (byte) ((0xff000000 & i) >> 24);
		}
		return abyte;
	}

	/**
	 * 小端转大端
	 * 
	 * @param i
	 * @param len
	 * @return
	 */
	// public static byte[] intToByte(int i, int len) {
	// byte[] abyte = null;
	// if (len == 1) {
	// abyte = new byte[len];
	// abyte[0] = (byte) (0xff & i);
	// } else {
	// abyte = new byte[len];
	// abyte[0] = (byte) ((i >>> 24) & 0xff);
	// abyte[1] = (byte) ((i >>> 16) & 0xff);
	// abyte[2] = (byte) ((i >>> 8) & 0xff);
	// abyte[3] = (byte) (i & 0xff);
	// }
	// return abyte;
	// }

}

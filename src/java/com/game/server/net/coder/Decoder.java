package com.game.server.net.coder;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

public class Decoder extends FrameDecoder {

	private static final Logger logger = Logger.getLogger(Decoder.class);
	private final static int MAX_MESSAGEBODE_LENGTH = 4 * 1024;

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		if (logger.isDebugEnabled()) {
			// logger.info("[解码][channelId(" + channel.getId()
			// + ")] [receiveBytes:" + buffer + "] ["
			// + ChannelBuffers.hexDump(buffer) + "]");
		}

		// Wait until the length prefix is available.
		int readableBytes = buffer.readableBytes();
		if (readableBytes < 4) {
			// logger.info("[解码][channelId(" + channel.getId() +
			// ")] [dataLength("
			// + readableBytes + ")<4]");
			return null;
		}

		buffer.markReaderIndex();

		// Wait until the whole data is available.
		byte[] b = new byte[4];
		buffer.readBytes(b);
		int dataLength = bytesToInt(b);

		if (readableBytes < dataLength) {
			buffer.resetReaderIndex();
			if (logger.isDebugEnabled()) {
				// logger.info("[解码][channelId(" + channel.getId()
				// + ")] [dataLength(" + readableBytes + ")<expectLength("
				// + (dataLength) + ")]");
			}
			return null;
		}

		if (dataLength >= MAX_MESSAGEBODE_LENGTH) {
			logger.error("[解码][channelId(" + channel.getId()
					+ ")] [closeSession] [dataLength(" + dataLength + ")>="
					+ MAX_MESSAGEBODE_LENGTH + "]");
			channel.close();
			return null;
		}

		// There's enough bytes in the buffer. Read it.
		ChannelBuffer frame = buffer.readBytes(dataLength);
		// Successfully decoded a frame. Return the decoded frame.
		return frame;
	}

	public static int bytesToInt(byte[] bytes) {
		int addr = 0;
		if (bytes.length == 1) {
			addr = bytes[0] & 0xFF;
		} else {
			addr = bytes[0] & 0xFF;
			addr |= ((bytes[1] << 8) & 0xFF00);
			addr |= ((bytes[2] << 16) & 0xFF0000);
			addr |= ((bytes[3] << 24) & 0xFF000000);
		}
		return addr;
	}

}

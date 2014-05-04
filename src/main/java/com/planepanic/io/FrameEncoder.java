package com.planepanic.io;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * A class for encoding frames to a stream of bytes.
 * @author Jonathan
 *
 */
public final class FrameEncoder extends MessageToByteEncoder<Frame> {
	@Override
	protected void encode(ChannelHandlerContext ctx, Frame frame, ByteBuf buf) throws Exception {
		buf.writeInt(frame.getData().readableBytes());
		buf.writeByte(FrameDecoder.VERSION);
		buf.writeByte(frame.getPacketId());
		buf.writeBytes(frame.getData());
	}

}

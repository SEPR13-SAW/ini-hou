package com.planepanic.io;

import com.planepanic.io.packet.Packet;
import com.planepanic.io.packet.decoder.Decoder;
import com.planepanic.io.packet.handler.Handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * A class for handling frames and calling packet decoding / handling.
 * @author Jonathan
 *
 */
public class FrameHandler extends ChannelInboundHandlerAdapter {
	private final Object attachment;

	public FrameHandler(Object attachment) {
		this.attachment = attachment;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		Frame frame = (Frame) msg;
		int packetId = frame.getPacketId() & 0xFF;

		Decoder<?> decoder = Decoder.get(packetId);
		Packet packet = (Packet) decoder.decode(frame.getData());

		try {
			Handler<?, ?> handler = Handler.get(packetId);
			if (handler == null) {
				System.err.println("No handler for frame " + packetId);
				return;
			}
			handler.handleGeneric(packet, attachment);
		} catch (Exception e) {
			e.printStackTrace();
			ctx.close();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}

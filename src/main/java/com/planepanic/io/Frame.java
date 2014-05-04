package com.planepanic.io;

import io.netty.buffer.ByteBuf;

/**
 * A frame for wrapping packets before being sent/received.
 * @author Jonathan
 *
 */
public final class Frame {
	private final int packetId;
	private final ByteBuf data;

	public Frame(int packetId, ByteBuf data) {
		this.packetId = packetId;
		this.data = data;
	}

	public int getPacketId() {
		return packetId;
	}

	public ByteBuf getData() {
		return data;
	}
}

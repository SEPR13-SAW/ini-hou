package com.planepanic.io.packet.encoder;

import com.planepanic.io.ByteBufUtils;
import com.planepanic.io.packet.JoinGamePacket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public final class JoinGamePacketEncoder extends Encoder<JoinGamePacket> {
	public JoinGamePacketEncoder() {
		super(JoinGamePacket.ID);
	}

	@Override
	public ByteBuf encode(ByteBufAllocator alloc, JoinGamePacket packet) {
		ByteBuf buf = alloc.buffer();

		ByteBufUtils.writeString(buf, packet.getName());

		return buf;
	}
}

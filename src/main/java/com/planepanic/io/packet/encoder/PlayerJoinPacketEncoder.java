package com.planepanic.io.packet.encoder;

import com.planepanic.io.ByteBufUtils;
import com.planepanic.io.packet.PlayerJoinPacket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public final class PlayerJoinPacketEncoder extends Encoder<PlayerJoinPacket> {
	public PlayerJoinPacketEncoder() {
		super(0x80);
	}

	@Override
	public ByteBuf encode(ByteBufAllocator alloc, PlayerJoinPacket packet) {
		ByteBuf buf = alloc.buffer();

		buf.writeByte(packet.getPlayerId());
		ByteBufUtils.writeString(buf, packet.getName());

		return buf;
	}

}

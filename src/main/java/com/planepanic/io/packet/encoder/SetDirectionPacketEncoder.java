package com.planepanic.io.packet.encoder;

import com.planepanic.io.packet.SetDirectionPacket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public final class SetDirectionPacketEncoder extends Encoder<SetDirectionPacket> {
	public SetDirectionPacketEncoder() {
		super(SetDirectionPacket.ID);
	}

	@Override
	public ByteBuf encode(ByteBufAllocator alloc, SetDirectionPacket packet) {
		ByteBuf buf = alloc.buffer();

		buf.writeInt(packet.getPlaneId());
		buf.writeFloat(packet.getDirection());

		return buf;
	}
}

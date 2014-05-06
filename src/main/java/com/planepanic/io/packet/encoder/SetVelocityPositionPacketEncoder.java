package com.planepanic.io.packet.encoder;

import com.planepanic.io.packet.SetVelocityPositionPacket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public final class SetVelocityPositionPacketEncoder extends Encoder<SetVelocityPositionPacket> {
	public SetVelocityPositionPacketEncoder() {
		super(SetVelocityPositionPacket.ID);
	}

	@Override
	public ByteBuf encode(ByteBufAllocator alloc, SetVelocityPositionPacket packet) {
		ByteBuf buf = alloc.buffer();

		buf.writeInt(packet.getPlaneId());
		buf.writeFloat(packet.getVelocity());

		buf.writeFloat(packet.getX());
		buf.writeFloat(packet.getY());

		return buf;
	}

}

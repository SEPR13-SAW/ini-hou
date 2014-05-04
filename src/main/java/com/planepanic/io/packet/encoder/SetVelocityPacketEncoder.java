package com.planepanic.io.packet.encoder;

import com.planepanic.io.packet.SetVelocityPacket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public final class SetVelocityPacketEncoder extends Encoder<SetVelocityPacket> {
	public SetVelocityPacketEncoder() {
		super(0x01);
	}

	@Override
	public ByteBuf encode(ByteBufAllocator alloc, SetVelocityPacket packet) {
		ByteBuf buf = alloc.buffer();

		buf.writeInt(packet.getPlaneId());
		buf.writeInt(packet.getVelocity());

		return buf;
	}

}

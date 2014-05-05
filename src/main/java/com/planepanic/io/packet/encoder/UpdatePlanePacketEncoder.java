package com.planepanic.io.packet.encoder;

import com.planepanic.io.packet.UpdatePlanePacket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public final class UpdatePlanePacketEncoder extends Encoder<UpdatePlanePacket> {
	public UpdatePlanePacketEncoder() {
		super(0x82);
	}

	@Override
	public ByteBuf encode(ByteBufAllocator alloc, UpdatePlanePacket packet) {
		ByteBuf buf = alloc.buffer();

		buf.writeInt(packet.getPlaneId());
		buf.writeFloat(packet.getX());
		buf.writeFloat(packet.getY());
		buf.writeFloat(packet.getDirection());
		buf.writeInt(packet.getAltitude());
		buf.writeFloat(packet.getVelocity());

		return buf;
	}

}

package com.planepanic.io.packet.encoder;

import com.planepanic.io.packet.LandPlanePacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public final class LandPlanePacketEncoder extends Encoder<LandPlanePacket> {
	public LandPlanePacketEncoder() {
		super(LandPlanePacket.ID);
	}

	@Override
	public ByteBuf encode(ByteBufAllocator alloc, LandPlanePacket packet) {
		ByteBuf buf = alloc.buffer();

		buf.writeInt(packet.getPlaneId());

		return buf;
	}
}

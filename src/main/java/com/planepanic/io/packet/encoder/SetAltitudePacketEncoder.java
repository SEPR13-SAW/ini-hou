package com.planepanic.io.packet.encoder;

import com.planepanic.io.packet.SetAltitudePacket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public final class SetAltitudePacketEncoder extends Encoder<SetAltitudePacket> {
	public SetAltitudePacketEncoder() {
		super(0x03);
	}

	@Override
	public ByteBuf encode(ByteBufAllocator alloc, SetAltitudePacket packet) {
		ByteBuf buf = alloc.buffer();

		buf.writeInt(packet.getPlaneId());
		buf.writeFloat(packet.getAltitude());

		return buf;
	}

}

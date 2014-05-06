package com.planepanic.io.packet.decoder;

import com.planepanic.io.packet.LandPlanePacket;
import io.netty.buffer.ByteBuf;

public final class LandPlanePacketDecoder extends Decoder<LandPlanePacket> {
	public LandPlanePacketDecoder() {
		super(LandPlanePacket.ID);
	}

	@Override
	public LandPlanePacket decode(ByteBuf buffer) {
		int planeId = buffer.readInt();

		return new LandPlanePacket(planeId);
	}

}

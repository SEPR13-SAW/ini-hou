package com.planepanic.io.packet.decoder;

import com.planepanic.io.packet.SetAltitudePacket;

import io.netty.buffer.ByteBuf;

public final class SetAltitudePacketDecoder extends Decoder<SetAltitudePacket> {
	public SetAltitudePacketDecoder() {
		super(0x01);
	}

	@Override
	public SetAltitudePacket decode(ByteBuf buffer) {
		int planeId = buffer.readInt();
		int altitude = buffer.readInt();

		return new SetAltitudePacket(planeId, altitude);
	}

}

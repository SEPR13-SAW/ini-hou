package com.planepanic.io.packet.decoder;

import com.planepanic.io.packet.SetAltitudePacket;

import io.netty.buffer.ByteBuf;

public final class SetAltitudePacketDecoder extends Decoder<SetAltitudePacket> {
	public SetAltitudePacketDecoder() {
		super(SetAltitudePacket.ID);
	}

	@Override
	public SetAltitudePacket decode(ByteBuf buffer) {
		int planeId = buffer.readInt();
		float altitude = buffer.readFloat();

		return new SetAltitudePacket(planeId, altitude);
	}

}

package com.planepanic.io.packet.decoder;

import com.planepanic.io.packet.SetVelocityPacket;

import io.netty.buffer.ByteBuf;

public final class SetVelocityPacketDecoder extends Decoder<SetVelocityPacket> {
	public SetVelocityPacketDecoder() {
		super(0x03);
	}

	@Override
	public SetVelocityPacket decode(ByteBuf buffer) {
		int planeId = buffer.readInt();
		int velocity = buffer.readInt();

		return new SetVelocityPacket(planeId, velocity);
	}

}

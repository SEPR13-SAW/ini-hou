package com.planepanic.io.packet.decoder;

import com.planepanic.io.packet.SetVelocityPositionPacket;

import io.netty.buffer.ByteBuf;

public final class SetVelocityPositionPacketDecoder extends Decoder<SetVelocityPositionPacket> {
	public SetVelocityPositionPacketDecoder() {
		super(0x03);
	}

	@Override
	public SetVelocityPositionPacket decode(ByteBuf buffer) {
		int planeId = buffer.readInt();
		float velocity = buffer.readFloat();
		float x = buffer.readFloat();
		float y = buffer.readFloat();

		return new SetVelocityPositionPacket(planeId, velocity, x, y);
	}

}

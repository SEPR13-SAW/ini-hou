package com.planepanic.io.packet.decoder;

import com.planepanic.io.packet.SetDirectionPacket;

import io.netty.buffer.ByteBuf;

public final class SetDirectionPacketDecoder extends Decoder<SetDirectionPacket> {
	public SetDirectionPacketDecoder() {
		super(SetDirectionPacket.ID);
	}

	@Override
	public SetDirectionPacket decode(ByteBuf buffer) {
		int planeId = buffer.readInt();
		float direction = buffer.readFloat();

		return new SetDirectionPacket(planeId, direction);
	}

}

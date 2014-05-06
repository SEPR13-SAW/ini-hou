package com.planepanic.io.packet.decoder;

import com.planepanic.io.ByteBufUtils;
import com.planepanic.io.packet.JoinGamePacket;

import io.netty.buffer.ByteBuf;

public final class JoinGamePacketDecoder extends Decoder<JoinGamePacket> {
	public JoinGamePacketDecoder() {
		super(JoinGamePacket.ID);
	}

	@Override
	public JoinGamePacket decode(ByteBuf buffer) {
		String name = ByteBufUtils.readString(buffer);

		return new JoinGamePacket(name);
	}

}

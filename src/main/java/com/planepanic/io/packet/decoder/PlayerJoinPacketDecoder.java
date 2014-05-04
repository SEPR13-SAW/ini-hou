package com.planepanic.io.packet.decoder;

import com.planepanic.io.ByteBufUtils;
import com.planepanic.io.packet.PlayerJoinPacket;

import io.netty.buffer.ByteBuf;

public final class PlayerJoinPacketDecoder extends Decoder<PlayerJoinPacket> {
	public PlayerJoinPacketDecoder() {
		super(0x80);
	}

	@Override
	public PlayerJoinPacket decode(ByteBuf buffer) {
		byte playerId = buffer.readByte();
		String name = ByteBufUtils.readString(buffer);

		return new PlayerJoinPacket(playerId, name);
	}

}

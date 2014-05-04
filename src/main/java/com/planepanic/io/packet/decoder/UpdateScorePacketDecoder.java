package com.planepanic.io.packet.decoder;

import com.planepanic.io.packet.UpdateScorePacket;

import io.netty.buffer.ByteBuf;

public final class UpdateScorePacketDecoder extends Decoder<UpdateScorePacket> {
	public UpdateScorePacketDecoder() {
		super(0x83);
	}

	@Override
	public UpdateScorePacket decode(ByteBuf buffer) {
		byte playerId = buffer.readByte();
		int score = buffer.readInt();

		return new UpdateScorePacket(playerId, score);
	}

}

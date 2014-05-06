package com.planepanic.io.packet.decoder;

import com.planepanic.io.packet.EndGamePacket;
import io.netty.buffer.ByteBuf;

public final class EndGamePacketDecoder extends Decoder<EndGamePacket> {
	public EndGamePacketDecoder() {
		super(EndGamePacket.ID);
	}

	@Override
	public EndGamePacket decode(ByteBuf buffer) {
		int score = buffer.readInt();
		boolean win = buffer.readByte() == 1;

		return new EndGamePacket(score, win);
	}

}

package com.planepanic.io.packet.encoder;

import com.planepanic.io.packet.EndGamePacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public final class EndGamePacketEncoder extends Encoder<EndGamePacket> {
	public EndGamePacketEncoder() {
		super(EndGamePacket.ID);
	}

	@Override
	public ByteBuf encode(ByteBufAllocator alloc, EndGamePacket packet) {
		ByteBuf buf = alloc.buffer();

		buf.writeInt(packet.getScore());
		buf.writeByte(packet.isWin()? 1 : 0);

		return buf;
	}

}

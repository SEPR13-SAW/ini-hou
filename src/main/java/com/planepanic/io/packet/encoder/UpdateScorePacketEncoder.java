package seprini.network.packet.codec.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import seprini.network.packet.UpdateScorePacket;

public final class UpdateScorePacketEncoder extends Encoder<UpdateScorePacket> {
	public UpdateScorePacketEncoder() {
		super(0x83);
	}

	@Override
	public ByteBuf encode(ByteBufAllocator alloc, UpdateScorePacket packet) {
		ByteBuf buf = alloc.buffer();

		buf.writeByte(packet.getPlayerId());
		buf.writeInt(packet.getScore());

		return buf;
	}

}

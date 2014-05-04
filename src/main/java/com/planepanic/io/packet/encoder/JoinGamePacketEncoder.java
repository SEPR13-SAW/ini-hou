package seprini.network.packet.codec.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import seprini.network.ByteBufUtils;
import seprini.network.packet.JoinGamePacket;

public final class JoinGamePacketEncoder extends Encoder<JoinGamePacket> {
	public JoinGamePacketEncoder() {
		super(0x00);
	}

	@Override
	public ByteBuf encode(ByteBufAllocator alloc, JoinGamePacket packet) {
		ByteBuf buf = alloc.buffer();

		ByteBufUtils.writeString(buf, packet.getName());

		return buf;
	}
}

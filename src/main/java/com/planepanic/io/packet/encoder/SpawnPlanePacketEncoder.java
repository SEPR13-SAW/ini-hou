package seprini.network.packet.codec.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import seprini.network.ByteBufUtils;
import seprini.network.packet.SpawnPlanePacket;

public final class SpawnPlanePacketEncoder extends Encoder<SpawnPlanePacket> {
	public SpawnPlanePacketEncoder() {
		super(0x81);
	}

	@Override
	public ByteBuf encode(ByteBufAllocator alloc, SpawnPlanePacket packet) {
		ByteBuf buf = alloc.buffer();

		buf.writeInt(packet.getPlaneId());
		buf.writeByte(packet.getPlayerId());
		ByteBufUtils.writeString(buf, packet.getName());

		return buf;
	}

}

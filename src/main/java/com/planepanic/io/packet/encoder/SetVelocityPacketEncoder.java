package seprini.network.packet.codec.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import seprini.network.packet.SetVelocityPacket;

public final class SetVelocityPacketEncoder extends Encoder<SetVelocityPacket> {
	public SetVelocityPacketEncoder() {
		super(0x01);
	}

	@Override
	public ByteBuf encode(ByteBufAllocator alloc, SetVelocityPacket packet) {
		ByteBuf buf = alloc.buffer();

		buf.writeInt(packet.getPlaneId());
		buf.writeInt(packet.getVelocity());

		return buf;
	}

}

package seprini.network.packet.codec.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import seprini.network.packet.SetAltitudePacket;

public final class SetAltitudePacketEncoder extends Encoder<SetAltitudePacket> {
	public SetAltitudePacketEncoder() {
		super(0x03);
	}

	@Override
	public ByteBuf encode(ByteBufAllocator alloc, SetAltitudePacket packet) {
		ByteBuf buf = alloc.buffer();

		buf.writeInt(packet.getPlaneId());
		buf.writeInt(packet.getAltitude());

		return buf;
	}

}

package seprini.network.packet.codec.decoder;

import io.netty.buffer.ByteBuf;
import seprini.network.packet.SetVelocityPacket;

public final class SetVelocityPacketDecoder extends Decoder<SetVelocityPacket> {
	public SetVelocityPacketDecoder() {
		super(0x03);
	}

	@Override
	public SetVelocityPacket decode(ByteBuf buffer) {
		int planeId = buffer.readInt();
		int velocity = buffer.readInt();

		return new SetVelocityPacket(planeId, velocity);
	}

}

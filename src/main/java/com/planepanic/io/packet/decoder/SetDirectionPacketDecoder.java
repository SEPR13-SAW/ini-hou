package seprini.network.packet.codec.decoder;

import io.netty.buffer.ByteBuf;
import seprini.network.packet.SetDirectionPacket;

public final class SetDirectionPacketDecoder extends Decoder<SetDirectionPacket> {
	public SetDirectionPacketDecoder() {
		super(0x02);
	}

	@Override
	public SetDirectionPacket decode(ByteBuf buffer) {
		int planeId = buffer.readInt();
		float direction = buffer.readFloat();

		return new SetDirectionPacket(planeId, direction);
	}

}

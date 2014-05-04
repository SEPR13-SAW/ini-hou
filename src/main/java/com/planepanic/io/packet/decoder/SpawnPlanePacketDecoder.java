package seprini.network.packet.codec.decoder;

import io.netty.buffer.ByteBuf;
import seprini.network.ByteBufUtils;
import seprini.network.packet.SpawnPlanePacket;

public final class SpawnPlanePacketDecoder extends Decoder<SpawnPlanePacket> {
	public SpawnPlanePacketDecoder() {
		super(0x81);
	}

	@Override
	public SpawnPlanePacket decode(ByteBuf buffer) {
		int planeId = buffer.readInt();
		byte playerId = buffer.readByte();
		String name = ByteBufUtils.readString(buffer);

		return new SpawnPlanePacket(planeId, playerId, name);
	}

}

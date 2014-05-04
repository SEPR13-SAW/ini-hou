package seprini.network.packet.codec.decoder;

import io.netty.buffer.ByteBuf;
import seprini.network.ByteBufUtils;
import seprini.network.packet.PlayerJoinPacket;

public final class PlayerJoinPacketDecoder extends Decoder<PlayerJoinPacket> {
	public PlayerJoinPacketDecoder() {
		super(0x80);
	}

	@Override
	public PlayerJoinPacket decode(ByteBuf buffer) {
		byte playerId = buffer.readByte();
		String name = ByteBufUtils.readString(buffer);

		return new PlayerJoinPacket(playerId, name);
	}

}

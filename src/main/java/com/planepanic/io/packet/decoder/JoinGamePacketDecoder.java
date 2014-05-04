package seprini.network.packet.codec.decoder;

import io.netty.buffer.ByteBuf;
import seprini.network.ByteBufUtils;
import seprini.network.packet.JoinGamePacket;

public final class JoinGamePacketDecoder extends Decoder<JoinGamePacket> {
	public JoinGamePacketDecoder() {
		super(0x00);
	}

	@Override
	public JoinGamePacket decode(ByteBuf buffer) {
		String name = ByteBufUtils.readString(buffer);

		return new JoinGamePacket(name);
	}

}

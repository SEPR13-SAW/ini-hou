package seprini.network.packet.codec.decoder;

import io.netty.buffer.ByteBuf;
import seprini.network.packet.UpdateScorePacket;

public final class UpdateScorePacketDecoder extends Decoder<UpdateScorePacket> {
	public UpdateScorePacketDecoder() {
		super(0x83);
	}

	@Override
	public UpdateScorePacket decode(ByteBuf buffer) {
		byte playerId = buffer.readByte();
		int score = buffer.readInt();

		return new UpdateScorePacket(playerId, score);
	}

}

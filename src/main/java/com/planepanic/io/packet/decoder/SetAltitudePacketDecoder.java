package seprini.network.packet.codec.decoder;

import io.netty.buffer.ByteBuf;
import seprini.network.packet.SetAltitudePacket;

public final class SetAltitudePacketDecoder extends Decoder<SetAltitudePacket> {
	public SetAltitudePacketDecoder() {
		super(0x01);
	}

	@Override
	public SetAltitudePacket decode(ByteBuf buffer) {
		int planeId = buffer.readInt();
		int altitude = buffer.readInt();

		return new SetAltitudePacket(planeId, altitude);
	}

}

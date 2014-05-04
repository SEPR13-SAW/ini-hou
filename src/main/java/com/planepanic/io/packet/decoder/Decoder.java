package seprini.network.packet.codec.decoder;

import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import seprini.network.packet.Packet;

/**
 * An abstract class for representing packet decoders.
 * @author Jonathan
 *
 * @param <T>
 */
public abstract class Decoder<T extends Packet> {
	private final static Map<Integer, Decoder<?>> decoders = new HashMap<>();

	public Decoder(int packetId) {
		decoders.put(packetId, this);
	}

	public abstract T decode(ByteBuf buffer);

	public static Decoder<?> get(int packetId) {
		return decoders.get(packetId);
	}

	static {
		new JoinGamePacketDecoder();
		new SetAltitudePacketDecoder();
		new SetDirectionPacketDecoder();
		new SetVelocityPacketDecoder();
		new PlayerJoinPacketDecoder();
		new SpawnPlanePacketDecoder();
		new UpdatePlanePacketDecoder();
		new UpdateScorePacketDecoder();
	}
}

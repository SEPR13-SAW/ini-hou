package seprini.network.packet.handler;

import java.util.HashMap;
import java.util.Map;

import seprini.network.packet.Packet;

public abstract class Handler<T extends Packet, A> {
	private final static Map<Integer, Handler<?, ?>> handlers = new HashMap<>();

	public Handler(int packetId) {
		handlers.put(packetId, this);
	}

	public abstract void handle(T packet, A attachment) throws Exception;

	@SuppressWarnings("unchecked")
	public void handleGeneric(Packet packet, Object attachment) throws Exception {
		handle((T) packet, (A) attachment);
	}

	public static Handler<?, ?> get(int packetId) {
		return handlers.get(packetId);
	}

	static {
		new JoinGamePacketHandler();
		new PlayerJoinPacketHandler();
	}
}

package seprini.network.packet;

public final class JoinGamePacket extends Packet {
	private final String name;

	public JoinGamePacket(String name) {
		this.name = name;
	}

	@Override
	public int getId() {
		return 0x00;
	}

	public String getName() {
		return name;
	}

}

package seprini.network.packet;

public final class SpawnPlanePacket extends Packet {
	private final int planeId;
	private final byte playerId;
	private final String name;

	public SpawnPlanePacket(int planeId, byte playerId, String name) {
		this.planeId = planeId;
		this.playerId = playerId;
		this.name = name;
	}

	@Override
	public int getId() {
		return 0x81;
	}

	public int getPlaneId() {
		return planeId;
	}

	public byte getPlayerId() {
		return playerId;
	}

	public String getName() {
		return name;
	}

}

package seprini.network.packet;

public final class SetVelocityPacket extends Packet {
	private final int planeId;
	private final int velocity;

	public SetVelocityPacket(int planeId, int velocity) {
		this.planeId = planeId;
		this.velocity = velocity;
	}

	@Override
	public int getId() {
		return 0x03;
	}

	public int getPlaneId() {
		return planeId;
	}

	public int getVelocity() {
		return velocity;
	}

}

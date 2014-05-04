package seprini.network.packet;

public final class SetDirectionPacket extends Packet {
	private final int planeId;
	private final float direction;

	public SetDirectionPacket(int planeId, float direction) {
		this.planeId = planeId;
		this.direction = direction;
	}

	@Override
	public int getId() {
		return 0x02;
	}

	public int getPlaneId() {
		return planeId;
	}

	public float getDirection() {
		return direction;
	}

}

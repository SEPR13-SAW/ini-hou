package seprini.network.packet;

public final class SetAltitudePacket extends Packet {
	private final int planeId;
	private final int altitude;

	public SetAltitudePacket(int planeId, int altitude) {
		this.planeId = planeId;
		this.altitude = altitude;
	}

	@Override
	public int getId() {
		return 0x01;
	}

	public int getPlaneId() {
		return planeId;
	}

	public int getAltitude() {
		return altitude;
	}

}

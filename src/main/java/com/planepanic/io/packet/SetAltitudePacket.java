package com.planepanic.io.packet;

public final class SetAltitudePacket extends Packet {
	private final int planeId;
	private final float altitude;

	public SetAltitudePacket(int planeId, float altitude) {
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

	public float getAltitude() {
		return altitude;
	}

}

package com.planepanic.io.packet;

public final class SetAltitudePacket extends Packet {
	public final static int ID = 0x01;

	private final int planeId;
	private final float altitude;

	public SetAltitudePacket(int planeId, float altitude) {
		this.planeId = planeId;
		this.altitude = altitude;
	}

	@Override
	public int getId() {
		return ID;
	}

	public int getPlaneId() {
		return planeId;
	}

	public float getAltitude() {
		return altitude;
	}

}

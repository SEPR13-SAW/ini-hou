package com.planepanic.io.packet;

public final class LandPlanePacket extends Packet {
	public final static int ID = 0x04;

	private final int planeId;

	public LandPlanePacket(int planeId) {
		this.planeId = planeId;
	}

	@Override
	public int getId() {
		return ID;
	}

	public int getPlaneId() {
		return planeId;
	}
}

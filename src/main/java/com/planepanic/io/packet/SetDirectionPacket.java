package com.planepanic.io.packet;

public final class SetDirectionPacket extends Packet {
	public final static int ID = 0x02;

	private final int planeId;
	private final float direction;

	public SetDirectionPacket(int planeId, float direction) {
		this.planeId = planeId;
		this.direction = direction;
	}

	@Override
	public int getId() {
		return ID;
	}

	public int getPlaneId() {
		return planeId;
	}

	public float getDirection() {
		return direction;
	}

}

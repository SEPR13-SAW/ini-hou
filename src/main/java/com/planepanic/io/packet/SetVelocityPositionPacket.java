package com.planepanic.io.packet;

public final class SetVelocityPositionPacket extends Packet {
	private final int planeId;
	private final float velocity;
	private final float x, y;

	public SetVelocityPositionPacket(int planeId, float velocity, float x, float y) {
		this.planeId = planeId;
		this.velocity = velocity;
		this.x = x;
		this.y = y;
	}

	@Override
	public int getId() {
		return 0x03;
	}

	public int getPlaneId() {
		return planeId;
	}

	public float getVelocity() {
		return velocity;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

}

package com.planepanic.io.packet;

public final class UpdatePlanePacket extends Packet {
	private final int planeId;
	private final float x, y, direction;
	private final int altitude;
	private final float velocity;

	public UpdatePlanePacket(int planeId, float x, float y, float direction, int altitude, float velocity) {
		this.planeId = planeId;
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.altitude = altitude;
		this.velocity = velocity;
	}

	@Override
	public int getId() {
		return 0x82;
	}

	public int getPlaneId() {
		return planeId;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getDirection() {
		return direction;
	}

	public int getAltitude() {
		return altitude;
	}

	public float getVelocity() {
		return velocity;
	}

}

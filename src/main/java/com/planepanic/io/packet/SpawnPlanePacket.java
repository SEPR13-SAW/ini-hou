package com.planepanic.io.packet;

import java.util.List;
import java.util.Stack;

import com.planepanic.model.waypoint.Waypoint;

public final class SpawnPlanePacket extends Packet {
	public final static int ID = 0x81;

	private final int planeId;
	private final byte playerId;
	private final String name;
	private final Stack<Waypoint> flightPlan;
	private final float altitude;

	public SpawnPlanePacket(int planeId, int playerId, String name, Stack<Waypoint> flightPlan, float altitude) {
		this.planeId = planeId;
		this.playerId = (byte) playerId;
		this.name = name;
		this.flightPlan = flightPlan;
		this.altitude = altitude;
	}

	@Override
	public int getId() {
		return ID;
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

	public List<Waypoint> getFlightPlan() {
		return flightPlan;
	}

	public float getAltitude() {
		return altitude;
	}

}

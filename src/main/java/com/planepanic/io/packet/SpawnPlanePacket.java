package com.planepanic.io.packet;

import java.util.List;

import com.planepanic.model.Waypoint;

public final class SpawnPlanePacket extends Packet {
	private final int planeId;
	private final byte playerId;
	private final String name;
	private final List<Waypoint> flightPlan;

	public SpawnPlanePacket(int planeId, byte playerId, String name, List<Waypoint> flightPlan) {
		this.planeId = planeId;
		this.playerId = playerId;
		this.name = name;
		this.flightPlan = flightPlan;
	}

	@Override
	public int getId() {
		return 0x81;
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

}

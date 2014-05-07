package com.planepanic.io.packet.handler;

import java.util.Stack;

import com.planepanic.io.client.Client;
import com.planepanic.io.packet.SpawnPlanePacket;
import com.planepanic.model.Airspace;
import com.planepanic.model.entity.Plane;
import com.planepanic.model.waypoint.Waypoint;

public final class SpawnPlanePacketHandler extends Handler<SpawnPlanePacket, Client> {
	public SpawnPlanePacketHandler() {
		super(SpawnPlanePacket.ID);
	}

	@Override
	public void handle(SpawnPlanePacket packet, Client client) throws Exception {
		Airspace airspace = client.getAirspace();
		Plane plane = new Plane(airspace, packet.getPlaneId(), (Stack<Waypoint>) packet.getFlightPlan());
		plane.setAltitude(packet.getAltitude());
		plane.setDesiredAltitude(packet.getAltitude());
		airspace.addPlane(plane);
	}

}

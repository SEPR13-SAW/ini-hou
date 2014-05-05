package com.planepanic.io.packet.handler;

import java.util.ArrayList;

import com.planepanic.io.client.Client;
import com.planepanic.io.packet.SpawnPlanePacket;
import com.planepanic.model.Waypoint;

public final class SpawnPlanePacketHandler extends Handler<SpawnPlanePacket, Client> {
	public SpawnPlanePacketHandler() {
		super(0x81);
	}

	@Override
	public void handle(SpawnPlanePacket packet, Client client) throws Exception {
		System.out.println(client.getPlayer() + ", " + client.getPlayer().getController());
		client.getPlayer().getController().addAircraft(client.getPlayer(), packet.getPlaneId(), packet.getName(), (ArrayList<Waypoint>) packet.getFlightPlan(), true, packet.getAltitude());
	}

}

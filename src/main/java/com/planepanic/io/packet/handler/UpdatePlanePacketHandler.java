package com.planepanic.io.packet.handler;

import com.planepanic.io.client.Client;
import com.planepanic.io.packet.UpdatePlanePacket;
import com.planepanic.model.Airspace;
import com.planepanic.model.entity.Plane;

public final class UpdatePlanePacketHandler extends Handler<UpdatePlanePacket, Client> {
	public UpdatePlanePacketHandler() {
		super(0x82);
	}

	@Override
	public void handle(UpdatePlanePacket packet, Client client) throws Exception {
		Airspace airspace = client.getAirspace();
		for (Plane plane : airspace.getPlanes()) {
			if (packet.getId() == packet.getPlaneId()) {
				plane.setAltitude(packet.getAltitude());
				plane.setVelocity(packet.getVelocity());
				plane.setRotation(packet.getDirection());
				plane.setX(packet.getX());
				plane.setY(packet.getY());
				break;
			}
		}
	}

}

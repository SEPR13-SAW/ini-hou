package com.planepanic.io.packet.handler;

import com.planepanic.io.client.Client;
import com.planepanic.io.packet.UpdatePlanePacket;
import com.planepanic.model.Aircraft;
import com.planepanic.model.controllers.aircraft.ClientAircraftController;

public final class UpdatePlanePacketHandler extends Handler<UpdatePlanePacket, Client> {
	public UpdatePlanePacketHandler() {
		super(0x82);
	}

	@Override
	public void handle(UpdatePlanePacket packet, Client client) throws Exception {
		ClientAircraftController c = client.getPlayer().getController();

		for (Aircraft a : c.getAircraftList()) {
			if (a.getId() == packet.getPlaneId()) {
				a.setX(packet.getX());
				a.setY(packet.getY());
				a.setRotation(packet.getDirection());
				a.setSpeed(packet.getVelocity());
				a.setAltitude(packet.getAltitude());
				break;
			}
		}
	}

}

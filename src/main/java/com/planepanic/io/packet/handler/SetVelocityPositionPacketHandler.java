package com.planepanic.io.packet.handler;

import com.planepanic.io.packet.SetVelocityPositionPacket;
import com.planepanic.io.server.Player;
import com.planepanic.model.Aircraft;
import com.planepanic.model.controllers.aircraft.ServerAircraftController;

public final class SetVelocityPositionPacketHandler extends Handler<SetVelocityPositionPacket, Player> {
	public SetVelocityPositionPacketHandler() {
		super(0x03);
	}

	@Override
	public void handle(SetVelocityPositionPacket packet, Player player) throws Exception {
		ServerAircraftController c = (ServerAircraftController) player.getServer().getController();

		for (Aircraft a : c.getAircraftList()) {
			if (a.getId() == packet.getPlaneId()) {
				a.setSpeed(packet.getVelocity());
				a.setX(packet.getX());
				a.setY(packet.getY());
				break;
			}
		}
	}

}

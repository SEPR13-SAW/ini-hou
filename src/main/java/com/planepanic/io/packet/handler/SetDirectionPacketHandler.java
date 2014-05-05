package com.planepanic.io.packet.handler;

import com.planepanic.io.packet.SetDirectionPacket;
import com.planepanic.io.server.Player;
import com.planepanic.model.Aircraft;
import com.planepanic.model.controllers.aircraft.ServerAircraftController;

public final class SetDirectionPacketHandler extends Handler<SetDirectionPacket, Player> {
	public SetDirectionPacketHandler() {
		super(0x02);
	}

	@Override
	public void handle(SetDirectionPacket packet, Player player) throws Exception {
		ServerAircraftController c = (ServerAircraftController) player.getServer().getController();

		for (Aircraft a : c.getAircraftList()) {
			if (a.getId() == packet.getPlaneId()) {
				a.setRotation(packet.getDirection());
				break;
			}
		}
	}

}

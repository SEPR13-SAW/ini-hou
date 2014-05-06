package com.planepanic.io.packet.handler;

import com.planepanic.io.packet.SetVelocityPositionPacket;
import com.planepanic.io.server.Player;
import com.planepanic.model.Airspace;
import com.planepanic.model.entity.Plane;

public final class SetVelocityPositionPacketHandler extends Handler<SetVelocityPositionPacket, Player> {
	public SetVelocityPositionPacketHandler() {
		super(SetVelocityPositionPacket.ID);
	}

	@Override
	public void handle(SetVelocityPositionPacket packet, Player player) throws Exception {
		Airspace a = player.getServer().getAirspace();
		for (Plane p : a.getPlanes()) {
			if (p.getId() == packet.getPlaneId()) {
				p.setVelocity(packet.getVelocity());
				p.getCoords().x = packet.getX();
				p.getCoords().y = packet.getY();
				break;
			}
		}
	}

}

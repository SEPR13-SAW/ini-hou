package com.planepanic.io.packet.handler;

import com.planepanic.io.packet.SetDirectionPacket;
import com.planepanic.io.server.Player;
import com.planepanic.model.Airspace;
import com.planepanic.model.entity.Plane;

public final class SetDirectionPacketHandler extends Handler<SetDirectionPacket, Player> {
	public SetDirectionPacketHandler() {
		super(SetDirectionPacket.ID);
	}

	@Override
	public void handle(SetDirectionPacket packet, Player player) throws Exception {
		Airspace a = player.getServer().getAirspace();
		for (Plane p : a.getPlanes()) {
			if (p.getId() == packet.getPlaneId()) {
				p.setRotation(packet.getDirection());
				break;
			}
		}
	}
}

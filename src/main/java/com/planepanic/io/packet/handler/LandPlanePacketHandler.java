package com.planepanic.io.packet.handler;

import com.planepanic.io.packet.LandPlanePacket;
import com.planepanic.io.server.Player;
import com.planepanic.model.Airspace;
import com.planepanic.model.entity.Plane;
import com.planepanic.model.entity.Plane.State;

public final class LandPlanePacketHandler extends Handler<LandPlanePacket, Player> {
	public LandPlanePacketHandler() {
		super(LandPlanePacket.ID);
	}

	@Override
	public void handle(LandPlanePacket packet, Player player) throws Exception {
		Airspace a = player.getServer().getAirspace();
		for (Plane p : a.getPlanes()) {
			if (p.getId() == packet.getPlaneId()) {
				p.setState(State.APPROACHING);
			}
		}
	}
}

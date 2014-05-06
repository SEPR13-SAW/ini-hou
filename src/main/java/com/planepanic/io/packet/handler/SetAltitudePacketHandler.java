package com.planepanic.io.packet.handler;

import com.planepanic.io.packet.SetAltitudePacket;
import com.planepanic.io.server.Player;
import com.planepanic.model.Airspace;
import com.planepanic.model.entity.Plane;

public final class SetAltitudePacketHandler extends Handler<SetAltitudePacket, Player> {
	public SetAltitudePacketHandler() {
		super(SetAltitudePacket.ID);
	}

	@Override
	public void handle(SetAltitudePacket packet, Player player) throws Exception {
		Airspace a = player.getServer().getAirspace();
		for (Plane p : a.getPlanes()) {
			if (p.getId() == packet.getPlaneId()) {
				p.setRotation(packet.getAltitude());
				break;
			}
		}
	}
}

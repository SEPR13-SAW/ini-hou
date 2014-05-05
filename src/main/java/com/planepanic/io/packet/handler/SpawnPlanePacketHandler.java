package com.planepanic.io.packet.handler;

import com.planepanic.io.client.Client;
import com.planepanic.io.packet.SpawnPlanePacket;

public final class SpawnPlanePacketHandler extends Handler<SpawnPlanePacket, Client> {
	public SpawnPlanePacketHandler() {
		super(0x81);
	}

	@Override
	public void handle(SpawnPlanePacket packet, Client client) throws Exception {

	}

}

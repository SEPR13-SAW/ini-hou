package com.planepanic.io.packet.handler;

import com.planepanic.io.client.Client;
import com.planepanic.io.packet.UpdatePlanePacket;

public final class UpdatePlanePacketHandler extends Handler<UpdatePlanePacket, Client> {
	public UpdatePlanePacketHandler() {
		super(0x82);
	}

	@Override
	public void handle(UpdatePlanePacket packet, Client client) throws Exception {

	}

}

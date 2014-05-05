package com.planepanic.io.packet.handler;

import com.planepanic.io.client.Client;
import com.planepanic.io.packet.UpdateScorePacket;

public final class UpdateScorePacketHandler extends Handler<UpdateScorePacket, Client> {
	public UpdateScorePacketHandler() {
		super(0x83);
	}

	@Override
	public void handle(UpdateScorePacket packet, Client client) throws Exception {
		client.getPlayers().get(packet.getId()).setScore(packet.getScore());
	}

}

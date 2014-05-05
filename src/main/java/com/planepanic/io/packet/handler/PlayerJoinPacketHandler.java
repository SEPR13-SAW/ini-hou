package com.planepanic.io.packet.handler;

import com.planepanic.io.client.Client;
import com.planepanic.io.client.Player;
import com.planepanic.io.packet.PlayerJoinPacket;

public final class PlayerJoinPacketHandler extends Handler<PlayerJoinPacket, Client> {
	public PlayerJoinPacketHandler() {
		super(0x80);
	}

	@Override
	public void handle(PlayerJoinPacket packet, Client client) throws Exception {
		int id = packet.getPlayerId();
		String name = packet.getName();

		if (name.equals(client.getName())) {
			Player player = new Player(id, name);
			client.addPlayer(player);
			client.setPlayer(player);
		} else {
			Player player = new Player(id, name);
			client.addPlayer(player);
		}
	}

}

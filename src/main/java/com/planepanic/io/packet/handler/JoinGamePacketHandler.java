package com.planepanic.io.packet.handler;

import java.io.IOException;
import java.util.Map.Entry;

import com.planepanic.io.packet.JoinGamePacket;
import com.planepanic.io.packet.PlayerJoinPacket;
import com.planepanic.io.server.Player;
import com.planepanic.io.server.Server;

public final class JoinGamePacketHandler extends Handler<JoinGamePacket, Player> {
	public JoinGamePacketHandler() {
		super(0x00);
	}

	@Override
	public void handle(JoinGamePacket packet, Player client) throws Exception {
		Server server = client.getServer();
		int id = server.getUnusedId();

		if (packet.getName().length() <= 0) throw new IOException("Name is too short.");
		if (packet.getName().length() > 32) throw new IOException("Name is too long.");
		if (server.playerConnected(packet.getName())) throw new IOException("Player already connected with that username.");

		client.setName(packet.getName());
		client.setId(id);

		server.addPlayer(client);

		for (Entry<Integer, Player> c : server.getPlayers().entrySet()) {
			c.getValue().writePacket(new PlayerJoinPacket((byte) id, client.getName()));
		}
	}

}

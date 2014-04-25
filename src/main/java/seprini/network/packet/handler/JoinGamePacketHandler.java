package seprini.network.packet.handler;

import java.io.IOException;
import java.util.Map.Entry;

import seprini.network.packet.JoinGamePacket;
import seprini.network.packet.PlayerJoinPacket;
import seprini.network.server.Client;
import seprini.network.server.Server;

public final class JoinGamePacketHandler extends Handler<JoinGamePacket, Client> {
	public JoinGamePacketHandler() {
		super(0x00);
	}

	@Override
	public void handle(JoinGamePacket packet, Client client) throws Exception {
		Server server = client.getServer();
		int id = server.getUnusedId();

		if (packet.getName().length() <= 0) throw new IOException("Name is too short.");
		if (packet.getName().length() > 32) throw new IOException("Name is too long.");
		if (server.playerConnected(packet.getName())) throw new IOException("Player already connected with that username.");

		client.setName(packet.getName());
		client.setId(id);

		server.addClient(client);

		for (Entry<Integer, Client> c : server.getClients().entrySet()) {
			c.getValue().writePacket(new PlayerJoinPacket((byte) id, client.getName()));
		}
	}

}

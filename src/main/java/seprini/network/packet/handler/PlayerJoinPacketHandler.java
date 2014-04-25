package seprini.network.packet.handler;

import seprini.network.client.Client;
import seprini.network.client.Player;
import seprini.network.packet.PlayerJoinPacket;

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
			client.addPlayer(new Player(id, name));
		}
	}

}

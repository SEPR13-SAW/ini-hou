package seprini.network.packet.handler;

import seprini.network.client.Client;
import seprini.network.packet.UpdateScorePacket;

public final class UpdateScorePacketHandler extends Handler<UpdateScorePacket, Client> {
	public UpdateScorePacketHandler() {
		super(0x83);
	}

	@Override
	public void handle(UpdateScorePacket packet, Client client) throws Exception {
		client.getPlayers().get(packet.getId()).setScore(packet.getScore());
	}

}

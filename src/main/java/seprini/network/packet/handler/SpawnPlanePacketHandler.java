package seprini.network.packet.handler;

import java.util.ArrayList;

import seprini.models.Waypoint;
import seprini.network.client.Client;
import seprini.network.packet.SpawnPlanePacket;

public final class SpawnPlanePacketHandler extends Handler<SpawnPlanePacket, Client> {
	public SpawnPlanePacketHandler() {
		super(0x81);
	}

	@Override
	public void handle(SpawnPlanePacket packet, Client client) throws Exception {
		System.out.println(client.getPlayer() + ", " + client.getPlayer().getController());
		client.getPlayer().getController().addAircraft(client.getPlayer(), packet.getPlaneId(), packet.getName(), new ArrayList<Waypoint>(), true);
	}

}

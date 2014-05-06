package com.planepanic.io.packet.handler;

import com.planepanic.io.packet.EndGamePacket;
import com.planepanic.io.client.Player;

public final class EndGamePacketHandler extends Handler<EndGamePacket, Player> {
	public EndGamePacketHandler() {
		super(EndGamePacket.ID);
	}

	@Override
	public void handle(EndGamePacket packet, Player player) throws Exception {
		player.getClient().getAirspace().endGame(packet.getScore(), packet.isWin());
	}

}

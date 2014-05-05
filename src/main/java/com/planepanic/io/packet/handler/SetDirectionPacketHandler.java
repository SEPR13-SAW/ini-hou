package com.planepanic.io.packet.handler;

import com.planepanic.io.packet.SetDirectionPacket;
import com.planepanic.io.server.Player;

public final class SetDirectionPacketHandler extends Handler<SetDirectionPacket, Player> {
	public SetDirectionPacketHandler() {
		super(0x02);
	}

	@Override
	public void handle(SetDirectionPacket packet, Player player) throws Exception {

	}

}

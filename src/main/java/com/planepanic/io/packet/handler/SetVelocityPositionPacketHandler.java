package com.planepanic.io.packet.handler;

import com.planepanic.io.packet.SetVelocityPositionPacket;
import com.planepanic.io.server.Player;

public final class SetVelocityPositionPacketHandler extends Handler<SetVelocityPositionPacket, Player> {
	public SetVelocityPositionPacketHandler() {
		super(0x03);
	}

	@Override
	public void handle(SetVelocityPositionPacket packet, Player player) throws Exception {

	}

}

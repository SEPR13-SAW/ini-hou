package com.planepanic.io.packet;

public final class PlayerJoinPacket extends Packet {
	private final byte playerId;
	private final String name;

	public PlayerJoinPacket(byte playerId, String name) {
		this.playerId = playerId;
		this.name = name;
	}

	@Override
	public int getId() {
		return 0x80;
	}

	public byte getPlayerId() {
		return playerId;
	}

	public String getName() {
		return name;
	}

}

package com.planepanic.io.packet;

public final class PlayerJoinPacket extends Packet {
	public final static int ID = 0x80;

	private final byte playerId;
	private final String name;

	public PlayerJoinPacket(byte playerId, String name) {
		this.playerId = playerId;
		this.name = name;
	}

	@Override
	public int getId() {
		return ID;
	}

	public byte getPlayerId() {
		return playerId;
	}

	public String getName() {
		return name;
	}

}

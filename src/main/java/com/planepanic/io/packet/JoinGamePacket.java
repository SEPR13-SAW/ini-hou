package com.planepanic.io.packet;

public final class JoinGamePacket extends Packet {
	public final static int ID = 0x00;

	private final String name;

	public JoinGamePacket(String name) {
		this.name = name;
	}

	@Override
	public int getId() {
		return ID;
	}

	public String getName() {
		return name;
	}

}

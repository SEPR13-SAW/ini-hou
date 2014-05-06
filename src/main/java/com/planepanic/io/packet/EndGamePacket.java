package com.planepanic.io.packet;

public final class EndGamePacket extends Packet {
	public final static int ID = 0x84;
	private final int score;
	private final boolean win;

	public EndGamePacket(int score, boolean win) {
		this.score = score;
		this.win = win;
	}

	@Override
	public int getId() {
		return ID;
	}

	public int getScore() {
		return score;
	}

	public boolean isWin() {
		return win;
	}
}

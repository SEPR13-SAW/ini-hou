package com.planepanic.io.packet;

public final class UpdateScorePacket extends Packet {
	public final static int ID = 0x83;

	private final int playerId;
	private final int score;

	public UpdateScorePacket(int playerId, int score) {
		this.playerId = playerId;
		this.score = score;
	}

	@Override
	public int getId() {
		return ID;
	}

	public int getPlayerId() {
		return playerId;
	}

	public int getScore() {
		return score;
	}

}

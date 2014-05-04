package seprini.network.packet;

public final class UpdateScorePacket extends Packet {
	private final int playerId;
	private final int score;

	public UpdateScorePacket(int playerId, int score) {
		this.playerId = playerId;
		this.score = score;
	}

	@Override
	public int getId() {
		return 0x83;
	}

	public int getPlayerId() {
		return playerId;
	}

	public int getScore() {
		return score;
	}

}

package seprini.network.client;

import lombok.Getter;
import lombok.Setter;

public final class Player {
	@Getter private final int id;
	@Getter private final String name;
	@Getter @Setter private int score;

	public Player(int id, String name) {
		this.id = id;
		this.name = name;
		this.score = 0;
	}

	public void addScore(int delta) {
		score += delta;
	}
}

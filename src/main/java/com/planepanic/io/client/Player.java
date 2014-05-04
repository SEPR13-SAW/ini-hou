package com.planepanic.io.client;

import com.planepanic.model.controllers.ClientAircraftController;

import lombok.Getter;
import lombok.Setter;

/**
 * Client-side representation of a player.
 * @author Jonathan
 *
 */
public class Player {
	@Getter private final int id;
	@Getter private final String name;
	@Getter @Setter private int score;
	@Getter @Setter private ClientAircraftController controller;

	public Player(int id, String name) {
		this.id = id;
		this.name = name;
		this.score = 0;
	}

	public void addScore(int delta) {
		score += delta;
	}
}

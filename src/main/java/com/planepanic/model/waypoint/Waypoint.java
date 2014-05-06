package com.planepanic.model.waypoint;

import com.badlogic.gdx.math.Vector2;
import com.planepanic.model.entity.Entity;
import com.planepanic.model.resources.Art;

public class Waypoint extends Entity {
	private final int id;

	public Waypoint(float x, float y, int id) {
		this.id = id;
		this.texture = Art.getTextureRegion("waypoint");
		this.coords = new Vector2(x, y);
		this.size = new Vector2(20, 20);
	}

	public int getId() {
		return id;
	}
}
package com.planepanic.model.waypoint;

import com.badlogic.gdx.math.Vector2;
import com.planepanic.model.entity.Entity;
import com.planepanic.model.resources.Art;

public class Waypoint extends Entity {
	public Waypoint(float x, float y) {
		this.texture = Art.getTextureRegion("waypoint");
		this.coords = new Vector2(x, y);
		this.size = new Vector2(20, 20);
	}
}
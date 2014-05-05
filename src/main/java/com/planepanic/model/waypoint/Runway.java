package com.planepanic.model.waypoint;

import com.badlogic.gdx.math.Vector2;
import com.planepanic.model.resources.Art;

public final class Runway extends Waypoint {
	public Runway(float x, float y) {
		super(x, y);
		this.texture = Art.getTextureRegion("airport");
		this.size = new Vector2(200, 200);
	}
}

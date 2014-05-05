package com.planepanic.model;

import com.badlogic.gdx.math.Vector2;
import com.planepanic.model.resources.Art;

public final class Exitpoint extends Waypoint {

	public Exitpoint(Vector2 position) {
		super(position, true);
		this.texture = Art.getTextureRegion("exitpoint");
	}
}

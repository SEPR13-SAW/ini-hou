package com.planepanic.model.waypoint;

import com.planepanic.model.resources.Art;

public final class EntryPoint extends Waypoint {
	public EntryPoint(float x, float y, int id) {
		super(x, y, id);
		this.texture = Art.getTextureRegion("entrypoint");
	}
}

package com.planepanic.model.waypoint;

import com.planepanic.model.resources.Art;

public final class EntryPoint extends Waypoint {
	public EntryPoint(float x, float y) {
		super(x, y);
		this.texture = Art.getTextureRegion("entrypoint");
	}
}

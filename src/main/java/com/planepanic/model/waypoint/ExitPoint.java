package com.planepanic.model.waypoint;

import com.planepanic.model.resources.Art;

public final class ExitPoint extends Waypoint {
	public ExitPoint(float x, float y) {
		super(x, y);
		this.texture = Art.getTextureRegion("exitpoint");
	}
}

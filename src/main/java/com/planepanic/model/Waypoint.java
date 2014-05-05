package com.planepanic.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.planepanic.model.resources.Art;

/**
 * A class to represent waypoints used for navigation within the airspace.
 * @author Jonathan
 *
 */
public class Waypoint extends Entity {
	private final boolean visible;

	public Waypoint(float x, float y, boolean visible) {
		this(new Vector2(x, y), visible);
	}

	public Waypoint(Vector2 position, boolean visible) {
		coords = position;
		this.visible = visible;

		this.debugShape = true;
		this.texture = Art.getTextureRegion("waypoint");
		this.size = Config.WAYPOINT_SIZE;

		// set the origin to the centre
		this.setOrigin(getWidth() / 2, getHeight() / 2);

		// set its bounds so it's clickable
		this.setBounds(getX() - getWidth() / 2, getY() - getWidth() / 2,
				getWidth(), getHeight());
	}
	
	public boolean isVisible() {
		return visible;
	}

	@Override
	public String toString() {
		return "Waypoint - x: " + getX() + " y: " + getY();
	}

	public Waypoint cpy() {
		return new Waypoint(getX(), getY(), this.visible);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		if (visible){
			super.draw(batch, parentAlpha);
		}
			
	}

}

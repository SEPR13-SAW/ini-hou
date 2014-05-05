package com.planepanic.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.planepanic.model.resources.Art;

public final class Airport extends Entity {
	Waypoint runwayEnd;
	Waypoint runwayMid;
	Waypoint runwayStart;
	Waypoint approach;
	Queue<Aircraft> landedPlanes = new LinkedList<Aircraft>();
	TextureRegion aircraft;
	public ArrayList<Waypoint> availablePositions = new ArrayList<Waypoint>();
	public Queue<Waypoint> takenPositions = new LinkedList<Waypoint>();
	public Waypoint nextPosition;

	private final boolean visible = true;
	private int id;

	public Airport(Vector2 midPoint, int ID) {
		this.runwayMid = new Waypoint(midPoint.x, midPoint.y, false);
		this.runwayStart = new Waypoint(midPoint.x, midPoint.y - 60, false);
		this.runwayEnd = new Waypoint(midPoint.x, midPoint.y + 60, false);
		this.approach = new Waypoint(runwayStart.getX(),
				runwayStart.getY() - 100, false);
		this.debugShape = true;
		this.coords = new Vector2(midPoint.x, midPoint.y);
		this.size = new Vector2(154, 120);
		this.texture = Art.getTextureRegion("airport");
		this.aircraft = Art.getTextureRegion("aircraft");
		this.id = ID;
		this.adjustPositions();
	}

	// Fills the arraylist with positions for the landed planes to appear on.
	public void adjustPositions() {
		int offsetX = 40, offsetY = 25, stepY = 20;
		Waypoint waypoint;
		for (int i = 0; i < 5; i++) {
			waypoint = new Waypoint(this.runwayStart.getX() + offsetX, this.runwayStart.getY() + offsetY, false);
			offsetY += stepY;
			this.availablePositions.add(waypoint);
		};
		offsetY = 5;
		offsetX = -35;

		for (int o = 5; o < 10; o++) {
			waypoint = new Waypoint(this.runwayStart.getX() + offsetX, this.runwayStart.getY() + offsetY, false);
			offsetY += stepY;
			this.availablePositions.add(waypoint);
		}
		this.nextPosition = this.availablePositions.get(0);
	}

	public void findNext() {
		for (int i = 0; i < this.availablePositions.size(); i++) {
			this.nextPosition = this.availablePositions.get(i);
			for (int o = 0; o < this.takenPositions.size(); o++) {
				if (this.takenPositions.contains(this.availablePositions.get(i))) {
					this.nextPosition = null;
					break;
				}
			}
			if (this.nextPosition != null)
				break;
		}
	}

	public int getID() {
		return this.id;
	}

	public Waypoint getStart() {
		return runwayStart;
	}

	public Waypoint getEnd() {
		return runwayEnd;
	}

	public Waypoint getMid() {
		return runwayMid;
	}

	public Queue<Aircraft> getLandedPlanes() {
		return this.landedPlanes;
	}

	public boolean isVisible() {
		return visible;
	}

	public Waypoint cpy() {
		return new Waypoint(getX(), getY(), this.visible);
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		if (visible) {
			super.draw(batch, parentAlpha);
			// Draws planes landed on the airport.
			for (Waypoint position : this.takenPositions) {
				if (position.getX() > this.getMid().getX())
					batch.draw(aircraft, position.getX(),
							position.getY(), 0, 0,
							aircraft.getRegionWidth(),
							aircraft.getRegionHeight(), 0.3f, 0.3f, 160f);
				else
					batch.draw(aircraft, position.getX(),
							position.getY(), 0, 0,
							aircraft.getRegionWidth(),
							aircraft.getRegionHeight(), 0.3f, 0.3f, 20f);
			}
		}
	}

	public Waypoint getApproach() {
		return approach;
	}

	public void addLanded(Aircraft x) {
		landedPlanes.add(x);
		this.takenPositions.add(this.nextPosition);
		this.findNext();
		x.setActive(false);
	}

}

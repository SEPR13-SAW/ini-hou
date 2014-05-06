package com.planepanic.model.waypoint;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import lombok.Getter;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.planepanic.model.entity.Plane;
import com.planepanic.model.resources.Art;

public final class Runway extends Waypoint {
	@Getter Queue<Plane> landedPlanes = new LinkedList<Plane>();
	TextureRegion aircraft;
	private ArrayList<Waypoint> availablePositions = new ArrayList<Waypoint>();
	@Getter private Queue<Waypoint> takenPositions = new LinkedList<Waypoint>();
	private Waypoint nextPosition;
	@Getter private Waypoint startOfRunway;
	@Getter private Waypoint endOfRunway;
	@Getter private Waypoint approach;
	
	public Runway(float x, float y, int id) {
		super(x, y, id);
		this.texture = Art.getTextureRegion("airport");
		this.size = new Vector2(200, 200);
		this.aircraft = Art.getTextureRegion("aircraft");
		System.out.println("Runway constructor x = " + x + " y = " + y);
		this.startOfRunway = new Waypoint(x, (y - 100), id + 1000);
		WaypointManager.getAll().put(this.startOfRunway.getId(), this.startOfRunway);
		System.out.println("startOfRunway = " + startOfRunway);
		this.endOfRunway = new Waypoint(x, (y + 100), id + 2000);
		WaypointManager.getAll().put(this.endOfRunway.getId(), this.endOfRunway);
		System.out.println("endOfRunway = " + endOfRunway);
		this.approach = new Waypoint(x, (y - 200), id + 3000); 
		WaypointManager.getAll().put(this.approach.getId(), this.approach);
		System.out.println("approach = " + approach);
		this.adjustPositions();
	}
	
//	 Fills the arraylist with positions for the landed planes to appear on.
	public void adjustPositions() {
		int offsetX = 37, offsetY = 10, stepY = 35;
		Waypoint waypoint;
		for (int i = 0; i < 5; i++) {
			waypoint = new Waypoint(this.startOfRunway.getX() + offsetX, this.startOfRunway.getY() + offsetY, 4000 + i);
			offsetY += stepY;
			this.availablePositions.add(waypoint);
		};
		offsetY = 20;
		offsetX = -15;

		for (int o = 5; o < 10; o++) {
			waypoint = new Waypoint(this.startOfRunway.getX() + offsetX, this.startOfRunway.getY() + offsetY, 5000 + o);
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
	
	public void addLanded(Plane x) {
		landedPlanes.add(x);
		this.takenPositions.add(this.nextPosition);
		this.findNext();
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
//		 Draws planes landed on the airport.
		for (Waypoint position : this.takenPositions) {
			if (position.getX() > this.getX())
				batch.draw(aircraft, position.getX(), position.getY(), 0, 0,
						aircraft.getRegionWidth(), aircraft.getRegionHeight(),
						0.45f, 0.45f, 60f);
			else
				batch.draw(aircraft, position.getX(), position.getY(), 0, 0,
						aircraft.getRegionWidth(), aircraft.getRegionHeight(),
						0.45f, 0.45f, 120f);
		}

	}
	
	
}

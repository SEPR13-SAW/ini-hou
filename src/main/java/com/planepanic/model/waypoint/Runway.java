package com.planepanic.model.waypoint;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import lombok.Getter;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.planepanic.model.entity.Plane;
import com.planepanic.model.resources.Art;

public final class Runway extends Waypoint {
	@Getter Queue<Plane> landedPlanes = new LinkedList<Plane>();
	TextureRegion aircraft;
	public ArrayList<Waypoint> availablePositions = new ArrayList<Waypoint>();
	public Queue<Waypoint> takenPositions = new LinkedList<Waypoint>();
	public Waypoint nextPosition;
	@Getter public Waypoint startOfRunway;
	@Getter public Waypoint endOfRunway;
	@Getter public Waypoint approach;
	
	public Runway(float x, float y, int id) {
		super(x, y, id);
		this.texture = Art.getTextureRegion("airport");
		this.size = new Vector2(200, 200);
		this.aircraft = Art.getTextureRegion("aircraft");
		System.out.println("x = " + x + " y = " + y);
		this.startOfRunway = new Waypoint(x, (y - 100), id + 1000);
		this.endOfRunway = new Waypoint(x, (y + 100), id + 2000);
		this.approach = new Waypoint(x, (y - 200), id + 3000); 
//		this.adjustPositions();
	}
	
	// Fills the arraylist with positions for the landed planes to appear on.
//	public void adjustPositions() {
//		int offsetX = 40, offsetY = 25, stepY = 20;
//		Waypoint waypoint;
//		for (int i = 0; i < 5; i++) {
//			waypoint = new Waypoint(this.runwayStart.getX() + offsetX, this.runwayStart.getY() + offsetY, false);
//			offsetY += stepY;
//			this.availablePositions.add(waypoint);
//		};
//		offsetY = 5;
//		offsetX = -35;
//
//		for (int o = 5; o < 10; o++) {
//			waypoint = new Waypoint(this.runwayStart.getX() + offsetX, this.runwayStart.getY() + offsetY, false);
//			offsetY += stepY;
//			this.availablePositions.add(waypoint);
//		}
//		this.nextPosition = this.availablePositions.get(0);
//	}
//	
//	public void findNext() {
//		for (int i = 0; i < this.availablePositions.size(); i++) {
//			this.nextPosition = this.availablePositions.get(i);
//			for (int o = 0; o < this.takenPositions.size(); o++) {
//				if (this.takenPositions.contains(this.availablePositions.get(i))) {
//					this.nextPosition = null;
//					break;
//				}
//			}
//			if (this.nextPosition != null)
//				break;
//		}
//	}
	
	public void addLanded(Plane x) {
		landedPlanes.add(x);
//		this.takenPositions.add(this.nextPosition);
//		this.findNext();
//		x.setActive(false);
	}
	
	
}

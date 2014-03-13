package seprini.models;

import com.badlogic.gdx.math.Vector2;

import seprini.data.Art;

public final class Airport extends Entity{

	Waypoint runwayEnd;
	Waypoint runwayMid;
	Waypoint runwayStart;
	
	public Airport(Vector2 midPoint) {
		
		this.runwayMid = new Waypoint(midPoint.x, midPoint.y, false);
		this.runwayStart = new Waypoint(midPoint.x, midPoint.y -60, false);
		this.runwayEnd = new Waypoint(midPoint.x, midPoint.y +60, false);
		
		this.coords = new Vector2(387, 335);
		this.size = new Vector2(154, 120);
		this.texture = Art.getTextureRegion("airport");
	}
	
	
	public Waypoint getStart(){
		return runwayStart;
	}
	
	public Waypoint getEnd(){
		return runwayEnd;
	}
	
	public Waypoint getMid(){
		return runwayMid;
	}
	
}

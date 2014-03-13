package seprini.models;

import com.badlogic.gdx.math.Vector2;

import seprini.data.Art;
import seprini.data.Config;

public final class Airport extends Entity{

	Waypoint runwayEnd = new Waypoint(464, 395, false);
	Waypoint runwayMid = new Waypoint(387, 335, false);
	Waypoint runwayStart = new Waypoint(310, 275, false);
	
	public Airport() {
		this.coords = new Vector2(154,120);
		this.size = Config.AIRSPACE_SIZE;
		this.texture = Art.getTextureRegion("airspace");
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

package seprini.models;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import seprini.data.Art;

public final class Airport extends Entity{

	Waypoint runwayEnd;
	Waypoint runwayMid;
	Waypoint runwayStart;
	Waypoint approach;
	Queue<Aircraft> landedPlanes = new LinkedList<Aircraft>();
	
	private final boolean visible = true;

	public Airport(Vector2 midPoint) {
		this.runwayMid = new Waypoint(midPoint.x, midPoint.y, false);
		this.runwayStart = new Waypoint(midPoint.x, midPoint.y -60, false);
		this.runwayEnd = new Waypoint(midPoint.x, midPoint.y +60, false);
		this.approach = new Waypoint(runwayStart.getX(), runwayStart.getY() - 100, false);
		this.debugShape = true;
		this.coords = new Vector2(midPoint.x, midPoint.y);
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
	
	public Queue<Aircraft> getLandedPlanes(){
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
		if (visible){
			super.draw(batch, parentAlpha);
		}
	}

	public Waypoint getApproach(){
		return approach;
	}
	
	public void addLanded(Aircraft x){
		landedPlanes.add(x);
		x.setActive(false);
	}
	
}

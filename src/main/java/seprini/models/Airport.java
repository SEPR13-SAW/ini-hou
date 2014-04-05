package seprini.models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import seprini.data.Art;

public final class Airport extends Entity{
	Waypoint runwayEnd;
	Waypoint runwayMid;
	Waypoint runwayStart;
	Waypoint approach;
	Queue<Aircraft> landedPlanes = new LinkedList<Aircraft>();
	TextureRegion aircraft;
	float[] landedPositionsX = new float[10];
	float[] landedPositionsY = new float[10];
	
	
	
	private final boolean visible = true;
	private int id;

	public Airport(Vector2 midPoint, int ID) {
		this.runwayMid = new Waypoint(midPoint.x, midPoint.y, false);
		this.runwayStart = new Waypoint(midPoint.x, midPoint.y -60, false);
		this.runwayEnd = new Waypoint(midPoint.x, midPoint.y +60, false);
		this.approach = new Waypoint(runwayStart.getX(), runwayStart.getY() - 100, false);
		this.debugShape = true;
		this.coords = new Vector2(midPoint.x, midPoint.y);
		this.size = new Vector2(154, 120);
		this.texture = Art.getTextureRegion("airport");
		this.aircraft = Art.getTextureRegion("aircraft");
		this.id = ID;
		this.adjustPositions();
	}
	
	public void adjustPositions(){
		int offsetX = 40, offsetY = 25, stepY = 20;
		for(int i = 0; i < 5; i++){
			this.landedPositionsX[i] = this.runwayStart.getX() + offsetX;
			this.landedPositionsY[i] = this.runwayStart.getY() + offsetY;
			offsetY += stepY;
		};
		offsetY = 5;
		offsetX = - 35;
		
		for(int o = 5; o < 10; o++){
			this.landedPositionsX[o] = this.runwayStart.getX() + offsetX;
			this.landedPositionsY[o] = this.runwayStart.getY() + offsetY;
			offsetY += stepY;
		}
	}
	
	public int getID(){
		return this.id;
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
			for(int i = 0; i < this.landedPlanes.size(); i++)
				if(i < 5)
					batch.draw(aircraft, this.landedPositionsX[i], this.landedPositionsY[i], 0, 0, aircraft.getRegionWidth(), aircraft.getRegionHeight(), 0.3f, 0.3f, 160f);
				else
					batch.draw(aircraft, this.landedPositionsX[i], this.landedPositionsY[i], 0, 0, aircraft.getRegionWidth(), aircraft.getRegionHeight(), 0.3f, 0.3f, 20f);
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

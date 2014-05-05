package com.planepanic.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.planepanic.model.resources.Art;
/**
 * 
 * @author Mantas
 * 
 * A score bar for the competitive multiplayer.
 * It is manipulated by increasing or decreasing the amount of red color
 * for simplicity
 *
 */
public class ScoreBar extends Entity {
	private final float lengthBar = 200;
	private final float height = 20;
	private float positionX = 440, positionY = 0;
	private float step = 10;
	private float lengthRed, lengthBlue;
	public ScoreBar(){
		this.texture = Art.getTextureRegion("dot");
		this.lengthRed = lengthBar / 2;
		this.lengthBlue = lengthRed;
	}

	public void increaseRed(){
		if(this.lengthRed > 0 && this.lengthRed < this.lengthBar)
			this.lengthRed += this.step;
		this.lengthBlue = this.lengthBar - this.lengthRed;
		System.out.println("red" + lengthRed);
		System.out.println("blue" + lengthBlue);
		isRedBarFull();
		

	}
	
	public void increaseBlue(){
		if(this.lengthRed > 0 && this.lengthRed < this.lengthBar)
			this.lengthRed -= this.step;
		this.lengthBlue = this.lengthBar - this.lengthRed;


	}
	
	public boolean isBlueBarFull(){
		if(this.lengthRed == 0)
			return true;
		else
			return false;
	}
	
	public boolean isRedBarFull(){
		if(this.lengthRed == this.lengthBar){
			System.out.println("red full");
			return true;	
		}else
			return false;
	}
	
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setColor(Color.BLUE);
		batch.draw(this.texture, this.positionX, this.positionY, lengthBlue, this.height);
		batch.setColor(Color.RED);
		batch.draw(this.texture, this.positionX + this.lengthBlue, this.positionY, lengthRed, this.height);
		batch.setColor(Color.BLACK);
		//Draws the line in the middle of the screen
		batch.draw(this.texture, (this.getStage().getWidth() - 200) / 2, 0, 2, this.getStage().getHeight());
	}
}

package seprini.models;

import seprini.data.Art;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	}
	
	public void decreaseRed(){
		if(this.lengthRed > 0 && this.lengthRed < this.lengthBar)
			this.lengthRed -= this.step;
		this.lengthBlue = this.lengthBar - this.lengthRed;
	}
	
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setColor(Color.BLUE);
		batch.draw(this.texture, this.positionX, this.positionY, lengthBlue, this.height);
		batch.setColor(Color.RED);
		batch.draw(this.texture, this.positionX + this.lengthBlue, this.positionY, lengthRed, this.height);
	}
}

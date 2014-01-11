package seprini.screens;

import seprini.ATC;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Screen {

	public final static int WIDTH = Gdx.graphics.getWidth();
	public final static int HEIGHT = Gdx.graphics.getHeight();

	/**
	 * Instance of the main class, used for changing screen among other things
	 */
	private ATC atc;

	/**
	 * A sprite batch, used to draw textures
	 */
	protected final static SpriteBatch spriteBatch = new SpriteBatch();

	/**
	 * Simple way of writing on the screen, uses 15pt Arial font by default
	 */
	private final static BitmapFont font = new BitmapFont();
	
	public final static ShapeRenderer shapeDebugger = new ShapeRenderer();

	/**
	 * Dispose of unused resources
	 */
	public void removed() {
		spriteBatch.dispose();
	}

	/**
	 * Initialise all the required default parameters
	 * 
	 * @param atc
	 */
	public final void init(ATC atc) {
		this.atc = atc;
	}
	/**
	 * Convenience method for the main setScreen method
	 * 
	 * @param screen
	 */
	public void setScreen(Screen screen) {
		atc.setScreen(screen);
	}

	/**
	 * Draw a texture from a texture region
	 * 
	 * @param region
	 * @param x
	 * @param y
	 */
	public void draw(TextureRegion region, int x, int y) {
		if (region == null)
			Gdx.app.error("Screen", "Region drawing is null");

		int width = region.getRegionWidth();

		if (width < 0)
			width = -width;

		spriteBatch.draw(region, x, y, width, region.getRegionHeight());
	}

	/**
	 * Draw a texture from a sprite
	 * 
	 * @param sprite
	 */
	public void drawSprite(Sprite sprite) {
		sprite.draw(spriteBatch);
	}

	/**
	 * Draws a string using the default 15pt Arial font
	 * 
	 * @param str
	 * @param x
	 * @param y
	 * @param color
	 */
	public void drawString(CharSequence str, float x, float y, Color color,
			SpriteBatch batch) {
		batch.begin();
		font.setColor(color);
		font.draw(batch, str, x, y);
		batch.end();
	}

	public abstract void render();

	public abstract void update();

}
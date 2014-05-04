package seprini.screens;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;
import seprini.ATC;
import seprini.data.Art;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import seprini.data.Config;

public class AbstractScreen implements ScreenBase
{
	public final static ShapeRenderer shapeRenderer = new ShapeRenderer();

	private final ATC atc;
	private final Stage stage;
	private boolean paused;

	/**
	 * Initializes the AbstractScreen class with an ATC game reference
	 *
	 * @param game reference to game
	 */
	public AbstractScreen(ATC game)
	{
		this.atc = game;
		this.stage = new Stage(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
	}

	/**
	 * Returns the stage at the root of the scene graph
	 *
	 * @return stage to draw to
	 */
	public Stage getStage()
	{
		return stage;
	}

	/**
	 * Returns game object controlling this screen
	 *
	 * @return root game object
	 */
	@Override
	public ATC getGame()
	{
		return atc;
	}

	@Override
	public boolean isPaused() { return paused; }

	@Override
	public void setPaused(boolean paused) { this.paused = paused; }

	@Override
	public void render(float delta)
	{
		// Clear screen
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// Act and render stage
		if (!paused)
			stage.act(delta);

		stage.draw();
	}

	@Override
	public void resize(int width, int height)
	{
		// Reset stage viewport
		stage.setViewport(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
	}

	@Override
	public void show()
	{
		// Set new input processor
		Gdx.input.setInputProcessor(stage);
		setPaused(false);
	}

	@Override
	public void dispose()
	{
		stage.dispose();
	}

	@Override public void hide() { }
	@Override public void pause() { }
	@Override public void resume() { }

	/**
	 * Draw a texture from a texture region
	 *
	 * @param region
	 * @param x
	 * @param y
	 */
	public static void draw(TextureRegion region, int x, int y, SpriteBatch batch) {

		if (region == null)
			throw new IllegalArgumentException("region to draw to is null");

		int width = region.getRegionWidth() + 1;

		if (width < 0)
			width = -width;

		batch.begin();
		batch.draw(region, x, y, width, region.getRegionHeight());
		batch.end();
	}

	/**
	 * Draws a string using the default 15pt Arial font
	 *
	 * @param str
	 * @param x
	 * @param y
	 * @param color
	 */
	public static void drawString(CharSequence str, float x, float y,
			Color color, SpriteBatch batch, boolean isNative, float scale) {
		BitmapFont font = Art.getSkin().get(BitmapFont.class);

		if (!isNative)
			batch.begin();

		font.setColor(color);
		font.setScale(scale);
		font.draw(batch, str, x, y);

		if (!isNative)
			batch.end();
	}
}

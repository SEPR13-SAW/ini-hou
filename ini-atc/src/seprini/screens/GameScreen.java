package seprini.screens;

import seprini.controllers.AircraftController;
import seprini.data.Config;
import seprini.data.GameDifficulty;
import seprini.data.GameState;
import seprini.models.Airspace;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * The game screen - all game logic starts here
 * 
 * @author Paulius, Miguel
 * 
 */
public class GameScreen extends Screen {

	private final Stage root;
	private final AircraftController controller;

	public GameScreen(GameDifficulty diff) {

		// create a table layout, main ui
		Table ui = new Table();

		// create a separate layout for sidebar with all the buttons and
		// required info
		Table sidebar = new Table();

		// create and add the Airspace group, contains aircraft and waypoints
		Airspace airspace = new Airspace();

		controller = new AircraftController(diff, airspace, sidebar, this);
		root = new Stage();

		root.setKeyboardFocus(airspace);

		Gdx.input.setInputProcessor(root);

		// make it fill the whole screen
		ui.setFillParent(true);
		root.addActor(ui);

		airspace.addListener(controller);
		ui.add(airspace).width(Config.AIRSPACE_SIZE.x)
				.height(Config.AIRSPACE_SIZE.y);

		// Temporary background creator for sidebar
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();

		// set the temporary background
		sidebar.setBackground(new TextureRegionDrawable(new TextureRegion(
				new Texture(pixmap))));

		// move the sidebar to the top right, add it to the main table and set
		// its size
		sidebar.top().right();
		ui.add(sidebar).width(Config.SIDEBAR_SIZE.x)
				.height(Config.SIDEBAR_SIZE.y);

		controller.init();
	}

	@Override
	public void render() {
		root.draw();

		if (Config.DEBUG) {
			Table.drawDebug(root);
			drawString("fps: " + Gdx.graphics.getFramesPerSecond(), 10, 20,
					Color.BLACK, root.getSpriteBatch());
		}
	}

	@Override
	public void update() {
		if (GameState.paused)
			return;

		controller.update();
		root.act();
	}

	@Override
	public void removed() {
		root.dispose();
	}

}
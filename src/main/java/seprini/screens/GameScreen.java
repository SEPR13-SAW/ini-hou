package seprini.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;

import seprini.ATC;
import seprini.controllers.AircraftController;
import seprini.controllers.ClientAircraftController;
import seprini.controllers.SidebarController;
import seprini.controllers.SingleAircraftController;
import seprini.data.Art;
import seprini.data.Config;
import seprini.data.GameDifficulty;
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
 */
public class GameScreen extends AbstractScreen
{
	private final AircraftController controller;

	// Paused state
	//private boolean paused;

	public GameScreen(ATC game, GameDifficulty diff) {

		super(game);

		// create a table layout, main ui
		Stage root = getStage();
		Table ui = new Table();

		// create a separate layout for sidebar with all the buttons and
		// required info
		Table sidebar = new Table();

		if (Config.DEBUG_UI)
			sidebar.debug();

		// create and add the Airspace group, contains aircraft and waypoints
		Airspace airspace = new Airspace();
		if (diff.getMultiplayer()) {
			controller = new ClientAircraftController(diff, airspace, this);
		} else {
			controller = new SingleAircraftController(diff, airspace, this);
		}
		root.setKeyboardFocus(airspace);

		// create sidebar
		final SidebarController sidebarController = new SidebarController(sidebar, controller, this);

		// set controller update as first actor
		ui.addActor(new Actor()
		{
			@Override
			public void act(float delta)
			{
				controller.update(delta);
				sidebarController.update();
			}
		});

		// make it fill the whole screen
		ui.setFillParent(true);
		root.addActor(ui);

		airspace.addListener(controller);
		ui.add(airspace).width(Config.AIRSPACE_SIZE.x)
				.height(Config.AIRSPACE_SIZE.y);

		// Temporary background creator for sidebar
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(new Color(0.31f, 0.33f, 0.32f, 1));
		pixmap.fill();

		// set the temporary background
		sidebar.setBackground(new TextureRegionDrawable(new TextureRegion(
				new Texture(pixmap))));

		// move the sidebar to the top right, add it to the main table and set
		// its size
		ui.add(sidebar).width(Config.SIDEBAR_SIZE.x)
				.height(Config.SIDEBAR_SIZE.y);

		Art.getSound("ambience").playLooping(0.7f);
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		// debug the ui and draw fps
		if (Config.DEBUG_UI) {
			Stage root = getStage();

			Table.drawDebug(root);
			drawString("fps: " + Gdx.graphics.getFramesPerSecond(), 10, 20,
					Color.BLACK, root.getSpriteBatch(), false, 1);
		}
	}

}

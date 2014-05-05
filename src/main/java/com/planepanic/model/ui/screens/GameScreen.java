package com.planepanic.model.ui.screens;

import java.net.InetSocketAddress;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.planepanic.ATC;
import com.planepanic.io.client.Player;
import com.planepanic.model.Airspace;
import com.planepanic.model.Config;
import com.planepanic.model.Difficulty;
import com.planepanic.model.resources.Art;
import com.planepanic.model.ui.controllers.SidebarController;

/**
 * The game screen - all game logic starts here
 */
public class GameScreen extends AbstractScreen {
	public GameScreen(ATC game, Difficulty difficulty, boolean host, InetSocketAddress address, String name) {
		super(game);

		// create a table layout, main ui
		Stage root = getStage();
		Table ui = new Table();

		// create a separate layout for sidebar with all the buttons and
		// required info
		Table sidebar = new Table();

		if (Config.DEBUG)
			sidebar.debug();

		// create and add the Airspace group, contains aircraft and waypoints
		final Airspace airspace = new Airspace(difficulty, new Player(0, ""), null, null);
		root.setKeyboardFocus(airspace);

		// create sidebar
		final SidebarController sidebarController = new SidebarController(sidebar, airspace, this);

		ui.addActor(new Actor() {
			@Override
			public void act(float delta) {
				airspace.tick(delta);
				sidebarController.update();
			}
		});

		// make it fill the whole screen
		ui.setFillParent(true);
		root.addActor(ui);

		//airspace.addListener(controller);
		ui.add(airspace);

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
		if (Config.DEBUG) {
			Stage root = getStage();

			Table.drawDebug(root);
			drawString("fps: " + Gdx.graphics.getFramesPerSecond(), 10, 20,
					Color.BLACK, root.getSpriteBatch(), false, 1);
		}
	}

}

package com.planepanic.model.screens;

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
import com.planepanic.io.client.Client;
import com.planepanic.io.packet.JoinGamePacket;
import com.planepanic.io.server.Server;
import com.planepanic.model.Airspace;
import com.planepanic.model.Art;
import com.planepanic.model.Config;
import com.planepanic.model.GameDifficulty;
import com.planepanic.model.controllers.AircraftController;
import com.planepanic.model.controllers.ClientAircraftController;
import com.planepanic.model.controllers.SidebarController;
import com.planepanic.model.controllers.SingleAircraftController;

/**
 * The game screen - all game logic starts here
 */
public class GameScreen extends AbstractScreen
{
	private final AircraftController controller;

	// Paused state
	//private boolean paused;
	private Client client;

	public GameScreen(ATC game, GameDifficulty diff, boolean host, InetSocketAddress address, String name) {

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
		Server server = null;
		Airspace airspace = new Airspace();
		if (diff.getMultiplayer()) {
			if (host) {
				server = new Server(Server.PORT);
				new Thread(server).start();
			}
			if (host) address = new InetSocketAddress("127.0.0.1", Server.PORT);
			client = new Client(address, new Runnable() {
				@Override
				public void run() {
					try {
						client.writePacket(new JoinGamePacket(client.getName()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, name);
			controller = new ClientAircraftController(diff, airspace, this, client);
			client.setController((ClientAircraftController) controller);
		} else {
			controller = new SingleAircraftController(diff, airspace, this);
		}
		root.setKeyboardFocus(airspace);

		// create sidebar
		final SidebarController sidebarController = new SidebarController(sidebar, controller, this);

		// set controller update as first actor
		if (server == null || host == false) {
			ui.addActor(new Actor() {
				@Override
				public void act(float delta)
				{
					controller.update(delta);
					sidebarController.update();
				}
			});
		} else {
			final Server s = server;
			ui.addActor(new Actor() {
				@Override
				public void act(float delta)
				{
					if (s.getController() != null)
						s.getController().update(delta);
					controller.update(delta);
					sidebarController.update();
				}
			});
		}

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

package com.planepanic.model.ui.controllers;

import java.util.HashMap;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.esotericsoftware.tablelayout.Cell;
import com.planepanic.model.Airspace;
import com.planepanic.model.Config;
import com.planepanic.model.Difficulty;
import com.planepanic.model.entity.Plane;
import com.planepanic.model.entity.Plane.State;
import com.planepanic.model.resources.Art;
import com.planepanic.model.ui.screens.ScreenBase;

/**
 * Controls the sidebar in the GameScreen 
 */
public final class SidebarController extends ChangeListener {
	private final Airspace airspace;

	private final HashMap<String, TextButton> buttons = new HashMap<String, TextButton>();
	private final HashMap<String, Label> labels = new HashMap<String, Label>();

	private final ScreenBase screen;

	// UI wrappers for the controls and the buttons at the bottom
	private Table sidebar, aircraftControls, bottomButtons;

	/**
	 * 
	 * 
	 * @param sidebar
	 *            the sidebar layout, so the controller can add all of the
	 *            buttons
	 * @param aircrafts
	 *            so this can get the selected aircraft
	 * @param screen
	 *            for changing screens once Menu or Pause have been clicked
	 */
	public SidebarController(Table sidebar, Airspace airspace, ScreenBase screen) {
		this.sidebar = sidebar;
		this.airspace = airspace;
		this.screen = screen;
		this.init();
	}

	/**
	 * Initialise all the buttons and labels
	 */
	private void init() {

		// wrapper for aicraft controls
		aircraftControls = new Table();
		aircraftControls.setFillParent(true);

		if (Config.DEBUG)
			aircraftControls.debug();

		aircraftControls.top();
		sidebar.addActor(aircraftControls);

		// wrapper for bottom buttons
		bottomButtons = new Table();

		bottomButtons.setFillParent(true);

		if (Config.DEBUG)
			aircraftControls.debug();

		bottomButtons.bottom();
		sidebar.addActor(bottomButtons);

		// adding labels to aircraft controls
		createLabel("speed", " Speed: ", aircraftControls).width(200)
				.colspan(2);

		aircraftControls.row();

		createLabel("altitude", " Altitude: ", aircraftControls).width(200)
				.colspan(2);

		aircraftControls.row();

		// adding buttons to aircraft controls

		//createButton("assignWaypoint", " Assign Waypoint", aircraftControls,
		//		true).width(200).colspan(2);

		//aircraftControls.row();

		createButton("returnToPath", " Return to Path (R)", aircraftControls, false)
				.width(200).colspan(2);

		aircraftControls.row();

		createButton("accelerate", " Accelerate (E)", aircraftControls, false)
				.width(200).colspan(2);
		
		aircraftControls.row().colspan(2);

		createButton("decelerate", " Decelerate (Q)", aircraftControls, false)
				.width(200);
		
		aircraftControls.row().colspan(2);
		
		createButton("takeOff", "Take Off (T)", aircraftControls, false).width(200);

		aircraftControls.row().colspan(2);
		
		createButton("land", "Land (F)", aircraftControls, false).width(200);


		aircraftControls.row().spaceTop(100);

		createButton("up", " Up (W)", aircraftControls, true).width(100)
				.colspan(2);

		aircraftControls.row();

		createButton("left", " Left (A)", aircraftControls, true).width(100);
		createButton("right", "Right (D)", aircraftControls, true).width(100);

		aircraftControls.row();

		createButton("down", "Down (S)", aircraftControls, true).width(100)
				.colspan(2);

		aircraftControls.row();

		createLabel("", " Time:", bottomButtons).width(100);
		createLabel("timer", "..", bottomButtons).width(100);
		
		bottomButtons.row();
		
		createLabel("", " Score:", bottomButtons).width(100);
		createLabel("score", "..", bottomButtons).width(100);
		
		bottomButtons.row();

		// adding buttons to bottom
		createButton("menu", " Menu", bottomButtons, false).width(100);
		createButton("pause", " Pause", bottomButtons, false).width(100);
	}

	/**
	 * Update the sidebar according to changes in the AircraftController
	 */
	public void update() {
		if (airspace.getDifficulty() != Difficulty.MULTIPLAYER_CLIENT) {
			labels.get("score").setText(Integer.toString(airspace.getPlayer().getScore()));
		}

		labels.get("timer").setText(Integer.toString((int) airspace.getTime()));

		Plane selected = airspace.getSelected();

		String altitudeText, speedText;
		if (selected != null) {
			altitudeText = " Altitude: " + selected.getAltitude() + "m";
			speedText = " Speed: " + Math.round(selected.getVelocity()
							* Config.AIRCRAFT_SPEED_MULTIPLIER) + "km/h";
		} else {
			altitudeText = " Altitude: ";
			speedText = " Speed: ";
		}

		labels.get("altitude").setText(altitudeText);
		labels.get("speed").setText(speedText);
	}

	/**
	 * Convenience method to create buttons and add them to the sidebar
	 * 
	 * @param name
	 * @param text
	 * @return
	 */
	private Cell<?> createButton(String name, String text, Table parent,
			boolean toggle) {
		TextButton button = new TextButton(text, Art.getSkin(), (toggle)
				? "toggle"
				: "default");
		button.pad(3);
		button.addListener(this);

		buttons.put(name, button);

		return parent.add(button);
	}

	/**
	 * Convinience method to create labels and add them to the sidebar
	 * 
	 * @param name
	 * @param text
	 * @return
	 */
	@SuppressWarnings("unused")
	private Cell<?> createLabel(String name, String text) {
		Label label = new Label(text, Art.getSkin());
		labels.put(name, label);

		return sidebar.add(label);
	}

	/**
	 * Convinience method to create labels and add them to the sidebar
	 * 
	 * @param name
	 * @param text
	 * @return
	 */
	private Cell<?> createLabel(String name, String text, Table parent) {
		Label label = new Label(text, Art.getSkin());

		labels.put(name, label);

		return parent.add(label);
	}

	@Override
	public void changed(ChangeEvent event, Actor actor) {
		if (!screen.isPaused()) {
			Plane selectedAircraft = airspace.getSelected();
			if (selectedAircraft != null) {
				if (actor.equals(buttons.get("left"))) {
					selectedAircraft.setState(State.FLIGHTPLAN);
				}

				if (actor.equals(buttons.get("left"))) {
					airspace.setLeft(!airspace.isLeft());
				}

				if (actor.equals(buttons.get("right"))) {
					airspace.setRight(!airspace.isRight());
				}

				if (actor.equals(buttons.get("land"))) {
					selectedAircraft.setState(State.APPROACHING);
					airspace.sendLand();
				}


				if (actor.equals(buttons.get("up"))) {
					airspace.setUp(!airspace.isUp());
				}

				if (actor.equals(buttons.get("down"))) {
					airspace.setDown(!airspace.isDown());
				}

				if (actor.equals(buttons.get("accelerate"))) {
					selectedAircraft.setVelocity(selectedAircraft.getVelocity() + 5);
						if (selectedAircraft.getVelocity() > Config.MAX_VELOCITY) selectedAircraft.setVelocity(Config.MAX_VELOCITY);
				}

				if (actor.equals(buttons.get("decelerate"))) {
					selectedAircraft.setVelocity(selectedAircraft.getVelocity() + 5);
					if (selectedAircraft.getVelocity() < Config.MIN_VELOCITY) selectedAircraft.setVelocity(Config.MIN_VELOCITY);
				}

			}
			

			if (actor.equals(buttons.get("takeOff"))) {
				if (airspace.getRunways().get(airspace.getPlayer().getId())
						.getLandedPlanes().size() != 0) {
					Plane plane = airspace.getRunways()
							.get(airspace.getPlayer().getId())
							.getLandedPlanes().poll();
					plane.takeOff();
					airspace.getRunways().get(airspace.getPlayer().getId()).getTakenPositions().poll();
					airspace.getRunways().get(airspace.getPlayer().getId()).findNext();
				}
			}
			
		}

		if (actor.equals(buttons.get("menu"))) {
			Art.getSound("ambience").stop();
			screen.getGame().showMenuScreen();
		}

		if (actor.equals(buttons.get("pause"))) {
			screen.setPaused(!screen.isPaused());
		}

	}
}

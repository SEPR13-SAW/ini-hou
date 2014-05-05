package com.planepanic.model.controllers.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.planepanic.model.Debug;
import com.planepanic.model.Entrypoint;
import com.planepanic.model.Exitpoint;
import com.planepanic.model.Waypoint;
import com.planepanic.model.controllers.aircraft.AircraftController;

public class WaypointComponent {
	
	private ArrayList<Waypoint> permanentList = new ArrayList<Waypoint>();
	private ArrayList<Entrypoint> entryList = new ArrayList<Entrypoint>();
	private ArrayList<Exitpoint> exitList = new ArrayList<Exitpoint>();

	private final AircraftController controller;
	
	public WaypointComponent(AircraftController controller) {

		this.controller = controller;

		// add entry waypoints to entryList
		createEntrypoint(0, 360);
		createEntrypoint(1080, 360);
		createEntrypoint(540, 720);

		// add exit waypoints to exitList
		createExitpoint(1080, 720);
		createExitpoint(1080, 0);
		createExitpoint(0, 720);
		createExitpoint(0, 0);

		// add some waypoints
		createWaypoint(150, 150);
		createWaypoint(400, 150);
		createWaypoint(400, 360);
		createWaypoint(150, 570);
		createWaypoint(400, 570);
		createWaypoint(930, 150);
		createWaypoint(680, 150);
		createWaypoint(680, 360);
		createWaypoint(930, 570);
		createWaypoint(680, 570);
//		createWaypoint(150, 360);
//		createWaypoint(300, 500);
//		createWaypoint(600, 650);
//		createWaypoint(700, 200);
//		createWaypoint(850, 360);
//		createWaypoint(700, 500);
//		createWaypoint(450, 100);
		
		
		Collections.shuffle(permanentList, new Random());
	}

	/**
	 * Creates a new waypoint.
	 *
	 * <p>
	 * Creates a new user waypoint when the user left-clicks within the airspace
	 * window.
	 * 
	 * Also is convinience method for generated permanent waypoints
	 * 
	 * @param x
	 * @param y
	 */
	public boolean createWaypoint(float x, float y) {
		Debug.msg("Creating waypoint at: " + x + ":" + y);

		final Waypoint waypoint = new Waypoint(x, y, true);

		// add it to the correct list according to whether it is user created or
		// not
		getPermanentList().add(waypoint);

		// add it to the airspace so it is automatically drawn using root.draw()
		controller.getAirspace().addActor(waypoint);

		// add a listener is a user can remove it or redirect aircraft to it
		waypoint.addListener(new ClickListener() {

			/**
			 * Removes a user waypoint if a user waypoint is right-clicked.
			 * Alternatively, should call redirection method.
			 */
			@Override
			public boolean touchDown(InputEvent event, float tX, float tY,
					int pointer, int button) {

				if (button == Buttons.LEFT && controller.allowRedirection()) {
					controller.redirectAircraft(waypoint);
					return true;
				}


				return true;
			}
		});

		return true;
	}
	
	/**
	 * Creates an exitpoint, adds it to the list of exitpoints and adds it to
	 * the airspace
	 * 
	 * @param x
	 * @param y
	 */
	private void createExitpoint(float x, float y) {
		Exitpoint point = new Exitpoint(new Vector2(x, y));
		getExitList().add(point);
		controller.getAirspace().addActor(point);
	}

	/**
	 * Creates an entry point, adds it to the list of exitpoints and adds it to
	 * the airspace
	 * 
	 * @param x
	 * @param y
	 */
	private void createEntrypoint(float x, float y) {
		Entrypoint point = new Entrypoint(new Vector2(x, y));
		getEntryList().add(point);
		controller.getAirspace().addActor(point);
	}

	public ArrayList<Waypoint> getPermanentList() {
		return permanentList;
	}

	public ArrayList<Entrypoint> getEntryList() {
		return entryList;
	}

	public ArrayList<Exitpoint> getExitList() {
		return exitList;
	}
}

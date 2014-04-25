package seprini.controllers;

import java.util.ArrayList;
import java.util.Random;

import seprini.controllers.components.FlightPlanComponent;
import seprini.controllers.components.WaypointComponent;
import seprini.data.Art;
import seprini.data.Config;
import seprini.data.Debug;
import seprini.data.GameDifficulty;
import seprini.models.Aircraft;
import seprini.models.Airport;
import seprini.models.Airspace;
import seprini.models.Map;
import seprini.models.ScoreBar;
import seprini.models.Waypoint;
import seprini.models.types.AircraftType;
import seprini.network.client.Player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public abstract class AircraftController extends InputListener {

	protected Random rand = new Random();

	// aircraft and aircraft type lists
	protected final ArrayList<AircraftType> aircraftTypeList = new ArrayList<AircraftType>();
	protected final ArrayList<Aircraft> aircraftList = new ArrayList<Aircraft>();
	protected final ArrayList<Airport> airportList = new ArrayList<Airport>();

	protected float lastGenerated, lastWarned;
	protected boolean breachingSound, breachingIsPlaying;

	protected Aircraft selectedAircraft;

	protected final GameDifficulty difficulty;

	// helpers for this class
	protected final WaypointComponent waypoints;
	protected final FlightPlanComponent flightplan;

	// ui related
	protected final Airspace airspace;

	protected boolean allowRedirection;

	protected int aircraftId = 0;

	// game timer
	protected float timer = 0;

	// airport selection flag
	protected int airportFlag = 0;

	protected int tabIndex = 0;

	protected Random scoreCheck = new Random();

	public ScoreBar scoreBar;

	/**
	 * 
	 * @param diff game difficulty, changes number of aircraft and time between
	 *            them
	 * @param airspace the group where all of the waypoints and aircraft will be
	 *            added
	 * @param screen
	 */
	public AircraftController(GameDifficulty diff, Airspace airspace) {
		this.difficulty = diff;
		this.airspace = airspace;

		// add the background
		airspace.addActor(new Map());

		// initialise airports
		airportList.add(new Airport(new Vector2(387, 355), 0));
		airportList.add(new Airport(new Vector2(487, 555), 1));
		airspace.addActor(airportList.get(0));
		airspace.addActor(airportList.get(1));

		// manages the waypoints
		this.waypoints = new WaypointComponent(this);

		// helper for creating the flight plan of an aircraft
		this.flightplan = new FlightPlanComponent(waypoints);

		// initialise aircraft types.
		aircraftTypeList.add(new AircraftType()
				.setMaxClimbRate(600)
				.setMinSpeed(30f)
				.setMaxSpeed(90f)
				.setMaxTurningSpeed(48f)
				.setRadius(15)
				.setSeparationRadius(diff.getSeparationRadius())
				.setTexture(Art.getTextureRegion("aircraft"))
				.setInitialSpeed(30f));
		boolean multy = difficulty.getMultiplayer();
		if (multy) {
			this.scoreBar = new ScoreBar();
			airspace.addActor(this.scoreBar);
		}
	}

	/**
	 * Updates the aircraft positions. Generates a new aircraft and adds it to
	 * the stage. Collision Detection. Removes aircraft if inactive.
	 */
	public void update(float delta) {
		// Update timer
		timer += delta;

		breachingSound = false;

		// wait at least 2 seconds before allowing to warn again
		breachingIsPlaying = (timer - lastWarned < 2);

		// Updates aircraft in turn
		// Removes aircraft which are no longer active from aircraftList.
		// Manages collision detection.
		for (int i = 0; i < aircraftList.size(); i++) {
			Aircraft planeI = aircraftList.get(i);

			// Update aircraft.
			planeI.act(delta);
			planeI.setBreaching(false);

			// Collision Detection + Separation breach detection.
			for (Aircraft planeJ : aircraftList) {

				// Quite simply checks if distance between the centres of both
				// the aircraft <= the radius of aircraft i + radius of aircraft
				// j

				if (!planeI.equals(planeJ)
						// Check difference in altitude.
						&& Math.abs(planeI.getAltitude() - planeJ.getAltitude()) < Config.MIN_ALTITUDE_DIFFERENCE
						// Check difference in horizontal 2d plane.
						&& planeI.getCoords().dst(planeJ.getCoords()) < planeI
								.getRadius() + planeJ.getRadius()) {
					collisionHasOccured(planeI, planeJ);
					return;
				}

				// Checking for breach of separation.
				if (!planeI.equals(planeJ)
						// Check difference in altitude.
						&& Math.abs(planeI.getAltitude() - planeJ.getAltitude()) < planeI
								.getSeparationRadius()
						// Check difference in horizontal 2d plane.
						&& planeI.getCoords().dst(planeJ.getCoords()) < planeI
								.getSeparationRadius()) {

					separationRulesBreached(planeI, planeJ);
				}
			}

			// Remove inactive aircraft.
			if (!planeI.isActive()) {
				removeAircraft(i);
			}

		}

		// make sure the breaching sound plays only when a separation breach
		// occurs. Also makes sure it start playing it only one time so there
		// aren't multiple warning sounds at the same time
		if (breachingSound && !breachingIsPlaying) {
			breachingIsPlaying = true;
			lastWarned = timer;
			Art.getSound("warning").play(1.0f);
		}

		// sort aircraft so they appear in the right order
		airspace.sortAircraft();
	}

	/**
	 * Handles what happens after a collision
	 * 
	 * @param a first aircraft that collided
	 * @param b second aircraft that collided
	 */
	protected abstract void collisionHasOccured(Aircraft a, Aircraft b);

	/**
	 * Handles what happens after the separation rules have been breached
	 * 
	 * @param a first aircraft that breached
	 * @param b second aircraft that breached
	 */
	protected void separationRulesBreached(Aircraft a, Aircraft b) {
		// for scoring mechanisms, if applicable
		a.setBreaching(true);
		b.setBreaching(true);
	}

	/**
	 * Generates aircraft of random type with 'random' flight plan.
	 * <p>
	 * Checks if maximum number of aircraft is not exceeded. If it isn't, a new
	 * aircraft is generated with the arguments randomAircraftType() and
	 * generateFlightPlan().
	 * 
	 * @return an <b>Aircraft</b> if the following conditions have been met: <br>
	 *         a) there are no more aircraft on screen than allowed <br>
	 *         b) enough time has passed since the last aircraft has been
	 *         generated <br>
	 *         otherwise <b>null</b>
	 * 
	 */
	protected Aircraft generateAircraft(Player player) {
		// number of aircraft has reached maximum, abort
		if (aircraftList.size() >= difficulty.getMaxAircraft())
			return null;

		// time difference between aircraft generated - depends on difficulty
		// selected
		if (timer - lastGenerated < difficulty.getTimeBetweenGenerations() + rand.nextInt(100))
			return null;

		int landChoice = rand.nextInt(3);
		boolean shouldLand = false;
		if (landChoice == 0) {
			shouldLand = true;
		}

		int airportChoice = rand.nextInt(2);
		Airport airport;
		if (airportChoice == 0) {
			airport = airportList.get(0);
		} else {
			airport = airportList.get(1);
		}

		Aircraft newAircraft = new Aircraft(this, randomAircraftType(),
				flightplan.generate(), aircraftId++, shouldLand, airport, player);

		aircraftList.add(newAircraft);

		// store the time when an aircraft was last generated to know when to
		// generate the next aircraft
		lastGenerated = timer;

		return newAircraft;
	}

	/**
	 * Selects random aircraft type from aircraftTypeList.
	 * 
	 * @return AircraftType
	 */
	private AircraftType randomAircraftType() {
		return aircraftTypeList.get(rand.nextInt(aircraftTypeList.size()));
	}

	/**
	 * Removes aircraft from aircraftList at index i.
	 * 
	 * @param i
	 */
	protected void removeAircraft(int i) {
		Aircraft aircraft = aircraftList.get(i);

		if (aircraft.equals(selectedAircraft))
			selectedAircraft = null;

		// removes the aircraft from the list of aircrafts on screen
		aircraftList.remove(i);

		// removes the aircraft from the stage
		aircraft.remove();
	}

	/**
	 * Redirects aircraft to another waypoint.
	 * 
	 * @param waypoint Waypoint to redirect to
	 */
	public void redirectAircraft(Waypoint waypoint) {
		Debug.msg("Redirecting aircraft " + 0 + " to " + waypoint);

		if (getSelectedAircraft() == null || getSelectedAircraft().isLanded() == true)
			return;

		getSelectedAircraft().insertWaypoint(waypoint);
	}

	public float getTimer() {
		return timer;
	}

	public Aircraft getSelectedAircraft() {
		return selectedAircraft;
	}

	public ArrayList<Aircraft> getAircraftList() {
		return aircraftList;
	}

	public Airspace getAirspace() {
		return airspace;
	}

	public boolean allowRedirection() {
		return allowRedirection;
	}

	public void setAllowRedirection(boolean value) {
		allowRedirection = value;
	}

	public abstract void incScore(int ammount, Aircraft aircraft);
}

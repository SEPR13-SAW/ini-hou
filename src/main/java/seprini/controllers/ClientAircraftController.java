package seprini.controllers;

import java.util.ArrayList;

import seprini.data.Art;
import seprini.data.Debug;
import seprini.data.GameDifficulty;
import seprini.models.Aircraft;
import seprini.models.Airspace;
import seprini.models.Waypoint;
import seprini.network.client.Client;
import seprini.screens.ScreenBase;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public final class ClientAircraftController extends AircraftController {

	private final ScreenBase screen;

	// game score
	public float score = 0;
	// to delay the take off of planes.
	public float lastTakeOff = 0;

	private final Client client;

	/**
	 * 
	 * @param diff game difficulty, changes number of aircraft and time between
	 *            them
	 * @param airspace the group where all of the waypoints and aircraft will be
	 *            added
	 * @param screen
	 */
	public ClientAircraftController(GameDifficulty diff, Airspace airspace, ScreenBase screen, Client client) {
		super(diff, airspace);
		this.score = 0;
		this.screen = screen;
		this.client = client;
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

			if (planeI.getAltitude() < 0) {
				screen.getGame().showEndScreen(timer, score);
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
	@Override
	protected void collisionHasOccured(Aircraft a, Aircraft b) {
		// stop the ambience sound and play the crash sound
		Art.getSound("ambience").stop();
		Art.getSound("crash").play(0.6f);

		// change the screen to the endScreen
		screen.getGame().showEndScreen(timer, score);
	}

	/**
	 * Handles what happens after the separation rules have been breached
	 * 
	 * @param a first aircraft that breached
	 * @param b second aircraft that breached
	 */
	@Override
	protected void separationRulesBreached(Aircraft a, Aircraft b) {
		// for scoring mechanisms, if applicable
		a.setBreaching(true);
		b.setBreaching(true);

		if (scoreCheck.nextInt(60) == 0) {
			score -= 1;
		}
		breachingSound = true;
	}

	/**
	 * Selects an aircraft.
	 * 
	 * @param aircraft
	 */
	@SuppressWarnings("unused")
	private void selectAircraft(Aircraft aircraft) {
		// Only allows to select planes on the left side of the screen
		if (aircraft.getCoords().x <= (this.getAirspace().getStage().getWidth() - 200) / 2) {
			// make sure old selected aircraft is no longer selected in its own
			// object
			if (selectedAircraft != null) {
				selectedAircraft.setSelected(false);

				// make sure the old aircraft stops turning after selecting a
				// new
				// aircraft; prevents it from going in circles
				selectedAircraft.turnLeft(false);
				selectedAircraft.turnRight(false);
			}

			// set new selected aircraft
			selectedAircraft = aircraft;

			// make new aircraft know it's selected
			selectedAircraft.setSelected(true);
		}
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

	public float getScore() {
		return score;
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

	@Override
	protected void removeAircraft(int i) {
		super.removeAircraft(i);

		Aircraft aircraft = aircraftList.get(i);

		if (aircraft.isMustLand()) {
			score -= 1000;
		}
	}

	@Override
	/**
	 * Enables Keyboard Shortcuts as alternatives to the on screen buttons
	 */
	public boolean keyDown(InputEvent event, int keycode) {
		if (selectedAircraft != null && !screen.isPaused()) {

			if (keycode == Keys.LEFT || keycode == Keys.A)
				selectedAircraft.turnLeft(true);

			if (keycode == Keys.RIGHT || keycode == Keys.D)
				selectedAircraft.turnRight(true);

			if (keycode == Keys.UP || keycode == Keys.W)
				selectedAircraft.increaseAltitude();

			if (keycode == Keys.DOWN || keycode == Keys.S)
				selectedAircraft.decreaseAltitude();

			if (keycode == Keys.E)
				selectedAircraft.increaseSpeed();

			if (keycode == Keys.Q)
				selectedAircraft.decreaseSpeed();

			if (keycode == Keys.R)
				selectedAircraft.returnToPath();

			if (keycode == Keys.F && selectedAircraft.getAltitude() == 5000 && selectedAircraft.getAirport().getLandedPlanes().size() < 10) {
				selectedAircraft.landAircraft();
				this.selectedAircraft = null;
			}

		}

		if (keycode == Keys.T) {
			if (airportList.get(airportFlag).getLandedPlanes().size() != 0 && this.getTimer() - this.lastTakeOff > 2) {
				this.lastTakeOff = this.getTimer();
				Aircraft aircraft = airportList.get(airportFlag).getLandedPlanes().poll();
				airportList.get(airportFlag).takenPositions.poll();
				airportList.get(airportFlag).findNext();
				aircraft.setActive(true);
				aircraft.setSelected(false);
				this.aircraftList.add(aircraft);
				this.airspace.addActor(aircraft);
				aircraft.takeOff();
			}
			if (airportFlag == 0) {
				airportFlag = 1;
			} else {
				airportFlag = 0;
			}
		}

		if (keycode == Keys.SPACE)
			screen.setPaused(!screen.isPaused());

		if (keycode == Keys.U)
			this.scoreBar.increaseRed();
		if (keycode == Keys.J)
			this.scoreBar.increaseBlue();

		if (keycode == Keys.ESCAPE) {
			Art.getSound("ambience").stop();
			screen.getGame().showMenuScreen();
		}

		if (keycode == Keys.TAB && this.aircraftList.size() != 0) {

			int listSize = this.aircraftList.size();

			if (this.selectedAircraft != null) {
				tabIndex = this.aircraftList.indexOf(selectedAircraft) + 1;
				this.selectedAircraft.returnToPath();
				this.selectedAircraft.setSelected(false);
				this.selectedAircraft = null;
			}

			if ((tabIndex) % listSize == 0 || listSize == 1)
				tabIndex = 0;

			this.selectedAircraft = this.aircraftList.get(tabIndex);
			this.selectedAircraft.setSelected(true);
		}

		return false;
	}
	
	

	@Override
	/**
	 * Enables Keyboard Shortcuts to disable the turn left and turn right buttons on screen
	 */
	public boolean keyUp(InputEvent event, int keycode) {

		if (selectedAircraft != null) {

			if (keycode == Keys.LEFT || keycode == Keys.A)
				selectedAircraft.turnLeft(false);

			if (keycode == Keys.RIGHT || keycode == Keys.D)
				selectedAircraft.turnRight(false);

		}

		return false;
	}

	@Override
	public void incScore(int ammount, Aircraft aircraft) {
		score += ammount;
	}

}

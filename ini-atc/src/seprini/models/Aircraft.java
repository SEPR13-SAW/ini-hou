package seprini.models;

import java.util.ArrayList;
import java.util.Calendar;

import seprini.data.Config;
import seprini.models.types.AircraftType;
import seprini.screens.Screen;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public final class Aircraft extends Entity {

	private int altitude;
	protected Vector2 velocity = new Vector2(0, 0);

	protected float radius;
	protected float separationRadius;

	protected ArrayList<Waypoint> waypoints;

	protected float maxTurningRate;
	protected float maxClimbRate;
	protected float maxSpeed;

	protected int sepRulesBreachCounter;
	protected int lastTimeTurned;

	protected boolean isActive = true;
	protected boolean turningFlag; // May not be used

	// used for smooth rotation, to remember the original angle to the next
	// waypoint
	private float startAngle;

	// whether the aircraft is selected by the player
	private boolean selected;

	public Aircraft(AircraftType aircraftType, ArrayList<Waypoint> flightPlan) {

		// allows drawing debug shape of this entity
		debugShape = true;

		// initialise all of the aircraft values according to the passed
		// aircraft type
		radius = aircraftType.getRadius();
		separationRadius = aircraftType.getSeparationRadius();
		texture = aircraftType.getTexture();
		maxTurningRate = aircraftType.getMaxTurningSpeed();
		maxClimbRate = aircraftType.getMaxClimbRate();
		maxSpeed = aircraftType.getMaxSpeed();
		velocity = aircraftType.getVelocity();

		// set the flightplan to the generated by the controller
		waypoints = flightPlan;

		// set the size
		size = new Vector2(76, 63);

		// set the coords to the entry point, remove it from the flight plan
		Waypoint entryPoint = flightPlan.get(0);
		coords = new Vector2(entryPoint.getX(), entryPoint.getY());
		flightPlan.remove(0);

		// set origin to center of the aircraft, makes rotation more intuitive
		this.setOrigin(getWidth() / 2, getHeight() / 2);

		this.setScale(0.5f);

		// set bounds so the aircraft is clickable
		this.setBounds(getX() - getWidth() / 1.5f, getY() - getWidth() / 1.5f,
				getWidth() * 2, getHeight() * 2);

		// set rotation & velocity angle to fit next waypoint
		float angleToWaypoint = Math.round(angleToWaypoint());
		
		this.setRotation(angleToWaypoint);
	}

	/**
	 * Update the aircraft rotation & position
	 */
	public void act() {
		// Vector to next waypoint
		Vector2 nextWaypoint = vectorToWaypoint();

		// degrees to nextWaypoint relative to aircraft
		float degrees = (float) ((Math.atan2(getX() - nextWaypoint.x,
				-(getY() - nextWaypoint.y)) * 180.0f / Math.PI) + 90.0f);

		// round it to 2 points after decimal so it's not rotating forever
		float rounded = Math.round(degrees * 100.0f) / 100.0f;

		// smoothly rotate aircraft sprite
		// if current rotation is not the one needed
		if (getRotation() != rounded) {
			// set the startAngle to remember it
			startAngle = rounded;

			// making sure we rotate to the correct side, otherwise may results
			// in a helicopter with no tail rotor
			if (startAngle < getRotation()) {
				rotate(-maxTurningRate);
			} else {
				rotate(maxTurningRate);
			}
		}

		// Calculating vector to waypoint to check how close we are to it
		Vector2 vectorToWaypoint = nextWaypoint.sub(coords);

		// checking whether aircraft is at the next waypoint (close enough =
		// 10px)
		if (vectorToWaypoint.len() < 15) {
			isActive();
			waypoints.remove(0);
		}

		// set velocity angle to fit rotation, allows for smooth turning
		velocity.setAngle(getRotation());

		// finally updating coordinates
		coords.add(velocity);

		// updating bounds to make sure the aircraft is clickable
		this.setBounds(getX() - getWidth() / 1.5f, getY() - getWidth() / 1.5f,
				getWidth() * 2, getHeight() * 2);
	}

	/**
	 * Calculates the vector to the next waypoint
	 * 
	 * @return 3d vector to the next waypoint
	 */
	private Vector2 vectorToWaypoint() {
		// Creates a new vector to store the new velocity in temporarily
		Vector2 nextWaypoint = new Vector2();

		// converts waypoints coordinates into 3d vectors to enabled
		// subtraction.
		nextWaypoint.x = waypoints.get(0).getCoords().x
				+ (Config.WAYPOINT_SIZE.x / 2);
		nextWaypoint.y = waypoints.get(0).getCoords().y
				+ (Config.WAYPOINT_SIZE.y / 2);

		// round it to 2 points after decimal, makes it more mangeable later
		nextWaypoint.x = (float) (Math.round(nextWaypoint.x * 100.0) / 100.0);
		nextWaypoint.y = (float) (Math.round(nextWaypoint.y * 100.0) / 100.0);

		return nextWaypoint;
	}

	/**
	 * Calculate angle to the next waypoint
	 * 
	 * @return
	 */
	private float angleToWaypoint() {
		Vector2 nextWaypoint = vectorToWaypoint();

		return angleToWaypoint(nextWaypoint);
	}

	/**
	 * Calculate angle to the next waypoint, use this if you already have the 3d
	 * vector to the next waypoint
	 * 
	 * @return
	 */
	private static float angleToWaypoint(Vector2 nextWaypoint) {
		// setting angle using the waypoint's size so it heads towards
		// the centre of the waypoint
		nextWaypoint.x += (Config.WAYPOINT_SIZE.x / 2);
		nextWaypoint.y += (Config.WAYPOINT_SIZE.x / 2);

		return nextWaypoint.angle();
	}

	/**
	 * Checks whether the aircraft is within 10 pixels of the next waypoint
	 * 
	 * @param vectorToWaypoint
	 * @return whether aicraft is at the next waypoint
	 */
	public boolean isAtNextWaypoint(Vector3 vectorToWaypoint) {
		if (vectorToWaypoint.len() < 10) {
			isActive();
			waypoints.remove(0);
			return true;
		} else {
			return false;
		}

	}

	public void checkSpeed() {
		this.velocity.clamp(0, this.maxSpeed);
	}

	public void updateCoords() {
		coords.add(velocity);
	}

	/**
	 * Adding a new waypoint to the head of the arraylist
	 * 
	 * @param newWaypoint
	 */
	public void insertWaypoint(Waypoint newWaypoint) {
		waypoints.add(0, newWaypoint);
	}

	/**
	 * Turns right by 5 degrees if the user presses the right key for more than
	 * 2000ms
	 */
	public void turnRight() {
		Vector3 zAxis = new Vector3();
		zAxis.set(0, 0, 1);
		if (delay())
			;
		velocity.rotate(5);
	}

	/**
	 * Turns left by 5 degrees if the user presses the right key for more than
	 * 2000ms
	 */
	public void turnLeft() {
		Vector3 zAxis = new Vector3();
		zAxis.set(0, 0, 1);
		if (delay())
			;
		velocity.rotate(-5);
	}

	/**
	 * Calculates the time for which the buttons have been pressed.
	 * 
	 * @return
	 */
	public boolean delay() {
		Calendar cal = Calendar.getInstance();
		long currentTime = cal.getTimeInMillis();
		long previousTime = currentTime;
		if (currentTime - previousTime >= 2000)
			;
		return true;
	}

	/**
	 * Increases rate of altitude change
	 */
	public void increaseAltitude() {
		// this.altitude += 5;
		// if (this.altitude > maxClimbRate) {
		// this.velocity.z = maxClimbRate;
		// }
	}

	/**
	 * Decreasing rate of altitude change
	 */
	public void decreaseAltitude() {
		// this.velocity.add(0, 0, -5);
		// if (this.velocity.z > -maxClimbRate) {
		// this.velocity.z = -maxClimbRate;
		// }

	}

	/**
	 * Regular regular getter for radius
	 * 
	 * @return int radius
	 */
	public float getRadius() {
		return radius;
	}

	/**
	 * check if its only got the exit point left to go to.
	 * 
	 * @return whether is active
	 */
	public boolean isActive() {
		// FIXME
		if (getX() < 0 || getY() < 0 || getX() > Screen.WIDTH
				|| getY() > Screen.HEIGHT)
			this.isActive = false;

		if (waypoints.size() == 1)
			this.isActive = false;

		return isActive;
	}

	/**
	 * Setter for selected
	 * 
	 * @param newSelected
	 * @return whether is selected
	 */
	public boolean selected(boolean newSelected) {
		return this.selected = newSelected;
	}

}

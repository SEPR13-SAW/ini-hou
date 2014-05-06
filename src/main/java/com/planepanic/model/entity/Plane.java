package com.planepanic.model.entity;

import java.util.Stack;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.planepanic.model.Airspace;
import com.planepanic.model.Config;
import com.planepanic.model.Difficulty;
import com.planepanic.model.MathUtils;
import com.planepanic.model.resources.Art;
import com.planepanic.model.ui.screens.AbstractScreen;
import com.planepanic.model.waypoint.Runway;
import com.planepanic.model.waypoint.Waypoint;
import com.planepanic.model.waypoint.WaypointManager;

import lombok.Getter;
import lombok.Setter;

public final class Plane extends Entity {
	public enum State {
		FLYING,
		FLIGHTPLAN,
		APPROACHING,
		LANDING,
		TAKINGOFF;
	}

	private static int nextId = 0;

	@Getter private final Airspace airspace;
	@Getter private final Stack<Waypoint> flightplan;
	@Getter private final int id;
	@Getter @Setter private int player = 0;
	@Getter private float time = 0;
	@Getter @Setter private float velocity;
	@Getter @Setter private float altitude;
	@Getter @Setter private State state = State.FLIGHTPLAN;
	@Getter @Setter private boolean breakingExclusion = false;

	public Plane(Airspace airspace, int id, Stack<Waypoint> flightplan) {

		this.id = id;
		this.rotationOffset = 90;
		this.airspace = airspace;
		this.texture = Art.getTextureRegion("aircraft");
		this.size = new Vector2(38, 31);
		this.scale(0.1f, 0.1f);
		this.velocity = 45;
		this.altitude = MathUtils.RNG.nextInt((int) (Config.MAXIMUM_ALTITUDE - Config.MINIMUM_ALTITUDE)) + Config.MINIMUM_ALTITUDE;

		if (flightplan == null) {
			this.flightplan = new Stack<>();
			randomFlightPlan();
		} else {
			this.flightplan = flightplan;
		}

		Waypoint a = this.flightplan.pop();
		Waypoint b = this.flightplan.peek();
		this.flightplan.push(a);

		this.coords = a.coords.cpy();
		setRotation((float) ((Math.atan2(b.coords.y - coords.y, b.coords.x - coords.x) / Math.PI) * 180));

		tick(0);
	}

	public Plane(Airspace airspace) {
		this(airspace, nextId++, null);
	}

	public void randomFlightPlan() {
		if (MathUtils.RNG.nextInt(1) == 0 || airspace.getDifficulty() == Difficulty.MULTIPLAYER_SERVER) {
			flightplan.push(WaypointManager.randomExit());
			Runway runway = (Runway) airspace.getRunways().values().toArray()[MathUtils.RNG.nextInt(airspace.getRunways().size())];
			flightplan.push(runway.getEndOfRunway());
			flightplan.push((Waypoint) runway);
			flightplan.push(runway.getStartOfRunway());
			flightplan.push(runway.getApproach());
		} else {
			flightplan.push(WaypointManager.randomExit());
		}

		int nWaypoints = MathUtils.RNG.nextInt(1) + 1;
		for (int i = 0; i < nWaypoints; i++) {
			Waypoint wp = WaypointManager.randomWaypoint();
			if (!flightplan.contains(wp))
				flightplan.push(wp);
		}

		flightplan.push(WaypointManager.randomEntry());
	}

	@Override
	protected void additionalDraw(SpriteBatch batch) {
		ShapeRenderer drawer = AbstractScreen.shapeRenderer;

		if (breakingExclusion) {
			batch.end();

			drawer.setColor(1, 0, 0, 0);
			drawer.begin(ShapeType.Line);
			drawer.circle(coords.x, coords.y, 100);
			drawer.end();

			batch.begin();
		}

		if (airspace.getDifficulty() == Difficulty.MULTIPLAYER_CLIENT) {
			batch.end();

			if (airspace.getPlayer().getId() == player) {
				drawer.setColor(0, 1, 0, 0);
			} else {
				drawer.setColor(0, 0, 1, 0);
			}
			drawer.begin(ShapeType.Line);
			drawer.circle(coords.x, coords.y, 30);
			drawer.end();

			batch.begin();
		}

		if (airspace.getSelected() == this) {
			batch.end();

			drawer.setColor(1, 1, 0, 0);
			drawer.begin(ShapeType.Line);
			drawer.circle(coords.x, coords.y, 20);
			drawer.end();

			drawer.setColor(1, 1, 0, 0);
			drawer.begin(ShapeType.Line);
			if (flightplan.size() > 0) {
				Waypoint next = flightplan.get(0);
				if (flightplan.size() > 1) {
					for (Waypoint point : flightplan.subList(1, flightplan.size())) {
						if (point instanceof Runway) point = airspace.getRunways().get(player);
						drawer.line(next.coords.x, next.coords.y, point.coords.x, point.coords.y);
						next = point;
					}
				}
				drawer.line(next.coords.x, next.coords.y, coords.x, coords.y);
			}
			drawer.end();

			batch.begin();
		}

		Color color = Color.WHITE;

		if (getAltitude() <= 7500) {
			color = Color.GREEN;
		} else if (getAltitude() <= 12500) {
			color = Color.YELLOW;
		} else if (getAltitude() > 15000) {
			color = Color.RED;
		} else {
			color = Color.ORANGE;
		}

		AbstractScreen.drawString("alt: " + ((int) getAltitude()) + "ft", getX() - 30, getY() - 20,
				color, batch, true, 1);
	}

	public void modifyAltitude(float delta) {
		this.altitude += delta;

		if (altitude < Config.MINIMUM_ALTITUDE) altitude = Config.MINIMUM_ALTITUDE;
		if (altitude > Config.MAXIMUM_ALTITUDE) altitude = Config.MAXIMUM_ALTITUDE;
	}

	public void turn(float delta) {
		setRotation((float) ((getRotation() + delta) % 360));
	}

	public void turnTowards(Vector2 location, float delta) {
		Vector2 diff = location.cpy().sub(coords);
		float angle = (270 - (float) (Math.atan2(diff.y, diff.x) / Math.PI) * 180) - getRotation();
		angle %= 360;
		if (angle > 180) angle -= 360;
		if (angle <= -180) angle += 360;

		if (Math.abs(angle) < 3) return;

		if (angle > 0) {
			turn(-70 * delta);
		} else {
			turn(70 * delta);
		}
	}

	@SuppressWarnings("deprecation")
	public void tick(float delta) {
		time += delta;
		
		/*if (getX() < Config.HALF_WIDTH) {
			player = 0;
		} else {
			player = 1;
		}*/

		if (state == State.APPROACHING) {
			if((airspace.getRunways().get(player).getLandedPlanes().size() + airspace.getApproachingPlanes() > 10)){
				state = State.FLIGHTPLAN;
				flightplan.push(airspace.getRunways().get(player).getStartOfRunway());
				flightplan.push(airspace.getRunways().get(player).getApproach());
				airspace.setApproachingPlanes(airspace.getApproachingPlanes() - 1);
			} else if (MathUtils.closeEnough(airspace.getRunways().get(player).coords.cpy().sub(new Vector2(0, 100)), coords, 30)) {
				state = State.LANDING;
			}
		} else if (state == State.LANDING) {
			velocity = Config.MIN_VELOCITY;

			altitude -= delta * 2000;
			if (altitude < 0) altitude = 0;

			turnTowards(airspace.getRunways().get(player).coords, delta);

			if (MathUtils.closeEnough(airspace.getRunways().get(player).coords, coords, 30)) {
				airspace.setApproachingPlanes(airspace.getApproachingPlanes() - 1);
				airspace.getRunways().get(player).addLanded(this);
				flightplan.pop();
				airspace.removePlane(this);
			}
		} else if (state == State.FLIGHTPLAN) {
			turnTowards(flightplan.peek().coords, delta);
		} else if (state == State.TAKINGOFF){
			velocity += delta * 40;
			altitude += delta * 2000;
			if (MathUtils.closeEnough(airspace.getRunways().get(player).getEndOfRunway().coords, coords, 30)) {
				state = State.FLIGHTPLAN;
			}
					
		}

		Waypoint point = flightplan.peek();
		if (!(point instanceof Runway)) {
			if (MathUtils.closeEnough(point.coords, coords, 10)) {

				flightplan.pop();

				if (!flightplan.isEmpty()) {
					if (flightplan.peek() instanceof Runway && state == State.FLIGHTPLAN) {
						state = State.APPROACHING;
						airspace.setApproachingPlanes(airspace.getApproachingPlanes() + 1);
					}
				}
			}
		}

		if (flightplan.isEmpty()) {
			airspace.removePlane(this);
		}

		this.coords.add(MathUtils.fromAngle((float) ((getRotation() / 180) * Math.PI)).mul(velocity).mul(delta));
	}
	
	public void takeOff(){
		altitude = 0;
		velocity = 0;
		state = State.TAKINGOFF;
		airspace.addPlane(this);
	}

	public int getScore() {
		return (int) (10000f / time);
	}
}

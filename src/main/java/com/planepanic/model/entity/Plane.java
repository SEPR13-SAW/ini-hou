package com.planepanic.model.entity;

import java.util.Stack;

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
		APPROACHING,
		LANDING;
	}

	private static int nextId = 0;

	@Getter private final Airspace airspace;
	@Getter private final Stack<Waypoint> flightplan;
	@Getter private final int id;
	@Getter private int player = 0;
	@Getter private float time = 0;
	@Getter @Setter private float velocity;
	@Getter @Setter private float altitude;
	@Getter @Setter private State state = State.FLYING;

	public Plane(Airspace airspace, int id) {
		this.id = id;
		this.rotationOffset = 90;
		this.airspace = airspace;
		this.texture = Art.getTextureRegion("aircraft");
		this.size = new Vector2(74, 63);
		this.velocity = 45;
		this.altitude = MathUtils.RNG.nextInt((int) (Config.MAXIMUM_ALTITUDE - Config.MINIMUM_ALTITUDE)) + Config.MINIMUM_ALTITUDE;

		this.flightplan = new Stack<>();
		randomFlightPlan();

		Waypoint a = flightplan.pop();
		Waypoint b = flightplan.peek();
		flightplan.push(a);

		this.coords = a.coords.cpy();
		setRotation((float) ((Math.atan2(b.coords.y - coords.y, b.coords.x - coords.x) / Math.PI) * 180));

		tick(0);
	}

	public Plane(Airspace airspace) {
		this(airspace, nextId++);
	}

	public void randomFlightPlan() {
		if (MathUtils.RNG.nextInt(2) == 0 || airspace.getDifficulty() == Difficulty.MULTIPLAYER_SERVER) {
			flightplan.push((Waypoint) airspace.getRunways().values().toArray()[MathUtils.RNG.nextInt(airspace.getRunways().size())]);
		} else {
			flightplan.push(WaypointManager.randomExit());
		}

		int nWaypoints = MathUtils.RNG.nextInt(3) + 1;
		for (int i = 0; i < nWaypoints; i++) {
			flightplan.push(WaypointManager.randomWaypoint());
		}

		flightplan.push(WaypointManager.randomEntry());
	}

	@Override
	protected void additionalDraw(SpriteBatch batch) {
		ShapeRenderer drawer = AbstractScreen.shapeRenderer;

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

			drawer.setColor(1, 0, 0, 0);
			drawer.begin(ShapeType.Line);
			drawer.circle(coords.x, coords.y, 20);
			drawer.end();

			drawer.setColor(1, 0, 0, 0);
			drawer.begin(ShapeType.Line);
			Waypoint last = flightplan.get(flightplan.size() - 1);
			drawer.line(last.coords.x, last.coords.y, coords.x, coords.y);
			for (Waypoint point : flightplan.subList(0, flightplan.size())) {
				drawer.line(last.coords.x, last.coords.y, point.coords.x, point.coords.y);
				last = point;
			}
			drawer.end();

			batch.begin();
		}
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
		float angle = ((float) (Math.atan2(diff.y, diff.x) / Math.PI) * 180) - getRotation();
		angle %= 360;
		if (angle > 180) angle -= 360;
		if (angle <= -180) angle += 360;

		System.out.println(((float) (Math.atan2(diff.y, diff.x) / Math.PI) * 180));

		if (Math.abs(angle) < 6) return;

		if (angle > 0) {
			turn(5);
		} else {
			turn(-5);
		}
	}

	public boolean closeEnough(Vector2 v1, Vector2 v2, float dist) {
		Vector2 d = v1.cpy().sub(v2);
		return (Math.abs(d.x) < dist && Math.abs(d.y) < dist);
	}

	@SuppressWarnings("deprecation")
	public void tick(float delta) {
		time += delta;

		if (getX() < Config.HALF_WIDTH) {
			player = 0;
		} else {
			player = 1;
		}

		if (state == State.APPROACHING) {
			turnTowards(airspace.getRunways().get(player).coords.cpy().sub(new Vector2(0, 200)), delta);

			if (closeEnough(airspace.getRunways().get(player).coords.cpy().sub(new Vector2(0, 200)), coords, 30)) {
				state = State.LANDING;
			}
		} else if (state == State.LANDING) {
			turnTowards(airspace.getRunways().get(player).coords, delta);

			if (closeEnough(airspace.getRunways().get(player).coords, coords, 30)) {
				airspace.removePlane(this);
			}
		}

		Waypoint point = flightplan.peek();
		if (!(point instanceof Runway)) {
			if (closeEnough(point.coords, coords, 30)) {
				flightplan.pop();
			}
		}

		if (flightplan.isEmpty()) {
			airspace.removePlane(this);
		}

		this.coords.add(MathUtils.fromAngle((float) ((getRotation() / 180) * Math.PI)).mul(velocity).mul(delta));
	}

	public int getScore() {
		return (int) (10000f / time);
	}
}

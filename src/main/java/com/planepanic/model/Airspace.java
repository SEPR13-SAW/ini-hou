package com.planepanic.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Getter;
import lombok.Setter;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.planepanic.io.client.Client;
import com.planepanic.io.client.Player;
import com.planepanic.io.packet.UpdatePlanePacket;
import com.planepanic.io.server.Server;
import com.planepanic.model.resources.Art;
import com.planepanic.model.ui.screens.AbstractScreen;
import com.planepanic.model.waypoint.Runway;
import com.planepanic.model.waypoint.Waypoint;
import com.planepanic.model.waypoint.WaypointManager;
import com.planepanic.model.entity.Entity;
import com.planepanic.model.entity.Plane;
import com.planepanic.model.entity.Plane.State;

public final class Airspace extends Entity {
	@Getter private final Player player;
	private final List<Plane> planes;
	private final Map<Integer, Runway> runways;
	@Getter private final Difficulty difficulty;
	private final List<Plane> deletion = new ArrayList<>();
	private final Server server;
	private final Client client;

	@Getter private Plane selected = null;
	private long tick = 0;

	@Getter @Setter private boolean left, right, up, down;

	public Airspace(Difficulty difficulty, Player player, Server server, Client client) {
		this.server = server;
		this.client = client;
		this.player = player;
		this.coords = Config.AIRSPACE_SIZE.cpy().div(2);
		this.size = Config.AIRSPACE_SIZE;
		this.texture = Art.getTextureRegion("airspace");

		final Airspace airspace = this;

		addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				if (!(event instanceof InputEvent)) return false;
				InputEvent e = (InputEvent) event;

				if (e.getType() == Type.touchDown) {
					selected = null;
					Vector2 mouse = new Vector2(e.getStageX(), e.getStageY());

					for (Plane p : planes) {
						Vector2 d = p.getCoords().cpy().sub(mouse);
						if (Math.abs(d.x) < 40 && Math.abs(d.y) < 40) {
							selected = p;
						}
					}
				}

				if (e.getType() == Type.keyDown) {
					switch (e.getKeyCode()) {
					case Keys.A:
					case Keys.LEFT:
						airspace.setLeft(true);
						return true;
					case Keys.D:
					case Keys.RIGHT:
						airspace.setRight(true);
						return true;
					case Keys.W:
					case Keys.UP:
						airspace.setUp(true);
						return true;
					case Keys.S:
					case Keys.DOWN:
						airspace.setDown(true);
						return true;

					case Keys.E:
						if (selected != null) {
							selected.setVelocity(selected.getVelocity() + 5);
							if (selected.getVelocity() > Config.MAX_VELOCITY) selected.setVelocity(Config.MAX_VELOCITY);
						}
						return true;
					case Keys.Q:
						if (selected != null) {
							selected.setVelocity(selected.getVelocity() - 5);
							if (selected.getVelocity() < Config.MIN_VELOCITY) selected.setVelocity(Config.MIN_VELOCITY);
						}
						return true;
					case Keys.T:
						return true;
					case Keys.F:
						if (selected != null) {
							if (selected.getFlightplan().peek() instanceof Runway) {
								selected.setState(State.APPROACHING);
							}
						}
						return true;
					case Keys.TAB:
						airspace.tab();
						return true;
					}
				}

				if (e.getType() == Type.keyUp) {
					switch (e.getKeyCode()) {
					case Keys.A:
					case Keys.LEFT:
						airspace.setLeft(false);
						return true;
					case Keys.D:
					case Keys.RIGHT:
						airspace.setRight(false);
						return true;
					case Keys.W:
					case Keys.UP:
						airspace.setUp(false);
						return true;
					case Keys.S:
					case Keys.DOWN:
						airspace.setDown(false);
						return true;
					}
				}

				return false;
			}
		});

		this.planes = new ArrayList<>();
		this.difficulty = difficulty;

		this.runways = new HashMap<>();

		runways.put(0, new Runway(270, 360));
		if (difficulty == Difficulty.MULTIPLAYER_CLIENT || difficulty == Difficulty.MULTIPLAYER_SERVER) {
			runways.put(1, new Runway(270 + 540, 360));
		}

		addPlane(new Plane(this));
	}

	@Override
	protected void additionalDraw(SpriteBatch batch) {
		if (difficulty == Difficulty.MULTIPLAYER_CLIENT) {
			ShapeRenderer drawer = AbstractScreen.shapeRenderer;

			batch.end();

			drawer.setColor(0, 0, 0, 0);
			drawer.begin(ShapeType.Line);
			drawer.line(Config.HALF_WIDTH, 0, Config.HALF_WIDTH, Config.SCREEN_HEIGHT);
			drawer.end();

			batch.begin();
		}

		for (Entry<Integer, Runway> entry : runways.entrySet()) {
			entry.getValue().draw(batch, this.getColor().a);
		}

		for (Entry<Integer, Waypoint> entry : WaypointManager.getAll().entrySet()) {
			entry.getValue().draw(batch, this.getColor().a);
		}

		for (Plane plane : planes) {
			plane.draw(batch, this.getColor().a);
		}
	}

	public void addPlane(Plane plane) {
		planes.add(plane);
		plane.setVisible(true);
	}

	public void tab() {
		if (planes.size() == 0) {
			selected = null;
			return;
		}

		if (selected == null) {
			selected = planes.get(0);
			return;
		}

		int index = planes.indexOf(selected);
		selected = planes.get(((index + 1) % planes.size()));
	}

	public void removePlane(Plane plane) {
		deletion.add(plane);
		plane.remove();

		if (difficulty == Difficulty.MULTIPLAYER_CLIENT) {
			client.getPlayer().addScore(plane.getScore());
		} else if (difficulty == Difficulty.MULTIPLAYER_SERVER) {
			
		} else {
			player.addScore(plane.getScore());
		}

		if (selected == plane) selected = null;
	}

	public void tick(float delta) {
		if (selected != null) {
			if (left) selected.turn(delta * -40);
			if (right) selected.turn(delta * 40);
			if (up) selected.modifyAltitude(delta * 1000);
			if (down) selected.modifyAltitude(delta * -1000);
		}

		for (Plane plane : planes) {
			plane.tick(delta);

			if (difficulty == Difficulty.MULTIPLAYER_SERVER) {
				if (tick % 10 == 0) {
					try {
						server.broadcast(new UpdatePlanePacket(plane.getId(), plane.getX(), plane.getY(), plane.getRotation(), (int) plane.getAltitude(), plane.getVelocity()));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			if (difficulty == Difficulty.MULTIPLAYER_CLIENT) {
				// Client networking.
			}
		}

		if (difficulty != Difficulty.MULTIPLAYER_CLIENT) {
			// Generate planes.
			if (MathUtils.RNG.nextInt(1000) == 0) {
				// Generate plane.
				if (Config.DEBUG) System.out.println("Generating plane.");
				addPlane(new Plane(this));
			}
		}

		Iterator<Plane> iterator = planes.iterator();
		while (iterator.hasNext()) {
			Plane p = iterator.next();
			if (deletion.contains(p))
				iterator.remove();
		}
		deletion.clear();

		tick++;
	}

	public Map<Integer, Runway> getRunways() {
		return runways;
	}
}

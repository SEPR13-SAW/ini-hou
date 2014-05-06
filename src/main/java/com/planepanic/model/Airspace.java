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
import com.planepanic.ATC;
import com.planepanic.io.client.Client;
import com.planepanic.io.client.Player;
import com.planepanic.io.packet.SetAltitudePacket;
import com.planepanic.io.packet.SetDirectionPacket;
import com.planepanic.io.packet.SetVelocityPositionPacket;
import com.planepanic.io.packet.SpawnPlanePacket;
import com.planepanic.io.packet.UpdatePlanePacket;
import com.planepanic.io.server.Server;
import com.planepanic.model.resources.Art;
import com.planepanic.model.ui.screens.AbstractScreen;
import com.planepanic.model.ui.screens.EndScreen;
import com.planepanic.model.waypoint.Runway;
import com.planepanic.model.waypoint.Waypoint;
import com.planepanic.model.waypoint.WaypointManager;
import com.planepanic.model.entity.Entity;
import com.planepanic.model.entity.Plane;
import com.planepanic.model.entity.Plane.State;

public final class Airspace extends Entity {
	@Getter private final ATC game;
	@Getter @Setter private Player player;
	@Getter private final List<Plane> planes;
	@Getter private final Map<Integer, Runway> runways;
	@Getter private final Difficulty difficulty;
	@Getter private final List<Plane> deletion = new ArrayList<>();
	@Getter private final Server server;
	@Getter private final Client client;

	@Getter private Plane selected = null;
	@Getter private long tick = 0;
	@Getter private float time = 0;

	@Getter @Setter private boolean left, right, up, down;

	public Airspace(ATC game, Difficulty difficulty, Player player, Server server, Client client) {
		this.game = game;
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

					case Keys.R:
						if (selected != null) {
							selected.setState(State.FLIGHTPLAN);
						}
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
					//case Keys.T:
					//	return true;
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

		runways.put(0, new Runway(270, 360, 0));
		if (difficulty != Difficulty.MULTIPLAYER_SERVER) {
			WaypointManager.set(0, runways.get(0));
		}
		if (difficulty == Difficulty.MULTIPLAYER_CLIENT || difficulty == Difficulty.MULTIPLAYER_SERVER) {
			runways.put(1, new Runway(270 + 540, 360, 1));
			if (difficulty != Difficulty.MULTIPLAYER_SERVER) {
				WaypointManager.set(1, runways.get(1));
			}
		} else {
			if (WaypointManager.getAll().containsKey(1)) {
				Runway r = (Runway) WaypointManager.getAll().get(1);
				WaypointManager.getAll().remove(1);
				r.remove();
			}
		}

		// Test plane.
		//addPlane(new Plane(this));
	}

	@Override
	protected void additionalDraw(SpriteBatch batch) {
		synchronized (this) {
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
	}

	public void addPlane(Plane plane) {
		synchronized (this) {
			planes.add(plane);
			plane.setVisible(true);
		}
	}

	public void tab() {
		List<Plane> selectable = new ArrayList<>();

		// Only allow the player to select their own planes.
		if (difficulty == Difficulty.MULTIPLAYER_CLIENT) {
			for (Plane p : planes) {
				if (p.getPlayer() == client.getPlayer().getId()) {
					selectable.add(p);
				}
			}
		} else {
			selectable.addAll(planes);
		}

		if (selectable.size() == 0) {
			selected = null;
			return;
		}

		if (selected == null) {
			selected = selectable.get(0);
			return;
		}

		int index = selectable.indexOf(selected);
		selected = selectable.get(((index + 1) %selectable.size()));
	}

	public void removePlane(Plane plane) {
		synchronized (this) {
			deletion.add(plane);
			plane.remove();
	
			if (difficulty == Difficulty.MULTIPLAYER_CLIENT) {
				if (client.getPlayers().containsKey(plane.getId()))
					client.getPlayers().get(plane.getId()).addScore(plane.getScore());
			} else if (difficulty == Difficulty.MULTIPLAYER_SERVER) {
				// TODO?
			} else {
				player.addScore(plane.getScore());
			}
	
			if (selected == plane) selected = null;
		}
	}

	public void tick(float delta) {
		if (selected != null) {
			if (difficulty == Difficulty.MULTIPLAYER_CLIENT) {
				if (selected.getPlayer() != player.getId()) {
					selected = null;
				}
			}
		}

		if (selected != null) {
			if (left || right) selected.setState(State.FLYING);
			if (left) selected.turn(delta * -40);
			if (right) selected.turn(delta * 40);
			if (up) selected.modifyAltitude(delta * 1000);
			if (down) selected.modifyAltitude(delta * -1000);
		}

		for (Plane plane : planes) {
			plane.tick(delta);

			int bounds = 50;
			if (plane.getX() < -bounds || plane.getY() < -bounds || plane.getX() > Config.AIRSPACE_SIZE.x + bounds || plane.getY() > Config.AIRSPACE_SIZE.y + bounds) {
				if (Config.DEBUG) System.out.println("plane out of bounds " + plane);
				removePlane(plane);
				continue;
			}

			if (difficulty == Difficulty.MULTIPLAYER_CLIENT || difficulty == Difficulty.MULTIPLAYER_SERVER)
				if (plane.getX() < Config.HALF_WIDTH) {
					plane.setPlayer(0);
				} else {
					plane.setPlayer(1);
				}

			plane.setBreakingExclusion(false);

			for (Plane other : planes) {
				if (plane == other) continue;

				if (Math.abs(plane.getAltitude() - other.getAltitude()) > 1000) continue;

				if (MathUtils.closeEnough(plane.getCoords(), other.getCoords(), Config.EXCLUSION_ZONE)) {
					// Breaking exclusion zone.
					plane.setBreakingExclusion(true);
				}

				if (MathUtils.closeEnough(plane.getCoords(), other.getCoords(), 30)) {
					// Crash.
					endGame();
				}
			}

			if (difficulty == Difficulty.MULTIPLAYER_SERVER) {
				if (tick % 10 == 0) {
					try {
						// Broadcast plane updates every 10 ticks.
						server.broadcast(new UpdatePlanePacket(plane.getId(), plane.getX(), plane.getY(), plane.getRotation(), (int) plane.getAltitude(), plane.getVelocity()));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			if (difficulty == Difficulty.MULTIPLAYER_SERVER) {
				if (tick % 10 == 0) {
					if (plane.getState() == State.FLYING) {
						try {
							client.writePacket(new SetVelocityPositionPacket(plane.getId(), plane.getVelocity(), plane.getCoords().x, plane.getCoords().y));
							client.writePacket(new SetAltitudePacket(plane.getId(), plane.getAltitude()));
							client.writePacket(new SetDirectionPacket(plane.getId(), plane.getRotation()));
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		if (difficulty != Difficulty.MULTIPLAYER_CLIENT) {
			// Generate planes.
			int rate = 1500;
			if (difficulty == Difficulty.MEDIUM || difficulty == Difficulty.MULTIPLAYER_SERVER) rate = 1000;
			if (difficulty == Difficulty.HARD) rate = 500;

			if (MathUtils.RNG.nextInt(rate) == 0) {
				// Generate plane.
				if (Config.DEBUG) System.out.println("Generating plane.");
				Plane plane = new Plane(this);
				addPlane(plane);

				if (difficulty == Difficulty.MULTIPLAYER_SERVER) {
					// Send spawn plane packet.
					try {
						server.broadcast(new SpawnPlanePacket(plane.getId(), plane.getPlayer(), "YO" + String.format("%03d", MathUtils.RNG.nextInt(1000)), plane.getFlightplan(), plane.getAltitude()));
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}

				// Prevent plane spawns too close to existing planes.
				for (Plane other : planes) {
					if (other == plane) continue;

					if (MathUtils.closeEnough(plane.getCoords(), other.getCoords(), Config.EXCLUSION_ZONE)) {
						removePlane(plane);
						break;
					}
				}
			}
		}

		Iterator<Plane> iterator = planes.iterator();
		while (iterator.hasNext()) {
			Plane p = iterator.next();
			if (deletion.contains(p))
				iterator.remove();
		}
		deletion.clear();

		time += delta;
		tick++;
	}

	public void endGame() {
		if (difficulty == Difficulty.MULTIPLAYER_SERVER) {
			server.shutdown();
		} else if (difficulty == Difficulty.MULTIPLAYER_CLIENT) {
			int score = player.getScore();
			int max = score;

			for (Entry<Integer, Player> entry : client.getPlayers().entrySet()) {
				int s = entry.getValue().getScore();
				if (s > max) max = s;
			}

			if (score == max) {
				// Won.
				game.setScreen(new EndScreen(game, time, score));
			} else {
				// Lost.
				game.setScreen(new EndScreen(game, time, score));
			}
		} else {
			game.setScreen(new EndScreen(game, time, player.getScore()));
		}
	}
}

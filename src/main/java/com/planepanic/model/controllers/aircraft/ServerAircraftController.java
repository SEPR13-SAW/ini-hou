package com.planepanic.model.controllers.aircraft;

import java.io.IOException;

import com.planepanic.io.packet.SpawnPlanePacket;
import com.planepanic.io.packet.UpdatePlanePacket;
import com.planepanic.io.server.Player;
import com.planepanic.io.server.Server;
import com.planepanic.model.Aircraft;
import com.planepanic.model.Airspace;
import com.planepanic.model.Config;
import com.planepanic.model.GameDifficulty;
import com.planepanic.model.resources.Art;

public final class ServerAircraftController extends AircraftController {
	
	private final Server server;

	/**
	 * 
	 * @param diff game difficulty, changes number of aircraft and time between
	 *            them
	 * @param airspace the group where all of the waypoints and aircraft will be
	 *            added
	 * @param server
	 */
	public ServerAircraftController(GameDifficulty diff, Airspace airspace, Server server) {
		super(diff, airspace);
		this.server = server;
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

			if(difficulty.getMultiplayer()){			
				if(planeI.getCoords().x > 540)
					planeI.setPlayer(server.getPlayers().get(0));   //Player 1
				if(planeI.getCoords().x < 540)
					planeI.setPlayer(server.getPlayers().get(1));   //Player 2
				}
			
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

		// try to generate a new aircraft
		// TODO Assign aircraft to player (random for multiplayer).
		Player player = server.getPlayers().get(0);
		if (player == null) player = server.getPlayers().get(1);
		if (player != null) {
			final Aircraft generatedAircraft = generateAircraft(player);
	
			// if the newly generated aircraft is not null (ie checking one was
			// generated), add it as an actor to the stage
			if (generatedAircraft != null) {
				try {
					server.broadcast(new SpawnPlanePacket(generatedAircraft.getId(), (byte) generatedAircraft.getPlayer().getId(), generatedAircraft.getName(), generatedAircraft.getFlightPlan(), generatedAircraft.getAltitude()));
				} catch (IOException e) {
					e.printStackTrace();
				}

				// add it to the airspace (stage group) so its automatically drawn
				// upon calling root.draw()
				airspace.addActor(generatedAircraft);
			}
		}

		// sort aircraft so they appear in the right order
		airspace.sortAircraft();

		tick++;

		if (tick == 6) {
			tick = 0;
			for (Aircraft a : aircraftList) {
				try {
					System.out.println("update " + a);
					server.broadcast(new UpdatePlanePacket(a.getId(), a.getX(), a.getY(), a.getRotation(), a.getAltitude(), a.getSpeed()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	private int tick = 0;

	/**
	 * Handles what happens after a collision
	 * 
	 * @param a first aircraft that collided
	 * @param b second aircraft that collided
	 */
	@Override
	protected void collisionHasOccured(Aircraft a, Aircraft b) {
		server.getPlayers().get(a.getPlayer().getId()).addScore(-1000);
		server.getPlayers().get(b.getPlayer().getId()).addScore(-1000);
	}

	@Override
	protected void removeAircraft(int i) {
		super.removeAircraft(i);
		
		Aircraft aircraft = aircraftList.get(i);
		
		if (aircraft.isMustLand()) {
			aircraft.getPlayer().addScore(-1000);
		}
	}

	@Override
	public void incScore(int ammount, Aircraft aircraft) {
		aircraft.getPlayer().addScore(ammount);
	}

}

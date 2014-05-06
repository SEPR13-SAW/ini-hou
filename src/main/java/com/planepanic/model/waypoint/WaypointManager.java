package com.planepanic.model.waypoint;

import java.util.HashMap;
import java.util.Map;

import com.planepanic.model.MathUtils;

import lombok.Getter;

public final class WaypointManager {
	@Getter private final static Map<Integer, Waypoint> all         = new HashMap<>();
	@Getter private final static Map<Integer, Waypoint> waypoints   = new HashMap<>();
	@Getter private final static Map<Integer, Waypoint> entryPoints = new HashMap<>();
	@Getter private final static Map<Integer, Waypoint> exitPoints  = new HashMap<>();

	public static void addWaypoint(int id, Waypoint waypoint) {
		if (waypoint instanceof EntryPoint) {
			entryPoints.put(id, waypoint);
		} else if (waypoint instanceof ExitPoint) {
			exitPoints.put(id, waypoint);
		} else {
			waypoints.put(id, waypoint);
		}
		all.put(id, waypoint);
	}

	public static void set(int id, Waypoint waypoint) {
		all.put(id, waypoint);
	}

	public static Waypoint randomExit() {
		return (Waypoint) exitPoints.values().toArray()[MathUtils.RNG.nextInt(exitPoints.size())];
	}

	public static Waypoint randomEntry() {
		return (Waypoint) entryPoints.values().toArray()[MathUtils.RNG.nextInt(entryPoints.size())];
	}

	public static Waypoint randomWaypoint() {
		return (Waypoint) waypoints.values().toArray()[MathUtils.RNG.nextInt(waypoints.size())];
	}

	static {
		// Start IDs from 2, 0 and [1] are runways.
		int id = 2;

		addWaypoint(id, new EntryPoint(0, 360, id++));
		addWaypoint(id, new EntryPoint(1080, 360, id++));
		addWaypoint(id, new EntryPoint(540, 720, id++));

		addWaypoint(id, new ExitPoint(1080, 720, id++));
		addWaypoint(id, new ExitPoint(1080, 0, id++));
		addWaypoint(id, new ExitPoint(0, 720, id++));
		addWaypoint(id, new ExitPoint(0, 0, id++));

		addWaypoint(id, new Waypoint(150, 150, id++));
		addWaypoint(id, new Waypoint(400, 150, id++));
		addWaypoint(id, new Waypoint(400, 360, id++));
		addWaypoint(id, new Waypoint(150, 570, id++));
		addWaypoint(id, new Waypoint(400, 570, id++));
		addWaypoint(id, new Waypoint(930, 150, id++));
		addWaypoint(id, new Waypoint(680, 150, id++));
		addWaypoint(id, new Waypoint(680, 360, id++));
		addWaypoint(id, new Waypoint(930, 570, id++));
		addWaypoint(id, new Waypoint(680, 570, id++));
	}
}

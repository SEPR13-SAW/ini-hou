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
		addWaypoint(0, new Waypoint(100, 100));
		addWaypoint(1, new EntryPoint(0, 0));
		addWaypoint(2, new ExitPoint(0, 200));
	}
}

package com.planepanic.model;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

public final class MathUtils {
	public static final Random RNG = new Random();

	public static Vector2 fromAngle(float angle) {
		return new Vector2((float) Math.sin(angle), (float) Math.cos(angle));
	}

	public static boolean closeEnough(Vector2 v1, Vector2 v2, float dist) {
		Vector2 d = v1.cpy().sub(v2);
		return (Math.abs(d.x) < dist && Math.abs(d.y) < dist);
	}
}

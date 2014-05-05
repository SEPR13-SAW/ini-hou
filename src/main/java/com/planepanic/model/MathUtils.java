package com.planepanic.model;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

public final class MathUtils {
	public static final Random RNG = new Random();

	public static Vector2 fromAngle(float angle) {
		return new Vector2((float) Math.sin(angle), (float) Math.cos(angle));
	}
}

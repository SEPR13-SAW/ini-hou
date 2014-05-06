package com.planepanic.model;

import com.badlogic.gdx.math.Vector2;

public final class Config {

	// Used for debugging all around
	public final static boolean DEBUG = false;

	// General, graphics related settings
	public final static String GAME_TITLE = "Controller Concern";
	public final static int SCREEN_WIDTH = 1280;
	public final static int SCREEN_HEIGHT = 720;
	public final static boolean VSYNC = true;
	public final static boolean RESIZABLE = true;

	public final static float MINIMUM_ALTITUDE = 5000, MAXIMUM_ALTITUDE = 20000;
	public static final float MAX_VELOCITY = 140;
	public static final float MIN_VELOCITY = 40;

	// Art related config
	public final static Vector2 AIRSPACE_SIZE = new Vector2(1080, SCREEN_HEIGHT);
	public final static int HALF_WIDTH = 540;

	// UI related
	public final static Vector2 SIDEBAR_SIZE = new Vector2(200, SCREEN_HEIGHT);

	// other
	public final static String COPYRIGHT_NOTICE = "Copyright Disclaimer Under Section 107 of the Copyright Act 1976, allowance is made "
			+ "for 'fair use' for purposes such as criticism, comment, news reporting, teaching, "
			+ "scholarship, and research. Fair use is a use permitted by copyright statute that "
			+ "might otherwise be infringing. Non-profit, educational or personal use tips the "
			+ "balance in favor of fair use.";

	public static final float EXCLUSION_ZONE = 100;

	public static final float AIRCRAFT_SPEED_MULTIPLIER = 5;

}

package com.planepanic.model;

import com.badlogic.gdx.math.Vector2;

public final class Config {

	// Used for debugging all around
	public final static boolean DEBUG_UI = false;
	public final static boolean DEBUG_TEXT = false;

	// General, graphics related settings
	public final static String GAME_TITLE = "Controller Concern";
	public final static int SCREEN_WIDTH = 1280;
	public final static int SCREEN_HEIGHT = 720;
	public final static boolean VSYNC = true;
	public final static boolean RESIZABLE = true;

	// Art related config
	public final static Vector2 AIRSPACE_SIZE = new Vector2(1080, SCREEN_HEIGHT);
	public final static Vector2 WAYPOINT_SIZE = new Vector2(20, 20);
	public final static Vector2 EXIT_WAYPOINT_SIZE = new Vector2(50, 50);

	// UI related
	public final static Vector2 SIDEBAR_SIZE = new Vector2(200, SCREEN_HEIGHT);

	// Game related
	public final static float AIRCRAFT_SPEED_MULTIPLIER = 40f / 3f;
	public final static int MIN_ALTITUDE_DIFFERENCE = 1000;
	public final static int[] ALTITUDES = { 5000, 10000, 15000 };
	//Coordinates of the middle point of the airports
	public final static Vector2[] AIRPORT_COORDIATES = {new Vector2(387, 335)};

	// other
	public final static String COPYRIGHT_NOTICE = "Copyright Disclaimer Under Section 107 of the Copyright Act 1976, allowance is made "
			+ "for 'fair use' for purposes such as criticism, comment, news reporting, teaching, "
			+ "scholarship, and research. Fair use is a use permitted by copyright statute that "
			+ "might otherwise be infringing. Non-profit, educational or personal use tips the "
			+ "balance in favor of fair use.";

}

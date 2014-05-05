package com.planepanic.model;

/**
 * Class containing fields configuring the game's difficulty
 */
public class GameDifficulty
{
	// maxAircraft, timeBetweenGenerations, separationRadius, scoreMultiplier, multiplayermode?
	public static final GameDifficulty EASY   = new GameDifficulty(10, 4, 150, 17, false);
	public static final GameDifficulty MEDIUM = new GameDifficulty(10, 3, 100, 22, false);
	public static final GameDifficulty HARD   = new GameDifficulty(10, 2,  75, 27, false);
	public static final GameDifficulty MULTI =  new GameDifficulty(10, 3, 100, 22, true);

	private final int maxAircraft, timeBetweenGenerations, separationRadius, scoreMultiplier;
	private boolean multiplayer;

	/**
	 * Initializes a new game difficulty
	 *
	 * @param maxAircraft maximum number of aircraft
	 * @param timeBetweenGenerations minimum time between new aircraft
	 * @param separationRadius separation radius between aircraft
	 * @param scoreMultiplier score multiplier
	 * @param  
	 */
	public GameDifficulty(int maxAircraft, int timeBetweenGenerations, int separationRadius, int scoreMultiplier, boolean multiplayer)
	{
		this.maxAircraft = maxAircraft;
		this.timeBetweenGenerations = timeBetweenGenerations;
		this.separationRadius = separationRadius;
		this.scoreMultiplier = scoreMultiplier;
		this.multiplayer = multiplayer;
	}

	/** Returns the maximum number of aircraft allowed */
	public int getMaxAircraft()
	{
		return maxAircraft;
	}

	/** Returns the minimum time between new aircraft */
	public int getTimeBetweenGenerations()
	{
		return timeBetweenGenerations;
	}

	/** Returns the separation radius between aircraft */
	public int getSeparationRadius()
	{
		return separationRadius;
	}

	/** Returns the score multiplier */
	public int getScoreMultiplier()
	{
		return scoreMultiplier;
	}
	
	/**Returns if the game is multiplayer or not */
	public boolean getMultiplayer()
	{
		return multiplayer;
	}
}

package seprini.data;

/**
 * Class containing fields configuring the game's difficulty
 */
public class GameDifficulty
{
	// maxAircraft, timeBetweenGenerations, separationRadius, scoreMultiplier
	public static final GameDifficulty EASY   = new GameDifficulty(10, 4, 150, 17);
	public static final GameDifficulty MEDIUM = new GameDifficulty(10, 3, 100, 22);
	public static final GameDifficulty HARD   = new GameDifficulty(10, 2,  75, 27);

	private final int maxAircraft, timeBetweenGenerations, separationRadius, scoreMultiplier;

	/**
	 * Initializes a new game difficulty
	 *
	 * @param maxAircraft maximum number of aircraft
	 * @param timeBetweenGenerations minimum time between new aircraft
	 * @param separationRadius separation radius between aircraft
	 * @param scoreMultiplier score multiplier
	 */
	public GameDifficulty(int maxAircraft, int timeBetweenGenerations, int separationRadius, int scoreMultiplier)
	{
		this.maxAircraft = maxAircraft;
		this.timeBetweenGenerations = timeBetweenGenerations;
		this.separationRadius = separationRadius;
		this.scoreMultiplier = scoreMultiplier;
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
}

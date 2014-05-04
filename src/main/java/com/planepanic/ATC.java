package seprini;

import com.badlogic.gdx.Game;
import seprini.data.Art;
import seprini.data.GameDifficulty;
import seprini.screens.EndScreen;
import seprini.screens.GameScreen;
import seprini.screens.MenuScreen;

/**
 * Main class, calls all subsequent classes. Initialises Input, Art classes,
 * first and last class to be called
 */
public class ATC extends Game
{
	@Override
	public void create()
	{
		Art.load();
		showMenuScreen();
	}

	/**
	 * Shows the menu screen
	 */
	public void showMenuScreen()
	{
		setScreen(new MenuScreen(this));
	}

	/**
	 * Shows the game screen based on the selected difficulty
	 */
	public void showGameScreen(GameDifficulty difficulty)
	{
		setScreen(new GameScreen(this, difficulty));
	}

	/**
	 * Shows the end screen
	 * 
	 * @param time final time
	 * @param score final score
	 */
	public void showEndScreen(float time, float score)
	{
		setScreen(new EndScreen(this, time, score));
	}
}

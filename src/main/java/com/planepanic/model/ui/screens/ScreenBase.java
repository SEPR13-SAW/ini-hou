package com.planepanic.model.ui.screens;

import com.badlogic.gdx.Screen;
import com.planepanic.ATC;

/**
 * Base interface for screens in the game
 * 
 * <p>
 * This interface allows the test code to implement "non-graphics" versions of
 * this interface for testing purposes.
 */
public interface ScreenBase extends Screen
{
	/** Returns an instance of the main game object */
	ATC getGame();

	/** Returns true if this screen is paused */
	boolean isPaused();

	/**
	 * Sets the paused state of this screen
	 * 
	 * <p>
	 * When the screen is paused, no calls to the act methods occur for any
	 * objects on the stage
	 * <p>
	 * Do not confuse with the libGDX {@link com.badlogic.gdx.Screen#pause()}
	 * method
	 * </p>
	 * 
	 * @param paused true if the screen is paused
	 */
	void setPaused(boolean paused);
}

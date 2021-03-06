package com.planepanic;

import java.net.InetSocketAddress;

import com.badlogic.gdx.Game;
import com.planepanic.model.Difficulty;
import com.planepanic.model.resources.Art;
import com.planepanic.model.ui.screens.EndScreen;
import com.planepanic.model.ui.screens.GameScreen;
import com.planepanic.model.ui.screens.JoinGameScreen;
import com.planepanic.model.ui.screens.MenuScreen;
import com.planepanic.model.ui.screens.StartMultiplayerScreen;

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
	 * @param host 
	 * @param address 
	 * @param n 
	 */
	public void showGameScreen(Difficulty difficulty, boolean host, InetSocketAddress address, String name)
	{
		setScreen(new GameScreen(this, difficulty, host, address, name));
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

	public void showStartMultiplayerScreen() {
		setScreen(new StartMultiplayerScreen(this));
		
	}
	
	public void showJoinGameScreen(){
		setScreen(new JoinGameScreen(this));
	}
}

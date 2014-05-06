package com.planepanic;

import java.net.InetSocketAddress;

import com.badlogic.gdx.Game;
import com.planepanic.io.client.Client;
import com.planepanic.model.GameDifficulty;
import com.planepanic.model.resources.Art;
import com.planepanic.model.screens.EndScreen;
import com.planepanic.model.screens.GameScreen;
import com.planepanic.model.screens.JoinGameScreen;
import com.planepanic.model.screens.MenuScreen;
import com.planepanic.model.screens.StartMultiplayerScreen;

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
	public void showGameScreen(GameDifficulty difficulty, boolean host, InetSocketAddress address, String name)
	{
		setScreen(new GameScreen(this, difficulty, host, address, name));
	}

	/**
	 * Shows the end screen
	 * 
	 * @param time final time
	 * @param score final score
	 * @param client 
	 */
	public void showEndScreen(float time, float score, boolean gameWon, Client client)
	{
		setScreen(new EndScreen(this, time, score, gameWon, client));
	}
	
	/**
	 * Shows the end screen
	 * 
	 * @param time final time
	 * @param score final score
	 * @param client 
	 */
	public void showEndScreen(float time, float score, boolean gameWon)
	{
		setScreen(new EndScreen(this, time, score, gameWon));
	}

	public void showStartMultiplayerScreen(GameDifficulty multi) {
		setScreen(new StartMultiplayerScreen(this));
		
	}
	
	public void showJoinGameScreen(GameDifficulty multi){
		setScreen(new JoinGameScreen(this));
	}
}

package com.planepanic.model.controllers;

import java.net.InetSocketAddress;
import java.util.HashMap;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.esotericsoftware.tablelayout.Cell;
import com.planepanic.io.server.Server;
import com.planepanic.model.Art;
import com.planepanic.model.GameDifficulty;
import com.planepanic.model.screens.JoinGameScreen;

public class JoinGameController extends ChangeListener {

	private final Table ui;
	private final JoinGameScreen screen;

	private HashMap<String, TextButton> buttons;

	private TextField name, address;

	public JoinGameController(JoinGameScreen screen, Table ui) {
		this.ui = ui;
		// Moves ui down, to leave space for buttons
		ui.setPosition(0, -100);
		this.screen = screen;

		buttons = new HashMap<String, TextButton>();
		addWidgets();

	}

	private void addWidgets() {
		Label enterPlayerName = new Label("Please enter your player name:", Art.getSkin());
		ui.add(enterPlayerName);
		ui.row();
		// adds textfield for entering player name
		TextField playerName = new TextField("", Art.getSkin(), "default");
		playerName.addListener(this);
		name = playerName;
		// makes the textfield selected for instant input
		this.screen.getStage().setKeyboardFocus(playerName);
		ui.add(playerName);
		ui.row();

		Label selectServerLabel = new Label("Server address:", Art.getSkin());
		ui.add(selectServerLabel).padTop(20f).width(200f);
		ui.row();

		// scrollpane to allow accomodating unbounded number of servers
		TextField serverAddress = new TextField("127.0.0.1", Art.getSkin(), "default");
		serverAddress.addListener(this);
		address = serverAddress;
		ui.add(serverAddress);
		ui.row();

		ui.row();
		addButton("startGame", "Join Game", this).padTop(20f);
		ui.row();
		addButton("back", "Back", this);
		ui.toFront();
	}

	/**
	 * Convenience method to add a button to the UI
	 * 
	 * @param name
	 * @param text
	 * @return
	 */
	private Cell<?> addButton(String name, String text, ChangeListener listener) {
		TextButton button = new TextButton(text, Art.getSkin());
		buttons.put(name, button);
		button.addListener(listener);
		return ui.add(button);
	}

	@Override
	public void changed(ChangeEvent event, Actor actor) {
		Art.getSound("comeflywithme").stop();
		Art.getSkin().getFont("default").setScale(1f);

		System.out.println(name.getText());
		String n = name.getText();

		if (actor.equals(buttons.get("startGame")))
			screen.getGame().showGameScreen(GameDifficulty.MULTI, false, new InetSocketAddress(address.getText(), Server.PORT), n);
		if (actor.equals(buttons.get("back")))
			screen.getGame().showMenuScreen();
	}

}

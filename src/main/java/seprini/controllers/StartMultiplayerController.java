package seprini.controllers;

import java.util.HashMap;

import seprini.data.Art;
import seprini.data.GameDifficulty;
import seprini.screens.StartMultiplayerScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.esotericsoftware.tablelayout.Cell;

public class StartMultiplayerController extends ChangeListener {

	private final Table ui;
	private final StartMultiplayerScreen screen;

	private HashMap<String, TextButton> buttons;

	public StartMultiplayerController(StartMultiplayerScreen screen, Table ui) {
		this.ui = ui;
		// Moves ui down, to leave space for buttons
		ui.setPosition(0, -30);
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
		// makes the textfield selected for instant input
		this.screen.getStage().setKeyboardFocus(playerName);
		ui.add(playerName);

		ui.row();
		addButton("startGame", "Start Game", this).padTop(20f);
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

		// Pass difficulty to the newly created GameScreen so the game can
		// change variables depending on it
		if (actor.equals(buttons.get("startGame")))
			screen.getGame().showGameScreen(GameDifficulty.MULTI);
		if (actor.equals(buttons.get("back")))
			screen.getGame().showMenuScreen();
	}

}

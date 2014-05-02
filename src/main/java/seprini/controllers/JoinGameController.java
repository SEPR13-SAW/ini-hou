package seprini.controllers;

import java.util.HashMap;

import seprini.data.Art;
import seprini.data.GameDifficulty;
import seprini.screens.JoinGameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.tablelayout.Cell;

public class JoinGameController extends ChangeListener {

	private final Table ui;
	private final JoinGameScreen screen;
	List serverList;

	private HashMap<String, TextButton> buttons;

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
		// makes the textfield selected for instant input
		this.screen.getStage().setKeyboardFocus(playerName);
		ui.add(playerName);
		ui.row();

		Label selectServerLabel = new Label("Please select server:", Art.getSkin());
		ui.add(selectServerLabel).padTop(20f).width(200f);
		ui.row();
		//Test objects
		Object[] temp = new Object[4];
		temp[0] = new String("Hello world1");
		temp[1] = new String("Hello world2");
		temp[2] = new String("Hello world3");
		temp[3] = new String("Hello world4");
		// A list holding list of potential servers
		serverList = new List(temp, Art.getSkin());
		// scrollpane to allow accomodating unbounded number of servers
		ScrollPane scrollPane = new ScrollPane(serverList, Art.getSkin(), "default");
		scrollPane.setScrollbarsOnTop(false);
		scrollPane.setSmoothScrolling(true);
//		ui.add(serverList).padTop(20f);
		ui.add(scrollPane).padTop(20f).height(80f).width(150f).align(Align.center);
		
		ui.row();
		addButton("startGame", "Start Game", this).padTop(20f);
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
	
	private void showServers(){
		for(int i = 0; i < 0; i++){
			addButton("serverName", "playerName", this);
		}
	}

	@Override
	public void changed(ChangeEvent event, Actor actor) {
		Art.getSound("comeflywithme").stop();
		Art.getSkin().getFont("default").setScale(1f);

		// Pass difficulty to the newly created GameScreen so the game can
		// change variables depending on it
		//Test get selection of the list
		System.out.println(serverList.getSelection());
		if (actor.equals(buttons.get("startGame")))
			screen.getGame().showGameScreen(GameDifficulty.MULTI);

		if (actor.equals(buttons.get("exit")))
			Gdx.app.exit();
	}

}

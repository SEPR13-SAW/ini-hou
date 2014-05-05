package com.planepanic.model.screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.planepanic.ATC;
import com.planepanic.model.resources.Art;

public class EndScreen extends AbstractScreen
{
	public EndScreen(ATC game, float time, float score) {

		super(game);

		Stage root = getStage();
		Table ui = new Table();

		ui.setFillParent(true);
		root.addActor(ui);

		ui.addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if (keycode == Keys.ESCAPE)
					getGame().showMenuScreen();

				return false;
			}
		});

		root.setKeyboardFocus(ui);

		Art.getSkin().getFont("default").setScale(1f);

		Label text = new Label(
				"You have failed.\n"
						+ "Two aeroplanes have collided mid-flight in a huge crash which resulted in the death of many innocent people.\n"
						+ "However, surprisingly, you managed to avoid a crash for exactly "
						+ Math.round(time)
						+ " seconds, which is respectable (at least by some standards).\n"
						+ "In addition you achieved a score of "
						+ Math.round(score)
						+ ".\n"
						+ "\nPRESS ESC TO RETURN TO THE MENU ",
				Art.getSkin(), "textStyle");

		ui.add(text).center();

		ui.row();

		TextButton button = new TextButton("Menu", Art.getSkin());

		button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				getGame().showMenuScreen();
			}
		});

		ui.add(button).width(150);
	}
}

package com.planepanic.model.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.planepanic.ATC;
import com.planepanic.model.Art;
import com.planepanic.model.Config;
import com.planepanic.model.controllers.JoinGameController;

public class JoinGameScreen extends AbstractScreen {
	private final Table ui;

	public JoinGameScreen(ATC game) {
		super(game);
		ui = new Table();
		getStage().addActor(ui);

		new JoinGameController(this, ui);

		// make it fill the whole screen
		ui.setFillParent(true);
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		Stage root = getStage();

		drawString(Config.COPYRIGHT_NOTICE, 10, 10, Color.BLACK,
				root.getSpriteBatch(), false, 0.5f);

		draw(Art.getTextureRegion("menuAircraft"), 300, 390,
				root.getSpriteBatch());

		draw(Art.getTextureRegion("libgdx"), 1228, 0,
				root.getSpriteBatch());
	}

	@Override
	public void show()
	{
		super.show();
		Art.getSound("comeflywithme").play(1f);
	}

}

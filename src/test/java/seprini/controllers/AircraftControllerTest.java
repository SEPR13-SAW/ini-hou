package seprini.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import seprini.ATC;
import seprini.data.Config;
import seprini.data.FakeArtEnabler;
import seprini.data.GameDifficulty;
import seprini.models.Aircraft;
import seprini.models.Airport;
import seprini.models.Airspace;
import seprini.models.Waypoint;
import seprini.models.types.AircraftType;
import seprini.screens.ScreenBase;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Test class for {@link AircraftController}
 */
@RunWith(JUnit4.class)
public class AircraftControllerTest extends FakeArtEnabler
{
	/** Returns a difficulty which no aircraft can be generated in */
	private static GameDifficulty getNoAircraftDifficulty()
	{
		return new GameDifficulty(0, 100000, 0, 0, false);
	}

	/** Generates a fake aircraft at the given position */
	private static Aircraft makeFakeAircraft(float x, float y)
	{
		AircraftType aircraftType = new AircraftType().setRadius(50).setMaxClimbRate(100000000);

		ArrayList<Waypoint> flightPlan = new ArrayList<Waypoint>();
		flightPlan.add(new Waypoint(x, y, false));
		flightPlan.add(new Waypoint(1000, 1000, false));

		Aircraft aircraft = new Aircraft(aircraftType, flightPlan, 1, false, new Airport(Config.AIRPORT_COORDIATES[0], 0), null);

		// Force middle altitude
		if (aircraft.getAltitude() < 10000)
		{
			aircraft.increaseAltitude();
			aircraft.act(1);
		}
		if (aircraft.getAltitude() > 10000)
		{
			aircraft.decreaseAltitude();
			aircraft.act(1);
		}

		return aircraft;
	}

	@Test
	public void testNoCollision()
	{
		// Create 2 aircraft in different places - should not collide
		ScreenBaseImpl screenBase = new ScreenBaseImpl();
		AircraftController controller = new AircraftController(getNoAircraftDifficulty(), new Airspace(), screenBase);

		controller.getAircraftList().add(makeFakeAircraft(100, 100));
		controller.getAircraftList().add(makeFakeAircraft(400, 100));

		// Should not crash + aircraft should still be on the lists
		controller.update(0.1f);
		assertThat(controller.getAircraftList(), hasSize(2));
		assertThat(screenBase.gameEnded, is(false));
	}

	@Test
	public void testCollision()
	{
		// Create 2 aircraft in same place - should collide
		ScreenBaseImpl screenBase = new ScreenBaseImpl();
		AircraftController controller = new AircraftController(getNoAircraftDifficulty(), new Airspace(), screenBase);

		controller.getAircraftList().add(makeFakeAircraft(100, 100));
		controller.getAircraftList().add(makeFakeAircraft(100, 100));

		// Should crash
		controller.update(0.1f);
		assertThat(screenBase.gameEnded, is(true));
	}

	@Test
	public void testMaxAircraft()
	{
		// Generate airspace with 2 aircraft in it (with limit on 2)
		GameDifficulty difficulty = new GameDifficulty(2, 0, 0, 0, false);
		AircraftController controller = new AircraftController(difficulty, new Airspace(), null);
		controller.getAircraftList().add(makeFakeAircraft(500, 500));
		controller.getAircraftList().add(makeFakeAircraft(100, 100));

		// Keep refreshing, no more should appear
		for (int i = 0; i < 100; i++)
		{
			controller.update(1);
			assertThat(controller.getAircraftList(), hasSize(2));
		}
	}

	/**
	 * Implementation of ScreenBase which detects when the game ends
	 */
	private class ScreenBaseImpl implements ScreenBase
	{
		public boolean gameEnded = false;

		@Override
		public ATC getGame()
		{
			return new ATC()
			{
				public void showEndScreen(float time, float score)
				{
					gameEnded = true;
				}
			};
		}

		@Override public boolean isPaused() { return false; }
		@Override public void setPaused(boolean paused) { }
		@Override public void render(float delta) { }
		@Override public void resize(int width, int height) { }
		@Override public void show() { }
		@Override public void hide() { }
		@Override public void pause() { }
		@Override public void resume() { }
		@Override public void dispose() { }
	}
}

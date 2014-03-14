package seprini.models;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import seprini.data.Config;
import seprini.models.types.AircraftType;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static seprhou.logic.IsCloseToFloat.closeTo;

/**
 * Test class for {@link Aircraft}
 */
@RunWith(JUnit4.class)
public class AircraftTest
{
	private static final Integer[] OBJECT_ALTITUDES = arrayToObject(Config.ALTITUDES);

	private Aircraft aircraft;
	private AircraftType aircraftType;
	private List<Waypoint> originalFlightPlan;

	@Before
	public void setupAircraft()
	{
		// Create flight plan
		ArrayList<Waypoint> flightPlan = new ArrayList<Waypoint>();
		flightPlan.add(new Waypoint(0, 0, true));
		flightPlan.add(new Waypoint(100, 500, true));
		flightPlan.add(new Waypoint(700, 700, true));
		originalFlightPlan = new ArrayList<Waypoint>(flightPlan);

		// Create aircraft type
		aircraftType = new AircraftType()
				.setMaxClimbRate(100)
				.setMinSpeed(30f)
				.setMaxSpeed(90f)
				.setMaxTurningSpeed(50f)
				.setRadius(15)
				.setSeparationRadius(100)
				.setInitialSpeed(30f);

		// Create aircraft
		aircraft = new Aircraft(aircraftType, flightPlan, 1, false, new Airport(Config.AIRPORT_COORDIATES[0]), null);
	}

	/**
	 * Tests the initial state of the aircraft
	 */
	@Test
	public void testInitialState()
	{
		assertThat(aircraft.isActive(),        is(true));
		assertThat(aircraft.isBreaching(),     is(false));
		assertThat(aircraft.getAltitude(),     isIn(OBJECT_ALTITUDES));
		assertThat(aircraft.getFlightPlan(),   hasSize(originalFlightPlan.size() - 1));
		assertThat(aircraft.getNextWaypoint(), is(originalFlightPlan.get(1)));
		assertThat(aircraft.getCoords(),       is(originalFlightPlan.get(0).getCoords()));
		assertThat(aircraft.getSpeed(),        is(closeTo(aircraftType.getInitialSpeed())));
	}

	@Test
	public void testSpeedChanges()
	{
		float initialSpeed = aircraft.getSpeed();

		// Test increase
		aircraft.increaseSpeed();
		aircraft.act(1);
		assertThat(aircraft.getSpeed(), is(greaterThan(initialSpeed)));

		// Test decrease back to original speed
		aircraft.decreaseSpeed();
		aircraft.act(1);
		assertThat(aircraft.getSpeed(), is(closeTo(initialSpeed)));
	}

	@Test
	public void testIncreaseAltitude()
	{
		int initialAltitude = aircraft.getAltitude();

		// Increase altitude
		aircraft.increaseAltitude();
		aircraft.act(1);

		// Test result
		if (initialAltitude != 15000)
		{
			// Should have got closer to altitude and eventually get to it
			int nextHigher = initialAltitude + 5000;
			assertThat(aircraft.getAltitude(), is(both(greaterThan(initialAltitude)).and(lessThanOrEqualTo(nextHigher))));
		}
		else
		{
			assertThat(aircraft.getAltitude(), is(initialAltitude));
		}
	}

	@Test
	public void testDecreaseAltitude()
	{
		int initialAltitude = aircraft.getAltitude();

		// Decrease altitude
		aircraft.decreaseAltitude();
		aircraft.act(1);

		// Test result
		if (initialAltitude != 5000)
		{
			// Should have got closer to altitude and eventually get to it
			int nextLower = initialAltitude - 5000;
			assertThat(aircraft.getAltitude(), is(both(lessThan(initialAltitude)).and(greaterThanOrEqualTo(nextLower))));
		}
		else
		{
			assertThat(aircraft.getAltitude(), is(initialAltitude));
		}
	}

	@Test
	public void testFollowFlightPath()
	{
		int prevWaypointsLeft = aircraft.getFlightPlan().size();

		// The plane should eventually follow the waypoints and complete the flight plan by itself
		for (int i = 0; i < 100; i++)
		{
			aircraft.act(1);

			// Number of waypoints left muct decrease by 1 or stay the same
			int waypointsLeft = aircraft.getFlightPlan().size();

			assertThat(waypointsLeft, is(either(equalTo(prevWaypointsLeft)).or(equalTo(prevWaypointsLeft - 1))));

			// Exit if there are no waypoints left
			if (waypointsLeft == 0)
				return;

			prevWaypointsLeft = waypointsLeft;
		}

		// Did not complete in the time
		fail("did not complete flight plan in the allowed time");
	}

	@Test
	public void testManualControl()
	{
		// Take manual control for 1 tick
		aircraft.act(1);
		aircraft.turnRight(true);
		aircraft.act(1);
		aircraft.turnRight(false);

		// Must still be active
		assertThat(aircraft.isActive(), is(true));

		// Should exit without hitting any waypoints
		for (int i = 0; i < 100; i++)
		{
			aircraft.act(1);

			// Test flight plan has not changed
			assertThat(aircraft.getFlightPlan(), hasSize(2));
			assertThat(aircraft.getSpeed(), is(closeTo(aircraftType.getInitialSpeed())));

			// If inactive, exit now
			if (!aircraft.isActive())
				return;
		}

		// Did not complete in the time
		fail("did not exit airspace in the allowed time");
	}

	@Test
	public void testReturnToPath()
	{
		// Take manual control for 2 ticks and then return it
		aircraft.act(1);
		aircraft.turnRight(true);
		aircraft.act(1);
		aircraft.act(1);
		aircraft.returnToPath();

		// Must still be active
		assertThat(aircraft.isActive(), is(true));

		// Must now follow the flight plan
		testFollowFlightPath();
	}

	/** Converts primitive integer arrays to their object equivalent */
	private static Integer[] arrayToObject(int[] array)
	{
		Integer[] result = new Integer[array.length];
		for (int i = 0; i < array.length; i++)
			result[i] = array[i];
		return result;
	}
}

package seprini.controllers.components;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import seprini.controllers.AircraftController;
import seprini.data.Config;
import seprini.data.GameDifficulty;
import seprini.models.Airport;
import seprini.models.Airspace;
import seprini.models.Waypoint;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static seprhou.logic.IsDistinct.distinct;

/**
 * Test class for {@link FlightPlanComponent}
 */
@RunWith(Parameterized.class)
public class FlightPlanComponentTest
{
	private FlightPlanComponent flightPlanComponent;
	private WaypointComponent waypointComponent;

	@Parameterized.Parameters
	public static Collection<Object[]> data()
	{
		// Run tests 10 times
		return Arrays.asList(new Object[10][0]);
	}

	/** Setup flight plan generator */
	@Before
	public void setupGenerator()
	{
		waypointComponent = new WaypointComponent(
				new AircraftController(GameDifficulty.EASY, new Airspace(), new Airport(Config.AIRPORT_COORDIATES[0]), null));
		flightPlanComponent = new FlightPlanComponent(waypointComponent);
	}

	@Test
	public void testValidFlightPlan()
	{
		// Generates a flight plan and validates it
		List<Waypoint> flightPlan = flightPlanComponent.generate();
		assertThat(flightPlan, is(notNullValue()));

		// Test list size
		assertThat(flightPlan, hasSize(greaterThanOrEqualTo(3)));

		// Test waypoints are from correct lists
		//  here the toArray is a hack to force covariance of the list (which is ok as it's never modified)
		List<Waypoint> waypointList = waypointComponent.getPermanentList();
		Object[] entryList = waypointComponent.getEntryList().toArray();
		Object[] exitList = waypointComponent.getExitList().toArray();

		assertThat(flightPlan.get(0), isIn(entryList));
		assertThat(flightPlan.get(flightPlan.size() - 1), isIn(exitList));
		assertThat(flightPlan.subList(1, flightPlan.size() - 1), everyItem(isIn(waypointList)));

		// All waypoints must be distinct
		assertThat(flightPlan, is(distinct()));
	}
}

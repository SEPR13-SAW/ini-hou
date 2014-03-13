package seprini.controllers.components;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import seprini.controllers.AircraftController;
import seprini.data.Config;
import seprini.data.GameDifficulty;
import seprini.models.Airport;
import seprini.models.Airspace;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Test class for {@link WaypointComponent}
 */
@RunWith(JUnit4.class)
public class WaypointComponentTest
{
	private WaypointComponent waypointComponent;

	@Before
	public void setUpWaypointComponent()
	{
		waypointComponent = new WaypointComponent(
		                        new AircraftController(GameDifficulty.EASY, new Airspace(), new Airport(Config.AIRPORT_COORDIATES[0]), null));
	}

	/** Should be >= 2 entry points */
	@Test
	public void testEntrypoints()
	{
		assertThat(waypointComponent.getEntryList(), hasSize(greaterThanOrEqualTo(2)));
	}

	/** Should be >= 2 exit points */
	@Test
	public void testExitpoints()
	{
		assertThat(waypointComponent.getExitList(), hasSize(greaterThanOrEqualTo(2)));
	}

	/** Should be >= 6 way points */
	@Test
	public void testWaypoints()
	{
		assertThat(waypointComponent.getPermanentList(), hasSize(greaterThanOrEqualTo(6)));
	}
}

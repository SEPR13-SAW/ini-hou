package seprini.controllers.components;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.planepanic.model.controllers.aircraft.SingleAircraftController;
import com.planepanic.model.GameDifficulty;
import com.planepanic.model.Airspace;
import com.planepanic.model.controllers.components.WaypointComponent;

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
		                        new SingleAircraftController(GameDifficulty.EASY, new Airspace(), null));
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

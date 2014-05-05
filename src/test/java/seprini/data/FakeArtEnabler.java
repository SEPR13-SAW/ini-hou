package seprini.data;

import org.junit.BeforeClass;
import com.planepanic.model.resources.Art;

/**
 * Class which enables the use of fake artwork so that classes can be tested without a graphics system
 */
public class FakeArtEnabler
{
	@BeforeClass
	public static void enableFakeArt()
	{
		Art.setUseFakeArt(true);
	}
}

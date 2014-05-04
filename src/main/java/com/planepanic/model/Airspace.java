package seprini.models;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.Comparator;

public class Airspace extends Group {

	/**
	 * Sorts this airspace's aircraft so they appear in altitude order
	 */
	public void sortAircraft()
	{
		this.getChildren().sort(AltitudeComparator.INSTANCE);
	}

	private static class AltitudeComparator implements Comparator<Actor>
	{
		public static final AltitudeComparator INSTANCE = new AltitudeComparator();

		@Override
		public int compare(Actor o1, Actor o2)
		{
			if (o1 instanceof Aircraft)
			{
				if (o2 instanceof Aircraft)
				{
					// Test altitudes
					return Float.compare(((Aircraft) o1).getAltitude(), ((Aircraft) o2).getAltitude());
				}
				else
				{
					// aircraft > non-aircraft
					return 1;
					
				}
			}
			else
			{
				return (o2 instanceof Aircraft) ? -1 : 0;
			}
		}
	}
}

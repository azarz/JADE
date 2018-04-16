package eu.ensg.jade.semantic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

/**
 * LineRoadTest is the class testing the {@link LineRoad} class
 * 
 * @author JADE
 */

@RunWith(MockitoJUnitRunner.class)
public class LineRoadTest {

	/**
	 *  Constructor and mutators
	 */
	@Test
	public void testLineRoadTest() {

		LineRoad lineRoad = new LineRoad();
		assertNotNull(lineRoad);

		//Geometry used for the roads
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

		//Coordinates
		Coordinate[] right1Coord = new Coordinate[] {new Coordinate(0, 0), new Coordinate(10, 0)};

		//LineString
		LineString right1LS = geometryFactory.createLineString(right1Coord);

		//MultiLineString (last step, as MultiLineString is the one we need)
		MultiLineString right1MLS = geometryFactory.createMultiLineString(new LineString[] {right1LS});


		lineRoad = new LineRoad(1, 0, 1, 1, "Direct", "Boulevard", "National", "1", "Unexisting", right1MLS);
		
		assertNotNull(lineRoad);
		assertTrue(lineRoad.getGeom().equalsExact(right1MLS));
		assertNotNull(lineRoad.getSF());
		
		int sizeBefore = lineRoad.getSF().size();
		
		lineRoad.addSF(Mockito.mock(StreetFurniture.class));
		
		assertTrue(lineRoad.getSF().size()-sizeBefore==1);
		
		//Not unit if we try to replicate enlarge
		assertNotNull(lineRoad.enlarge());
	}
}

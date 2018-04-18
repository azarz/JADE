package eu.ensg.jade.geometricObject;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

import eu.ensg.jade.semantic.LineRoad;



/**
 * RoadTest is the class testing the {@link Road} class
 * 
 * @author JADE
 */

public class RoadTest {

	/**
	 *  Constructor and mutators
	 */
	@Test
	public void testRoadTest(){	
		
	Road road = new LineRoad();
	assertNotNull(road); 
	
	//Geometry used for the roads
	GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

	//Coordinates
	Coordinate[] right1Coord = new Coordinate[] {new Coordinate(0, 0), new Coordinate(10, 0)};

	//LineString
	LineString right1LS = geometryFactory.createLineString(right1Coord);

	//MultiLineString (last step, as MultiLineString is the one we need)
	MultiLineString right1MLS = geometryFactory.createMultiLineString(new LineString[] {right1LS});


	road = new LineRoad(1, 0, 1, 1, "Direct", "Boulevard", "2", "NC", "Unexisting", right1MLS);

	assertNotNull(road);
	assertTrue(road.getWidth() == 1);
	assertTrue(road.getLaneNumber() == 0);
	assertTrue(road.getZ_ini() == 1);
	assertTrue(road.getZ_fin() == 1);
	assertTrue(road.getDirection().equals("Direct"));
	assertTrue(road.getNature().equals("Boulevard"));
	assertTrue(road.getImportance().equals("2"));
	assertTrue(road.getNumber().equals("NC"));
	assertTrue(road.getName().equals("Unexisting"));
	assertTrue(road.getSpeed().equals("50 km/h"));

	}
	
	/**
	 * Test method for {@link eu.ensg.jade.geometricObject.Road#setSpeed()}.
	 */
	@Test
	public void setSpeedTest(){
		//Geometry used for the roads
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

		//Coordinates
		Coordinate[] right1Coord = new Coordinate[] {new Coordinate(0, 0), new Coordinate(10, 0)};

		//LineString
		LineString right1LS = geometryFactory.createLineString(right1Coord);

		//MultiLineString (last step, as MultiLineString is the one we need)
		MultiLineString right1MLS = geometryFactory.createMultiLineString(new LineString[] {right1LS});


		Road road1 = new LineRoad(1, 0, 1, 1, "Direct", "Boulevard", "2", "NC", "Unexisting", right1MLS);
		Road road2 = new LineRoad(1, 0, 1, 1, "Direct", "Boulevard", "4", "NC", "Unexisting", right1MLS);
		
		Road road3 = new LineRoad(1, 0, 1, 1, "Direct", "Boulevard", "4", "D", "Unexisting", right1MLS);
		Road road4 = new LineRoad(1, 0, 1, 1, "Direct", "Bretelle", "2", "AB", "Unexisting", right1MLS);
		
		Road road5 = new LineRoad(1, 0, 1, 1, "Direct", "Boulevard", "1", "D", "Unexisting", right1MLS);
		Road road6 = new LineRoad(1, 0, 1, 1, "Direct", "Bretelle", "1", "AB", "Unexisting", right1MLS);
		Road road7 = new LineRoad(1, 0, 1, 1, "Direct", "Boulevard", "1", "NC", "Unexisting", right1MLS);

		Road road8 = new LineRoad(1, 0, 1, 1, "Direct", "Boulevard", "2", "Ntest", "Unexisting", right1MLS);

		Road road9 = new LineRoad(1, 0, 1, 1, "Direct", "Boulevard", "2", "Autoroute", "Unexisting", right1MLS);

		assertTrue(road1.getSpeed().equals("50 km/h"));
		assertTrue(road2.getSpeed().equals("50 km/h"));
		
		assertTrue(road3.getSpeed().equals("70 km/h"));
		assertTrue(road4.getSpeed().equals("70 km/h"));
		
		assertTrue(road5.getSpeed().equals("90 km/h"));
		assertTrue(road6.getSpeed().equals("90 km/h"));
		assertTrue(road7.getSpeed().equals("90 km/h"));
		
		assertTrue(road8.getSpeed().equals("110 km/h"));
		
		assertTrue(road9.getSpeed().equals("130 km/h"));
	}
}

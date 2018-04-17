package eu.ensg.jade.semantic;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * ArcIntersectionTest is the class testing the {@link ArcIntersection} class
 * 
 * @author JADE
 */

@RunWith(MockitoJUnitRunner.class)
public class StreetFurnitureTest {
	
	/**
	 * Test for constructor and mutators
	 */
	@Test
	public void TestStreetFurniture(){
		
		Coordinate coord = new Coordinate(0,0);
		
		StreetFurniture sf1 = new StreetFurniture(coord);
		StreetFurniture sf2 = new StreetFurniture("Somewhere",coord,Math.PI);
		
		assertNotNull(sf1);
		assertNotNull(sf1);
		assertTrue(sf2.getPath()=="Somewhere");
		assertEquals(sf2.getRotation(),Math.PI,0.01);
		assertTrue(sf2.getCoord().equals2D(coord));
		assertEquals(sf2.getId(),sf1.getId()+1);
	}

}

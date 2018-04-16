package eu.ensg.jade.semantic;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * IntersectionTest is the class testing the {@link Intersection} class
 * 
 * @author JADE
 */
@RunWith(MockitoJUnitRunner.class)
public class IntersectionTest {
	
	/**
	 * Test constructor, and mutators
	 */
	@Test
	public void TestIntersection() {
		
		assertNotNull(new Intersection());
		
		Intersection intersection = new Intersection(new Coordinate(0,0));
		intersection.addRoadID("NotARoad", false);
		
		assertNotNull(intersection);
		assertTrue(intersection.getGeometry().equals2D(new Coordinate(0,0)));
		assertNotNull(intersection.getRoadId());
		assertTrue(intersection.getRoadId().get("NotARoad")==false);
		assertTrue(intersection.getRoadId().size()==1);
		

	}

}

package eu.ensg.jade.geometricObject;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.*;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

import eu.ensg.jade.semantic.StreetFurniture;

/**
 * PunctualObjectTest is the class testing the {@link PunctualObject} class
 * 
 * @author JADE
 */
public class PunctualObjectTest {
	
	/**
	 *  Constructor and mutators
	 */
	@Test
	public void testPunctualObjectTest(){
		Coordinate coord = new Coordinate(0,0);

		PunctualObject po = new StreetFurniture(coord);
		assertNotNull(po);
		assertTrue(po.getCoord().equals(coord));
	
	}
	
}

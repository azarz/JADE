package eu.ensg.jade.semantic;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.vividsolutions.jts.geom.Coordinate;

import eu.ensg.jade.rules.VegetationRule.TREE;

/**
 * PointVegetationTest is the class testing the {@link PointVegetation} class
 * 
 * @author JADE
 */

@RunWith(MockitoJUnitRunner.class)
public class PointVegetationTest {
	
	/**
	 * Test for constructor and mutators
	 */
	@Test
	public void testPointVegetation() {
		
		Coordinate coord = new Coordinate(0,0);
		
		PointVegetation point = new PointVegetation(coord, TREE.DECIDUOUS);
		
		assertNotNull(point);
		assertTrue(point.getNature().equals("Models/Trees/deciduousTree/tree.obj"));
	}

}

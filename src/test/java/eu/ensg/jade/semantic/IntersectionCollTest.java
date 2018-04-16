package eu.ensg.jade.semantic;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * ArcIntersectionTest is the class testing the {@link ArcIntersection} class
 * 
 * @author JADE
 */
@RunWith(MockitoJUnitRunner.class)
public class IntersectionCollTest {

	/**
	 * Test Constructor and mutators
	 */
	@Test
	public void TestIntersectionColl(){
		HashMap<String, Intersection> mapIntersection = new HashMap<String, Intersection>();
		IntersectionColl coll = new IntersectionColl(mapIntersection);
		
		assertNotNull(coll);
		assertNotNull(coll.getMapIntersection());
		
		coll = new IntersectionColl();
		
		assertNotNull(coll);
		assertNotNull(coll.getMapIntersection());
		
		Intersection interMock = Mockito.mock(Intersection.class);
		
		coll.addIntersection("Mockito", interMock);
		
		assertTrue(coll.getMapIntersection().size()==1);
		assertNotNull(coll.getMapIntersection().get("Mockito"));
	}
}

package eu.ensg.jade.semantic;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * IntersectionTest is the class testing the {@link Intersection} class
 * 
 * @author JADE
 */
@RunWith(MockitoJUnitRunner.class)
public class IntersectionTest {
	
	/**
	 * Test constructor
	 */
	@Test
	public void TestIntersection() {
		assertNotNull(new Intersection());
		
	}

}

package utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import eu.ensg.jade.utils.PoissonDiskSampler;

/**
 * PoissonDiskSamplerTest is the class testing the {@link PoissonDiskSampler} class
 * 
 * @author JADE
 */

@RunWith(MockitoJUnitRunner.class)
public class PoissonDiskSamplerTest {

	/**
	 * Constructor
	 */
	@Test
	public void TestPoissonDiskSampler() {
		PoissonDiskSampler sample = new PoissonDiskSampler(10.0,10.0,20.0,20.0,1.0);
		assertNotNull(sample);
		sample = new PoissonDiskSampler(10.0,10.0,20.0,20.0,1.0,1);

	}

	/**
	 * Compute
	 */
	@Test
	public void TestCompute() {
		PoissonDiskSampler sample = new PoissonDiskSampler(0,0,10,10,10);
		assertNotNull(sample);
		assertTrue(sample.compute().size()==1);
		sample = new PoissonDiskSampler(0,0,100,100,1);
		assertTrue(sample.compute().size()>1);
	}
	
	/**
	 * Next
	 */
	@Test
	public void TestNext() {
		PoissonDiskSampler sample = new PoissonDiskSampler(0,0,10,10,10);
		assertNotNull(sample);
		double[] next1 = sample.next();
		double[] next2 = sample.next();
		assertNotNull(next1);
		assertNull(next2);
	}

}

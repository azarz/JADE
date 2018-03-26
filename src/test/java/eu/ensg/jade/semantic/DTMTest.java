package eu.ensg.jade.semantic;

import static org.junit.Assert.*;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

import eu.ensg.jade.input.BuildingSHP;
import eu.ensg.jade.input.ShpReader;

/**
 * DTMTest is the class testing the {@link DTM} class
 * 
 * @author JADE
 */

public class DTMTest {
	
// ========================== ATTRIBUTES ===========================
	
	/**
	 * The instance representing the tested class
	 */
	private DTM dtmObject;
	
	/**
	 * The table associates to the DTM
	 */
	private double[][] tabGet;
	
	/**
	 * The header containing the metadata of the DTM 
	 */
	private Map<String,Double> headerGet;
	
	/**
	 * The instance representing the maximum delta between two double
	 */
	private static final double delta = 10e-15;
	
// ========================== METHODS ==============================

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		// Tab init
		tabGet = new double[2][3];
		tabGet[0][0] = 1;
		tabGet[0][1] = 2;
		tabGet[0][2] = 3;
		tabGet[1][0] = 4;
		tabGet[1][1] = 5.2;
		tabGet[1][2] = 7.8;
		
		// Header init
		headerGet = new HashMap<String,Double>();
		headerGet.put("ncols", 3.0);
		headerGet.put("nrows", 2.0);
		
		// DTM init
		dtmObject = new DTM(tabGet,headerGet);
	}

	/**
	 * Test method for {@link eu.ensg.jade.semantic.DTM#getTabDTM()}.
	 */
	@Test
	public void testGetTabDTM() {
		double[][] result = dtmObject.getTabDTM();
		assertNotNull(result);
		assertEquals(result.length,2);
		assertEquals(result[0].length,3);
		assertEquals(result[0][0],1.0,delta);
		assertEquals(result[0][1],2.0,delta);
		assertEquals(result[0][2],3.0,delta);
		assertEquals(result[1][0],4.0,delta);
		assertEquals(result[1][1],5.2,delta);
		assertEquals(result[1][2],7.8,delta);
	}

	/**
	 * Test method for {@link eu.ensg.jade.semantic.DTM#getHeaderDTM()}.
	 */
	@Test
	public void testGetHeaderDTM() {
		assertEquals(dtmObject.getNcols(),3.0,delta);
		assertEquals(dtmObject.getNrows(),2.0,delta);
	}

	/**
	 * Test method for {@link eu.ensg.jade.semantic.DTM#toPNG()}.
	 * 
	 * @throws IOException 
	 */
	@Test
	public void testToPNG() throws IOException {
		dtmObject.toPNG("src/test/resources/imageDTM.png");
		
		// Open the file
		File result = new File("src/test/resources/imageDTM.png");
		assertNotNull(result);
		
		// Read the image
		Image resultImage = ImageIO.read(result);
		assertNotNull(resultImage);
	}

}

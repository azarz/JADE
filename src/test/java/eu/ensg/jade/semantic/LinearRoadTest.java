package eu.ensg.jade.semantic;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;

import org.junit.Test;

import com.vividsolutions.jts.geom.MultiLineString;

import eu.ensg.jade.input.BuildingSHP;
import eu.ensg.jade.input.OutputRGE;

public class LinearRoadTest {

	/**
	 * Test method for {@link eu.ensg.jade.semantic.LinearRoad#enlarge())}.
	 * @throws IOException 
	 */
	@Test
	public void testEnlarge() {

		MultiLineString geom=new MultiLineString(null, null);
		LinearRoad linearRoad=new LinearRoad(1, 0, 0, 0, null, geom);
		//Comment créer une geom? voir la mocker?

	}

}

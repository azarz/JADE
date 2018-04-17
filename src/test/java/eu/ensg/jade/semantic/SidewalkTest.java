package eu.ensg.jade.semantic;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;

import java.util.ArrayList;
import java.util.List;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

/**
 * SidewalkTest is the class testing the {@link Sidewalk} class
 * 
 * @author JADE
 */

@RunWith(MockitoJUnitRunner.class)
public class SidewalkTest {
	
	/**
	 * Tests for all
	 */
	@Test
	public void testSidewalk() {
		
		//GeometryFactory
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
		
		Coordinate[] coods = new Coordinate[] {new Coordinate(0,0), new Coordinate(10,0)};
		
		LineString lS = geometryFactory.createLineString(coods);
	
		DTM dtm = Mockito.mock(DTM.class);
		Mockito.when(dtm.getHeightAtPoint(any(double.class), any(double.class))).thenReturn(0d);
		Mockito.when(dtm.getCellsize()).thenReturn(1d);
		
		MultiLineString mLS = geometryFactory.createMultiLineString(new LineString[]{lS});
		
		List<Integer> indexOffsets = new ArrayList<Integer>();
		indexOffsets.add(1);
		indexOffsets.add(1);
		indexOffsets.add(1);
		
		Sidewalk sidewalk = new Sidewalk(mLS, 1, mLS.buffer(1), dtm);
		
		assertNotNull(sidewalk);
		assertNotNull(sidewalk.toOBJ(indexOffsets, 1., 1.));
		
	}

}

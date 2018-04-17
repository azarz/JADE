package utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Polygon;

import eu.ensg.jade.semantic.LineRoad;
import eu.ensg.jade.utils.JadeUtils;

/**
 * JadeUtilsTest is the class testing the {@link JadeUtils} class
 * 
 * @author JADE
 */

@RunWith(MockitoJUnitRunner.class)
public class JadeUtilsTest {

	/**
	 * Test for getDistance
	 */
	@Test
	public void testGetDistance() {
		double[] pt1 = new double[] {0,0,0};
		double[] pt2 = new double[] {1,0,0};
		double[] pt3 = new double[] {0,1,0};
		double[] pt4 = new double[] {0,0,1};
		double[] pt5 = new double[] {-1,-1,-1};
		double[] pt6 = new double[] {2,3,4};

		assertEquals(JadeUtils.getDistance(pt1, pt2),1,0);
		assertEquals(JadeUtils.getDistance(pt1, pt3),1,0);
		assertEquals(JadeUtils.getDistance(pt1, pt4),1,0);
		assertEquals(JadeUtils.getDistance(pt1, pt5),Math.sqrt(3),0.01);
		assertEquals(JadeUtils.getDistance(pt1, pt6),Math.sqrt(29),0.01);
	}

	/**
	 * Test for normal vector
	 */
	@Test
	public void testGetNormalVector() {
		double[] pt1 = new double[] {0,0,0};
		double[] pt2 = new double[] {1,0,0};
		double[] pt3 = new double[] {0,1,0};

		double[] norm = JadeUtils.getNormalVector(pt1, pt2, pt3);

		assertEquals(norm[0],0,0);
		assertEquals(norm[2],0,0);
		assertEquals(norm[1],-1,0);

		pt2 = new double[] {3,0,0};
		pt3 = new double[] {0,3,0};

		norm = JadeUtils.getNormalVector(pt1, pt2, pt3);

		assertEquals(norm[0],0,0);
		assertEquals(norm[2],0,0);
		assertEquals(norm[1],-1,0);

		Coordinate c1 = new Coordinate(0,0,0);
		Coordinate c2 = new Coordinate(1,0,0);
		Coordinate c3 = new Coordinate(0,1,0);
		Coordinate c4 = new Coordinate(3,0,0);
		Coordinate c5 = new Coordinate(0,3,0);

		norm = JadeUtils.getNormalVector(c1, c2, c3);

		assertEquals(norm[0],0,0);
		assertEquals(norm[2],0,0);
		assertEquals(norm[1],-1,0);

		norm = JadeUtils.getNormalVector(c1, c4, c5);

		assertEquals(norm[0],0,0);
		assertEquals(norm[2],0,0);
		assertEquals(norm[1],-1,0);
	}

	
	/**
	 * Test method for roadAngle.
	 */
	@Test
	public void testRoadAngle(){

		//Geometry factory
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

		//Road coordinate
		Coordinate coorA = new Coordinate(0,0);
		Coordinate coorB = new Coordinate(1,0);
		Coordinate coorC = new Coordinate(0,1);
		Coordinate coorD = new Coordinate(-1,0);
		Coordinate coorE = new Coordinate(0,-1);
		Coordinate coorF = new Coordinate(0,-0.5);
		Coordinate coorG = new Coordinate(1,-0.5);
		Coordinate coorH = new Coordinate(2,0);
		Coordinate coorI = new Coordinate(3,0);
		Coordinate coorJ = new Coordinate(-1,-1);

		//Road LineString
		LineString lsAB = geometryFactory.createLineString(new Coordinate[] {coorA,coorB});
		LineString lsAC = geometryFactory.createLineString(new Coordinate[] {coorA,coorC});
		LineString lsAD = geometryFactory.createLineString(new Coordinate[] {coorA,coorD});
		LineString lsAE = geometryFactory.createLineString(new Coordinate[] {coorA,coorE});
		LineString lsFG = geometryFactory.createLineString(new Coordinate[] {coorF,coorG});
		LineString lsHI = geometryFactory.createLineString(new Coordinate[] {coorH,coorI});
		LineString lsDJ = geometryFactory.createLineString(new Coordinate[] {coorD,coorJ});

		//Road MultiLineString
		MultiLineString mlsAB = geometryFactory.createMultiLineString(new LineString[] {lsAB});
		MultiLineString mlsAC = geometryFactory.createMultiLineString(new LineString[] {lsAC});
		MultiLineString mlsAD = geometryFactory.createMultiLineString(new LineString[] {lsAD});
		MultiLineString mlsAE = geometryFactory.createMultiLineString(new LineString[] {lsAE});
		MultiLineString mlsFG = geometryFactory.createMultiLineString(new LineString[] {lsFG});
		MultiLineString mlsHI = geometryFactory.createMultiLineString(new LineString[] {lsHI});
		MultiLineString mlsDJ = geometryFactory.createMultiLineString(new LineString[] {lsDJ});

		//Creating the lineroads with mockito
		LineRoad lrAB = Mockito.mock(LineRoad.class);
		LineRoad lrAC = Mockito.mock(LineRoad.class);
		LineRoad lrAD = Mockito.mock(LineRoad.class);
		LineRoad lrAE = Mockito.mock(LineRoad.class);
		LineRoad lrFG = Mockito.mock(LineRoad.class);
		LineRoad lrHI = Mockito.mock(LineRoad.class);
		LineRoad lrDJ = Mockito.mock(LineRoad.class);

		//Set getGeom 
		Mockito.when(lrAB.getGeom()).thenReturn(mlsAB);
		Mockito.when(lrAC.getGeom()).thenReturn(mlsAC);
		Mockito.when(lrAD.getGeom()).thenReturn(mlsAD);
		Mockito.when(lrAE.getGeom()).thenReturn(mlsAE);
		Mockito.when(lrFG.getGeom()).thenReturn(mlsFG);
		Mockito.when(lrHI.getGeom()).thenReturn(mlsHI);
		Mockito.when(lrDJ.getGeom()).thenReturn(mlsDJ);
		
		assertEquals(JadeUtils.roadAngle(lrAB, 0),90.0/180.0*Math.PI,0.01);
		assertEquals(JadeUtils.roadAngle(lrAC, 0),Math.PI,0.01);
		assertEquals(JadeUtils.roadAngle(lrAD, 0),270.0/180.0*Math.PI,0.01);
		assertEquals(JadeUtils.roadAngle(lrAE, 0),0,0.01);
		assertEquals(JadeUtils.roadAngle(lrFG, 0),90.0/180.0*Math.PI,0.01);
		assertEquals(JadeUtils.roadAngle(lrHI, 0),90.0/180.0*Math.PI,0.01);
		assertEquals(JadeUtils.roadAngle(lrDJ, 0),0,0.01);
	}
	
	/**
	 * Test triangulate
	 */
	@Test
	public void testTriangulate() {
		//Geometry factory
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
		
		Coordinate[] coordinates = new Coordinate[] { new Coordinate(0,0), new Coordinate(1,0), new Coordinate(1,1), new Coordinate(0,1), new Coordinate(0,0)};
		
		Polygon polygon = geometryFactory.createPolygon(coordinates);
		
		assertNotNull(JadeUtils.triangulate(polygon));
	}
	
	/**
	 * Test lerp
	 */
	@Test
	public void testLerp() {
		
		assertEquals(30,JadeUtils.lerp(5,10,5),0);
		
	}
	
}

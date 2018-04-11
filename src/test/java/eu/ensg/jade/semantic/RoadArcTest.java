package eu.ensg.jade.semantic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.geotools.geometry.jts.CircularArc;
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
 * ArcIntersectionTest is the class testing the {@link RoadArc} class
 * 
 * @author JADE
 */
@RunWith(MockitoJUnitRunner.class)
public class RoadArcTest {

	// ========================== METHODS ==============================

	/**
	 * Test Constructor
	 */
	@Test
	public void testRoadArc(){
		RoadArc roadArc = new RoadArc();
		assertNotNull(roadArc);
	}

	/**
	 * Test method for {@link eu.ensg.jade.semantic#intersectOther(org.geotools.geometry.jts.CircularArc,eu.ensg.jade.semantic.LineRoad)}.
	 */
	@Test
	public void testIntersectOther(){

		//Geometry used for the roads
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

		//Creating the roadArc
		CircularArc cirularArc = new CircularArc(0,0,5,5,0,10);

		Coordinate[] trueDoubleCoor = new Coordinate[] {new Coordinate(5, -1), new Coordinate(5, 11)};
		Coordinate[] trueTangCoor = new Coordinate[] {new Coordinate(2.5, -1), new Coordinate(2.5, 11)};
		Coordinate[] trueEdgeCoor = new Coordinate[] {new Coordinate(-1, 9), new Coordinate(1, 11)};
		Coordinate[] falseFar1Coor = new Coordinate[] {new Coordinate(6, -1), new Coordinate(6, 11)};
		Coordinate[] falseFar2Coor = new Coordinate[] {new Coordinate(-1, -1), new Coordinate(-1, 11)};
		Coordinate[] falseFar3Coor = new Coordinate[] {new Coordinate(-1, -1), new Coordinate(11, -1)};
		Coordinate[] falseFar4Coor = new Coordinate[] {new Coordinate(-1, 11), new Coordinate(11, 11)};

		//LineString
		LineString trueDoubleLS = geometryFactory.createLineString(trueDoubleCoor);
		LineString trueTangLS = geometryFactory.createLineString(trueTangCoor);
		LineString trueEdgeLS = geometryFactory.createLineString(trueEdgeCoor);
		LineString falseFar1LS = geometryFactory.createLineString(falseFar1Coor);
		LineString falseFar2LS = geometryFactory.createLineString(falseFar2Coor);
		LineString falseFar3LS = geometryFactory.createLineString(falseFar3Coor);
		LineString falseFar4LS = geometryFactory.createLineString(falseFar4Coor);
		
		//MultiLineString
		MultiLineString trueDoubleMLS = geometryFactory.createMultiLineString(new LineString[] {trueDoubleLS});
		MultiLineString trueTangMLS = geometryFactory.createMultiLineString(new LineString[] {trueTangLS});
		MultiLineString trueEdgeMLS = geometryFactory.createMultiLineString(new LineString[] {trueEdgeLS});
		MultiLineString falseFar1MLS = geometryFactory.createMultiLineString(new LineString[] {falseFar1LS});
		MultiLineString falseFar2MLS = geometryFactory.createMultiLineString(new LineString[] {falseFar2LS});
		MultiLineString falseFar3MLS = geometryFactory.createMultiLineString(new LineString[] {falseFar3LS});
		MultiLineString falseFar4MLS = geometryFactory.createMultiLineString(new LineString[] {falseFar4LS});

		//LineRoad
		LineRoad trueDouble = Mockito.mock(LineRoad.class);
		LineRoad trueTang = Mockito.mock(LineRoad.class);
		LineRoad trueEdge = Mockito.mock(LineRoad.class);
		LineRoad falseFar1 = Mockito.mock(LineRoad.class);
		LineRoad falseFar2 = Mockito.mock(LineRoad.class);
		LineRoad falseFar3 = Mockito.mock(LineRoad.class);
		LineRoad falseFar4 = Mockito.mock(LineRoad.class);
		
		//Set getGeom 
		Mockito.when(trueDouble.getGeom()).thenReturn(trueDoubleMLS);
		Mockito.when(trueTang.getGeom()).thenReturn(trueTangMLS);
		Mockito.when(trueEdge.getGeom()).thenReturn(trueEdgeMLS);
		Mockito.when(falseFar1.getGeom()).thenReturn(falseFar1MLS);
		Mockito.when(falseFar2.getGeom()).thenReturn(falseFar2MLS);
		Mockito.when(falseFar3.getGeom()).thenReturn(falseFar3MLS);
		Mockito.when(falseFar4.getGeom()).thenReturn(falseFar4MLS);
		
		assertTrue(RoadArc.intersectOther(cirularArc, trueDouble));
		assertTrue(RoadArc.intersectOther(cirularArc, trueTang));
		assertTrue(RoadArc.intersectOther(cirularArc, trueEdge));
		assertFalse(RoadArc.intersectOther(cirularArc, falseFar1));
		assertFalse(RoadArc.intersectOther(cirularArc, falseFar2));
		assertFalse(RoadArc.intersectOther(cirularArc, falseFar3));
		assertFalse(RoadArc.intersectOther(cirularArc, falseFar4));
	}

	/**
	 * Test method for cuttingPoint.
	 */
	@Test
	public void testCuttingPoint(){

	}

	/**
	 * Test method for calculAngle.
	 */
	@Test
	public void testCalculAngle(){

	}

	/**
	 * Test method for createRoadArc.
	 */
	@Test
	public void testCreateRoadArc(){

	}
}

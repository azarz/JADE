package eu.ensg.jade.semantic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

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
import com.vividsolutions.jts.geom.Point;

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

		//Geometry factory
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

		//Road coordinate
		Coordinate coorA = new Coordinate(12,0);
		Coordinate coorB = new Coordinate(0,0);
		Coordinate coorC = new Coordinate(0,12);

		//Road LineString
		LineString lsAB = geometryFactory.createLineString(new Coordinate[] {coorA,coorB});
		LineString lsBC = geometryFactory.createLineString(new Coordinate[] {coorB,coorC});

		//Road MultiLineString
		MultiLineString mlsAB = geometryFactory.createMultiLineString(new LineString[] {lsAB});
		MultiLineString mlsBC = geometryFactory.createMultiLineString(new LineString[] {lsBC});

		//Creating the lineroads with mockito
		LineRoad lrAB = Mockito.mock(LineRoad.class);
		LineRoad lrBC = Mockito.mock(LineRoad.class);

		//Set getGeom 
		Mockito.when(lrAB.getGeom()).thenReturn(mlsAB);
		Mockito.when(lrBC.getGeom()).thenReturn(mlsBC);

		//Set getWidth 
		Mockito.when(lrAB.getWidth()).thenReturn(10d);
		Mockito.when(lrBC.getWidth()).thenReturn(10d);

		//Set getNom 
		Mockito.when(lrAB.getName()).thenReturn("lrAB");
		Mockito.when(lrBC.getName()).thenReturn("lrBC");

		//Set getSpeed 
		Mockito.when(lrAB.getSpeed()).thenReturn("30 KM");
		Mockito.when(lrBC.getSpeed()).thenReturn("30 KM");

		RoadArc roadArc = new RoadArc(lrAB,lrBC);
		assertNotNull(roadArc);
	}

	/**
	 * Test method for {@link eu.ensg.jade.semantic#intersectOther(org.geotools.geometry.jts.CircularArc,eu.ensg.jade.semantic.LineRoad)}.
	 */
	@Test
	public void testIntersectOther(){

		//Geometry factory
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

		//Geometry factory
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

		//Points
		Point intersection= geometryFactory.createPoint(new Coordinate(0,0));
		Point extremity1 = geometryFactory.createPoint(new Coordinate(-2,5));
		Point extremity2 = geometryFactory.createPoint(new Coordinate(-2,-5));
		Point extremity3 = geometryFactory.createPoint(new Coordinate(2,5));
		Point extremity4 = geometryFactory.createPoint(new Coordinate(2,-5));
		Point extremity5 = geometryFactory.createPoint(new Coordinate(3,0));
		Point extremity6 = geometryFactory.createPoint(new Coordinate(-3,0));
		Point extremity7 = geometryFactory.createPoint(new Coordinate(3,13));
		Point extremity8 = geometryFactory.createPoint(new Coordinate(3,-13));

		Point expected1 = geometryFactory.createPoint(new Coordinate(0,5));
		Point expected2 = geometryFactory.createPoint(new Coordinate(0,-5));
		Point expected3 = geometryFactory.createPoint(new Coordinate(0,5));
		Point expected4 = geometryFactory.createPoint(new Coordinate(0,-5));
		Point expected5 = geometryFactory.createPoint(new Coordinate(0,0));
		Point expected6 = geometryFactory.createPoint(new Coordinate(0,0));
		Point expected7 = geometryFactory.createPoint(new Coordinate(0,10));
		Point expected8 = geometryFactory.createPoint(new Coordinate(0,-10));


		//Road coordinate
		Coordinate coorA = new Coordinate(0,10);
		Coordinate coorB = new Coordinate(0,0);
		Coordinate coorC = new Coordinate(0,-10);

		//Road LineString
		LineString lsAB = geometryFactory.createLineString(new Coordinate[] {coorA,coorB});
		LineString lsBC = geometryFactory.createLineString(new Coordinate[] {coorB,coorC});

		//Road MultiLineString
		MultiLineString mlsAB = geometryFactory.createMultiLineString(new LineString[] {lsAB});
		MultiLineString mlsBC = geometryFactory.createMultiLineString(new LineString[] {lsBC});

		//Creating the lineroads with mockito
		LineRoad lrAB = Mockito.mock(LineRoad.class);
		LineRoad lrBC = Mockito.mock(LineRoad.class);

		//Set getGeom 
		Mockito.when(lrAB.getGeom()).thenReturn(mlsAB);
		Mockito.when(lrBC.getGeom()).thenReturn(mlsBC);

		List<Point> list1 =RoadArc.cuttingPoint(extremity1,extremity2, lrAB, lrBC, intersection);
		List<Point> list2 =RoadArc.cuttingPoint(extremity3,extremity4, lrAB, lrBC, intersection);
		List<Point> list3 =RoadArc.cuttingPoint(extremity5,extremity6, lrAB, lrBC, intersection);
		List<Point> list4 =RoadArc.cuttingPoint(extremity7,extremity8, lrAB, lrBC, intersection);

		//Results extraction
		Point result1 = list1.get(0);
		Point result2 = list1.get(1);
		Point result3 = list2.get(0);
		Point result4 = list2.get(1);
		Point result5 = list3.get(0);
		Point result6 = list3.get(1);
		Point result7 = list4.get(0);
		Point result8 = list4.get(1);

		assertEquals(expected1,result1);
		assertEquals(expected2,result2);
		assertEquals(expected3,result3);
		assertEquals(expected4,result4);
		assertEquals(expected5,result5);
		assertEquals(expected6,result6);
		assertEquals(expected7,result7);
		assertEquals(expected8,result8);

		list1 =RoadArc.cuttingPoint(extremity1,extremity2, lrBC, lrAB, intersection);
		list2 =RoadArc.cuttingPoint(extremity3,extremity4, lrBC, lrAB, intersection);
		list3 =RoadArc.cuttingPoint(extremity5,extremity6, lrBC, lrAB, intersection);
		list4 =RoadArc.cuttingPoint(extremity7,extremity8, lrBC, lrAB, intersection);

		//Results extraction
		result1 = list1.get(0);
		result2 = list1.get(1);
		result3 = list2.get(0);
		result4 = list2.get(1);
		result5 = list3.get(0);
		result6 = list3.get(1);
		result7 = list4.get(0);
		result8 = list4.get(1);

		assertFalse(expected1.equals(result1));
		assertFalse(expected2.equals(result2));
		assertFalse(expected3.equals(result3));
		assertFalse(expected4.equals(result4));
		assertTrue(expected5.equals(result5));
		assertTrue(expected6.equals(result6));
		assertFalse(expected7.equals(result7));
		assertFalse(expected8.equals(result8));

	}

	/**
	 * Test method for calculAngle.
	 */
	@Test
	public void testCalculAngle(){

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

		assertEquals(RoadArc.angleBetweenRoads(lrAB, lrAB),0,1);
		assertEquals(RoadArc.angleBetweenRoads(lrAB, lrAC),90,1);
		assertEquals(RoadArc.angleBetweenRoads(lrAC, lrAB),90,1);
		assertEquals(RoadArc.angleBetweenRoads(lrAB, lrAD),180,1);
		assertEquals(RoadArc.angleBetweenRoads(lrAB, lrAE),90,1);
		assertEquals(RoadArc.angleBetweenRoads(lrAB, lrFG),Double.NaN,1);
		assertEquals(RoadArc.angleBetweenRoads(lrAB, lrHI),Double.NaN,1);
		assertEquals(RoadArc.angleBetweenRoads(lrAB, lrDJ),Double.NaN,1);
	}

	/**
	 * Test method for createRoadArc.
	 */
	@Test
	public void testCreateRoadArc(){

		//Geometry factory
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

		//Road coordinate
		Coordinate coorA = new Coordinate(12,0);
		Coordinate coorB = new Coordinate(0,0);
		Coordinate coorC = new Coordinate(0,12);

		//Road LineString
		LineString lsAB = geometryFactory.createLineString(new Coordinate[] {coorA,coorB});
		LineString lsBC = geometryFactory.createLineString(new Coordinate[] {coorB,coorC});
		LineString lsBD = geometryFactory.createLineString(new Coordinate[] {coorB,coorA});

		//Road MultiLineString
		MultiLineString mlsAB = geometryFactory.createMultiLineString(new LineString[] {lsAB});
		MultiLineString mlsBC = geometryFactory.createMultiLineString(new LineString[] {lsBC});
		MultiLineString mlsBD = geometryFactory.createMultiLineString(new LineString[] {lsBD});

		//Creating the lineroads with mockito
		LineRoad lrAB = Mockito.mock(LineRoad.class);
		LineRoad lrBD = Mockito.mock(LineRoad.class);
		LineRoad lrBC = Mockito.mock(LineRoad.class);
		LineRoad pl = Mockito.mock(LineRoad.class);
		LineRoad rpt = Mockito.mock(LineRoad.class);
		LineRoad tod = Mockito.mock(LineRoad.class);

		//Set getGeom 
		Mockito.when(lrAB.getGeom()).thenReturn(mlsAB);
		Mockito.when(lrBD.getGeom()).thenReturn(mlsBD);
		Mockito.when(lrBC.getGeom()).thenReturn(mlsBC);
		Mockito.when(rpt.getGeom()).thenReturn(mlsBC);
		Mockito.when(pl.getGeom()).thenReturn(mlsBC);
		Mockito.when(tod.getGeom()).thenReturn(mlsBC);

		//Set getWidth 
		Mockito.when(lrAB.getWidth()).thenReturn(10d);
		Mockito.when(lrBC.getWidth()).thenReturn(10d);
		Mockito.when(lrBD.getWidth()).thenReturn(10d);
		Mockito.when(rpt.getWidth()).thenReturn(10d);
		Mockito.when(pl.getWidth()).thenReturn(10d);
		Mockito.when(tod.getWidth()).thenReturn(0d);

		//Set getNom 
		Mockito.when(lrAB.getName()).thenReturn("lrAB");
		Mockito.when(lrBC.getName()).thenReturn("lrBC");
		Mockito.when(lrBD.getName()).thenReturn("lrBD");
		Mockito.when(rpt.getName()).thenReturn("RPT");
		Mockito.when(pl.getName()).thenReturn("PL");
		Mockito.when(tod.getName()).thenReturn("tod");

		//Set getSpeed 
		Mockito.when(lrAB.getSpeed()).thenReturn("30 KM");
		Mockito.when(lrBC.getSpeed()).thenReturn("30 KM");
		Mockito.when(lrBD.getSpeed()).thenReturn("30 KM");
		Mockito.when(rpt.getSpeed()).thenReturn("30 KM");
		Mockito.when(pl.getSpeed()).thenReturn("30 KM");
		Mockito.when(tod.getSpeed()).thenReturn("30 KM");

		//RoadArcs
		RoadArc raT1 = new RoadArc(lrAB,lrBC);
		RoadArc raT2 = new RoadArc(lrAB, rpt);
		RoadArc raT3 = new RoadArc(rpt, lrAB);
		RoadArc raT4 = new RoadArc(lrAB, pl);
		RoadArc raT5 = new RoadArc(pl, lrAB);
		RoadArc raT6 = new RoadArc(lrAB, lrAB);
		RoadArc raT7 = new RoadArc(lrAB, lrBD);
		RoadArc raT8 = new RoadArc(lrAB, tod);
		RoadArc raT9 = new RoadArc(tod, lrAB);
		
		assertNotNull(raT1.getCircularArc());
		assertNull(raT2.getCircularArc());
		assertNull(raT3.getCircularArc());
		assertNull(raT4.getCircularArc());
		assertNull(raT5.getCircularArc());
		assertNull(raT6.getCircularArc());
		assertNull(raT7.getCircularArc());
		assertNull(raT8.getCircularArc());
		assertNull(raT9.getCircularArc());

	}
}

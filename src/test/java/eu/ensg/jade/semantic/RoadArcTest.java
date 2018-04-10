package eu.ensg.jade.semantic;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
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
		LineRoad roadMock = Mockito.mock(LineRoad.class);
		
		Mockito.when(roadMock.getSpeed()).thenReturn("030");
		Mockito.when(roadMock.getWidth()).thenReturn(1d);
		
		RoadArc roadArc = new RoadArc(roadMock,roadMock);
		
		assertNotNull(roadArc);
		assertNotNull(roadArc.getRadius());
	}
	
	/**
	 * Test Constructor
	 */
	@Test
	public void testGetIntersection(){
		LineRoad roadMock = Mockito.mock(LineRoad.class);
		
		Mockito.when(roadMock.getSpeed()).thenReturn("030");
		Mockito.when(roadMock.getWidth()).thenReturn(1d);
		
		RoadArc roadArc = new RoadArc(roadMock,roadMock);
		
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
		
		Coordinate ptCoor = new Coordinate(0,0);
		
		Point ptbuffer = geometryFactory.createPoint(ptCoor);	
		
		//assertTrue(roadArc.getIntersection(ptbuffer.buffer(10), ptbuffer, ptbuffer).equalsExact(ptbuffer));
		
		Geometry buffer = ptbuffer.buffer(5);
		LineString lr1test = geometryFactory.createLineString(new Coordinate[] {new Coordinate(5,-1),new Coordinate(5,1)});
		LineString lr2test = geometryFactory.createLineString(new Coordinate[] {new Coordinate(-10,3),new Coordinate(10,3)});
		Point pt1exp = geometryFactory.createPoint(new Coordinate(5,0));
		Point pt2exp = geometryFactory.createPoint(new Coordinate(4,3));
		Point pt1ref = geometryFactory.createPoint(new Coordinate(10,0));
		Point pt2ref = geometryFactory.createPoint(new Coordinate(8,3));
		//assertTrue(roadArc.getIntersection(buffer, lr1test, pt1ref).equalsExact(pt1exp));
		//assertTrue(roadArc.getIntersection(buffer, lr2test, pt2ref).equalsExact(pt2exp));
		
		
	}
	
}

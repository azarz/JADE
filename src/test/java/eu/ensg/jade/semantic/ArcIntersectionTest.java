package eu.ensg.jade.semantic;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

import eu.ensg.jade.scene.Scene;

/**
 * ArcIntersectionTest is the class testing the {@link ArcIntersection} class
 * 
 * @author JADE
 */

@RunWith(MockitoJUnitRunner.class)
public class ArcIntersectionTest {

// ========================== ATTRIBUTES ===========================	

	/**
	 * The scene for the test, mocked.
	 */
	@Mock
	private Scene scene;

// ========================== METHODS ==============================
	/**
	 * Test method for {@link eu.ensg.jade.semantic.ArcIntersection#generateSmoothRoad(eu.ensg.jade.scene.Scene)}.
	 */
	@Test
	public void testGenerateSmoothRoad(){
		/*
		 Schema:
		  |
		  |
		  |
		 -+--
		| |

		(0,0) on the + (intersection) and road length = 1
		 */
		//Geometry used for the roads
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
		
		//Coordinates
		Coordinate[] right1Coord = new Coordinate[] {new Coordinate(0, 0), new Coordinate(1, 0)};
		Coordinate[] right2Coord = new Coordinate[] {new Coordinate(1, 0), new Coordinate(2, 0)};
		Coordinate[] left1Coord = new Coordinate[] {new Coordinate(0, 0), new Coordinate(-1, 0)};
		Coordinate[] up1Coord = new Coordinate[] {new Coordinate(0, 0), new Coordinate(0, 1)};
		Coordinate[] up2Coord = new Coordinate[] {new Coordinate(0, 1), new Coordinate(0, 2)};
		Coordinate[] up3Coord = new Coordinate[] {new Coordinate(0, 2), new Coordinate(0, 3)};
		Coordinate[] down1Coord = new Coordinate[] {new Coordinate(0, 0), new Coordinate(0, -1)};
		Coordinate[] downleft1Coord = new Coordinate[] {new Coordinate(-1, 0), new Coordinate(-1, -1)};
		
		//LineString
		LineString right1LS = geometryFactory.createLineString(right1Coord);
		LineString right2LS = geometryFactory.createLineString(right2Coord);
		LineString left1LS = geometryFactory.createLineString(left1Coord);
		LineString up1LS = geometryFactory.createLineString(up1Coord);
		LineString up2LS = geometryFactory.createLineString(up2Coord);
		LineString up3LS = geometryFactory.createLineString(up3Coord);
		LineString down1LS = geometryFactory.createLineString(down1Coord);
		LineString downleft1LS = geometryFactory.createLineString(downleft1Coord);
		
		//MultiLineString (last step, as MultiLineString is the one we need)
		MultiLineString right1MLS = geometryFactory.createMultiLineString(new LineString[] {right1LS});
		MultiLineString right2MLS = geometryFactory.createMultiLineString(new LineString[] {right2LS});
		MultiLineString left1MLS = geometryFactory.createMultiLineString(new LineString[] {left1LS});
		MultiLineString up1MLS = geometryFactory.createMultiLineString(new LineString[] {up1LS});
		MultiLineString up2MLS = geometryFactory.createMultiLineString(new LineString[] {up2LS});
		MultiLineString up3MLS = geometryFactory.createMultiLineString(new LineString[] {up3LS});
		MultiLineString down1MLS = geometryFactory.createMultiLineString(new LineString[] {down1LS});
		MultiLineString downleft1MLS = geometryFactory.createMultiLineString(new LineString[] {downleft1LS});

		//Creating the lineroads with mockito
		LineRoad right1LR = Mockito.mock(LineRoad.class);
		LineRoad right2LR = Mockito.mock(LineRoad.class);
		LineRoad left1LR = Mockito.mock(LineRoad.class);
		LineRoad up1LR = Mockito.mock(LineRoad.class);
		LineRoad up2LR = Mockito.mock(LineRoad.class);
		LineRoad up3LR = Mockito.mock(LineRoad.class);
		LineRoad down1LR = Mockito.mock(LineRoad.class);
		LineRoad downleft1LR = Mockito.mock(LineRoad.class);

		//Setting the return of get Geom
		Mockito.when(right1LR.getGeom()).thenReturn(right1MLS);
		Mockito.when(right2LR.getGeom()).thenReturn(right2MLS);
		Mockito.when(left1LR.getGeom()).thenReturn(left1MLS);
		Mockito.when(up1LR.getGeom()).thenReturn(up1MLS);
		Mockito.when(up2LR.getGeom()).thenReturn(up2MLS);
		Mockito.when(up3LR.getGeom()).thenReturn(up3MLS);
		Mockito.when(down1LR.getGeom()).thenReturn(down1MLS);
		Mockito.when(downleft1LR.getGeom()).thenReturn(downleft1MLS);

		//Setting the return of getWidth
		Mockito.when(right1LR.getWidth()).thenReturn(1d);
		Mockito.when(right2LR.getWidth()).thenReturn(0.5d);
		Mockito.when(left1LR.getWidth()).thenReturn(1d);
		Mockito.when(up1LR.getWidth()).thenReturn(1d);
		Mockito.when(up2LR.getWidth()).thenReturn(1d);
		Mockito.when(up3LR.getWidth()).thenReturn(0d);
		Mockito.when(down1LR.getWidth()).thenReturn(1d);
		Mockito.when(downleft1LR.getWidth()).thenReturn(1d);

		//Setting the return of getSpeed
		Mockito.when(right1LR.getSpeed()).thenReturn("030");
		Mockito.when(right2LR.getSpeed()).thenReturn("030");
		Mockito.when(left1LR.getSpeed()).thenReturn("030");
		Mockito.when(up1LR.getSpeed()).thenReturn("030");
		Mockito.when(up2LR.getSpeed()).thenReturn("030");
		Mockito.when(up3LR.getSpeed()).thenReturn("030");
		Mockito.when(down1LR.getSpeed()).thenReturn("030");
		Mockito.when(downleft1LR.getSpeed()).thenReturn("030");

		//Setting the return of getName
		Mockito.when(right1LR.getName()).thenReturn("right1LR");
		Mockito.when(right2LR.getName()).thenReturn("right2LR");
		Mockito.when(left1LR.getName()).thenReturn("left1LR");
		Mockito.when(up1LR.getName()).thenReturn("up1LR");
		Mockito.when(up2LR.getName()).thenReturn("up2LR");
		Mockito.when(up3LR.getName()).thenReturn("up3LR");
		Mockito.when(down1LR.getName()).thenReturn("down1LR");
		Mockito.when(downleft1LR.getName()).thenReturn("downleft1LR");

		//Check Mocked scene
		assertNotNull(scene);

		Map<String, LineRoad> lineRoads = new HashMap<String, LineRoad>();
		lineRoads.put("right1LR", right1LR);
		lineRoads.put("right2LR", right2LR);
		lineRoads.put("left1LR", left1LR);
		lineRoads.put("up1LR", up1LR);
		lineRoads.put("up2LR", up2LR);
		lineRoads.put("up3LR", up3LR);
		lineRoads.put("down1LR", down1LR);
		lineRoads.put("downleft1LR", downleft1LR);

		Mockito.when(scene.getLineRoads()).thenReturn(lineRoads);
		
		//Intersection

		//Coordinates
		Coordinate centerICoor = new Coordinate(0,0);
		Coordinate up1ICoor = new Coordinate(0,1);
		Coordinate up2ICoor = new Coordinate(0,2);
		Coordinate up3ICoor = new Coordinate(0,3);
		Coordinate down1ICoor = new Coordinate(0,-1);
		Coordinate right1ICoor = new Coordinate(1,0);
		Coordinate right2ICoor = new Coordinate(2,0);
		Coordinate left1ICoor = new Coordinate(-1,0);
		Coordinate downleft1ICoor = new Coordinate(-1,-1);

		//Maps
		Map<String,Boolean> centerIMap = new HashMap<String, Boolean>();
		centerIMap.put("right1LR",true);
		centerIMap.put("left1LR",true);
		centerIMap.put("up1LR",true);
		centerIMap.put("down1LR",true);
		Map<String,Boolean> up1IMap = new HashMap<String, Boolean>();
		centerIMap.put("up1LR",true);
		centerIMap.put("up2LR",true);
		Map<String,Boolean> up2IMap = new HashMap<String, Boolean>();
		centerIMap.put("up2LR",true);
		centerIMap.put("up3LR",true);
		Map<String,Boolean> up3IMap = new HashMap<String, Boolean>();
		centerIMap.put("up3LR",true);
		Map<String,Boolean> down1IMap = new HashMap<String, Boolean>();
		centerIMap.put("down1LR",true);
		Map<String,Boolean> right1IMap = new HashMap<String, Boolean>();
		centerIMap.put("right1LR",true);
		centerIMap.put("right2LR",true);
		Map<String,Boolean> right2IMap = new HashMap<String, Boolean>();
		centerIMap.put("right2LR",true);
		Map<String,Boolean> left1IMap = new HashMap<String, Boolean>();
		centerIMap.put("left1LR",true);
		centerIMap.put("downleft1LR",true);
		Map<String,Boolean> downleft1IMap = new HashMap<String, Boolean>();
		centerIMap.put("downleft1LR",true);

		//Intersections
		Intersection centerI = Mockito.mock(Intersection.class);
		Intersection up1I = Mockito.mock(Intersection.class);
		Intersection up2I = Mockito.mock(Intersection.class);
		Intersection up3I = Mockito.mock(Intersection.class);
		Intersection down1I = Mockito.mock(Intersection.class);
		Intersection right1I = Mockito.mock(Intersection.class);
		Intersection right2I = Mockito.mock(Intersection.class);
		Intersection leftI1 = Mockito.mock(Intersection.class);
		Intersection downleftI1 = Mockito.mock(Intersection.class);

		//Setting the get getGeometry
		Mockito.when(centerI.getGeometry()).thenReturn(centerICoor);
		Mockito.when(up1I.getGeometry()).thenReturn(up1ICoor);
		Mockito.when(up2I.getGeometry()).thenReturn(up2ICoor);
		Mockito.when(up3I.getGeometry()).thenReturn(up3ICoor);
		Mockito.when(down1I.getGeometry()).thenReturn(down1ICoor);
		Mockito.when(right1I.getGeometry()).thenReturn(right1ICoor);
		Mockito.when(right2I.getGeometry()).thenReturn(right2ICoor);
		Mockito.when(leftI1.getGeometry()).thenReturn(left1ICoor);
		Mockito.when(downleftI1.getGeometry()).thenReturn(downleft1ICoor);

		//Setting the get getRoadId
		Mockito.when(centerI.getRoadId()).thenReturn(centerIMap);
		Mockito.when(up1I.getRoadId()).thenReturn(up1IMap);
		Mockito.when(up2I.getRoadId()).thenReturn(up2IMap);
		Mockito.when(up3I.getRoadId()).thenReturn(up3IMap);
		Mockito.when(down1I.getRoadId()).thenReturn(down1IMap);
		Mockito.when(right1I.getRoadId()).thenReturn(right1IMap);
		Mockito.when(right2I.getRoadId()).thenReturn(right2IMap);
		Mockito.when(leftI1.getRoadId()).thenReturn(left1IMap);
		Mockito.when(downleftI1.getRoadId()).thenReturn(downleft1IMap);

		//Intersection Map
		HashMap<String, Intersection> mapIntersection = new HashMap<String, Intersection>();
		mapIntersection.put("centerI", centerI);
		mapIntersection.put("up1I", up1I);
		mapIntersection.put("up2I", up2I);
		mapIntersection.put("up3I", up3I);
		mapIntersection.put("down1I", down1I);
		mapIntersection.put("right1I", right1I);
		mapIntersection.put("right2I", right2I);
		mapIntersection.put("leftI1", leftI1);
		mapIntersection.put("downleftI1", downleftI1);

		//IntersectionColl
		IntersectionColl intersectColl = Mockito.mock(IntersectionColl.class);
		Mockito.when(intersectColl.getMapIntersection()).thenReturn(mapIntersection);

		Mockito.when(scene.getCollIntersect()).thenReturn(intersectColl);
/*
		List<Geometry> result = ArcIntersection.generateSmoothRoad(scene);

		assertNotNull(result);
		assertEquals(result.size(),8);
*/
	}
}

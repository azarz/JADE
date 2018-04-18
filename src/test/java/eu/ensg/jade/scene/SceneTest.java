package eu.ensg.jade.scene;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

import eu.ensg.jade.semantic.Building;
import eu.ensg.jade.semantic.DTM;
import eu.ensg.jade.semantic.Hydrography;
import eu.ensg.jade.semantic.IntersectionColl;
import eu.ensg.jade.semantic.LineRoad;
import eu.ensg.jade.semantic.PointVegetation;
import eu.ensg.jade.semantic.Sidewalk;
import eu.ensg.jade.semantic.StreetFurniture;
import eu.ensg.jade.semantic.SurfaceRoad;
import eu.ensg.jade.semantic.SurfaceVegetation;

/**
 * SceneTest is the class testing the {@link Scene} class
 * 
 * @author JADE
 */

@RunWith(MockitoJUnitRunner.class)
public class SceneTest {

	/**
	 * Constructor and mutators
	 */
	@Test
	public void testScene() {
		
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
		
		Scene sceneTest = new Scene();
		assertNotNull(sceneTest);
		
		List<Building> buildings = new ArrayList<Building>();
		sceneTest.setBuildings(buildings);
		assertTrue(sceneTest.getBuildings().equals(buildings));
		
		Coordinate centroid = new Coordinate(0,0,0);
		sceneTest.setCentroid(centroid);
		assertTrue(centroid.equals3D(sceneTest.getCentroid()));
		
		IntersectionColl collIntersect = Mockito.mock(IntersectionColl.class);	
		sceneTest.setCollIntersect(collIntersect);
		assertTrue(collIntersect.equals(sceneTest.getCollIntersect()));
		
		DTM dtm = Mockito.mock(DTM.class);
		sceneTest.setDtm(dtm);
		assertTrue(dtm.equals(sceneTest.getDtm()));
		
		List<Hydrography> hydrography = new ArrayList<Hydrography>();
		sceneTest.setHydrography(hydrography);
		assertTrue(hydrography.equals(sceneTest.getHydrography()));
		
		Map<String, LineRoad> lineRoads = new HashMap<String, LineRoad>();
		sceneTest.setLineRoads(lineRoads );
		assertTrue(lineRoads.equals(sceneTest.getLineRoads()));
		
		Polygon polygon = geometryFactory.createPolygon(new Coordinate[]{new Coordinate(0,0,0),new Coordinate(1,0,0),new Coordinate(1,1,0),new Coordinate(0,1,0),new Coordinate(0,0,0)});
		MultiPolygon roadArea = geometryFactory.createMultiPolygon(new Polygon[] {polygon});
		sceneTest.setRoadArea(roadArea);
		assertTrue(sceneTest.getRoadArea().equals(roadArea));
		
		List<Sidewalk> sidewalks = new ArrayList<Sidewalk>();
		sceneTest.setSidewalks(sidewalks);
		assertTrue(sceneTest.getSidewalks().equals(sidewalks));
		
		List<StreetFurniture> streetFurniture = new ArrayList<StreetFurniture>();
		sceneTest.setStreetFurniture(streetFurniture);
		assertTrue(sceneTest.getStreetFurniture().equals(streetFurniture));
		
		Map<String, SurfaceRoad> surfaceRoads = new HashMap<String,SurfaceRoad>();
		sceneTest.setSurfaceRoads(surfaceRoads);
		assertTrue(sceneTest.getSurfaceRoads().equals(surfaceRoads));
		
		List<SurfaceVegetation> vegetations = new ArrayList<SurfaceVegetation>();
		sceneTest.setSurfaceVegetation(vegetations);
		assertTrue(sceneTest.getSurfaceVegetation().equals(vegetations));
		
		List<PointVegetation> vegetation = new ArrayList<PointVegetation>();
		sceneTest.setVegetation(vegetation);
		assertTrue(sceneTest.getVegetation().equals(vegetation));
		
		
		Building building = Mockito.mock(Building.class);
		sceneTest.addBuilding(building);
		assertTrue(sceneTest.getBuildings().size()==1);
		assertTrue(sceneTest.getBuildings().contains(building));
		
		Hydrography newWaterSurface = Mockito.mock(Hydrography.class);
		sceneTest.addHydro(newWaterSurface);
		assertTrue(sceneTest.getHydrography().size()==1);
		assertTrue(sceneTest.getHydrography().contains(newWaterSurface));
		
		StreetFurniture newFurniture = Mockito.mock(StreetFurniture.class);
		sceneTest.addStreetFurniture(newFurniture);
		assertTrue(sceneTest.getStreetFurniture().size()==1);
		assertTrue(sceneTest.getStreetFurniture().contains(newFurniture));
		
		SurfaceVegetation newSurfaceTree = Mockito.mock(SurfaceVegetation.class);
		sceneTest.addSurfaceVegetation(newSurfaceTree);
		assertTrue(sceneTest.getSurfaceVegetation().size()==1);
		assertTrue(sceneTest.getSurfaceVegetation().contains(newSurfaceTree));
		
		PointVegetation newTree = Mockito.mock(PointVegetation.class);
		sceneTest.addVegetation(newTree);
		assertTrue(sceneTest.getVegetation().size()==1);
		assertTrue(sceneTest.getVegetation().contains(newTree));
		
	}
}

package eu.ensg.jade.semantic;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

/**
 * SurfaceVegetationTest is the class testing the {@link SurfaceVegetation} class
 * 
 * @author JADE
 */

@RunWith(MockitoJUnitRunner.class)
public class SurfaceVegetationTest {

	/**
	 * Constructors and mutators
	 */
	@Test
	public void TestSurfaceVegetation( ) {
		
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

		Polygon p = geometryFactory.createPolygon(new Coordinate[]{new Coordinate(0,0),new Coordinate(1,0),new Coordinate(1,1), new Coordinate(0,1), new Coordinate(0,0)});
		
		MultiPolygon mP = geometryFactory.createMultiPolygon(new Polygon[]{p});
		
		List<Integer> indexOffsets = new ArrayList<Integer>();
		indexOffsets.add(1);
		indexOffsets.add(1);
		indexOffsets.add(1);
		
		SurfaceVegetation sV = new SurfaceVegetation(mP, "Grass Square");
		
		assertNotNull(sV);
		assertTrue(sV.getGeometry().equalsExact(mP));
		assertTrue(sV.getNature()=="Grass Square");
		assertNotNull(sV.toOBJ(indexOffsets, 1, 1));
		
	}
}

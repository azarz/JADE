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
 * HydrographyTest is the class testing the {@link Hydrography} class
 * 
 * @author JADE
 */
@RunWith(MockitoJUnitRunner.class)
public class HydrographyTest {

	/**
	 * Constructor & getter test
	 */
	@Test
	public void TestHydrography() {
		
		//Geometry used for the polygons
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
		
		Coordinate[] coordinates = new Coordinate[] { new Coordinate(0,0), new Coordinate(1,0), new Coordinate(1,1), new Coordinate(0,1), new Coordinate(0,0)};
		
		Polygon polygon = geometryFactory.createPolygon(coordinates);
		MultiPolygon multipolygon = geometryFactory.createMultiPolygon(new Polygon[]{polygon});
		
		Hydrography wateredSquare = new Hydrography("Carré d'eau", 10.0, multipolygon);
		
		assertNotNull(wateredSquare);
		assertNotNull(wateredSquare.getGeometry());
		assertNotNull(wateredSquare.getNature());
		assertNotNull(wateredSquare.getZ_average());
		assertTrue(wateredSquare.getGeometry().equalsExact(multipolygon));
		assertTrue(wateredSquare.getNature().equals("Carré d'eau"));
		assertTrue(wateredSquare.getZ_average()==10.0);
	}
	
	/**
	 * Test for toOBJ
	 */
	@Test
	public void TestToOBJ(){
		
		//Geometry used for the polygons
				GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
				
				Coordinate[] coordinates = new Coordinate[] { new Coordinate(0,0), new Coordinate(1,0), new Coordinate(1,1), new Coordinate(0,1), new Coordinate(0,0)};
				
				Polygon polygon = geometryFactory.createPolygon(coordinates);
				MultiPolygon multipolygon = geometryFactory.createMultiPolygon(new Polygon[]{polygon});
				
				Hydrography wateredSquare = new Hydrography("Carré d'eau", 10.0, multipolygon);
				List<Integer> list = new ArrayList<Integer>();
				list.add(1);
				list.add(1);
				list.add(1);
				//Can't test more, see @Amaury for more details on problem about testing the string
				assertNotNull(wateredSquare.toOBJ(list, 0, 0));
	}
}

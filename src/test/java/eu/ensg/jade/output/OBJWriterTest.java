package eu.ensg.jade.output;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Matchers.*;

import eu.ensg.jade.semantic.Building;
import eu.ensg.jade.semantic.LineRoad;

/**
 * OBJWriterTest is the class testing the {@link OBJWriter} class
 * 
 * @author JADE
 */
@RunWith(MockitoJUnitRunner.class)
public class OBJWriterTest {
	
	private OBJWriter objWriter = new OBJWriter();
	
	@Mock
	private Building building;
	
	@Mock
	private LineRoad road;
	
	@Before
	public void setUp() throws Exception {
		OBJWriter objWriter = new OBJWriter();
	}
	
	@Test
	public void testBuildingExport() {
		List<Building> objectList = new ArrayList<Building>();
		for(int i=0; i<10; i++) {
			objectList.add(building);
		}
		
		Mockito.when(building.toOBJ(Matchers.<List<Integer>>any(), any(double.class), any(double.class))).thenReturn("ok");
		
		objWriter.exportBuilding("src/test/resources/buildings.obj", objectList, 0, 0);
	}

}

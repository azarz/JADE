package eu.ensg.jade.output;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import eu.ensg.jade.semantic.Building;
import eu.ensg.jade.semantic.LineRoad;

/**
 * OBJWriterTest is the class testing the {@link OBJWriter} class
 * 
 * @author JADE
 */
@RunWith(MockitoJUnitRunner.class)
public class OBJWriterTest {
	
	private OBJWriter objWriter;
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
		
		List<Integer> indexOffsets = new ArrayList<Integer>();
		double xOffset = 0;
		double yOffset = 0;
		
		Mockito.when(building.toOBJ(indexOffsets, xOffset, yOffset)).thenReturn("ok");
		
		objWriter.exportBuilding("src/test/resources/buildings.obj", objectList, 0, 0);
	}

}

package eu.ensg.jade.semantic;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;

import org.junit.Test;

import eu.ensg.jade.input.BuildingSHP;
import eu.ensg.jade.input.OutputRGE;

public class BuildingTest {

	@Test
	public void toObjTest() throws IOException {
		URL url = Thread.currentThread().getContextClassLoader().getResource("RGE/BD_TOPO/BATI_INDIFFERENCIE.SHP");
		String shpPath = url.getPath();
		
		BuildingSHP buildingShp = new BuildingSHP();
		
		OutputRGE allBuildings = buildingShp.getRGE(shpPath);
		
		Building building = allBuildings.getBuildings().get(10);
		
		building.addHeight();
		
		building.toOBJ(1, 1, 1);
	}

}

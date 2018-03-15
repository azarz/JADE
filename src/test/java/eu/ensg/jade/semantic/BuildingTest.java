package eu.ensg.jade.semantic;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
		
		List<Building> buildings = allBuildings.getBuildings();
		
		List<Integer> offsets = new ArrayList<Integer>();
		offsets.add(1);
		offsets.add(1);
		offsets.add(1);
		
		for (int i = 1000; i < 1020; i++) {
			buildings.get(i).addHeight();
			buildings.get(i).toOBJ(offsets);
		}
	}

}

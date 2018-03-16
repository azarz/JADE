package eu.ensg.jade.semantic;

import java.io.IOException;
import java.io.PrintWriter;
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
		
		String parisBuildingsObj = "";
		
		for (int i = 0; i < buildings.size(); i++) {
			buildings.get(i).addHeight();
			String buildingObj = buildings.get(i).toOBJ(offsets);
			
			parisBuildingsObj += "o Building_" + i + "\n";
			parisBuildingsObj += buildingObj;
			System.out.println(i);
			System.out.println(buildings.size());
			System.out.println(100* i / buildings.size() + "%");
		}
		
		PrintWriter out = new PrintWriter("/home/prof/paris.obj");
		
		out.println(parisBuildingsObj);
		
		out.close();
	}

}

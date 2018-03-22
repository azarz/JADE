package eu.ensg.jade.semantic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import eu.ensg.jade.input.BuildingSHP;
import eu.ensg.jade.input.InputRGE;

public class BuildingTest {

	@Test
	public void toObjTest() throws IOException {
		URL url = Thread.currentThread().getContextClassLoader().getResource("RGE/BD_TOPO/BATI_INDIFFERENCIE.SHP");
		String shpPath = url.getPath();
		
		BuildingSHP buildingShp = new BuildingSHP();
		
		InputRGE allBuildings = buildingShp.getRGE(shpPath);
		
		List<Building> buildings = allBuildings.getBuildings();
		
		List<Integer> offsets = new ArrayList<Integer>();
		offsets.add(1);
		offsets.add(1);
		offsets.add(1);
		
		File file = new File("paris.obj");
		Files.deleteIfExists(file.toPath());
		
		try(FileWriter fw = new FileWriter("paris.obj", true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)){
		
			out.print("mtllib paris.mtl\n");
			for (int i = 0; i < buildings.size(); i++) {
				
				System.out.println(100*i/buildings.size() + "%");
				
				Building building = buildings.get(i);
				String buildingObj = building.toOBJ(offsets, 655686.55, 6861084.26);
				
				// out.print("o Building_" + i + "\n");
				out.print(buildingObj);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}

}

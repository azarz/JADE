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

import eu.ensg.jade.input.LinearRoadSHP;
import eu.ensg.jade.input.OutputRGE;

public class SurfacicRoadTest {

	@Test
	public void toObjTest() throws IOException {
		URL url = Thread.currentThread().getContextClassLoader().getResource("RGE/BD_TOPO/ROUTE.SHP");
		String shpPath = url.getPath();
		
		LinearRoadSHP linearRoadShp = new LinearRoadSHP();
		
		OutputRGE allRoads = linearRoadShp.getRGE(shpPath);
		
		List<LinearRoad> roads = allRoads.getLinearRoads();
		
		List<Integer> offsets = new ArrayList<Integer>();
		offsets.add(1);
		offsets.add(1);
		offsets.add(1);
		
		File file = new File("paris_road.obj");
		Files.deleteIfExists(file.toPath());
		
		try(FileWriter fw = new FileWriter("paris_road.obj", true);
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw)){
		
			for (int i = 0; i < roads.size(); i++) {
				
				System.out.println(100*i/roads.size() + "%");
				
				LinearRoad road = roads.get(i);
				System.out.println(road.getGeom().getCoordinates()[0].z);
				ArealRoad surfRoad = road.enlarge();
				
				String buildingObj = surfRoad.toOBJ(offsets);
				
				out.print("o Road_" + i + "\n");
				out.print(buildingObj);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}

}

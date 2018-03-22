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
import java.util.Map;

import org.junit.Test;

import eu.ensg.jade.geometricObject.Road;
import eu.ensg.jade.input.InputRGE;
import eu.ensg.jade.input.LineRoadSHP;

public class SurfaceRoadTest {

	@Test
	public void toObjTest() throws IOException {
		URL url = Thread.currentThread().getContextClassLoader().getResource("RGE/BD_TOPO/ROUTE.SHP");
		String shpPath = url.getPath();
		
		LineRoadSHP linearRoadShp = new LineRoadSHP();
		
		InputRGE allRoads = linearRoadShp.getRGE(shpPath);
		
		Map<String,Road> roads = allRoads.getRoads();
		
		List<Integer> offsets = new ArrayList<Integer>();
		offsets.add(1);
		offsets.add(1);
		offsets.add(1);
		
		File file = new File("paris_road.obj");
		Files.deleteIfExists(file.toPath());
		
		try(FileWriter fw = new FileWriter("paris_road.obj", true);
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw)){
		
			for (Road value : roads.values()) {
			    
				// System.out.println(100*i/roads.size() + "%");
				LineRoad road = (LineRoad) value;
				//System.out.println(road.getGeom().getCoordinates()[0].z);
				SurfaceRoad surfRoad = road.enlarge();
				//System.out.println(surfRoad.getGeom().getCoordinates()[0].z);
				
				String roadObj = surfRoad.toOBJ(offsets, 655686.55, 6861084.26);
				
				// out.print("o Road_" + i + "\n");
				out.print(roadObj);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}

}

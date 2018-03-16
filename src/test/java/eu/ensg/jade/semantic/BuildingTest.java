package eu.ensg.jade.semantic;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

import eu.ensg.jade.input.BuildingSHP;
import eu.ensg.jade.input.OutputRGE;

public class BuildingTest {

	@Test
	public void toObjTest() throws IOException, InterruptedException, ExecutionException {
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
		
		ExecutorService exec = Executors.newFixedThreadPool(4);
		List<Callable<String>> tasks = new ArrayList<Callable<String>>();
		
		System.out.println("start " + System.currentTimeMillis());
		for (Building building: buildings) {
			Callable<String> c = new Callable<String>() {
				
				@Override
		        public String call() throws Exception {
					System.out.println("exec " + System.currentTimeMillis());
					String parisBuildingsObjTemp = "";
					
					building.addHeight();
					String buildingObj = building.toOBJ(offsets);
					
					parisBuildingsObjTemp += "o Building\n";
					parisBuildingsObjTemp += buildingObj;
		            return parisBuildingsObjTemp;
		        }
				
			};
			tasks.add(c);
			System.out.println(System.currentTimeMillis());
		}
		
		List<Future<String>> results = exec.invokeAll(tasks);

		
		for (Future<String> fr : results) {
			System.out.println("append " + System.currentTimeMillis());
			parisBuildingsObj += fr.get();
		}


		
		exec.shutdown();
		
		PrintWriter out = new PrintWriter("/home/prof/paris.obj");
		
		out.println(parisBuildingsObj);
		
		out.close();
	}

}

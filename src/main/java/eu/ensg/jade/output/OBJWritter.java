package eu.ensg.jade.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import eu.ensg.jade.semantic.ArealRoad;
import eu.ensg.jade.semantic.Building;
import eu.ensg.jade.semantic.LinearRoad;

/**
 * OBJCreator is the class implementing the creation of obj files for the objects to be added in the scene
 * 
 * @author JADE
 */

public class OBJWritter {
	
// ========================== ATTRIBUTES ===========================
	public boolean log = false;
	
// ========================== METHODS ==============================
	
	/**
	 * Default empty constructor
	 * 
	 */
	public OBJWritter(){
		// TODO: add a parameters to the constructor (maybe ?)
	}
	
	public void exportBuildings(String filePath, List<Building> buildings) {
		List<Integer> offsets = new ArrayList<Integer>();
		offsets.add(1);
		offsets.add(1);
		offsets.add(1);
		
		File file = new File(filePath);
		try(FileWriter fw = new FileWriter(filePath, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			
			Files.deleteIfExists(file.toPath());
			
			out.print("mtllib paris.mtl\n");
			Building building;
			for (int i = 0; i < buildings.size(); i++) {
				building = buildings.get(i);
				building.addHeight();
				
//				out.print("o Building_" + i + "\n");
				out.print(building.toOBJ(offsets));
				
				if(log) {
					System.out.println(100*i/buildings.size() + "%");
				}
			}
			
			out.close();			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void exportRoads(String filePath, List<LinearRoad> roads) {
		List<Integer> offsets = new ArrayList<Integer>();
		offsets.add(1);
		offsets.add(1);
		offsets.add(1);
		
		File file = new File("paris_roads.obj");
		try (FileWriter fw = new FileWriter(filePath, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			
			Files.deleteIfExists(file.toPath());
			
			out.print("mtllib paris.mtl\n");
			ArealRoad road;
			for (int i = 0; i < roads.size(); i++) {
				
				road = new ArealRoad(roads.get(i));
				
//				out.print("o Road_" + i + "\n");
				out.print(road.toOBJ(offsets));
				
				if(log) {
					System.out.println(100*i/roads.size() + "%");
					System.out.println(road.getGeom().getCoordinates()[0].z);
				}
			}
			
			out.close();			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

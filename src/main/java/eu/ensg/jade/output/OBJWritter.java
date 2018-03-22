package eu.ensg.jade.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eu.ensg.jade.geometricObject.Road;
import eu.ensg.jade.semantic.Building;

/**
 * OBJCreator is the class implementing the creation of obj files for the objects to be added in the scene
 * 
 * @author JADE
 */

public class OBJWritter {
	
// ========================== ATTRIBUTES ===========================

	
// ========================== METHODS ==============================
	
	/**
	 * Default empty constructor
	 * 
	 */
	public OBJWritter(){
		// TODO: add parameters to the constructor (maybe ?)
	}
	
	public void exportBuilding(String filePath, List<Building> objectList, double xCentroid, double yCentroid) {
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

			for (int i = 0; i < objectList.size(); i++) {				
				out.print(objectList.get(i).toOBJ(offsets, xCentroid, yCentroid));
			}		
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void exportRoad(String filePath, Map<String, Road> roads, double xCentroid, double yCentroid) {
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

			for (Road road: roads.values()) {				
				out.print(road.toOBJ(offsets, xCentroid, yCentroid));
			}		
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

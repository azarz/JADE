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

import com.vividsolutions.jts.geom.Geometry;

import eu.ensg.jade.semantic.Building;
import eu.ensg.jade.semantic.DTM;
import eu.ensg.jade.semantic.Hydrography;
import eu.ensg.jade.semantic.SurfaceRoad;
import eu.ensg.jade.semantic.SurfaceVegetation;

/**
 * OBJCreator is the class implementing the creation of obj files.
 * 
 * @author JADE
 */
public class OBJWriter {
	
// ========================== ATTRIBUTES ===========================

	
// ========================== METHODS ==============================
	
	/**
	 * Default empty constructor
	 * 
	 */
	public OBJWriter(){
	}
	
	/**
	 * Exports a list of Building object as a single <i>.obj</i> file
	 * 
	 * @param filePath the path to the obj file
	 * @param objectList the list of buildings
	 * @param xCentroid the centroid x coordinate
	 * @param yCentroid the centroid y coordinate
	 */
	public void exportBuilding(String filePath, List<Building> objectList, double xCentroid, double yCentroid) {
		List<Integer> offsets = new ArrayList<Integer>();
		offsets.add(1);
		offsets.add(1);
		offsets.add(1);
		
		File file = new File(filePath);
		try {
			Files.deleteIfExists(file.toPath());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		try(FileWriter fw = new FileWriter(filePath, true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw)) {			
			
			out.print("mtllib paris.mtl\n");
			for (int i = 0; i < objectList.size(); i++) {
				out.print(objectList.get(i).toOBJ(offsets, xCentroid, yCentroid));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Exports a list of Road (SurfaceRoad) object as a single <i>.obj</i> file
	 * 
	 * @param filePath the path to the obj file
	 * @param roads the list of roads
	 * @param xCentroid the centroid x coordinate
	 * @param yCentroid the centroid y coordinate
	 */
	public void exportRoad(String filePath, Map<String, SurfaceRoad> roads, double xCentroid, double yCentroid) {
		
		List<Integer> offsets = new ArrayList<Integer>();
		offsets.add(1);
		offsets.add(1);
		offsets.add(0);
		
		File file = new File(filePath);
		
		try {
			Files.deleteIfExists(file.toPath());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		try(FileWriter fw = new FileWriter(filePath, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			
			out.print("mtllib paris.mtl\n");

			for (SurfaceRoad road: roads.values()) {
				out.print(road.toOBJ(offsets, xCentroid, yCentroid));
			}		
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Exports a list of Road (SurfaceRoad) object as a single <i>.obj</i> file
	 * describing the sidewalks
	 * 
	 * @param filePath the path to the obj file
	 * @param roads the map of roads
	 * @param xCentroid the centroid x coordinate
	 * @param yCentroid the centroid y coordinate
	 * @param fullRoads The geometry of all the roads
	 */
	public void exportSidewalks(String filePath, Map<String, SurfaceRoad> roads, double xCentroid, double yCentroid,Geometry fullRoads, DTM dtm) {
		
		List<Integer> offsets = new ArrayList<Integer>();
		offsets.add(1);
		offsets.add(1);
		offsets.add(1);
		
		File file = new File(filePath);
		
		try {
			Files.deleteIfExists(file.toPath());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		try(FileWriter fw = new FileWriter(filePath, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			
			out.print("mtllib paris.mtl\n");
			int i=0;
			for (SurfaceRoad road: roads.values()) {
				System.out.println((100*i/3343.) + "%");
				out.print(road.sidewalksToOBJ(offsets, xCentroid, yCentroid, fullRoads, dtm));
				i++;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Exports a list of Hydrography (Hydrography) object as a single <i>.obj</i> file
	 * 
	 * @param filePath the path to the obj file
	 * @param objectList the list of roads
	 * @param xCentroid the centroid x coordinate
	 * @param yCentroid the centroid y coordinate
	 */
	public void exportWater(String filePath, List<Hydrography> objectList, double xCentroid, double yCentroid) {
		
		List<Integer> offsets = new ArrayList<Integer>();
		offsets.add(1);
		offsets.add(1);
		offsets.add(0);
		
		File file = new File(filePath);
		
		try {
			Files.deleteIfExists(file.toPath());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		try(FileWriter fw = new FileWriter(filePath, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			
			out.print("mtllib paris.mtl\n");

			for (int i = 0; i < objectList.size(); i++) {
				out.print(objectList.get(i).toOBJ(offsets, xCentroid, yCentroid));
			}	
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void exportVege(String filePath, List<SurfaceVegetation> objectList, double xCentroid, double yCentroid) {
		List<Integer> offsets = new ArrayList<Integer>();
		offsets.add(1);
		offsets.add(1);
		offsets.add(0);
		
		File file = new File(filePath);
		
		try {
			Files.deleteIfExists(file.toPath());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		try(FileWriter fw = new FileWriter(filePath, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			
			out.print("mtllib paris.mtl\n");
			
			for (int i = 0; i < objectList.size(); i++) {
				out.print(objectList.get(i).toOBJ(offsets, xCentroid, yCentroid));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void exportFromList(String filePath, List<IObjExport> objList, double xCentroid, double yCentroid) {
		
		List<Integer> offsets = new ArrayList<Integer>();
		offsets.add(1);
		offsets.add(1);
		offsets.add(0);
		
		File file = new File(filePath);
		
		try {
			Files.deleteIfExists(file.toPath());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		try(FileWriter fw = new FileWriter(filePath, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			
			out.print("mtllib paris.mtl\n");

			for (int i = 0; i < objList.size(); i++) {
				out.print(objList.get(i).toOBJ(offsets, xCentroid, yCentroid));
			}	
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}

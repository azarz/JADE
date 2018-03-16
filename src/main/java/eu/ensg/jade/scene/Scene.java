package eu.ensg.jade.scene;

import java.io.IOException;
import java.util.List;

import eu.ensg.jade.semantic.Building;
import eu.ensg.jade.semantic.DTM;
import eu.ensg.jade.semantic.Hydrography;
import eu.ensg.jade.semantic.PointVegetation;
import eu.ensg.jade.semantic.StreetFurniture;
import eu.ensg.jade.semantic.SurfacicRoad;

import eu.ensg.jade.input.OutputRGE;
import eu.ensg.jade.input.ReaderContext;
import eu.ensg.jade.input.ReaderFactory;

/**
 * Scene is the class implementing the scene and its elements to be created in OpenDS
 * 
 * @author JADE
 */

public class Scene {
	
	
// ========================== ATTRIBUTES ===========================

	/**
	 * List of surfacic roads to create {@link SurfacicRoad}
	 */
	private List<SurfacicRoad> roads;
	/**
	 * List of buildings to create {@link Building}
	 */
	private List<Building> buildings;
	/**
	 * List of water surfaces to create {@link Hydrography}
	 */
	private List<Hydrography> hydrography;
	/**
	 * List of trees to create {@link Vegetation}
	 */
	private List<PointVegetation> vegetation;
	/**
	 * List of street furniture to create {@link StreetFurniture}
	 */
	private List<StreetFurniture> streetFurniture;
	/**
	 * The DTM associated to the scene {@link DTM}
	 */
	private DTM dtm;

// ========================== GETTERS/SETTERS ======================

	/**
	 * Allows to access the list of surfacic roads in the scene
	 * 
	 * @return the scene surfacic roads
	 */
	public List<SurfacicRoad> getRoads() {
		return roads;
	}

	/**
	 * Allow to set the scene surfacic roads
	 * 
	 * @param roads the list of surfacic roads to be assigned
	 */
	public void setRoads(List<SurfacicRoad> roads) {
		this.roads = roads;
	}
	
	/**
	 * Allows to access the list of buildings in the scene
	 * 
	 * @return the scene buildings
	 */
	public List<Building> getBuildings() {
		return buildings;
	}

	/**
	 * Allows to set the scene buildings
	 * 
	 * @param buildings the list of buildings to be assigned
	 */
	public void setBuildings(List<Building> buildings) {
		this.buildings = buildings;
	}

	/**
	 * Allows to access the list of water surfaces in the scene
	 * 
	 * @return the scene water surfaces
	 */
	public List<Hydrography> getHydrography() {
		return hydrography;
	}

	/**
	 * Allows to set the scene water surfaces
	 * 
	 * @param hydrography the list of water surfaces to be assigned
	 */
	public void setHydrography(List<Hydrography> hydrography) {
		this.hydrography = hydrography;
	}

	/**
	 * Allows to access the list of trees in the scene
	 * 
	 * @return the scene threes
	 */
	public List<PointVegetation> getVegetation() {
		return vegetation;
	}

	/**
	 * Allows to set the scene vegetation (isolated trees)
	 * 
	 * @param vegetation the list of trees to be assigned
	 */
	public void setVegetation(List<PointVegetation> vegetation) {
		this.vegetation = vegetation;
	}

	/**
	 * Allows to access the list of street furniture in the scene
	 * 
	 * @return the scene street furniture
	 */
	public List<StreetFurniture> getStreetFurniture() {
		return streetFurniture;
	}

	/**
	 * Allows to set the scene street furniture
	 * 
	 * @param streetFurniture the list of street furniture to be assigned
	 */
	public void setStreetFurniture(List<StreetFurniture> streetFurniture) {
		this.streetFurniture = streetFurniture;
	}

	/**
	 * Allows to access the scene DTM
	 * 
	 * @return the scene DTM
	 */
	public DTM getDtm() {
		return dtm;
	}

	/**
	 * Allows to set the scene DTM
	 * 
	 * @param dtm the DTM to be assigned
	 */
	public void setDtm(DTM dtm) {
		this.dtm = dtm;
	}

// ========================== METHODS ==============================

	/**
	 * Adds a new road to the existing list of roads
	 * 
	 * @param newRoad the Road to be added
	 */
	public void addRoad(SurfacicRoad newRoad){
		this.roads.add(newRoad);
	}
	
	/**
	 * Adds a new buiding to the existing list of buildings
	 * 
	 * @param newBuilding the Building to be added
	 */
	public void addBuilding(Building newBuilding){
		this.buildings.add(newBuilding);
	}
	
	/**
	 * Adds a new water surface to the existing list hydrography
	 * 
	 * @param newWaterSurface the Hydrography to be added
	 */
	public void addHydro(Hydrography newWaterSurface){
		this.hydrography.add(newWaterSurface);

	}
	
	/**
	 * Adds a new tree to the existing list of vegetation
	 * 
	 * @param newTree the three to be added
	 */
	public void addVege(PointVegetation newTree){
		this.vegetation.add(newTree);

	}
	
	/**
	 * Adds a new street furniture to the existing list of furnitures
	 * 
	 * @param newFurniture the furniture to be added
	 */
	public void addStreetFurniture(StreetFurniture newFurniture){
		this.streetFurniture.add(newFurniture);

	}

	/**
	 * Testing main to read files ==> exemple de lecture pour la factory/strategy
	 */
	public static void main(String args[]) throws IOException{
		
		String buildingPath = "src/test/resources/RGE/BD_TOPO/BATI_INDIFFERENCIE.SHP";
		String hydroPath = "src/test/resources/RGE/BD_TOPO/SURFACE_EAU.SHP";
		String roadPath = "src/test/resources/RGE/BD_TOPO/ROUTE.SHP";
		String vegetPath = "src/test/resources/RGE/BD_TOPO/ZONE_VEGETATION.SHP";
		String dtmPath = "src/test/resources/inputTest/matriceTest.asc";
	
		//--------------------------------
		// Gets the data from the files
		//--------------------------------
		
		// Factory/Strategy Generation
		ReaderContext readerContx = new ReaderContext();
		ReaderFactory readerFact = new ReaderFactory();
		
		// Buildings
		readerContx.setIReaderStrategy(readerFact.createReader(1));
		OutputRGE buildingRGE = readerContx.createOutPutRGE(buildingPath);
		
		// Hydro
		readerContx.setIReaderStrategy(readerFact.createReader(2));
		OutputRGE hydroRGE = readerContx.createOutPutRGE(hydroPath);
		
		// Roads
		readerContx.setIReaderStrategy(readerFact.createReader(3));
		OutputRGE roadRGE = readerContx.createOutPutRGE(roadPath);
		
		// Veget
		readerContx.setIReaderStrategy(readerFact.createReader(4));
		OutputRGE vegetRGE = readerContx.createOutPutRGE(vegetPath);
		
		//DTM
		readerContx.setIReaderStrategy(readerFact.createReader(5));
		OutputRGE dtmRGE = readerContx.createOutPutRGE(dtmPath);
		
		//--------------------------------
		// Adds vegetation and urban furnitures
		//--------------------------------
				
		
		//--------------------------------
		// Transforms objects to obj
		//--------------------------------
		
		
		
		//--------------------------------
		// Puts obj in xml files
		//--------------------------------
		
	}

}

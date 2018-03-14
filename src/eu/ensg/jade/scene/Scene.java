package eu.ensg.jade.scene;


import java.util.List;

import eu.ensg.jade.semantic.Building;
import eu.ensg.jade.semantic.DTM;
import eu.ensg.jade.semantic.Hydrography;
import eu.ensg.jade.semantic.StreetFurniture;
import eu.ensg.jade.semantic.SurfacicRoad;
import eu.ensg.jade.semantic.Vegetation;


/*
import eu.ensg.jade.input.OutputRGE;
import eu.ensg.jade.input.ReaderContext;
import eu.ensg.jade.input.ReaderFactory;
*/

/**
 * Scene is the class implementing the scene and its elements to be created in OpenDS
 * 
 * @author JADE
 *
 */
public class Scene {
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
	private List<Vegetation> vegetation;
	/**
	 * List of street furniture to create {@link StreetFurniture}
	 */
	private List<StreetFurniture> streetFurniture;
	/**
	 * The DTM associated to the scene {@link DTM}
	 */
	private DTM dtm;
	
	
	/**
	 * This method allows to access the list of surfacic roads in the scene
	 * @return the scene surfacic roads
	 */
	public List<SurfacicRoad> getRoads() {
		return roads;
	}

	/**
	 * This method allow to set the scene surfacic roads
	 * @param roads the list of surfacic roads to be assigned
	 */
	public void setRoads(List<SurfacicRoad> roads) {
		this.roads = roads;
	}
	
	/**
	 * This method allows to access the list of buildings in the scene
	 * @return the scene buildings
	 */
	public List<Building> getBuildings() {
		return buildings;
	}

	/**
	 * This method allow to set the scene buildings
	 * @param buildings the list of buildings to be assigned
	 */
	public void setBuildings(List<Building> buildings) {
		this.buildings = buildings;
	}

	/**
	 * This method allows to access the list of water surfaces in the scene
	 * @return the scene water surfaces
	 */
	public List<Hydrography> getHydrography() {
		return hydrography;
	}

	/**
	 * This method allow to set the scene water surfaces
	 * @param hydrography the list of water surfaces to be assigned
	 */
	public void setHydrography(List<Hydrography> hydrography) {
		this.hydrography = hydrography;
	}

	/**
	 * This method allows to access the list of trees in the scene
	 * @return the scene threes
	 */
	public List<Vegetation> getVegetation() {
		return vegetation;
	}

	/**
	 * This method allow to set the scene vegetation (isolated trees)
	 * @param vegetation the list of trees to be assigned
	 */
	public void setVegetation(List<Vegetation> vegetation) {
		this.vegetation = vegetation;
	}

	/**
	 * This method allows to access the list of street furniture in the scene
	 * @return the scene street furniture
	 */
	public List<StreetFurniture> getStreetFurniture() {
		return streetFurniture;
	}

	/**
	 * This method allow to set the scene street furniture
	 * @param streetFurniture the list of street furniture to be assigned
	 */
	public void setStreetFurniture(List<StreetFurniture> streetFurniture) {
		this.streetFurniture = streetFurniture;
	}

	/**
	 * This method allows to access the scene DTM
	 * @return the scene DTM
	 */
	public DTM getDtm() {
		return dtm;
	}

	/**
	 * This method allow to set the scene DTM
	 * @param dtm the DTM to be assigned
	 */
	public void setDtm(DTM dtm) {
		this.dtm = dtm;
	}

	/**
	 * Add a new road to the existing list of roads
	 * 
	 * @param newRoad the Road to be added
	 */
	public void addRoad(SurfacicRoad newRoad){
		this.roads.add(newRoad);
	}
	
	/**
	 * Add a new buiding to the existing list of buildings
	 * 
	 * @param newBuilding the Building to be added
	 */
	public void addBuilding(Building newBuilding){
		this.buildings.add(newBuilding);
	}
	
	/**
	 * Add a new water surface to the existing list hydrography
	 * 
	 * @param newWaterSurface the Hydrography to be added
	 */
	public void addHydro(Hydrography newWaterSurface){
		this.hydrography.add(newWaterSurface);

	}
	
	
	/**
	 * Add a new tree to the existing list of vegetation
	 * 
	 * @param newTree the three to be added
	 */
	public void addVege(Vegetation newTree){
		this.vegetation.add(newTree);

	}
	
	/**
	 * Add a new street furniture to the existing list of furnitures
	 * 
	 * @param newFurniture the furniture to be added
	 */
	public void addStreetFurniture(StreetFurniture newFurniture){
		this.streetFurniture.add(newFurniture);

	}

	
	
	
	/*
	public static void main(String args[]){
		
		String buildingPath = "...";
	
		// We get the data 
		// Building
		ReaderContext readerContx = new ReaderContext();
		ReaderFactory readerFact = new ReaderFactory();
		readerContx.setIReaderStrategy(readerFact.createReader(1));
		OutputRGE buildingRGE = readerContx.createOutPutRGE(buildingPath);
	
	}*/

}

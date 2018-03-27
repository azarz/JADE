package eu.ensg.jade.scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.ensg.jade.geometricObject.Road;
import eu.ensg.jade.semantic.Building;
import eu.ensg.jade.semantic.DTM;
import eu.ensg.jade.semantic.Hydrography;
import eu.ensg.jade.semantic.IntersectionColl;
import eu.ensg.jade.semantic.LineRoad;
import eu.ensg.jade.semantic.PointVegetation;
import eu.ensg.jade.semantic.StreetFurniture;
import eu.ensg.jade.semantic.SurfaceRoad;
import eu.ensg.jade.semantic.SurfaceVegetation;

/**
 * Scene is the class implementing the scene and its elements to be created in OpenDS
 * 
 * @author JADE
 */

public class Scene {
	
	
// ========================== ATTRIBUTES ===========================

	/**
	 * List of surfacic roads to create {@link SurfaceRoad}
	 */
	private Map <String,Road> roads;
	
	/**
	 * List of linear roads to create {@link LineRoad}
	 */
	private List<LineRoad> lineRoads;
	
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
	 * List of trees area used for tree creation {@link Vegetation}
	 */
	private List<SurfaceVegetation> surfaceVegetation;
	
	/**
	 * List of street furniture to create {@link StreetFurniture}
	 */
	private List<StreetFurniture> streetFurniture;
	
	/**
	 * The DTM associated to the scene {@link DTM}
	 */
	private DTM dtm;
	
	/**
	 * The X coordinate of the area centroid
	 */
	private double xCentroid = 0;
	
	/**
	 * The Y coordinate of the area centroid
	 */
	private double yCentroid = 0;
	
	/**
	 * The collection of intersections between roads
	 */
	private IntersectionColl collIntersect;

	
	
// ========================== CONSTRUCTORS =========================

	public Scene() {
		this.buildings = new ArrayList<Building>();
		this.roads = new HashMap<String,Road>();
		this.lineRoads = new ArrayList<LineRoad>();
		this.hydrography = new ArrayList<Hydrography>();
		this.vegetation = new ArrayList<PointVegetation>();
		this.surfaceVegetation = new ArrayList<SurfaceVegetation>();
		this.streetFurniture = new ArrayList<StreetFurniture>();
	}
	

// ========================== GETTERS/SETTERS ======================

	/**
	 * Allows to access the list of roads in the scene
	 * 
	 * @return the scene roads
	 */
	public Map<String,Road> getRoads() {
		return roads;
	}
	
	/**
	 * Allow to set the scene roads
	 * 
	 * @param roads the list of roads to be assigned
	 */
	public void setRoads(Map<String,Road> roads) {
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
	 * Allows to access the list of trees area in the scene
	 * 
	 * @return the scene trees area
	 */
	public List<SurfaceVegetation> getSurfaceVegetation() {
		return surfaceVegetation;
	}

	/**
	 * Allows to set the scene vegetation area
	 * 
	 * @param vegetation the list of tree surface to be assigned
	 */
	public void setSurfaceVegetation(List<SurfaceVegetation> vegetation) {
		this.surfaceVegetation = vegetation;
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
	
	/**
	 * Gets the centroid's X
	 * 
	 * @return xCentroid
	 */
	public double getxCentroid() {
		return xCentroid;
	}

	/**
	 * Sets the centroid's X
	 * 
	 * @param xCentroid
	 */
	public void setxCentroid(double xCentroid) {
		this.xCentroid = xCentroid;
	}

	/**
	 * Gets the centroid's Y
	 * 
	 * @return yCentroid
	 */
	public double getyCentroid() {
		return yCentroid;
	}

	/**
	 * Sets the centroid's Y
	 * 
	 * @param yCentroid
	 */
	public void setyCentroid(double yCentroid) {
		this.yCentroid = yCentroid;
	}
	
	
	/**
	 * @return the collIntersect
	 */
	public IntersectionColl getCollIntersect() {
		return collIntersect;
	}


	/**
	 * @param collIntersect the collIntersect to set
	 */
	public void setCollIntersect(IntersectionColl collIntersect) {
		this.collIntersect = collIntersect;
	}

// ========================== METHODS ==============================

	/**
	 * Adds a new road to the existing list of roads
	 * 
	 * @param newRoad the Road to be added
	 */
	public void addRoad(String id, Road newRoad){
		this.roads.put(id, newRoad);
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
	public void addVegetation(PointVegetation newTree){
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

}

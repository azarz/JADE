package eu.ensg.jade.scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.MultiPolygon;

import eu.ensg.jade.semantic.Building;
import eu.ensg.jade.semantic.DTM;
import eu.ensg.jade.semantic.Hydrography;
import eu.ensg.jade.semantic.IntersectionColl;
import eu.ensg.jade.semantic.LineRoad;
import eu.ensg.jade.semantic.PedestrianCrossing;
import eu.ensg.jade.semantic.PointVegetation;
import eu.ensg.jade.semantic.Sidewalk;
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
	private Map <String,SurfaceRoad> surfaceRoads;
	
	/**
	 * List of line roads inside the scene {@link LineRoad}
	 */
	private Map <String,LineRoad> lineRoads;
	
	/**
	 * The geometry that contains the union of all surface roads and intersection
	 */
	private MultiPolygon roadArea;
	
	/**
	 * List of buildings to create {@link Building}
	 */
	private List<Building> buildings;

	/**
	 * List of water surfaces to create {@link Hydrography}
	 */
	private List<Hydrography> hydrography;

	/**
	 * List of trees to create {@link PointVegetation}
	 */
	private List<PointVegetation> vegetation;

	/**
	 * List of trees area used for tree creation {@link SurfaceVegetation}
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
	 * The coordinate of the scene centroid
	 */
	Coordinate centroid;

	/**
	 * The collection of intersections between roads
	 */
	private IntersectionColl collIntersect;

	/**
	 * List of the sidewalks
	 */
	private List<Sidewalk> sidewalks;

	/**
	 * List of the pedestrian crossing
	 */
	private List<PedestrianCrossing> pedestrianCrossing;


// ========================== CONSTRUCTORS =========================
	/**
	 * Default constructor
	 */
	public Scene() {
		this.buildings = new ArrayList<Building>();
		this.lineRoads = new HashMap<String,LineRoad>();
		this.surfaceRoads = new HashMap<String,SurfaceRoad>();
		this.hydrography = new ArrayList<Hydrography>();
		this.vegetation = new ArrayList<PointVegetation>();
		this.surfaceVegetation = new ArrayList<SurfaceVegetation>();
		this.streetFurniture = new ArrayList<StreetFurniture>();
		this.pedestrianCrossing = new ArrayList<PedestrianCrossing>();
		
		this.dtm = new DTM();
	}


// ========================== GETTERS/SETTERS ======================

	/**
	 * Allows to access the list of linear roads in the scene
	 *
	 * @return the scene roads
	 */
	public Map<String,LineRoad> getLineRoads() {
		return lineRoads;
	}

	/**
	 * Allow to set the scene linear roads
	 *
	 * @param lineRoads the list of roads to be assigned
	 */
	public void setLineRoads(Map<String,LineRoad> lineRoads) {
		this.lineRoads = lineRoads;
	}
	
	/**
	 * Allows to access the list of roads in the scene
	 *
	 * @return the scene roads
	 */
	public Map<String,SurfaceRoad> getSurfaceRoads() {
		return surfaceRoads;
	}

	/**
	 * Allow to set the scene roads
	 *
	 * @param surfaceRoads the list of roads to be assigned
	 */
	public void setSurfaceRoads(Map<String,SurfaceRoad> surfaceRoads) {
		this.surfaceRoads = surfaceRoads;
	}
	
	/**
	 * Allows to access the roads area
	 *
	 * @return the roads area
	 */
	public MultiPolygon getRoadArea() {
		return roadArea;
	}

	/**
	 * Allow to set the roads area
	 *
	 * @param roadArea the area of roads to be assigned
	 */
	public void setRoadArea(MultiPolygon roadArea) {
		this.roadArea = roadArea;
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
	 * Gets the centroid
	 *
	 * @return centroid
	 */
	public Coordinate getCentroid() {
		return centroid;
	}

	/**
	 * Sets the scene centroid
	 *
	 * @param centroid The centroid of the scene
	 */
	public void setCentroid(Coordinate centroid) {
		this.centroid = centroid;
	}


	/**
	 * Allows to access to the intersection collection
	 * 
	 * @return the collIntersect
	 */
	public IntersectionColl getCollIntersect() {
		return collIntersect;
	}


	/**
	 * Allows to set the intersection collection
	 * 
	 * @param collIntersect the collIntersect to set
	 */
	public void setCollIntersect(IntersectionColl collIntersect) {
		this.collIntersect = collIntersect;
	}
	
	/**
	 * Allows to get the list of sidewalks
	 * 
	 * @return the list of sidewalks
	 */
	public List<Sidewalk> getSidewalks() {
		return sidewalks;
	}
	
	/**
	 * Allows to set the sidewalks list
	 * 
	 * @param sidewalks The new list of sidewalks
	 */
	public void setSidewalks(List<Sidewalk> sidewalks) {
		this.sidewalks = sidewalks;
	}
	
	/**
	 * Allows to get the list of pedestrian crossing
	 * 
	 * @return the pedestrianCrossing
	 */
	public List<PedestrianCrossing> getPedestrianCrossing() {
		return pedestrianCrossing;
	}

	/**
	 * Allows to set the list of pedestrian crossing
	 * 
	 * @param pedestrianCrossing the pedestrianCrossing to set
	 */
	public void setPedestrianCrossing(List<PedestrianCrossing> pedestrianCrossing) {
		this.pedestrianCrossing = pedestrianCrossing;
	}

// ========================== METHODS ==============================

	/**
	 * Adds a new road to the existing list of roads
	 *
	 * @param newRoad the Road to be added
	 */
//	public void addRoad(String id, Road newRoad){
//		this.roads.put(id, newRoad);
//	}


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
	 * Adds a new tree in the existing list of surface vegetation
	 * 
	 * @param newTree the three to be added
	 */
	public void addSurfaceVegetation(SurfaceVegetation newTree){
		this.surfaceVegetation.add(newTree);
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
	 * Adds a new polygon of pedestrian crossing to the existing list of pedestrian crossing
	 *
	 * @param pedestrianPolygon the pedestrian crossing polygon to be added
	 */
	public void addPedestrianCrossing(PedestrianCrossing pedestrianPolygon){
		this.pedestrianCrossing.add(pedestrianPolygon);
	}

}

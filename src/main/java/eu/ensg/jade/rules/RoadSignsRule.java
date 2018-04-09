package eu.ensg.jade.rules;

import java.util.List;
import java.util.Map;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.impl.PackedCoordinateSequenceFactory;

import eu.ensg.jade.geometricObject.Road;
import eu.ensg.jade.scene.Scene;
import eu.ensg.jade.semantic.Intersection;
import eu.ensg.jade.semantic.IntersectionColl;
import eu.ensg.jade.semantic.LineRoad;
import eu.ensg.jade.semantic.StreetFurniture;
import eu.ensg.jade.semantic.SurfaceRoad;
import eu.ensg.jade.utils.JadeUtils;

/**
 * 
 * @author JADE
 */

public class RoadSignsRule implements RuleShape {
	
// ========================== ATTRIBUTES ===========================

	/**
	 * Path of the pedestrian crossing sign
	 */
	private String pedestrianCrossing = "Models/RoadSigns/dangerSigns/PedestrianCrossingAhead/PedestrianCrossingAhead.scene"; 
	
	
	
// ========================== METHODS ==============================

	/**
	 * Puts signs on roads
	 * 
	 * Example : pedestrian crossing, speed limitation
	 * 
	 * @param scene The scene where to add signs
	 */	
	@Override
	public void addPunctualObject(Scene scene) throws NoSuchAuthorityCodeException, FactoryException {

		Map<String, Road> roads = scene.getRoads();
		IntersectionColl interColl = scene.getCollIntersect();
		
		// We go through all the intersections
		for (Intersection intersect : interColl.getMapIntersection().values()){
			int numberOfRoads = intersect.getRoadId().size();
			LineRoad[] roadsTab = new LineRoad[numberOfRoads];
			boolean[] startOnIntersectTab = new boolean[numberOfRoads];
			
			roadTabFilling(roadsTab, startOnIntersectTab, intersect, roads);

			// We go through all the roads
			for (String road : intersect.getRoadId().keySet()){
				LineRoad lineRoad = (LineRoad) roads.get(road);
				boolean roadBool = intersect.getRoadId().get(road);
				// We check the road driving direction
				int enter = isEntering(lineRoad, roadBool);
				
				int intersectType = calcIntersectionType(roadsTab,numberOfRoads);
				
				// If the intersection has 5 or more roads, we've placed traffic lights
				// Intersection with 3 or 4 attached roads and traffic lights are harder to determine ...
				if ((lineRoad.getSpeed().equals("70 km/h") &&
						((lineRoad.getName().substring(0, 2).equals("PL") || lineRoad.getName().substring(0, 3).equals("RPT"))
						|| numberOfRoads == 5)) ||
						lineRoad.getSpeed().equals("50 km/h")){
					
					addSignsByRoad(enter, lineRoad, roadBool, this.pedestrianCrossing, scene, intersect);
					
			
				}
			}
		}
	}		
	
	
	/**
	 * @param roadsTab the list of roads attached to the intersection
	 * @param startOnIntersectTab the list of boolean associated to each road that tells if the road start point is on the intersection
	 * @param intersect the intersection on which to perform the work
	 * @param roads the list of roads contained by the scene
	 */
	private void roadTabFilling(LineRoad[] roadsTab, boolean[] startOnIntersectTab, Intersection intersect, Map <String,Road> roads){
		// Roads retrieval
		int k = 0;
		
		// We fill the roadsTab and startOnIntersectTab arrays
		for (String road : intersect.getRoadId().keySet()){
			roadsTab[k] = (LineRoad) roads.get(road);
			startOnIntersectTab[k] = intersect.getRoadId().get(road);
			k++;
		}
	}
	
	
	/**
	 * Gives the direction of the road compared to the intersection
	 * 
	 * @param road the road to test
	 * @param direction a boolean that specifies if the beginning of the road is on the intersection or not
	 * 
	 * @return -1 if leaving, 0 if double-way, +1 if entering 
	 */
	private int isEntering(Road road, Boolean direction){
		int modDirection = 1;

		// If it is a two-way driving road we return 0
		if(road.getDirection().equals("Double")){
			return 0;
		}
		
		if (road.getDirection().equals("Inverse")){
		// If the road driving direction is reverse
				modDirection=-1;
			}
			
		// And the beginning of the road is on the intersection, the road is entering it
		if(direction) {
			return -1*modDirection;
		}
		// else it is leaving it
		else if(!direction) {
			return 1*modDirection;
		}

		return 0;
	}
	
	/**
	 * Check if there is an importance change between roads
	 * 
	 * @param roadsTab the list of roads attached to the intersection
	 * @param size the number of roads in the considered intersection
	 * 
	 * @return table of int, 1 for bigger, 0 for lesser importance, null if same importance.
	 */
	private int hasDiffImportance(LineRoad[] roadsTab, int size) {
		// By order the greatest are 1, 2, 3, 4, 5
		int greatest = 300;
		int lowest = -1;

		for (int i=0; i<size; i++){
			if (roadsTab[i].getImportance().equals("NC") || roadsTab[i].getImportance().equals("NR")){
				return -1;
			}
			
			if (greatest > Integer.parseInt(roadsTab[i].getImportance())){
				greatest = Integer.parseInt(roadsTab[i].getImportance());
			}
			else if (lowest < Integer.parseInt(roadsTab[i].getImportance())){
				lowest = Integer.parseInt(roadsTab[i].getImportance());

			}
		}
		return lowest - greatest;
	}
	
	
	/**
	 * Calculates the intersection type
	 * 
	 * @param roadsTab the list of roads attached to the intersection
	 * @param size the number of roads in the considered intersection
	 * 
	 * @return int the type of intersection: 0: Traffic Lights, 1: Yield, 2: Break; 3: Priority to the right.
	 */
	private int calcIntersectionType(LineRoad[] roadsTab, int size) {
		int diff = hasDiffImportance(roadsTab, size);
		
		if (diff != -1){
			for(int i=0; i < size; i++){
				if ((roadsTab[i].getDirection().equals("Double") && roadsTab[i].getLaneNumber() >= 4) 
						|| ((roadsTab[i].getDirection().equals("Inverse") || roadsTab[i].getDirection().equals("Direct")) && roadsTab[i].getLaneNumber() >= 3)
						){
					return 1;
				}
				else if(diff == 0){
					if (roadsTab[i].getSpeed() != "" && size != 3){
						return 2;
					}
					return 3;
				}
				else if (diff >= 1){
					return 3;
				}
			}

		}
		return -1;
	}
	
	/**
	 * Creates signs for intersections of more than three roads
	 * 
	 * @param enter integer which tells the road traffic direction regarding the intersection
	 * @param lineRoad the road on which to perform the work
	 * @param startOnIntersect the boolean to know if the beginning of the road is on the intersection
	 * @param folder the path toward the right sign
	 * @param scene the object containing all the elements of the scene
	 * 
	 * @throws FactoryException 
	 * @throws NoSuchAuthorityCodeException 
	 */
	private void addSignsByRoad(int enter, LineRoad lineRoad, boolean startOnIntersect, String folder, Scene scene, Intersection intersect) throws NoSuchAuthorityCodeException, FactoryException {
		// If it is a direct driving direction
		if (enter == 1){
			StreetFurniture streetFurniture = addSigns(lineRoad,startOnIntersect,this.pedestrianCrossing,scene, intersect);
			addStreetFurniture(streetFurniture, lineRoad, scene);
		
		}		
		
		// If it has more than 2 lane, we had a sign on each side of the road
		else if (lineRoad.getLaneNumber() >= 2){
			//StreetFurniture streetFurniture = addSigns(lineRoad,startOnIntersect,folder,scene,intersect);
			//addStreetFurniture(streetFurniture, lineRoad, scene);
		}
	}
	
	
	/**
	 * Adds street furniture to the associated road and the scene
	 * 
	 * @param streetFurniture the furniture to add
	 * @param road the road on which the furniture is added
	 * @param scene the object containing all the elements of the scene
	 */
	private void addStreetFurniture(StreetFurniture streetFurniture, LineRoad road, Scene scene){

		boolean alreadyOn = false;
		
		if (streetFurniture != null){
			if (!road.getSF().isEmpty()){
				for (int i=0; i < road.getSF().size(); i++){
					if (streetFurniture.getCoord().equals2D(road.getSF().get(i).getCoord())){
						alreadyOn = true;
					}
				}
				if (!alreadyOn){
					road.addSF(streetFurniture);
					scene.addStreetFurniture(streetFurniture);
				}
			}
			else{
				road.addSF(streetFurniture);
				scene.addStreetFurniture(streetFurniture);
			}
		}
	}
	
	/**
	 * Creates new street furniture
	 * 
	 * @param road the road on which the furniture has to be created
	 * @param startOnIntersect the boolean to know if the beginning of the road is on the intersection
	 * @param folder the path toward the right sign
	 * @param scene the object containing all the elements of the scene
	 * @param intersect the intersection on which to perform the work
	 * 
	 * @return a street furniture object 
	 * 
	 * @throws FactoryException 
	 * @throws NoSuchAuthorityCodeException 
	 */
	private StreetFurniture addSigns(LineRoad road, boolean startOnIntersect, String folder, Scene scene, Intersection intersect) throws NoSuchAuthorityCodeException, FactoryException{

		if (startOnIntersect){
			// It is possible to return the sign angle in street furniture
			return signPosition(road, 0, folder, scene, intersect);
		}
		else{
			return signPosition(road, road.getGeom().getCoordinates().length-1, folder, scene, intersect);
		}
	}	
	

	
	/**
	 * Gives the possible position of a new street furniture
	 * 
	 * @param road the road on which the furniture is added
	 * @param left the boolean which allow to know if the sign has to be on the right or on the left of the orad
	 * @param position the position in the table of coordinate for the point we need to use
	 * 
	 * @return an object Coordinate that give the position of the sign from the intersection
	 * @throws FactoryException 
	 * @throws NoSuchAuthorityCodeException 
	 */
	private StreetFurniture signPosition(LineRoad road, int position, String folder, Scene scene, Intersection intersection) throws NoSuchAuthorityCodeException, FactoryException{

		// Variable 
		Coordinate[] coord = road.getGeom().getCoordinates();
		
		double x = coord[position].x;
		double y = coord[position].y;
		
		double newX;
		double newY;
		double rotation = 0;
		double[] sfCoord ;
		

		double d = 60; // 5 meters after the beginning of the road
		double D = road.getWidth()/2 + 0.7; // 0.70 meters after the border of the road

		double theta = JadeUtils.roadAngle(road,position); // Angle between road and horizontal line, in counter clockwise
		
		sfCoord = sfPositionning(folder, x, y, d, D, theta);
		newX = sfCoord[0];
		newY = sfCoord[1];
		rotation = sfCoord[2];
		
		boolean doesIntersect = true;
		while (doesIntersect && d < 15){
			doesIntersect = false;
			sfCoord = sfPositionning(folder, x, y, d, D, theta);
			newX = sfCoord[0];
			newY = sfCoord[1];
			rotation = sfCoord[2];
			
			/*Coordinate coord1 = new Coordinate(newX, newY);

			GeometryFactory factory = new GeometryFactory();
			Point point = new Point(coord1, null, 0);
			//System.out.println(coord1);*/
			
			PackedCoordinateSequenceFactory factory=PackedCoordinateSequenceFactory.DOUBLE_FACTORY;
			CoordinateSequence seq=factory.create(new Coordinate[]{new Coordinate(newX,newY)});

			Point pt = new Point(seq,new GeometryFactory());

			Geometry g = (Geometry) pt;

			for (String roadId: intersection.getRoadId().keySet()){
				
				SurfaceRoad surfaceRoad = ((LineRoad) scene.getRoads().get(roadId)).enlarge();
				if(surfaceRoad.getGeom().contains(g)){
					doesIntersect = true;
					d = d + 0.5;
				}
			}
		}
		
		// Be careful y is the vertical axis in OpenDS 
		//Coordinate newCoord = new Coordinate(newX - centroid.x, newZ - centroid.y, road.getZ_ini());
		if(!doesIntersect){
			double newZ = scene.getDtm().getHeightAtPoint(newX,  newY);
			Coordinate newCoord = new Coordinate(newX - scene.getCentroid().x, -1*(newY - scene.getCentroid().y), newZ); 

			return new StreetFurniture(folder, newCoord, rotation);
		}
		return null;
	}
	
	/**
	 * Determines the possible position of the signs
	 * 
	 * @param left the boolean which allow to know if the sign has to be on the right or on the left of the orad
	 * @param folder the path toward the right sign
	 * @param x the x intersection coordinate
	 * @param y the y intersection coordinate
	 * @param d the distance between the sign and the intersection along the road
	 * @param D the distance between the sign and the middle of the road across the road
	 * @param theta the angle between the horizontal and the road in clockwise order
	 * 
	 * @return
	 */
	private double[] sfPositionning (String folder, double x, double y, double d, double D, double theta){
		// We place the sign on the right side of the road
		// Initialization
		double newX;
		double newY; 
		
		double rotation = 0;

		rotation =  - Math.PI/2 + theta;
		
		// Up-Right quarter
		if (0<= theta && theta <= Math.PI/2){
			newX = x + d*Math.sin(theta) - D*Math.cos(theta);
			newY = y - d*Math.cos(theta) - D*Math.sin(theta);
		}
		// Down-Right quarter
		else if (theta> 3*Math.PI/2 && theta <= 2*Math.PI){
			newX = x - d*Math.sin(2*Math.PI - theta) - D*Math.cos(2*Math.PI - theta);
			newY = y - d*Math.cos(2*Math.PI - theta) + D*Math.sin(2*Math.PI - theta);
		}
		// Up-Left quarter
		else if (theta > Math.PI/2 && theta <= Math.PI){
			newX = x + d*Math.sin(Math.PI - theta) + D*Math.cos(Math.PI - theta);
			newY = y + d*Math.cos(Math.PI - theta) - D*Math.sin(Math.PI - theta);	
		}
		// Down-Left quarter
		else{
			newX = x - d*Math.sin(theta - Math.PI) + D*Math.cos(theta - Math.PI);
			newY = y + d*Math.cos(theta - Math.PI) + D*Math.sin(theta - Math.PI);		
		}
		
		return new double[]{newX, newY, rotation};
	}


}



package eu.ensg.jade.rules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geotools.feature.SchemaException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import eu.ensg.jade.geometricObject.Road;
import eu.ensg.jade.scene.Scene;
import eu.ensg.jade.semantic.PointVegetation;
import eu.ensg.jade.semantic.SurfaceRoad;
import eu.ensg.jade.semantic.SurfaceVegetation;
import eu.ensg.jade.utils.PoissonDiskSampler;

/**
 * 
 * @author JADE
 */

public class VegetationRule implements RuleShape {
	
// ========================== ENUM ==============================

	/**
	 * Enumeration of trees proposed by OpenDS
	 * 
	 * It allows to extract
	 * 
	 * - deciduous tree
	 * 
	 */
	public static enum TREE {
		DECIDUOUS
	};

// ========================== METHODS ==============================
	/**
	 * Puts vegetation on vegetation area
	 * 
	 * @param vegetation the list of surface of vegetation in RGE data
	 * @return 
	 * @throws IOException 
	 * @throws SchemaException 
	 */
	@Override
	public void addPunctualObject(Scene scene) throws SchemaException, IOException {
		
		double t1 = (new Date()).getTime();
		
		System.out.println("Start vegetation fusion");
		
		List<SurfaceVegetation> vege = scene.getSurfaceVegetation();
		Collection<Geometry> geometryCollectionVege = new ArrayList<Geometry>();
		for (SurfaceVegetation v: vege){
			Geometry g = v.getGeometry();
			geometryCollectionVege.add(g);
		}
        Geometry allVege = geomCollUnion(geometryCollectionVege);
        
        double t2 = (new Date()).getTime();
        System.out.println("End vegetation " + String.valueOf(t2-t1));
        t1 = t2;
        
        System.out.println("Start road fusion");
		
        Map<String, Road> roads = scene.getRoads();
		Collection<Geometry> geometryCollectionRoad = new ArrayList<Geometry>();
		for (Road road: roads.values()){
			SurfaceRoad surfRoad = (SurfaceRoad) road;
			Geometry g = (Geometry) surfRoad.getGeom();
			geometryCollectionRoad.add(g);
		}
        Geometry allRoads = geomCollUnion(geometryCollectionRoad);
        
        t2 = (new Date()).getTime();
        System.out.println("End road " + String.valueOf(t2-t1));
        t1 = t2;
        
        System.out.println("Start poisson disk sampling");
		
		// Recuperer la bounding box de la zone de vegetation
		Coordinate[] envelopDiff = allVege.getEnvelope().getCoordinates();
		double startX = envelopDiff[0].x;
		double endX = envelopDiff[2].x;
		double startY = envelopDiff[0].y;
		double endY = envelopDiff[2].y;
		
		// Faire un placement aléatoire régulier de points (Poisson Disk Sampling)
		PoissonDiskSampler poissonDisk = new PoissonDiskSampler(startX,startY,endX,endY,500);
		List<double[]> pointList = poissonDisk.compute();
		
		System.out.println(pointList.size());
		t2 = (new Date()).getTime();
        System.out.println("End sampling " + String.valueOf(t2-t1));
        t1 = t2;
        
        System.out.println("Start tree verification");

		GeometryFactory factory = new GeometryFactory();
		Coordinate centroid = scene.getCentroid();
		// Ne garder que ceux qui intersectent la géométrie
		for (double[] point : pointList){
			Coordinate vegetCoord = new Coordinate(point[0],point[1],0);
			Point pt = factory.createPoint(vegetCoord);
			
			// Creation de l'arbre 
			if (allVege.contains(pt)) {
				vegetCoord.x -= centroid.x;
				vegetCoord.y -= centroid.y;
				pt = factory.createPoint(vegetCoord);
				if(!allRoads.contains(pt)) {
					vegetCoord.z = scene.getDtm().getHeightAtPoint(point[0],point[1]);
					PointVegetation tree = new PointVegetation(vegetCoord,TREE.DECIDUOUS);
					scene.addVegetation(tree);
				}
			}
		}
		System.out.println(pointList.size());
		t2 = (new Date()).getTime();
        System.out.println("End verification " + String.valueOf(t2-t1));
        t1 = t2;
	}
	
	
	/**
	 * Returns the geometry obtained by the difference of a given list of vegetation and roads
	 * 
	 * @param vege the vegetation on which to work
	 * @param roads the roads of the same grip as the vegetation
	 * @return
	 */
	private Geometry diffVegeRoad(List<SurfaceVegetation> vege, Map<String, Road> roads){
		
		Collection<Geometry> geometryCollectionVege = new ArrayList<Geometry>();
		
		for (SurfaceVegetation v: vege){
			Geometry g = v.getGeometry();
			geometryCollectionVege.add(g);
		}
		
		
        Geometry allVege = geomCollUnion(geometryCollectionVege);
		
		Collection<Geometry> geometryCollectionRoad = new ArrayList<Geometry>();

		for (Road road: roads.values()){
			SurfaceRoad surfRoad = (SurfaceRoad) road;
			Geometry g = (Geometry) surfRoad.getGeom();
			geometryCollectionRoad.add(g);
		}

		
        Geometry allRoads = geomCollUnion(geometryCollectionRoad);
    
        // The difference between the vegetation and the road geometry
		Geometry diff = allVege.difference(allRoads);
		
		return diff;
	}
	
	
	/**
	 * Gathers all geometry of a collection in one unique geometry
	 * 
	 * @param geomColl the collection of geometry to be unified
	 * @return
	 */
	private Geometry geomCollUnion(Collection<Geometry> geomColl){
		
		Geometry all = null;
        for( Iterator<Geometry> i = geomColl.iterator(); i.hasNext(); ){
	        Geometry geometry = i.next();
	        if( geometry == null ) continue;
	        if( all == null ){
	        	all = geometry;
	        }
	        else {
	        	all = all.union( geometry );
	        }
        }
        return all;
	}
}

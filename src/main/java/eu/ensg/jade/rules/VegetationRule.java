package eu.ensg.jade.rules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geotools.feature.SchemaException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.operation.union.CascadedPolygonUnion;

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
		
		System.out.println("Start geometry list");
		List<Geometry> vegetGeometryList = new ArrayList<Geometry>();
		for (SurfaceVegetation v: scene.getSurfaceVegetation()){
			vegetGeometryList.add(v.getGeometry());
		}
        System.out.println("vegetGeometryList size: "+vegetGeometryList.size());
        
        double t2 = (new Date()).getTime();
        System.out.println("End: " + + (t2-t1)*0.001); t1 = t2;
        
        System.out.println("Start road fusion");
		List<Geometry> roadGeometryList = new ArrayList<Geometry>();
		for (Road road: scene.getRoads().values()){
			SurfaceRoad surfRoad = (SurfaceRoad) road;
			roadGeometryList.add(surfRoad.getGeom());
		}
//        Geometry roadGeometryUnion = geomCollUnion(roadGeometryList);
		Geometry roadGeometryUnion = CascadedPolygonUnion.union(roadGeometryList);
        
        t2 = (new Date()).getTime();
        System.out.println("End: " + (t2-t1)*0.001); t1 = t2;
        
        System.out.println("Start vegetation loop");
        GeometryFactory factory = new GeometryFactory();
        Coordinate centroid = scene.getCentroid();
        
        for(Geometry vegetGeometry : vegetGeometryList){
        	
    		// Get the bounding box of the current geometry
    		Coordinate[] envelope = vegetGeometry.getEnvelope().getCoordinates();
    		double startX = envelope[0].x;
    		double endX = envelope[2].x;
    		double startY = envelope[0].y;
    		double endY = envelope[2].y;
    		
    		// Apply the Poisson disk sampling algorithm
    		PoissonDiskSampler poissonDisk = new PoissonDiskSampler(startX,startY,endX,endY,20);
    		List<double[]> pointList = poissonDisk.compute();
    
            
//            System.out.println("\tStart tree verification, samples: "+pointList.size());
            
            for (double[] point : pointList){
            	Coordinate vegetCoord = new Coordinate(point[0],point[1],0);
            	Point pt = factory.createPoint(vegetCoord);
            	
            	// test the coordinates
            	if (vegetGeometry.contains(pt)) {
            		vegetCoord.x -= centroid.x;
            		vegetCoord.y -= centroid.y;
            		pt = factory.createPoint(vegetCoord);
            		if(!roadGeometryUnion.contains(pt)) {
            			// Creation of the tree
            			vegetCoord.z = scene.getDtm().getHeightAtPoint(point[0],point[1]);
            			PointVegetation tree = new PointVegetation(vegetCoord,TREE.DECIDUOUS);
            			scene.addVegetation(tree);
            		}
            	}
            }
        }

		t2 = (new Date()).getTime();
		System.out.println("Total trees created: "+scene.getVegetation().size());
        System.out.println("End loop " + (t2-t1)*0.001);
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
		
		List<Geometry> geometryCollectionVege = new ArrayList<Geometry>();
		
		for (SurfaceVegetation v: vege){
			Geometry g = v.getGeometry();
			geometryCollectionVege.add(g);
		}
        Geometry allVege = geomCollUnion(geometryCollectionVege);
		
        
        List<Geometry> geometryCollectionRoad = new ArrayList<Geometry>();

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
	private Geometry geomCollUnion(List<Geometry> geomColl){
		
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

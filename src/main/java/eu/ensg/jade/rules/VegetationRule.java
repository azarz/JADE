package eu.ensg.jade.rules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.feature.SchemaException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.operation.union.CascadedPolygonUnion;

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
		
		List<Geometry> vegetGeometryList = new ArrayList<Geometry>();
		for (SurfaceVegetation v: scene.getSurfaceVegetation()){
			vegetGeometryList.add(v.getGeometry());
		}
        System.out.println("vegetGeometryList size: "+vegetGeometryList.size());
        
		List<Geometry> roadGeometryList = new ArrayList<Geometry>();
		for (SurfaceRoad road: scene.getSurfaceRoads().values()){
			roadGeometryList.add(road.getGeom());
		}
		Geometry roadGeometryUnion = CascadedPolygonUnion.union(roadGeometryList);
        
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

		System.out.println("Total trees created: "+scene.getVegetation().size());
	}
}

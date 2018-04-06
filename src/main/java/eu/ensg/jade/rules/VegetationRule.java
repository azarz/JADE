package eu.ensg.jade.rules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geotools.feature.SchemaException;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.impl.PackedCoordinateSequenceFactory;
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
		
		List<SurfaceVegetation> vege = scene.getSurfaceVegetation();
		Map<String, Road> roads = scene.getRoads();

		Geometry diff = diffVegeRoad(vege, roads);
		
		// Recuperer la bounding box de la zone de vegetation
		Coordinate[] envelopDiff = diff.getEnvelope().getCoordinates();
		double startX = envelopDiff[0].x;
		double endX = envelopDiff[2].x;
		double startY = envelopDiff[0].y;
		double endY = envelopDiff[2].y;
		
		// Faire un placement aléatoire régulier de points (Poisson Disk Sampling)
		PoissonDiskSampler poissonDisk = new PoissonDiskSampler(startX,startY,endX,endY,10);
		List<double[]> pointList = poissonDisk.compute();
		
		// Ne garder que ceux qui intersectent la géométrie 
		for (double[] point : pointList){
			
			PackedCoordinateSequenceFactory factory = PackedCoordinateSequenceFactory.DOUBLE_FACTORY;
			
			Coordinate vegetCoord = new Coordinate(point[0],point[1],0);
			
			CoordinateSequence seq = factory.create(new Coordinate[]{vegetCoord});

			Point pt = new Point(seq,new GeometryFactory());

			Geometry g = (Geometry) pt;

			if (diff.contains(g)){
				// Creation de l'arbre 
				vegetCoord.z = scene.getDtm().getHeightAtPoint(point[0],point[1]);
				PointVegetation tree = new PointVegetation(vegetCoord,TREE.DECIDUOUS);
				scene.addVegetation(tree);
			}
		}    
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

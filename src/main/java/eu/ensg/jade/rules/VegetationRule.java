package eu.ensg.jade.rules;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.GeometryBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.simplify.TopologyPreservingSimplifier;

import eu.ensg.jade.geometricObject.Road;
import eu.ensg.jade.scene.Scene;
import eu.ensg.jade.semantic.SurfaceRoad;
import eu.ensg.jade.semantic.SurfaceVegetation;

/**
 * 
 * @author JADE
 */

public class VegetationRule implements RuleShape {

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
		// Faire un placement aléatoire régulier de points (Poisson Disk Sampling)
		// Ne garder que ceux qui intersectent la géométrie 
		// Mettre des arbres à leur position 
		
		
		
		Geometry simplified = TopologyPreservingSimplifier.simplify(diff, 4);
		//diff.getEnvelope().getCoordinates()[0];

		//scene.addVegetationSurface(new SurfaceVegetation(m, "blabla"));;
        

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

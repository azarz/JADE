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
		
		System.out.println("Creation de la liste vege");
		
		List<SurfaceVegetation> vege = scene.getSurfaceVegetation();
		
		System.out.println("Creation de la collection vege");
		
		Collection<Geometry> geometryCollectionVege = new ArrayList<Geometry>();
		
		System.out.println("Remplissage de la collection vege");
		
		for (SurfaceVegetation v: vege){
			Geometry g = v.getGeometry();
			geometryCollectionVege.add(g);
		}
		
		System.out.println("Creation de la BIG geom vege");
		
        Geometry allVege = null;
        for( Iterator<Geometry> i = geometryCollectionVege.iterator(); i.hasNext(); ){
	        Geometry geometry = i.next();
	        if( geometry == null ) continue;
	        if( allVege == null ){
	        	allVege = geometry;
	        }
	        else {
	        	allVege = allVege.union( geometry );
	        }
        }
	
		System.out.println("Creation de la liste road");
		
		Map<String, Road> roads = scene.getRoads();

		System.out.println("Creation de la collection road");
		
		Collection<Geometry> geometryCollectionRoad = new ArrayList<Geometry>();

		System.out.println("Remplissage de la collection road");
		
		for (Road road: roads.values()){
			SurfaceRoad surfRoad = (SurfaceRoad) road;
			Geometry g = (Geometry) surfRoad.getGeom();
			geometryCollectionRoad.add(g);
		}

		System.out.println("Creation de la BIG geom road");
		
        Geometry allRoads = null;
        for( Iterator<Geometry> i = geometryCollectionRoad.iterator(); i.hasNext(); ){
	        Geometry geometry = i.next();
	        if( geometry == null ) continue;
	        if( allRoads == null ){
	        	allRoads = geometry;
	        }
	        else {
	        	allRoads = allRoads.union( geometry );
	        }
	    }
	
		System.out.println("Differenciation des geoms");
		
		Geometry diff = allVege.difference(allRoads);

		System.out.println("Transformation diif en multipolygon");
		
		MultiPolygon m = (MultiPolygon) diff;
		
		System.out.println("Ajout du multipolygon a une liste");
			
		scene.addVegetationSurface(new SurfaceVegetation(m, "blabla"));;
        

	}
	
	
	
}

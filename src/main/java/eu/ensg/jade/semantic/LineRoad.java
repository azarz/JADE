package eu.ensg.jade.semantic;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFilter;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.operation.buffer.BufferParameters;
import com.vividsolutions.jts.operation.distance.DistanceOp;

import eu.ensg.jade.geometricObject.Road;

/**
 * LinearRoad is the class implementing the linear roads from the RGE
 * 
 * @author JADE
 */

public class LineRoad extends Road{
	
// ========================== ATTRIBUTES ===========================
	
	/**
	 * the attribute containing the geometry of the road
	 */
	protected MultiLineString geometry;
	
	/**
	 * the attribute containing the street furniture placed on the road
	 */
	protected List<StreetFurniture> listSF;
	
	
// ========================== CONSTRUCTORS =========================

	/**
	 * Constructor using all fields
	 * 
	 * @param width The width of the road
	 * @param laneNumber The lane number of the road
	 * @param z_ini The starting altitude of the road
	 * @param z_fin The ending altitude of the road
	 * @param direction The direction of the road
	 * @param nature The nature of the road
	 * @param importance The importance of the road
	 * @param number The number of the road
	 * @param geometry The geometry of the road
	 * @param name The name of the road
	 */
	public LineRoad(double width, int laneNumber, double z_ini, double z_fin, String direction,String nature, String importance, String number, String name, MultiLineString geometry) {
		super(width, laneNumber, z_ini, z_fin, direction, nature, importance, number, name);
		this.geometry = geometry;
		this.listSF = new ArrayList<>();
	}
	
	public LineRoad(){
		super();
	}

	
// ========================== GETTERS/SETTERS ======================


	/**
	 * Allows to access the geometry of the road
	 * 
	 * @return the road original linear road
	 */
	public MultiLineString getGeom() {
		return this.geometry;
	}
	
	/**
	 * Allows to access the road street furniture
	 * 
	 * @return the road street furniture
	 */
	public List<StreetFurniture> getSF() {
		return this.listSF;
	}
	
	
// ========================== METHODS ==============================
	/**
	 * Adds a street furniture to the existing list
	 * 
	 * @param streetFurniture The street furniture to add
	 */
	public void addSF(StreetFurniture streetFurniture){
		this.listSF.add(streetFurniture);
	}
	
	/**
	 * Enlarges the current road
	 * 
	 * @return the road area created
	 */
	public SurfaceRoad enlarge(){
		
		// Getting the buffered geometry (loses Z coordinate...)
		Polygon newGeometry =  (Polygon) geometry.buffer(this.width/2, 0, BufferParameters.CAP_SQUARE);
		
		// Defining a coordinate filter to add the z back
		CoordinateSequenceFilter filter = new CoordinateSequenceFilter() {
			
			@Override
			public void filter(CoordinateSequence seq, int i) {
				seq.getCoordinate(i).z = 0;	
				
				// Creating a point from the coordinate
				GeometryFactory factory = new GeometryFactory();
				Point point = factory.createPoint(seq.getCoordinate(i));
				
				// Creating a multipoint from the initial geometry (so only vertices are taken
				// into account)
				Coordinate[] geomCoords = geometry.getCoordinates();
				MultiPoint geomAsMultiPoint = factory.createMultiPoint(geomCoords);
								
				// Calculating the nearest coordinate in the collection
				Coordinate[] coords = DistanceOp.nearestPoints(geomAsMultiPoint, point);
				
				seq.getCoordinate(i).z = coords[0].z;
			}

			@Override
			public boolean isDone() {
				return false;
			}

			@Override
			public boolean isGeometryChanged() {
				return true;
			}
			
		};
		
		// Applying the filter
		newGeometry.apply(filter);
		
		SurfaceRoad surfacicRoad = new SurfaceRoad(width, laneNumber, z_ini, z_fin, direction, nature, importance, number, name, newGeometry, geometry);
		
		return surfacicRoad;
		
	}
	
	/**
	 * Smoothes the surfacic road previously created
	 * 
	 * @return the surfacic smoothed road
	 */
	public SurfaceRoad smooth(){
//		SurfaceRoad surf = this.enlarge();
		return null;
	}

}

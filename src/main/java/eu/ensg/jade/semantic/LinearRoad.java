package eu.ensg.jade.semantic;

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

public class LinearRoad extends Road{
	
// ========================== ATTRIBUTES ===========================
	
	/**
	 * the attribute containing the geometry of the road
	 */
	protected MultiLineString geometry;
	
	
// ========================== CONSTRUCTORS =========================

	/**
	 * Constructor using all fields
	 * 
	 * @param width
	 * @param wayNumber
	 * @param z_ini
	 * @param z_fin
	 * @param direction
	 * @param geometry
	 */
	public LinearRoad(double width, int wayNumber, double z_ini, double z_fin, String direction, MultiLineString geometry) {
		super(width, wayNumber, z_ini, z_fin, direction);
		
		this.geometry = geometry;
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
	
// ========================== METHODS ==============================
	
	/**
	 * Enlarges the current road
	 * 
	 * @return the road area created
	 */
	public ArealRoad enlarge(){
		
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
		
		ArealRoad surfacicRoad = new ArealRoad(width, wayNumber, z_ini, z_fin, direction, newGeometry);
		
		return surfacicRoad;
		
	}
	
	/**
	 * Smoothes the surfacic road previously created
	 * 
	 * @return the surfacic smoothed road
	 */
	public ArealRoad smooth(){
		ArealRoad surf = this.enlarge();
		return null;
	}
	
}

package eu.ensg.jade.geometricObject;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Road is the super-class for roads
 * 
 * @author JADE
 */

public class Road {	
	
// ========================== ATTRIBUTES ===========================

		/**
		 * Road width as defined in the RGE
		 */
		protected double width;
		/**
		 * The road number of ways
		 */
		protected int wayNumber;
		/**
		 * The elevation at the initial road summit
		 */
		protected double z_ini;
		/**
		 * The elevation at the final road summit
		 */
		protected double z_fin;
		/**
		 * The way driving direction
		 */
		protected String direction;
		
		/**
		 * The Geometry
		 */
		protected Geometry geom;
		
		/**
		 * Constructor using all fields
		 * 
		 * @param width
		 * @param wayNumber
		 * @param z_ini
		 * @param z_fin
		 * @param direction
		 */
		public Road(double width, int wayNumber, double z_ini, double z_fin, String direction, Geometry geom) {
			this.width = width;
			this.wayNumber = wayNumber;
			this.z_ini = z_ini;
			this.z_fin = z_fin;
			this.direction = direction;
			this.geom=geom;
		}
		
		
// ========================== GETTERS/SETTERS ======================
		
		/**
		 * Allows to access the road width
		 * 
		 * @return the road width
		 */
		public double getWidth() {
			return width;
		}


		/**
		 * Allows to access the road number of ways
		 * 
		 * @return the road number of ways
		 */
		public int getWayNumber() {
			return wayNumber;
		}

		/**
		 * Allows to access the elevation at the road initial summit
		 * 
		 * @return the elevation at the road initial summit
		 */
		public double getZ_ini() {
			return z_ini;
		}


		/**
		 * Allows to access the elevation at the road final summit
		 * 
		 * @return the elevation at the final road summit
		 */
		public double getZ_fin() {
			return z_fin;
		}

		/**
		 * Allows to access the road driving direction
		 * 
		 * @return the road driving direction
		 */
		public String getDirection() {
			return direction;
		}

		/**
		 * Allows to access the geometry of the road
		 * 
		 * @return the road original linear road
		 */
		public Geometry getGeom() {
			return geom;
		}		
		
}



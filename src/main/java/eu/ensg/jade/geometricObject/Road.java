package eu.ensg.jade.geometricObject;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import eu.ensg.jade.geometricObject.WorldObject;

/**
 * Road is the super-class for roads
 * 
 * @author JADE
 */

public class Road extends WorldObject {	
	
// ========================== ATTRIBUTES ===========================

		/**
		 * Road width as defined in the RGE
		 */
		protected double width;
		
		/**
		 * The number of lane
		 */
		protected int laneNumber;
		
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
		 * The nature
		 */
		protected String nature;
		
		/**
		 * The importance
		 */
		protected String importance;
		
		/**
		 * The number
		 */
		protected String number;
		
		/**
		 * The number
		 */
		protected String speed;
		
// ========================== CONSTRUCTORS =========================	
		
		/**
		 * Constructor using all fields
		 * 
		 * @param width
		 * @param wayNumber
		 * @param z_ini
		 * @param z_fin
		 * @param direction
		 */
		public Road(double width, int laneNumber, double z_ini, double z_fin, String direction,String nature, String importance, String number, String speed) {
			this.width = width;
			this.laneNumber = laneNumber;
			this.z_ini = z_ini;
			this.z_fin = z_fin;
			this.direction = direction;
			this.nature = nature;
			this.importance = importance;
			this.number = number;
			this.speed = speed;
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
		public int getLaneNumber() {
			return laneNumber;
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
		
		public String getNature() {
			return nature;
		}


		public String getImportance() {
			return importance;
		}


		public String getNumber() {
			return number;
		}
		
		public String getSpeed() {
			return speed;
		}

// ========================== METHODS ==============================

		/**
		 * This method will have a return that will soon be specified
		 * 
		 * @see eu.ensg.jade.geometricObject.WorldObject#toOBJ(java.util.List)
		 */
		@Override
		public String toOBJ(List<Integer> indexOffsets, double xOffset, double yOffset) {
			// TODO Auto-generated method stub
			return null;
		}		

}



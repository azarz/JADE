package eu.ensg.jade.geometricObject;

/**
 * Road is the super-class for roads
 * 
 * @author JADE
 */

public abstract class Road{	
	
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
	
	/**
	 * The name
	 */
	protected String name;
	
// ========================== CONSTRUCTORS =========================	
	
	/**
	 * Constructor using all fields
	 * 
	 * @param width The width of the road
	 * @param laneNumber The number of lanes of the road
	 * @param z_ini Altitude at the start of the road
	 * @param z_fin Altitude at the end of the road
	 * @param direction Direction of the road
	 * @param nature The nature of the road
	 * @param importance The importance of the road
	 * @param number The number of the road
	 * @param name The name of the road
	 */
	public Road(double width, int laneNumber, double z_ini, double z_fin, String direction,String nature, String importance, String number, String name) {
		this.width = width;
		this.laneNumber = laneNumber;
		this.z_ini = z_ini;
		this.z_fin = z_fin;
		this.direction = direction;
		this.nature = nature;
		this.importance = importance;
		this.number = number;
		setSpeed();
		this.name=name;
		}
	
	public Road(){
		
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
	
	/**
	 * Allows to access the road nature
	 * 
	 * @return the road nature
	 */
	public String getNature() {
		return nature;
	}

	/**
	 * Allows to access the road importance
	 * 
	 * @return the road importance
	 */
	public String getImportance() {
		return importance;
	}


	/**
	 * Allows to access the road number
	 * 
	 * @return the road number
	 */
	public String getNumber() {
		return number;
	}
	
	/**
	 * Allows to access the road speed limit
	 * 
	 * @return the road speed limit
	 */
	public String getSpeed() {
		return speed;
	}
	
	/**
	 * Allows to access the road name
	 * 
	 * @return the road name
	 */
	public String getName() {
		return name;
	}

// ========================== METHODS ==============================

	/**
	 * Allows to set speed limits regarding the road attributes
	 */
	public void setSpeed(){
		if((this.importance.equals("2") || this.importance.equals("4")) && (this.number.equals("NC"))){
			this.speed = "50 km/h";
		}
		
		else if((this.importance.equals("4") && this.number.indexOf('D')!=-1) || (this.nature.equals("Bretelle") && this.importance.equals("2"))){
			this.speed = "70 km/h";
		}
		
		else if(((this.importance.equals("1") || this.importance.equals("2") || this.importance.equals("3")) && this.number.indexOf('D')!=-1) || (this.nature.equals("Bretelle") && this.importance.equals("1")) || (this.importance.equals("1") && this.number.equals("NC"))){
			this.speed = "90 km/h";
		}
		
		else if(this.number.indexOf('N')!=-1){
			this.speed = "110 km/h";
		}
		
		else if(this.number.indexOf('A')!=-1){
			this.speed = "130 km/h";
		}
		else{
			this.speed = "";
		}
	}
	
}



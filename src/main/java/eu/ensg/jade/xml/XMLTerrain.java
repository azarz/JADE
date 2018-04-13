package eu.ensg.jade.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * XMLTerrain is the class which hold data representing a terrain, as defined in a scene.xml file
 * 
 * @author JADE
 *
 */
public class XMLTerrain implements IXMLExport {
	
// ========================== ATTRIBUTES ===========================
	/**
	 * The id
	 */
	private String id;
	
	/**
	 * The image Height map
	 */
	private String imageHeightmap;
	
	/**
	 * The height scale
	 */
	private double heightScale;
	
	/**
	 * The patch size
	 */
	private int patchSize;
	
	/**
	 * The total size
	 */
	private int totalSize;
	
	/**
	 * The distance factor
	 */
	private double distanceFactor;
	
// ========================== CONSTRUCTORS =========================
	
	/**
	 * Constructor with basic informations
	 * 
	 * @param id the ID used for the terrain
	 * @param imageHeightmap the path to the grayscale image used as a heightmap
	 */
	public XMLTerrain(String id, String imageHeightmap) {
		this.id = id;
		this.imageHeightmap = imageHeightmap;
		
		this.heightScale = 1;
		
		this.patchSize = 65;
		this.totalSize = 513;
		this.distanceFactor = 2.7;
	}
	
	/**
	 * Constructor with basic informations + size of the terrain
	 * 
	 * @param id the ID used for the terrain
	 * @param imageHeightmap the path to the grayscale image used as a heightmap
	 * @param terrainSize the terrain's size (which must be a power of 2)
	 */
	public XMLTerrain(String id, String imageHeightmap, int terrainSize) {
		this.id = id;
		this.imageHeightmap = imageHeightmap;
		
		this.heightScale = 1;
		
		this.patchSize = 65;
		this.totalSize = terrainSize + 1;
		this.distanceFactor = 2.7;
	}
	
// ========================== GETTERS/SETTERS ======================
	/**
	 * Allows to access to the id
	 * 
	 * @return The id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Allows to set the id
	 * 
	 * @param id The new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Allows to access to the image height map
	 * 
	 * @return The image height map
	 */
	public String getImageHeightmap() {
		return imageHeightmap;
	}

	/**
	 * Allows to set the image height map
	 * 
	 * @param imageHeightmap The new image height map
	 */
	public void setImageHeightmap(String imageHeightmap) {
		this.imageHeightmap = imageHeightmap;
	}

	/**
	 * Allows to access to the height scale
	 * 
	 * @return The height scale
	 */
	public double getHeightScale() {
		return heightScale;
	}

	/**
	 * Allows to set the height scale
	 * 
	 * @param heightScale The new height scale
	 */
	public void setHeightScale(double heightScale) {
		this.heightScale = heightScale;
	}

	/**
	 * Allows to access to the patch size
	 * 
	 * @return The patch size
	 */
	public int getPatchSize() {
		return patchSize;
	}

	/**
	 * Allows to set the patch size
	 * 
	 * @param patchSize The new patch size
	 */
	public void setPatchSize(int patchSize) {
		this.patchSize = patchSize;
	}

	/**
	 * Allows to access to the total size
	 * 
	 * @return The total size
	 */
	public int getTotalSize() {
		return totalSize;
	}

	/**
	 * Allows to set the total size
	 * 
	 * @param totalSize The new total size
	 */
	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	/**
	 * Allows to access to the distance factor
	 * 
	 * @return The distance factor
	 */
	public double getDistanceFactor() {
		return distanceFactor;
	}

	/**
	 * Allows to set the distance factor
	 * 
	 * @param distanceFactor The new distance factor
	 */
	public void setDistanceFactor(double distanceFactor) {
		this.distanceFactor = distanceFactor;
	}
	
// ========================== METHODS ==============================

	/**
	 * @see eu.ensg.jade.xml.IXMLExport#toXMLElement(org.w3c.dom.Document)
	 */
	@Override
	public Element toXMLElement(Document doc) {
		Element terrain = doc.createElement("terrain");
		terrain.setAttribute("id", this.id);
		
		Element heightmap = doc.createElement("imageBasedHeightMap");
		heightmap.setAttribute("key",this.imageHeightmap);
		heightmap.setAttribute("heightScale", String.valueOf(this.heightScale));
		terrain.appendChild(heightmap);
		
		Element smoothing = doc.createElement("smoothing");
		smoothing.setAttribute("percentage", String.valueOf(0.9));
		smoothing.setAttribute("radius", String.valueOf(10));
		terrain.appendChild(smoothing);
		
		Element lod = doc.createElement("lod");
		lod.setAttribute("patchSize", String.valueOf(this.patchSize));
		lod.setAttribute("totalSize", String.valueOf(this.totalSize));
		lod.setAttribute("distanceFactor", String.valueOf(this.distanceFactor));
		terrain.appendChild(lod);
		
		return terrain;
	}
	

}

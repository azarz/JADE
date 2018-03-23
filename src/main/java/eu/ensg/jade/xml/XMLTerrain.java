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
	
	private String id;
	private String imageHeightmap;
	
	private double heightScale;
	
	private int patchSize;
	private int totalSize;
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
		
		this.heightScale = 0.3;
		
		this.patchSize = 65;
		this.totalSize = 513;
		this.distanceFactor = 2.7;
	}
	
// ========================== GETTERS/SETTERS ======================
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImageHeightmap() {
		return imageHeightmap;
	}

	public void setImageHeightmap(String imageHeightmap) {
		this.imageHeightmap = imageHeightmap;
	}

	public double getHeightScale() {
		return heightScale;
	}

	public void setHeightScale(double heightScale) {
		this.heightScale = heightScale;
	}

	public int getPatchSize() {
		return patchSize;
	}

	public void setPatchSize(int patchSize) {
		this.patchSize = patchSize;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	public double getDistanceFactor() {
		return distanceFactor;
	}

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
		smoothing.setAttribute("radius", String.valueOf(1));
		terrain.appendChild(smoothing);
		
		Element lod = doc.createElement("lod");
		lod.setAttribute("patchSize", String.valueOf(this.patchSize));
		lod.setAttribute("totalSize", String.valueOf(this.totalSize));
		lod.setAttribute("distanceFactor", String.valueOf(this.distanceFactor));
		terrain.appendChild(lod);
		
		return terrain;
	}
	

}

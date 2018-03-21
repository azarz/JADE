package eu.ensg.jade.xml;

public class XMLTerrain {
	
// ========================== ATTRIBUTES ===========================
	
	private String id;
	private String imageHeightmap;
	
	private double heightScale;
	
	private int patchSize;
	private int totalSize;
	private double distanceFactor;
	
// ========================== CONSTRUCTORS =========================
	
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
	

}

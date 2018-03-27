package eu.ensg.jade.scene;

import java.io.IOException;
import java.util.Map;

import eu.ensg.jade.geometricObject.Road;
import eu.ensg.jade.input.InputRGE;
import eu.ensg.jade.input.ReaderContext;
import eu.ensg.jade.input.ReaderFactory;
import eu.ensg.jade.input.ReaderFactory.READER_METHOD;
import eu.ensg.jade.output.OBJWriter;
import eu.ensg.jade.output.XMLWriter;
import eu.ensg.jade.semantic.Building;
import eu.ensg.jade.semantic.DTM;
import eu.ensg.jade.semantic.LineRoad;
import eu.ensg.jade.semantic.SurfaceRoad;
import eu.ensg.jade.xml.XMLGroundModel;
import eu.ensg.jade.xml.XMLModel;
import eu.ensg.jade.xml.XMLTerrain;

public class SceneBuilder {
	
// ========================== ATTRIBUTES ===========================
	
	private Scene scene;
	
// ========================== CONSTRUCTORS =========================	
	
	public SceneBuilder() {
		this.scene = new Scene();
	}
	
// ========================== METHODS ==============================
	
	/* (non-Javadoc)
	 * 
	 * Main method
	 */
	
	public static void main(String[] args) {
		String buildingLayer = "src/test/resources/RGE/BD_TOPO/BATI_INDIFFERENCIE.SHP";
		String roadLayer = "src/test/resources/RGE/BD_TOPO/ROUTE.SHP";
		String hydroLayer = "src/test/resources/RGE/BD_TOPO/SURFACE_EAU.SHP";
		String treeLayer = "src/test/resources/RGE/BD_TOPO/ZONE_VEGETATION.SHP";
		String dtmLayer = "src/test/resources/RGE/Dpt_75_asc.asc";
		
		
		SceneBuilder builder = new SceneBuilder();
		builder.buildFromData(buildingLayer, roadLayer, hydroLayer, treeLayer, dtmLayer);
		builder.export();
	}
	
	/* (non-Javadoc)
	 * 
	 * Methods to load data
	 */
	
	public void buildFromData(
			String buildingLayer,
			String roadLayer,
			String hydroLayer,
			String treeLayer,
			String dtmLayer) {
		
		// Load shapefiles
		try {
			scene = loadData(buildingLayer, roadLayer, hydroLayer, treeLayer, dtmLayer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		build(scene);
	}
	
	public void buildFromRGE(String rge) {
		// TODO: implement RGE loading
		
		build(scene);
	}
	
	
	/* (non-Javadoc)
	 * 
	 * Public method to export the whole Scene
	 */
	
	public void export() {
		OBJWriter objWritter = new OBJWriter();
		
		objWritter.exportBuilding("assets/RGE/buildings.obj", scene.getBuildings(), scene.getBuildingCentroid().x, scene.getBuildingCentroid().y);		
		objWritter.exportRoad("assets/RGE/roads.obj", scene.getRoads(), scene.getBuildingCentroid().x, scene.getBuildingCentroid().y);
		
		scene.getDtm().toPNG("assets/RGE/paris.png");
		
		exportXML(scene);
	}
	
	
	/* (non-Javadoc)
	 * 
	 * Private utility methods, get the job done
	 */
	
	private Scene loadData(
			String buildingLayer,
			String roadLayer,
			String hydroLayer,
			String treeLayer,
			String dtmLayer) throws IOException {
		
		Scene scene = new Scene();
		
		ReaderContext readerContx = new ReaderContext();
		ReaderFactory readerFact = new ReaderFactory();
		InputRGE rge = new InputRGE();
		
		rge = readerContx.createInputRGE(readerFact.createReader(READER_METHOD.BUILDING), buildingLayer);
		scene.setBuildings(rge.getBuildings());
		scene.setBuildingCentroid(rge.getCentroid());
		
		rge = readerContx.createInputRGE(readerFact.createReader(READER_METHOD.ROAD), roadLayer);
		scene.setRoads(rge.getRoads());
		scene.setCollIntersect(rge.getCollIntersect());
		
		rge = readerContx.createInputRGE(readerFact.createReader(READER_METHOD.HYDRO), hydroLayer);
		scene.setHydrography(rge.getHydrography());
		
		rge = readerContx.createInputRGE(readerFact.createReader(READER_METHOD.VEGETATION), treeLayer);
		scene.setSurfaceVegetation(rge.getSurfaceVegetation());
		
		rge = readerContx.createInputRGE(readerFact.createReader(READER_METHOD.DTM), dtmLayer);
		scene.setDtm(rge.getDTM());	
		
		return scene;
	}
	
	
	private void build(Scene scene) {
		// Changing the roads and buildings data so it matches the DTM
		DTM dtm = scene.getDtm();
		
		for (Building building : scene.getBuildings()) {
			building.setZfromDTM(dtm);
			building.addHeight();
		}
		
		Map<String, Road> roads = scene.getRoads();
		for(String key : roads.keySet()) {
			SurfaceRoad surfRoad = new SurfaceRoad( (LineRoad) roads.get(key));
			surfRoad.setZfromDTM(dtm);
			roads.put(key, surfRoad);
		}
		scene.setRoads(roads);

		
		
		// TODO: add vegetation & street furniture
	}
	
	
	private void exportXML(Scene scene) {
		XMLWriter xmlWritter = new XMLWriter();
		
		xmlWritter.updateConfig("fileMainXML", "MAIN_FILE.xml");
//		xmlWritter.updateConfig("rainCoefficient", "5");
		
		XMLModel grassPlane = new XMLModel("grassPlane", "Scenes/grassPlane/Scene.j3o");
		xmlWritter.addModel(grassPlane);
		
		XMLModel buildindModel = new XMLModel("Building", "RGE/buildings.obj");
		xmlWritter.addModel(buildindModel);
		
		XMLModel roadsModel = new XMLModel("Roads", "RGE/roads.obj");
		xmlWritter.addModel(roadsModel);
		
		XMLGroundModel ground = getGroundModelFromScene(scene);
		xmlWritter.addTerrain(ground);
		
		xmlWritter.createAllXml();
	}
	
	private XMLGroundModel getGroundModelFromScene(Scene scene){
		DTM dtm = scene.getDtm();
		
		// Getting the largest dimension
		int largestDimension = Math.max(dtm.getNcols(), dtm.getNrows());
		
		// Calculating the smallest power of 2 above the largest dimension if it isn't a power of 2 itself using bitwise shift
		int powerOfTwo = largestDimension;
        if (Integer.highestOneBit(largestDimension) != Integer.lowestOneBit(largestDimension)) {
        	powerOfTwo = Integer.highestOneBit(largestDimension << 1);
        }
        
        // Calculating the transformation to apply to the ground model
 		// Scaling according to the DTM metadata
 		double[] groundScale = new double[3];
 		groundScale[0] = dtm.getCellsize();
 		groundScale[1] = 1d;
 		groundScale[2] = dtm.getCellsize();
 		
 		// No rotation
 		double[] groundRotation = {0,0,0};
 		
 		// Ground translation: the most tricky part
 		double[] groundTranslation = new double[3];
 		
 		double xCentroid = scene.getBuildingCentroid().x;
 		double yCentroid = scene.getBuildingCentroid().y;
             
        groundTranslation[0] = scene.getDtm().getXllcorner() - xCentroid + (powerOfTwo/2)*dtm.getCellsize();
        groundTranslation[1] = 0;
        groundTranslation[2] = yCentroid - dtm.getYllcorner() + ((powerOfTwo/2) - largestDimension)*dtm.getCellsize();
     		
        XMLTerrain terrain = new XMLTerrain("Terrain", "RGE/paris.png", powerOfTwo);
		XMLGroundModel ground = new XMLGroundModel("Ground", "Materials/OrthoImage.j3m", terrain, groundScale, groundRotation, groundTranslation);
	
		return ground;
	}
	
	
}
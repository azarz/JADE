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
import eu.ensg.jade.semantic.DTM;
import eu.ensg.jade.semantic.LineRoad;
import eu.ensg.jade.semantic.SurfaceRoad;
import eu.ensg.jade.xml.XMLGroundModel;
import eu.ensg.jade.xml.XMLModel;
import eu.ensg.jade.xml.XMLTerrain;

public class SceneBuilder {
	
	private Scene scene;
	
	public SceneBuilder() {
		this.scene = new Scene();
	}
	
	
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
	
	public void buildFromData(
			String buildingLayer,
			String roadLayer,
			String hydroLayer,
			String treeLayer,
			String dtmLayer) {
		
		try {
			scene = loadData(buildingLayer, roadLayer, hydroLayer, treeLayer, dtmLayer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		Map<String, Road> roads = scene.getRoads();
		for(String key : roads.keySet()) {
			roads.put(key, new SurfaceRoad( (LineRoad) roads.get(key) ));
		}

	}
	
	public void buildFromRGE(String rge) {
		// TODO: implement RGE
	}
	
	
	
	
	public void export() {
		
		OBJWriter objWritter = new OBJWriter();
		
		objWritter.exportBuilding("assets/RGE/buildings.obj", scene.getBuildings(), scene.getxCentroid(), scene.getyCentroid());
		
		System.out.println(  scene.getRoads().values().toArray()[0].getClass().getName() );
		
		objWritter.exportRoad("assets/RGE/roads.obj", scene.getRoads(), scene.getxCentroid(), scene.getyCentroid());
		
		scene.getDtm().toPNG("assets/RGE/paris.png");
		
		exportXML(scene);
	}
	
	
	
	
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
		scene.setxCentroid(rge.getxCentroid());
		scene.setyCentroid(rge.getyCentroid());
		
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
	
	
	private void exportXML(Scene scene) {
		XMLWriter xmlWritter = new XMLWriter();
		
		xmlWritter.updateConfig("fileMainXML", "MAIN_FILE.xml");
		xmlWritter.updateConfig("rainCoefficient", "5");
		
		XMLModel grassPlane = new XMLModel("grassPlane", "Scenes/grassPlane/Scene.j3o");
		xmlWritter.addModel(grassPlane);
		
//		XMLModel buildindModel = new XMLModel("Building", "RGE/buildings.obj");
//		xmlWritter.addModel(buildindModel);
		
		XMLGroundModel ground = getGroundModelFromScene(scene);
		xmlWritter.addTerrain(ground);
		
		xmlWritter.createAllXml();
	}
	
	private XMLGroundModel getGroundModelFromScene(Scene scene){
		DTM dtm  =scene.getDtm();
		
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
             
         groundTranslation[0] = scene.getDtm().getXllcorner() - scene.getxCentroid() + (powerOfTwo/2)*scene.getDtm().getCellsize();
         groundTranslation[1] = 0;
         groundTranslation[2] = scene.getyCentroid() - scene.getDtm().getYllcorner() + ((powerOfTwo/2) - largestDimension)*scene.getDtm().getCellsize();
     		
        XMLTerrain terrain = new XMLTerrain("Terrain", "RGE/paris.png", powerOfTwo);
		XMLGroundModel ground = new XMLGroundModel("Ground", "Materials/MyTerrain.j3m", terrain, groundScale, groundRotation, groundTranslation);
		
		
		return ground;
	}
	
	
}
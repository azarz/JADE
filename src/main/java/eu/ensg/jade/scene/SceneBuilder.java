package eu.ensg.jade.scene;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

import eu.ensg.jade.geometricObject.Road;
import eu.ensg.jade.input.InputRGE;
import eu.ensg.jade.input.ReaderContext;
import eu.ensg.jade.input.ReaderFactory;
import eu.ensg.jade.input.ReaderFactory.READER_METHOD;
import eu.ensg.jade.output.OBJWriter;
import eu.ensg.jade.output.XMLWriter;

import eu.ensg.jade.semantic.Intersection;
import eu.ensg.jade.xml.XMLGroundModel;
import eu.ensg.jade.xml.XMLModel;
import eu.ensg.jade.xml.XMLTerrain;

public class SceneBuilder {
	
	
	public static void main(String[] args) {
		String buildingLayer = "src/test/resources/RGE/BD_TOPO/BATI_INDIFFERENCIE.SHP";
		String roadLayer = "src/test/resources/RGE/BD_TOPO/ROUTE.SHP";
		String hydroLayer = "src/test/resources/RGE/BD_TOPO/SURFACE_EAU.SHP";
		String treeLayer = "src/test/resources/RGE/BD_TOPO/ZONE_VEGETATION.SHP";
		String dtmLayer = "src/test/resources/RGE/Dpt_75_asc.asc";
		
		try {
			 SceneBuilder.buildFromData(buildingLayer, roadLayer, hydroLayer, treeLayer, dtmLayer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void buildFromData (
			String buildingLayer,
			String roadLayer,
			String hydroLayer,
			String treeLayer,
			String dtmLayer
			) throws IOException {
		
		
		Scene scene = new Scene();
		
		/*
		 * Extract data from files
		 */
		
		// Factory/Strategy Generation
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
		
		/*
		 * Add Vegetation and street furniture
		 */
		
		// TODO: add vegetation & street furniture :)
		
		// TODO: add height to buildings & enlarge roads
		
		
		/*
		 * Export to OBJ & PNG
		 */
		OBJWriter objWritter = new OBJWriter();
		
//		objWritter.exportBuilding("buildings.obj", scene.getBuildings(), scene.getxCentroid(), scene.getyCentroid());
		
//		objWritter.exportRoad("roads.obj", scene.getRoads(), scene.getxCentroid(), scene.getyCentroid());
		
//		scene.getDtm().toPNG("paris.png");
		
		
		
		/*
		 * Write the XML files
		 */
		XMLWriter xmlWritter = new XMLWriter();
		
		xmlWritter.updateConfig("fileMainXML", "MAIN_FILE.xml");
		xmlWritter.updateConfig("rainCoefficient", "20");
		
		XMLModel grassPlane = new XMLModel("grassPlane", "Scenes/grassPlane/Scene.j3o");
		xmlWritter.addModel(grassPlane);
		
		XMLModel buildindModel = new XMLModel("Building", "buildings.obj");
		xmlWritter.addModel(buildindModel);
		
		XMLTerrain terrain = new XMLTerrain("terrain", "paris.png");
		XMLGroundModel ground = new XMLGroundModel("Ground", "Materials/MyTerrain.j3m", terrain);
		xmlWritter.addTerrain(ground);
		
		xmlWritter.createAllXml();
	}
	
	public static void buildFromRGE() {
		
	}
	
}

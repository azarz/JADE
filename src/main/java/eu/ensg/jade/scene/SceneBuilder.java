package eu.ensg.jade.scene;

import java.io.IOException;
import java.net.URL;

import eu.ensg.jade.input.InputRGE;
import eu.ensg.jade.input.ReaderContext;
import eu.ensg.jade.input.ReaderFactory;
import eu.ensg.jade.input.ReaderFactory.READER_METHOD;
import eu.ensg.jade.output.OBJWritter;

public class SceneBuilder {
	
	
	public static void main(String[] args) {
		String buildingPath = "src/test/resources/RGE/BD_TOPO/BATI_INDIFFERENCIE.SHP";
		String hydroPath = "src/test/resources/RGE/BD_TOPO/SURFACE_EAU.SHP";
		String roadPath = "src/test/resources/RGE/BD_TOPO/ROUTE.SHP";
		String vegetPath = "src/test/resources/RGE/BD_TOPO/ZONE_VEGETATION.SHP";
		String dtmPath = "src/test/resources/RGE/Dpt_75_asc.asc";
	}
	
	
	public static void buildFromData (
			String buildingLayer,
			String roadLayer,
			String hydroLayer,
			String plantLayer,
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
		
		rge = readerContx.createInputRGE(readerFact.createReader(READER_METHOD.ROAD), roadLayer);
		scene.setLineRoads(rge.getLineRoads());
		
		rge = readerContx.createInputRGE(readerFact.createReader(READER_METHOD.HYDRO), hydroLayer);
		scene.setHydrography(rge.getHydrography());
		
		rge = readerContx.createInputRGE(readerFact.createReader(READER_METHOD.VEGETATION), plantLayer);
		scene.setSurfaceVegetation(rge.getSurfaceVegetation());
		
		rge = readerContx.createInputRGE(readerFact.createReader(READER_METHOD.DTM), dtmLayer);
		scene.setDtm(rge.getDTM());		
		
		/*
		 * Add Vegetation and street furniture
		 */
		
		// TODO: add vegetation & street furniture :)
		
		/*
		 * Export to OBJ & PNG
		 */
		OBJWritter objWritter = new OBJWritter();
		
		objWritter.exportBuilding("buildings.obj", scene.getBuildings());
		
		objWritter.exportRoad("roads.obj", scene.getRoads());
		
		scene.getDtm().toPNG("paris.obj");
		
		/*
		 * Write the XML files
		 */
		
	}
	
	public static void buildFromRGE() {
		
	}
	
	
}

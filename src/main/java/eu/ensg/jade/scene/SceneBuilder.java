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
		
		
		/*
		 * Extract data from files
		 */
		
		// Factory/Strategy Generation
		ReaderContext readerContx = new ReaderContext();
		ReaderFactory readerFact = new ReaderFactory();
		
		readerContx.setIReaderStrategy(readerFact.createReader(READER_METHOD.BUILDING));
		InputRGE buildingRGE = readerContx.createInputRGE(buildingLayer);
		
		readerContx.setIReaderStrategy(readerFact.createReader(READER_METHOD.BUILDING.ROAD));
		InputRGE roadRGE = readerContx.createInputRGE(roadLayer);
		
		readerContx.setIReaderStrategy(readerFact.createReader(READER_METHOD.HYDRO));
		InputRGE hydroRGE = readerContx.createInputRGE(hydroLayer);
		
		readerContx.setIReaderStrategy(readerFact.createReader(READER_METHOD.BUILDING.VEGETATION));
		InputRGE vegetRGE = readerContx.createInputRGE(plantLayer);
		
		readerContx.setIReaderStrategy(readerFact.createReader(READER_METHOD.DTM));
		InputRGE dtmRGE = readerContx.createInputRGE(dtmLayer);
		
		dtmRGE.getDTM().toPNG("paris.obj");
		
		URL url = Thread.currentThread().getContextClassLoader().getResource("RGE/BD_TOPO/BATI_INDIFFERENCIE.SHP");
		String shpPath = url.getPath();
		
		/*
		 * Build the Scene with the data		
		 */		
		Scene scene = new Scene();
		
		/*
		 * Add Vegetation and street furniture
		 */
		
		/*
		 * Export to OBJ
		 */
		OBJWritter objWritter = new OBJWritter();
		
		objWritter.exportBuildings("buildings.obj", scene.getBuildings());
		
		/*
		 * Write the XML files
		 */
		
	}
	
	public static void buildFromRGE() {
		
	}
	
	
}

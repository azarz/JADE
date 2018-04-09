package eu.ensg.jade.scene;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.feature.SchemaException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

import com.vividsolutions.jts.geom.Polygon;

import eu.ensg.jade.input.InputRGE;
import eu.ensg.jade.input.ReaderContext;
import eu.ensg.jade.input.ReaderFactory;
import eu.ensg.jade.input.ReaderFactory.READER_TYPE;
import eu.ensg.jade.output.OBJWriter;
import eu.ensg.jade.output.XMLWriter;
import eu.ensg.jade.rules.RuleShapeMaker;
import eu.ensg.jade.semantic.ArcIntersection;
import eu.ensg.jade.semantic.Building;
import eu.ensg.jade.semantic.DTM;
import eu.ensg.jade.semantic.LineRoad;
import eu.ensg.jade.semantic.PointVegetation;
import eu.ensg.jade.semantic.StreetFurniture;
import eu.ensg.jade.semantic.SurfaceRoad;
import eu.ensg.jade.xml.XMLGroundModel;
import eu.ensg.jade.xml.XMLModel;
import eu.ensg.jade.xml.XMLTerrain;

public class SceneBuilder {
	
// ========================== ATTRIBUTES ===========================
	/**
	 * 
	 */
	private Scene scene;
	
// ========================== CONSTRUCTORS =========================	
	
	/**
	 * 
	 */
	public SceneBuilder() {
		this.scene = new Scene();
	}
	
// ========================== METHODS ==============================
	
	/* (non-Javadoc)
	 * 
	 * Main method
	 */
	public static void main(String[] args) throws NoSuchAuthorityCodeException, FactoryException, SchemaException, IOException {
		String buildingLayer = "src/test/resources/RGE/BD_TOPO/BATI_INDIFFERENCIE.SHP";
		String roadLayer = "src/test/resources/RGE/BD_TOPO/ROUTE.SHP";
//		String roadLayer = "src/test/resources/inputTest/openShpTestLinearRoad3.shp";
		String hydroLayer = "src/test/resources/RGE/BD_TOPO/SURFACE_EAU.SHP";
//		String treeLayer = "src/test/resources/RGE/BD_TOPO/ZONE_VEGETATION.SHP";
		String treeLayer = "src/test/resources/inputTest/openShpTestVege3.shp";
		String dtmLayer = "src/test/resources/RGE/Dpt_75_asc.asc";
		
		
		SceneBuilder builder = new SceneBuilder();
		builder.buildFromFiles(buildingLayer, roadLayer, hydroLayer, treeLayer, dtmLayer);
		builder.export();
	}
	
	
	/**
	 * @param buildingLayer
	 * @param roadLayer
	 * @param hydroLayer
	 * @param treeLayer
	 * @param dtmLayer
	 * @throws FactoryException 
	 * @throws NoSuchAuthorityCodeException 
	 * @throws IOException 
	 * @throws SchemaException 
	 */
	public void buildFromFiles(
			String buildingLayer,
			String roadLayer,
			String hydroLayer,
			String treeLayer,
			String dtmLayer) throws NoSuchAuthorityCodeException, FactoryException, SchemaException, IOException {
		
		// Load shapefiles
		try {
			scene = loadData(buildingLayer, roadLayer, hydroLayer, treeLayer, dtmLayer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		build(scene);
	}
	
	/**
	 * @param buildingLayer
	 * @param roadLayer
	 * @param hydroLayer
	 * @param treeLayer
	 * @param dtmLayer
	 * @throws Exception 
	 */
	public void buildFromRGE(
			String buildingLayer,
			String roadLayer,
			String hydroLayer,
			String treeLayer,
			String dtmLayer) throws Exception {		
		try {
			scene = loadRGE(buildingLayer, roadLayer, hydroLayer, treeLayer, dtmLayer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		build(scene);
	}
	
	
	/**
	 * Public method to export the whole Scene as a driving task, to be used in OpenDS
	 */
	public void export() {
		exportRGEData(scene);
		exportXML(scene);
	}
	
	
	/**
	 * Instantiate a Scene object, and fill it with data extracted from the specified files
	 * 
	 * @param buildingLayer the building shp file
	 * @param roadLayer the road shp file
	 * @param hydroLayer the hydrography shp file
	 * @param treeLayer the vegetation shp file
	 * @param dtmLayer the DTM png file
	 * 
	 * @return a new Scene
	 * @throws IOException
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
		
		rge = readerFact.createReader(READER_TYPE.BUILDING).loadFromFile(buildingLayer);
		scene.setBuildings(rge.getBuildings());
		scene.setCentroid(rge.getCentroid());
		
		
		rge = readerContx.createInputRGE(readerFact.createReader(READER_TYPE.ROAD), roadLayer);
		scene.setLineRoads(rge.getRoads());
		scene.setCollIntersect(rge.getCollIntersect());
		
		rge = readerContx.createInputRGE(readerFact.createReader(READER_TYPE.HYDRO), hydroLayer);
		scene.setHydrography(rge.getHydrography());
		
		rge = readerContx.createInputRGE(readerFact.createReader(READER_TYPE.VEGETATION), treeLayer);
		scene.setSurfaceVegetation(rge.getSurfaceVegetation());
		
		rge = readerContx.createInputRGE(readerFact.createReader(READER_TYPE.DTM), dtmLayer);
		scene.setDtm(rge.getDTM());
		
		return scene;
	}
	
	/**
	 * Instantiate a Scene object, and fill it with data extracted from the features of the RGE
	 * 
	 * @param buildingFeature the building shp feature
	 * @param roadFeature the road shp feature
	 * @param hydroFeature the hydrography shp feature
	 * @param treeFeature the vegetation shp feature
	 * @param dtmFeature the DTM png feature
	 * @return
	 * @throws Exception 
	 */
	private Scene loadRGE(
			String buildingFeature,
			String roadFeature,
			String hydroFeature,
			String treeFeature,
			String dtmFeature) throws Exception {
		Scene scene = new Scene();
		
//		ReaderFactory readerFact = new ReaderFactory();
//		InputRGE rge = new InputRGE();
//		
//		FluxConfiguration config = new FluxConfiguration();
//		
//		rge = readerFact.createReader(READER_TYPE.BUILDING).loadFromRGE(buildingFeature);
//		scene.setBuildings(rge.getBuildings());
//		scene.setBuildingCentroid(rge.getCentroid());
//		
//		rge = readerFact.createReader(READER_TYPE.ROAD).loadFromRGE(roadFeature);
//		scene.setRoads(rge.getRoads());
//		scene.setCollIntersect(rge.getCollIntersect());
//		
//		rge = readerFact.createReader(READER_TYPE.HYDRO).loadFromRGE(hydroFeature);
//		scene.setHydrography(rge.getHydrography());
//		
//		rge = readerFact.createReader(READER_TYPE.VEGETATION).loadFromFile(treeFeature);
//		scene.setSurfaceVegetation(rge.getSurfaceVegetation());
//		
		// TODO: Add the DTM ?
//		rge = readerFact.createReader(READER_TYPE.VEGETATION).loadFromRGE(treeFeature);
//		scene.setSurfaceVegetation(rge.getSurfaceVegetation());
//		
//		rge = readerFact.createReader(READER_TYPE.DTM).loadFromRGE(dtmFeature);
//		scene.setDtm(rge.getDTM());

		
		return scene;
	}
	
	
	/**
	 * Build the scene with additional data:
	 * <ul>
	 * <li>Street Furniture</li>
	 * <li>Vegetation</li>
	 * <li>Correct building height</li>
	 * <li>Streets at DTM level</li>
	 * </ul>
	 * 
	 * @param scene The scene to build
	 * @throws FactoryException 
	 * @throws NoSuchAuthorityCodeException 
	 * @throws IOException 
	 * @throws SchemaException 
	 */
	private void build(Scene scene) throws NoSuchAuthorityCodeException, FactoryException, SchemaException, IOException {
		// Changing the roads and buildings data so it matches the DTM
		DTM dtm = scene.getDtm();
		dtm.smooth(0.9, 1);
		
		// Add intersections
		RuleShapeMaker ruleShapeMaker = new RuleShapeMaker();
//		ruleShapeMaker.addIntersectionSigns(scene);

		// Set building height
		for (Building building : scene.getBuildings()) {
			building.setZfromDTM(dtm);
			building.addHeight();
		}
		
		// Create the areal roads, and set the correct height
		Map<String, LineRoad> lineRoads = scene.getLineRoads();
		Map<String, SurfaceRoad> surfaceRoads = new HashMap<String,SurfaceRoad>();
		for(String key : lineRoads.keySet()) {
			SurfaceRoad surfRoad = new SurfaceRoad(lineRoads.get(key));
			surfRoad.setZfromDTM(dtm);
			surfaceRoads.put(key, surfRoad);
		}
		scene.setSurfaceRoads(surfaceRoads);
		

		List<Polygon> intersectionPolygon = ArcIntersection.generateSmoothRoad(scene);
		
		// Add punctual vegetation
		ruleShapeMaker.addVegetation(scene);
//		SurfaceVegetation vege = scene.getSurfaceVegetation().get(scene.getSurfaceVegetation().size()-1);		
	}
	
	
	private void exportRGEData(Scene scene) {
		OBJWriter objWritter = new OBJWriter();
		
		File directory = new File("assets/RGE");
		if (! directory.exists()){ directory.mkdir(); }
		
		objWritter.exportBuilding("assets/RGE/buildings.obj", scene.getBuildings(), scene.getCentroid().x, scene.getCentroid().y);		
		objWritter.exportRoad("assets/RGE/roads.obj", scene.getSurfaceRoads(), scene.getCentroid().x, scene.getCentroid().y);
		objWritter.exportWater("assets/RGE/water.obj", scene.getHydrography(), scene.getCentroid().x, scene.getCentroid().y);
		
//		List<SurfaceVegetation> vege = new ArrayList<SurfaceVegetation>(); 
//		vege.add(scene.getSurfaceVegetation().get(scene.getSurfaceVegetation().size()-1));
//		objWritter.exportVege("assets/RGE/vegetation.obj", vege, scene.getBuildingCentroid().x, scene.getBuildingCentroid().y);	

		scene.getDtm().toPNG("assets/RGE/paris.png");
	}
	
	
	private void exportXML(Scene scene) {
		XMLWriter xmlWriter = new XMLWriter();
		xmlWriter.log = true;
		
		xmlWriter.updateConfig("fileMainXML", "MAIN_FILE.xml");
//		xmlWriter.updateConfig("rainCoefficient", "5");
		
		// Add flat ground (debug)
		XMLModel grassPlane = new XMLModel("grassPlane", "Scenes/grassPlane/Scene.j3o");
		xmlWriter.addModel(grassPlane);
		
		// Add driver
		XMLModel driver = new XMLModel("driverCar", "Models/Cars/drivingCars/CitroenC4/Car.j3o");
		driver.setMass(1000);
		//Coordinate coord = scene.getStreetFurniture().get(0).getCoord();
		//driver.setTranslation(new double[]{coord.x + 10, 60, coord.y});
		driver.setTranslation(new double[]{0, 70, 0});
		driver.setScale((new double[]{0.8, 0.8, 0.8}));
		xmlWriter.addModel(driver);
		
		// Add buildings
		XMLModel buildindModel = new XMLModel("Building", "RGE/buildings.obj");
		xmlWriter.addModel(buildindModel);
		
		// Add roads
		XMLModel roadsModel = new XMLModel("Roads", "RGE/roads.obj");
		xmlWriter.addModel(roadsModel);
//		
		// Add water
//		XMLModel waterModel = new XMLModel("Water", "RGE/water.obj");
//		xmlWriter.addModel(waterModel);
		
		// Add vegetation surface
//		XMLModel vegeModel = new XMLModel("Vegetation", "RGE/vegetation.obj");
//		xmlWriter.addModel(vegeModel);

		// Add street furniture
		int k = 0;
		for(StreetFurniture sign : scene.getStreetFurniture()) {
			XMLModel streetFurnitureModel = new XMLModel("StreetFurniture", sign.getPath());
			streetFurnitureModel.setRotation(new double[] {0, sign.getRotation()*180/Math.PI, 0});
			streetFurnitureModel.setTranslation(new double[] {sign.getCoord().x,sign.getCoord().z,sign.getCoord().y});
			xmlWriter.addModel(streetFurnitureModel);
			
			if (++k>1000){ break; }
		}
		
		int g = 0;
		for(PointVegetation tree : scene.getVegetation()) {
			XMLModel vegetationModel = new XMLModel("Tree", tree.getNature());
			vegetationModel.setTranslation(new double[]{tree.getCoord().x,tree.getCoord().z,tree.getCoord().y});
			vegetationModel.setScale(new double[]{8,8,8});
			xmlWriter.addModel(vegetationModel);
			
			if (++g>1000){ break; }
		}

		
		// Add DTM
		XMLGroundModel ground = getGroundModelFromScene(scene);
		ground.setVisible(true);
		xmlWriter.addTerrain(ground);
		
		xmlWriter.createAllXml();
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
 		
 		double xCentroid = scene.getCentroid().x;
 		double yCentroid = scene.getCentroid().y;
             
        groundTranslation[0] = scene.getDtm().getXllcorner() - xCentroid + (powerOfTwo/2)*dtm.getCellsize();
        groundTranslation[1] = 0;
        groundTranslation[2] = yCentroid - dtm.getYllcorner() + ((powerOfTwo/2) - largestDimension)*dtm.getCellsize();
     		
        XMLTerrain terrain = new XMLTerrain("Terrain", "RGE/paris.png", powerOfTwo);
		XMLGroundModel ground = new XMLGroundModel("Ground", "Materials/OrthoImage.j3m", terrain, groundScale, groundRotation, groundTranslation);
	
		return ground;
	}
	
	
}
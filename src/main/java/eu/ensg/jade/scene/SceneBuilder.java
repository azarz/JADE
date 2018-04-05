package eu.ensg.jade.scene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.geotools.feature.SchemaException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

import com.vividsolutions.jts.geom.Coordinate;

import eu.ensg.jade.geometricObject.Road;
import eu.ensg.jade.input.InputRGE;
import eu.ensg.jade.input.ReaderContext;
import eu.ensg.jade.input.ReaderFactory;
import eu.ensg.jade.input.ReaderFactory.READER_METHOD;
import eu.ensg.jade.output.OBJWriter;
import eu.ensg.jade.output.XMLWriter;
import eu.ensg.jade.rules.RuleShapeMaker;
import eu.ensg.jade.semantic.Building;
import eu.ensg.jade.semantic.DTM;
import eu.ensg.jade.semantic.LineRoad;
import eu.ensg.jade.semantic.StreetFurniture;
import eu.ensg.jade.semantic.SurfaceRoad;
import eu.ensg.jade.semantic.SurfaceVegetation;
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
		//String roadLayer = "src/test/resources/RGE/BD_TOPO/ROUTE.SHP";
		String roadLayer = "src/test/resources/inputTest/openShpTestLinearRoad3.shp";
		String hydroLayer = "src/test/resources/RGE/BD_TOPO/SURFACE_EAU.SHP";
		//String treeLayer = "src/test/resources/RGE/BD_TOPO/ZONE_VEGETATION.SHP";
		String treeLayer = "src/test/resources/inputTest/openShpTestVege2.shp";
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
		OBJWriter objWritter = new OBJWriter();
		
		File directory = new File("assets/RGE");
		if (! directory.exists()){ directory.mkdir(); }
		
		objWritter.exportBuilding("assets/RGE/buildings.obj", scene.getBuildings(), scene.getBuildingCentroid().x, scene.getBuildingCentroid().y);		
		objWritter.exportRoad("assets/RGE/roads.obj", scene.getRoads(), scene.getBuildingCentroid().x, scene.getBuildingCentroid().y);
		objWritter.exportWater("assets/RGE/water.obj", scene.getHydrography(), scene.getBuildingCentroid().x, scene.getBuildingCentroid().y);
		
		System.out.println("Ajout de l obj !");
		
//		List<SurfaceVegetation> vege = new ArrayList<SurfaceVegetation>(); 
//		vege.add(scene.getSurfaceVegetation().get(scene.getSurfaceVegetation().size()-1));
//		objWritter.exportVege("assets/RGE/vegetation.obj", vege, scene.getBuildingCentroid().x, scene.getBuildingCentroid().y);
//		
		System.out.println("Fin d ajout de l obj !");

		scene.getDtm().toPNG("assets/RGE/paris.png");
		
		exportXML(scene);
	}
	
	
	/* (non-Javadoc)
	 * Private utility methods, get the job done
	 */
	
	
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
		
//		rge = readerContx.createInputRGE(readerFact.createReader(READER_METHOD.BUILDING), buildingLayer);
		rge = readerFact.createReader(READER_METHOD.BUILDING).loadFromFile(buildingLayer);
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
		
		ReaderFactory readerFact = new ReaderFactory();
		InputRGE rge = new InputRGE();
		
		rge = readerFact.createReader(READER_METHOD.BUILDING).loadFromRGE(buildingFeature);
		scene.setBuildings(rge.getBuildings());
		scene.setBuildingCentroid(rge.getCentroid());
		
		rge = readerFact.createReader(READER_METHOD.ROAD).loadFromRGE(roadFeature);
		scene.setRoads(rge.getRoads());
		scene.setCollIntersect(rge.getCollIntersect());
		
		rge = readerFact.createReader(READER_METHOD.HYDRO).loadFromRGE(hydroFeature);
		scene.setHydrography(rge.getHydrography());
		
		rge = readerFact.createReader(READER_METHOD.VEGETATION).loadFromRGE(treeFeature);
		scene.setSurfaceVegetation(rge.getSurfaceVegetation());
		
		// TODO: Add the DTM ?
		
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

		// TODO: add vegetation & street furniture
		RuleShapeMaker ruleShapeMaker = new RuleShapeMaker();
		//ruleShapeMaker.addIntersectionSigns(scene);

		// Set building height
		for (Building building : scene.getBuildings()) {
			building.setZfromDTM(dtm);
			building.addHeight();
		}
		
		// Set road height
		Map<String, Road> roads = scene.getRoads();
		for(String key : roads.keySet()) {
			SurfaceRoad surfRoad = new SurfaceRoad( (LineRoad) roads.get(key));
			surfRoad.setZfromDTM(dtm);
			roads.put(key, surfRoad);
		}
		scene.setRoads(roads);

		//ruleShapeMaker.addVegetation(scene);
		//System.out.println("J ajoute la vegetation a la scene");

		
	}
	
	
	private void exportXML(Scene scene) {
		XMLWriter xmlWriter = new XMLWriter();
		xmlWriter.log = true;
		
		xmlWriter.updateConfig("fileMainXML", "MAIN_FILE.xml");
//		xmlWritter.updateConfig("rainCoefficient", "5");
		
		// Add flat ground
//		XMLModel grassPlane = new XMLModel("grassPlane", "Scenes/grassPlane/Scene.j3o");
//		xmlWriter.addModel(grassPlane);
		
		// Add driver
		XMLModel driver = new XMLModel("driverCar", "Models/Cars/drivingCars/CitroenC4/Car.j3o");
		driver.setMass(800);
		//Coordinate coord = scene.getStreetFurniture().get(0).getCoord();
		//driver.setTranslation(new double[]{coord.x + 10, 60, coord.y});
		driver.setTranslation(new double[]{scene.getBuildingCentroid().x, 120, scene.getBuildingCentroid().y});
		xmlWriter.addModel(driver);
		
		// Add buildings
//		XMLModel buildindModel = new XMLModel("Building", "RGE/buildings.obj");
//		xmlWriter.addModel(buildindModel);
		
		// Add roads
//		XMLModel roadsModel = new XMLModel("Roads", "RGE/roads.obj");
//		xmlWriter.addModel(roadsModel);
//		
		// Add water
//		XMLModel waterModel = new XMLModel("Water", "RGE/water.obj");
//		xmlWriter.addModel(waterModel);
		
		// Add water
//		System.out.println("creation du xml");
//		XMLModel vegeModel = new XMLModel("Vegetation", "RGE/vegetation.obj");
//		xmlWriter.addModel(vegeModel);
//		System.out.println("xml créé");
//		
		/*int k = 0;
		// Add street furniture
		for(StreetFurniture sign : scene.getStreetFurniture()) {
			k++;
			XMLModel streetFurnitureModel = new XMLModel("StreetFurniture", sign.getPath());
			streetFurnitureModel.setRotation(new double[] {0, sign.getRotation()*180/Math.PI, 0});
			streetFurnitureModel.setTranslation(new double[] {sign.getCoord().x,sign.getCoord().z,sign.getCoord().y});
			//streetFurnitureModel.setScale(new double[] {10,10,10});
			xmlWriter.addModel(streetFurnitureModel);
			
			if (k>5000){
				break;
			}
		}*/
		
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
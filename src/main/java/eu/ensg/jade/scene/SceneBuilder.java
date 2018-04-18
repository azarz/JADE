package eu.ensg.jade.scene;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.geotools.feature.SchemaException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.operation.union.CascadedPolygonUnion;

import eu.ensg.jade.input.InputRGE;
import eu.ensg.jade.input.ReaderContext;
import eu.ensg.jade.input.ReaderFactory;
import eu.ensg.jade.input.ReaderFactory.READER_TYPE;
import eu.ensg.jade.output.OBJWriter;
import eu.ensg.jade.output.XMLWriter;
import eu.ensg.jade.rules.RuleShapeMaker;
import eu.ensg.jade.semantic.Building;
import eu.ensg.jade.semantic.DTM;
import eu.ensg.jade.semantic.LineRoad;
import eu.ensg.jade.semantic.PointVegetation;
import eu.ensg.jade.semantic.Sidewalk;
import eu.ensg.jade.semantic.StreetFurniture;
import eu.ensg.jade.semantic.SurfaceRoad;
import eu.ensg.jade.xml.XMLGroundModel;
import eu.ensg.jade.xml.XMLModel;
import eu.ensg.jade.xml.XMLTerrain;

/**
 * SceneBuilder is the class building the scene and exporting it.
 *
 * @author JADE
 */
public class SceneBuilder {
	
// ========================== ATTRIBUTES ===========================
	/**
	 * The scene of the builder
	 */
	private Scene scene;
	
	private static String place = "Perros";
	
// ========================== CONSTRUCTORS =========================	
	
	/**
	 * Empty constructor
	 */
	public SceneBuilder() {
		this.scene = new Scene();
	}
	
// ========================== METHODS ==============================
	
	/* (non-Javadoc)
	 * 
	 * Main method
	 */
	/**
	 * Main method for JADE
	 * 
	 * @param args Default args argument for JAVA.
	 * 
	 * @throws IOException Throws IOException
	 * @throws SchemaException Throws SchemaException
	 * @throws NoSuchAuthorityCodeException Throws NoSuchAuthorityCodeException
	 * @throws FactoryException Throws FactoryException 
	 */
	public static void main(String[] args) throws NoSuchAuthorityCodeException, FactoryException, SchemaException, IOException {
		long begin = System.currentTimeMillis();
		
		// Reading the arguments
		if (args.length > 1) {
			place = args[1];
		}
		
		String buildingLayer = "input/" + place + "/BATI_INDIFFERENCIE.SHP";
		String roadLayer = "input/" + place + "/ROUTE.SHP";
		String hydroLayer = "input/" + place + "/SURFACE_EAU.SHP";
		String treeLayer = "input/" + place + "/ZONE_VEGETATION.shp";
		String dtmLayer = "input/" + place + "/DTM_1m.asc";
		String orthoImage = "input/" + place + "/ortho.png";
		
		SceneBuilder builder = new SceneBuilder();
		builder.buildFromFiles(buildingLayer, roadLayer, hydroLayer, treeLayer, dtmLayer);
		builder.export();
		builder.exportOrthoImage(orthoImage);
		
		long end = System.currentTimeMillis();
		System.out.println((end-begin)/1000 + " seconds elapsed");
	}
	
	
	/**
	 * Builds the scene from files
	 * 
	 * @param buildingLayer The path to the building layer file
	 * @param roadLayer The path to the road layer file
	 * @param hydroLayer The path to the hydro layer file
	 * @param treeLayer The path to the tree layer file
	 * @param dtmLayer The path to the dtm layer file
	 * 
	 * @throws FactoryException Throws FactoryException
	 * @throws NoSuchAuthorityCodeException Throws NoSuchAuthorityCodeException 
	 * @throws IOException Throws IOException
	 * @throws SchemaException Throws SchemaException
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
	 * Builds the scene from a RGE stream
	 * 
	 * @param buildingLayer The location of the building layer stream
	 * @param roadLayer The location of the road layer stream
	 * @param hydroLayer The location of the hydro layer stream
	 * @param treeLayer The location of the tree layer stream
	 * @param dtmLayer The location of the dtm layer stream
	 * @throws Exception Throws some exceptions (to be specified) 
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
		
		rge = readerContx.createInputRGE(readerFact.createReader(READER_TYPE.BUILDING), buildingLayer);
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
	 * 
	 * @return The new scene
	 * 
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
	 * 
	 * @throws FactoryException Throws FactoryException
	 * @throws NoSuchAuthorityCodeException Throws NoSuchAuthorityCodeException 
	 * @throws IOException Throws IOException
	 * @throws SchemaException Throws SchemaException 
	 */
	private void build(Scene scene) throws NoSuchAuthorityCodeException, FactoryException, SchemaException, IOException {
		// Changing the roads and buildings data so it matches the DTM
		DTM dtm = scene.getDtm();
		dtm.smooth(0.9, 10);

		// Set building height
		for (Building building : scene.getBuildings()) {
			building.setZfromDTM(dtm);
			building.addHeight();
		}
	
		List<Geometry> polygonList = new ArrayList<>();
//		polygonList = ArcIntersection.generateSmoothRoad(scene);
		
		// Create the areal roads, and set the correct height
		Map<String, LineRoad> lineRoads = scene.getLineRoads();
		Map<String, SurfaceRoad> surfaceRoads = new HashMap<String,SurfaceRoad>();
		for(String key : lineRoads.keySet()) {
			SurfaceRoad surfRoad = new SurfaceRoad(lineRoads.get(key));
			polygonList.add(surfRoad.getGeom());
			surfRoad.setZfromDTM(dtm);
			surfaceRoads.put(key, surfRoad);
		}
		scene.setSurfaceRoads(surfaceRoads);
		
		Geometry roadGeometryUnion =  CascadedPolygonUnion.union(polygonList);
		SurfaceRoad unifiedRoads = new SurfaceRoad(0, 0, 0, 0, "", "", "", "", "", roadGeometryUnion);
		unifiedRoads.setZfromDTM(dtm);
		surfaceRoads.put("-1", unifiedRoads);
		
		System.out.println("sidewalks creating...");
		List<Sidewalk> sidewalks = new ArrayList<Sidewalk>();
		for(LineRoad road : lineRoads.values()) {
			Sidewalk sidewalk = new Sidewalk(road.getGeom(),road.getWidth(),roadGeometryUnion,dtm);
			sidewalks.add(sidewalk);
		}
		scene.setSidewalks(sidewalks);
		System.out.println("sidewalks created!");

		RuleShapeMaker ruleShapeMaker = new RuleShapeMaker();
		
		// Add intersections
		ruleShapeMaker.addIntersectionSigns(scene);
		ruleShapeMaker.addRoadSigns(scene);

		// Add punctual vegetation
		ruleShapeMaker.addVegetation(scene);	

	}
	
	/**
	 * Exports the RGE data into a OBJ file
	 * 
	 * @param scene The scene where to get the data from
	 */
	private void exportRGEData(Scene scene) {
		System.out.println("Export RGE Data");
		
		//Copying the materials file and textures
		try {
			Files.copy((new File("assets/RGE/materials.mtl")).toPath(), 
					(new File("assets/RGE/" + place + "/materials.mtl")).toPath(),
					StandardCopyOption.REPLACE_EXISTING);
			Files.copy((new File("assets/RGE/window.png")).toPath(), 
					(new File("assets/RGE/" + place + "/window.png")).toPath(),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		OBJWriter objWriter = new OBJWriter();
		
		File directory = new File("assets/RGE/" + place);
		if (! directory.exists()){ directory.mkdir(); }
		
		Coordinate centroid = scene.getCentroid();
		objWriter.exportBuilding("assets/RGE/" + place + "/buildings.obj", scene.getBuildings(), centroid.x, centroid.y);
		
		scene.getSurfaceRoads().remove("-1");
		objWriter.exportRoad("assets/RGE/" + place + "/roads.obj", scene.getSurfaceRoads(), centroid.x, centroid.y);
		
		objWriter.exportSidewalks("assets/RGE/" + place + "/sidewalks.obj", scene.getSidewalks(), scene.getCentroid().x, scene.getCentroid().y);
		
		objWriter.exportWater("assets/RGE/" + place + "/water.obj", scene.getHydrography(), centroid.x, centroid.y);

		objWriter.exportPedestrianCrossing("assets/RGE/" + place + "/pedestrianCrossing.obj", scene.getPedestrianCrossing(), centroid.x, centroid.y);
		
		scene.getDtm().toPNG("assets/RGE/" + place + "/terrain.png");
	}
	
	/**
	 * Exports the XML data
	 * 
	 * @param scene The scene where to get the data from
	 */
	private void exportXML(Scene scene) {
		System.out.println("Export XML");
		File directory = new File("assets/DrivingTasks/Projects/RGE/" + place);
		if (! directory.exists()){ directory.mkdir(); }
		
		XMLWriter xmlWriter = new XMLWriter();
		xmlWriter.log = true;
		xmlWriter.setMainDirectory("assets/DrivingTasks/Projects/RGE/" + place);
		
		xmlWriter.updateConfig("fileMainXML", "MAIN_FILE.xml");
//		xmlWriter.updateConfig("rainCoefficient", "5");
		
		
		double driverX = scene.getCentroid().x;
		double driverY = scene.getCentroid().y;
		boolean startRoad = false;
		
		if (startRoad) {
			LineRoad[] type = new LineRoad[0];
			LineRoad start = scene.getLineRoads().values().toArray(type)[scene.getLineRoads().values().size()/2];
			Coordinate startCoord = start.getGeom().getCoordinates()[0];
			driverX = startCoord.x;
			driverY = startCoord.y;
		}
		
		double driverZ = scene.getDtm().getHeightAtPoint(driverX, driverY) + 10;
		
		// Add driver
		XMLModel driver = new XMLModel("driverCar", "Models/Cars/drivingCars/CitroenC4/Car.j3o");
		driver.setMass(1000);
		driver.setTranslation(new double[]{driverX - scene.getCentroid().x, 
				driverZ,
				driverY - scene.getCentroid().y});

		driver.setScale((new double[]{0.8, 0.8, 0.8}));
		xmlWriter.addModel(driver);
		
		// Add buildings
		XMLModel buildindModel = new XMLModel("Building", "RGE/" + place + "/buildings.obj");
		xmlWriter.addModel(buildindModel);
		
		// Add roads
		XMLModel roadsModel = new XMLModel("Roads", "RGE/" + place + "/roads.obj");
		xmlWriter.addModel(roadsModel);
		
		// Add sidewalks

		XMLModel sidewalksModel = new XMLModel("Sidewalks", "RGE/" + place + "/sidewalks.obj");
		xmlWriter.addModel(sidewalksModel);
		
		// Add water
		XMLModel waterModel = new XMLModel("Water", "RGE/" + place + "/water.obj");
		xmlWriter.addModel(waterModel);
		
		// Add pedestrian crossing
		XMLModel pcModel = new XMLModel("PedestrianCrossing", "RGE/" + place + "/pedestrianCrossing.obj");
		xmlWriter.addModel(pcModel);
				
		// Add street furniture
//		int k = 0;
		for(StreetFurniture sign : scene.getStreetFurniture()) {
			XMLModel streetFurnitureModel = new XMLModel("StreetFurniture", sign.getPath());
			streetFurnitureModel.setRotation(new double[] {0, sign.getRotation()*180/Math.PI, 0});
			streetFurnitureModel.setTranslation(new double[] {sign.getCoord().x,sign.getCoord().z,sign.getCoord().y});
			xmlWriter.addModel(streetFurnitureModel);
			
//			if (++k>1000){ break; }
		}
		
//		int g = 0;
		for(PointVegetation tree : scene.getVegetation()) {
			if (tree == null) {continue;}
			XMLModel vegetationModel = new XMLModel("Tree", tree.getNature());
			vegetationModel.setTranslation(new double[]{tree.getCoord().x,tree.getCoord().z,tree.getCoord().y});
			vegetationModel.setScale(new double[]{1,1,1});
			xmlWriter.addModel(vegetationModel);
			
//			if (++g>1000){ break; }
		}

		// Add DTM
		XMLGroundModel ground = getGroundModelFromScene(scene);
		ground.setVisible(true);
		xmlWriter.addTerrain(ground);
		
		xmlWriter.createAllXml();
	}
	
	/**
	 * Allows to get the ground model 
	 * 
	 * @param scene The scene from where to extract the ground model
	 * 
	 * @return The XML gournd model
	 */
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
     		
        XMLTerrain terrain = new XMLTerrain("Terrain", "RGE/" + place + "/terrain.png", powerOfTwo);
		XMLGroundModel ground = new XMLGroundModel("Ground", "Materials/" + place + "/OrthoImage.j3m", terrain, groundScale, groundRotation, groundTranslation);
	
		return ground;
	}
	
	/**
	 * Exports the orthoImage into a texture usable by JMonkey
	 * 
	 * @param orthoImagePath the path to the input orthoimage
	 */
	private void exportOrthoImage(String orthoImagePath) {
		
		// First adding the material in the Materials list
		File directory = new File("assets/Materials/" + place);
		if (! directory.exists()){ directory.mkdir(); }
		
		String materialPath = "assets/Materials/" + place + "/OrthoImage.j3m";
		File file = new File(materialPath);
		try {
			Files.deleteIfExists(file.toPath());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try(FileWriter fw = new FileWriter(materialPath, true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw)) {						
			out.print("Material jME Logo : Common/MatDefs/Misc/Unshaded.j3md {");
			out.print("MaterialParameters {");
			out.print("ColorMap : RGE/" + place + "/ortho_50cm.png}}");	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Then doing the image manipulation
		File orthoimageFile = new File(orthoImagePath);
		BufferedImage image = null;
		try {
			image = ImageIO.read(orthoimageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Calculating the smallest power of 2 above the image dimension if it isn't a power of 2 itself using bitwise shift
		int powerOfTwo = image.getWidth();
        if (Integer.highestOneBit(powerOfTwo) != Integer.lowestOneBit(powerOfTwo)) {
        	powerOfTwo = Integer.highestOneBit(powerOfTwo << 1);
        }
		
        //Adding the padding
		BufferedImage newImage = new BufferedImage(powerOfTwo, powerOfTwo, image.getType());
		WritableRaster output = newImage.getRaster();
		
		int[] blackPixel = new int[] {0,0,0,0};
		for (int i = 0; i < powerOfTwo; i++) {
			for (int j = 0; j < powerOfTwo; j++) {
				output.setPixel(i, j, blackPixel);
			}
		}
		
		output.setDataElements(0, 0, image.getRaster());

		// Flip the image vertically
	    for(int row = 0; row < powerOfTwo/2; row++){
	            for(int col = 0; col < powerOfTwo; col++){
	                int[] pixel = output.getPixel(col, row, new int[4]);
	                output.setPixel(col, row, output.getPixel(col,  powerOfTwo - row - 1, new int[4]));
	                output.setPixel(col,  powerOfTwo - row - 1, pixel);
	            }
	        }
		
		// Outputing the image
		File outputfile = new File("assets/RGE/" + place + "/ortho_50cm.png");
		try {
			Files.deleteIfExists(outputfile.toPath());
			ImageIO.write(newImage, "png", outputfile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
}
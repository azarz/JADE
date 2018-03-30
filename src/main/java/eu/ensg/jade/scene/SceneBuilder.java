package eu.ensg.jade.scene;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.spatial.Intersects;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

import eu.ensg.jade.geometricObject.Road;
import eu.ensg.jade.input.InputRGE;
import eu.ensg.jade.input.ReaderContext;
import eu.ensg.jade.input.ReaderFactory;
import eu.ensg.jade.input.ReaderFactory.READER_METHOD;
import eu.ensg.jade.output.OBJWriter;
import eu.ensg.jade.output.XMLWriter;
import eu.ensg.jade.rules.Rule;
import eu.ensg.jade.semantic.Building;
import eu.ensg.jade.semantic.DTM;
import eu.ensg.jade.semantic.LineRoad;
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
	
	public static void main(String[] args) {
		String buildingLayer = "src/test/resources/RGE/BD_TOPO/BATI_INDIFFERENCIE.SHP";
		String roadLayer = "src/test/resources/RGE/BD_TOPO/ROUTE.SHP";
		//String roadLayer = "src/test/resources/inputTest/openShpTestLinearRoad.shp";
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
		String getCapabilities = "http://localhost:8080/geoserver/wfs?REQUEST=GetCapabilities";

		Map<String, String> connectionParameters = new HashMap<String, String>();
		connectionParameters.put("WFSDataStoreFactory:GET_CAPABILITIES_URL", getCapabilities );
		
		// Step 2 - connection	
		DataStore data = DataStoreFinder.getDataStore( connectionParameters );
		
		// Step 3 - discovery
		String typeNames[] = data.getTypeNames();
		String typeName = typeNames[0];
		SimpleFeatureType schema = data.getSchema( typeName );

		// Step 4 - target
		FeatureSource<SimpleFeatureType, SimpleFeature> source = data.getFeatureSource( typeName );
		System.out.println( "Metadata Bounds:"+ source.getBounds() );

		// Step 5 - query
//		String geomName = schema.getDefaultGeometry().getLocalName();
//		Envelope bbox = new Envelope( -100.0, -70, 25, 40 );
//
//		FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2( GeoTools.getDefaultHints() );
//		Object polygon = JTS.toGeometry( bbox );
//		Intersects filter = ff.intersects( ff.property( geomName ), ff.literal( polygon ) );
//
//		Query query = new DefaultQuery( typeName, filter, new String[]{ geomName } );
//		FeatureCollection<SimpleFeatureType, SimpleFeature> features = source.getFeatures( query );
//
//		ReferencedEnvelope bounds = new ReferencedEnvelope();
//		Iterator<SimpleFeature> iterator = ((List<Building>) features).iterator();
//		try {
//		    while( iterator.hasNext() ){
//		        Feature feature = (Feature) iterator.next();
//		    bounds.include( feature.getBounds() );
//		}
//		    System.out.println( "Calculated Bounds:"+ bounds );
//		}
//		finally {
//		    features.close( iterator );
//		}
		
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
		objWritter.exportWater("assets/RGE/water.obj", scene.getHydrography(), scene.getBuildingCentroid().x, scene.getBuildingCentroid().y);
		
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
		
		// TODO: add vegetation & street furniture
		Rule ruleObject = new Rule();
		ruleObject.intersectSigns(scene);
		
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

		
	}
	
	
	private void exportXML(Scene scene) {
		XMLWriter xmlWriter = new XMLWriter();
		
		xmlWriter.updateConfig("fileMainXML", "MAIN_FILE.xml");
//		xmlWritter.updateConfig("rainCoefficient", "5");
		
		// Add flat ground
//		XMLModel grassPlane = new XMLModel("grassPlane", "Scenes/grassPlane/Scene.j3o");
//		xmlWriter.addModel(grassPlane);
		
		// Add driver
		XMLModel driver = new XMLModel("driverCar", "Models/Cars/drivingCars/CitroenC4/Car.j3o");
		driver.setMass(800);
		Coordinate coord = scene.getStreetFurniture().get(0).getCoord();
		driver.setTranslation(new double[]{coord.x - 2, 60, coord.y});
		xmlWriter.addModel(driver);
		
		// Add buildings
//		XMLModel buildindModel = new XMLModel("Building", "RGE/buildings.obj");
//		xmlWriter.addModel(buildindModel);
		
		// Add roads
		XMLModel roadsModel = new XMLModel("Roads", "RGE/roads.obj");
		xmlWriter.addModel(roadsModel);
		
		// Add water
		XMLModel waterModel = new XMLModel("Water", "RGE/water.obj");
		xmlWriter.addModel(waterModel);
		
		int k = 0;
		// Add street furniture
//		for(StreetFurniture sign : scene.getStreetFurniture()) {
//			k++;
//			XMLModel streetFurnitureModel = new XMLModel("StreetFurniture", sign.getPath());
//			streetFurnitureModel.setRotation(new double[] {0, sign.getRotation()*180/Math.PI, 0});
//			streetFurnitureModel.setTranslation(new double[] {sign.getCoord().x,sign.getCoord().z,sign.getCoord().y});
//			//streetFurnitureModel.setScale(new double[] {10,10,10});
//			xmlWriter.addModel(streetFurnitureModel);
//			
//			if (k>5000){
//				break;
//			}
//		}
		
		// Add DTM
		XMLGroundModel ground = getGroundModelFromScene(scene);
//		ground.setVisible(false);
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
package eu.ensg.jade.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLModelTest {
	
	private static final double DELTA = 10e-15;
	
	/**
	 * Test constructor
	 */
	@Test
	public void testConstructor(){
		
		XMLModel model = new XMLModel("ID", "path/to/model.j3o");
		assertNotNull(model);
		
		XMLVector vector = new XMLVector(new double[]{1,1,1}, new double[]{0,0,0}, new double[]{-1,0,1});
		
		model = new XMLModel("ID", "path/to/model.j3o", vector);
		assertNotNull(model);
	}
	
	/**
	 * Test getter and setter
	 */
	@Test
	public void testGetterSetter() {
		String id = "NewID";
		String key = "key";
		String ref = "rEfErEncE";		
		double mass = 45.2;
		boolean visible = false;
		String collision = "Nope";
		XMLVector vector = new XMLVector(new double[]{1,1,1}, new double[]{0,0,0}, new double[]{-1,0,1});
		
		XMLModel model = new XMLModel("ID", "path/to/model.j3o");
		
		model.setId(id);
		assertEquals(model.getId(), id);
		
		model.setKey(key);
		assertEquals(model.getKey(), key);
		
		model.setRef(ref);
		assertEquals(model.getRef(), ref);
		
		model.setMass(mass);
		assertEquals(model.getMass(), mass, DELTA);
		
		model.setVisible(visible);
		assertEquals(model.isVisible(), visible);
		
		model.setCollision(collision);
		assertEquals(model.getCollision(), collision);
		
		model.setXmlVector(vector);
		assertEquals(model.getVector().getScale(), vector.getScale());
		assertEquals(model.getVector().getRotation(), vector.getRotation());
		assertEquals(model.getVector().getTranslation(), vector.getTranslation());
	}
	
	/**
	 * Test XML export
	 */
//	@Test
//	public void testXMLElement() {
//		XMLModel model = new XMLModel("ID", "path/to/model.j3o");
//		
//		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//		try {
//			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//			Document doc = docBuilder.newDocument();
//			
//			Element element = model.toXMLElement(doc);
//			
//			assertNotNull(element.getElementsByTagName("mass"));
//			assertNotNull(element.getElementsByTagName("visible"));
//			assertNotNull(element.getElementsByTagName("scale"));
//			assertNotNull(element.getElementsByTagName("translation"));			
//			
//			assertEquals(
//					element.getElementsByTagName("mass").item(0).getTextContent(), 
//					String.valueOf(model.getMass()));
//			
//			assertEquals(
//					element.getElementsByTagName("collisionShape").item(0).getTextContent(),
//					model.getCollision());
//			
//			assertEquals(
//					element.getElementsByTagName("scale").item(0),
//					model.getVector().toXMLElement(doc).getElementsByTagName("scale").item(0));
//			
//		} catch (ParserConfigurationException e) {
//			e.printStackTrace();
//		}
//	}

}

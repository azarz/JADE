package eu.ensg.jade.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * XMLModel is the class which hold data representing any model, as defined in a scene.xml file
 * 
 * @author JADE
 *
 */
public class XMLModel implements IXMLExport {
	
// ========================== ATTRIBUTES ===========================
	
	protected String id;
	protected String key;
	protected String ref;
	
	protected double mass;
	protected boolean visible;
	protected String collision;	
	
	protected XMLVector vector;
	
// ========================== CONSTRUCTORS =========================
	
	/**
	 * Constructor with basic informations
	 * 
	 * @param id the ID used for the model
	 * @param key the key used for the model (optional)
	 */
	public XMLModel(String id, String key) {
		this.id = id;
		this.key = key;
		this.ref = "";
		
		this.collision = "meshShape";
		this.visible = true;
		this.mass = 0;
		
		this.vector = new XMLVector();
	}
	
	/**
	 * Constructor with transformation informations
	 * 
	 * @param id the ID used for the model
	 * @param key the key used for the model (optional)
	 * @param scale the scale Vector
	 * @param rotation the rotation Vector
	 * @param translation the translation Vector
	 */
	public XMLModel(String id, String key, XMLVector vector) {
		this.id = id;
		this.key = key;
		this.ref = "";
		
		this.collision = "meshShape";
		this.visible = true;
		this.mass = 0;
		
		this.vector = vector;
	}
	
	/**
	 * Constructor with transformation informations
	 * 
	 * @param id the ID used for the model
	 * @param key the key used for the model (optional)
	 * @param scale the scale Vector
	 * @param rotation the rotation Vector
	 * @param translation the translation Vector
	 */
	public XMLModel(String id, String key, double[] scale, double[] rotation, double[] translation) {
		this.id = id;
		this.key = key;
		this.ref = "";
		
		this.collision = "meshShape";
		this.visible = true;
		this.mass = 0;
		
		this.vector = new XMLVector(scale, rotation, translation);
	}
	
// ========================== GETTERS/SETTERS ======================
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getCollision() {
		return collision;
	}

	public void setCollision(String collision) {
		this.collision = collision;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}
	
	public XMLVector getVector() {
		return vector;
	}
	
	public void setXmlVector(XMLVector vector) {
		this.vector = vector;
	}
	
	public void setScale(double[] scale) {
		this.vector.setScale(scale);
	}
	
	public void setRotation(double[] rotation) {
		this.vector.setRotation(rotation);
	}
	
	public void setTranslation(double[] translation) {
		this.vector.setTranslation(translation);
	}
	
	
// ========================== METHODS ==============================

	/**
	 * @see eu.ensg.jade.xml.XMLVector#toXMLElement(org.w3c.dom.Document)
	 */
	@Override
	public Element toXMLElement(Document doc) {
		Element model = doc.createElement("model");
		
		model.setAttribute("id", this.id);
		model.setAttribute("key", this.key);
		
		Element mass = doc.createElement("mass");
		mass.appendChild(doc.createTextNode(String.valueOf(this.mass)));
		model.appendChild(mass);
		
		Element visible = doc.createElement("visible");
		visible.appendChild(doc.createTextNode(String.valueOf(this.visible)));
		model.appendChild(visible);
		
		Element collisionShape = doc.createElement("collisionShape");
		collisionShape.appendChild(doc.createTextNode(String.valueOf(this.collision)));
		model.appendChild(collisionShape);
		
		
		Element transform = this.vector.toXMLElement(doc);
		model.appendChild(transform.getElementsByTagName("scale").item(0));
		model.appendChild(transform.getElementsByTagName("rotation").item(0));
		model.appendChild(transform.getElementsByTagName("translation").item(0));
		
		Element shadowMode = doc.createElement("shadowMode");
		shadowMode.appendChild(doc.createTextNode("Receive"));
		model.appendChild(shadowMode);
		
		return model;
	}
}

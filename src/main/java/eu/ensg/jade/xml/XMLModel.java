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
	/**
	 * The id
	 */
	protected String id;
	
	/**
	 * The key of the model
	 */
	protected String key;
	
	/**
	 * The reference to an other model
	 */
	protected String ref;
	
	/**
	 * The mass
	 */
	protected double mass;
	
	/**
	 * The friction parameter
	 */
	protected double friction;

	/**
	 * The visibility
	 */
	protected boolean visible;
	
	/**
	 * The collision string
	 */
	protected String collision;	
	
	/**
	 * The XMLVector
	 */
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
		this.friction = 1.0;
		
		this.vector = new XMLVector();
	}
	
	/**
	 * Constructor with transformation informations
	 * 
	 * @param id the ID used for the model
	 * @param key the key used for the model (optional)
	 * @param vector The vector used for the model
	 */
	public XMLModel(String id, String key, XMLVector vector) {
		this.id = id;
		this.key = key;
		this.ref = "";
		
		this.collision = "meshShape";
		this.visible = true;
		this.mass = 0;
		this.friction = 1.0;
		
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
		this.friction = 1;
		
		this.vector = new XMLVector(scale, rotation, translation);
	}
	
// ========================== GETTERS/SETTERS ======================
	
	/**
	 * Allows to access to the id
	 * 
	 * @return The id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Allows to set the id
	 * 
	 * @param id The new id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Allows to access to the key
	 * 
	 * @return The key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Allows to set the key
	 * 
	 * @param key The new key
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
	/**
	 * Allows to access to the reference
	 * 
	 * @return The reference
	 */
	public String getRef() {
		return ref;
	}

	/**
	 * Allows to set the reference
	 * 
	 * @param ref The reference
	 */
	public void setRef(String ref) {
		this.ref = ref;
	}

	/**
	 * Allows to get the collision
	 * 
	 * @return The collision
	 */
	public String getCollision() {
		return collision;
	}

	/**
	 * Allows to set the collision
	 * 
	 * @param collision The new collision
	 */
	public void setCollision(String collision) {
		this.collision = collision;
	}

	/**
	 * Allows to get the visibility
	 * 
	 * @return The visibility
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Allows to set the visibility
	 * 
	 * @param visible The new visibility
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * Allows to access to the mass
	 * 
	 * @return The mass
	 */
	public double getMass() {
		return mass;
	}

	/**
	 * Allows to set the mass
	 * 
	 * @param mass The new mass
	 */
	public void setMass(double mass) {
		this.mass = mass;
	}
	
	/**
	 * Allows to get the XML vector
	 * 
	 * @return The vector
	 */
	public XMLVector getVector() {
		return vector;
	}
	
	/**
	 * Allows to set the new vector
	 * 
	 * @param vector The new vector
	 */
	public void setXmlVector(XMLVector vector) {
		this.vector = vector;
	}
	
	/**
	 * Allows to set the scale
	 * 
	 * @param scale The new scale
	 */
	public void setScale(double[] scale) {
		this.vector.setScale(scale);
	}
	
	/**
	 * Allows to set the rotation
	 * 
	 * @param rotation The new rotation
	 */
	public void setRotation(double[] rotation) {
		this.vector.setRotation(rotation);
	}
	
	/**
	 * Allows to set the translation
	 * 
	 * @param translation The new translation
	 */
	public void setTranslation(double[] translation) {
		this.vector.setTranslation(translation);
	}
	
	/**
	 * Allows to access to the friction
	 * 
	 * @return The friction
	 */
	public double getFriction() {
		return friction;
	}

	/**
	 * Allows to set the friction
	 * 
	 * @param friction The new friction
	 */
	public void setFriction(double friction) {
		this.friction = friction;
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
		
		Element friction = doc.createElement("friction");
		friction.appendChild(doc.createTextNode(String.valueOf(this.friction)));
		model.appendChild(friction);
		
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
		shadowMode.appendChild(doc.createTextNode("CastAndReceive"));
		model.appendChild(shadowMode);
		
		return model;
	}
}

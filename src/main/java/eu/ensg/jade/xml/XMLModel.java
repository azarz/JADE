package eu.ensg.jade.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * XMLModel is the class which hold data representing any model, as defined in a scene.xml file
 * 
 * @author JADE
 *
 */
public class XMLModel extends XMLVector implements IXMLExport {
	
// ========================== ATTRIBUTES ===========================
	
	protected String id;
	protected String key;
	protected String ref;
	
	protected double mass;
	protected boolean visible;
	protected String collision;	
	
// ========================== CONSTRUCTORS =========================
	
	/**
	 * Constructor with basic informations
	 * 
	 * @param id the ID used for the model
	 * @param key the key used for the model (optional)
	 */
	public XMLModel(String id, String key) {
		super();
		this.id = id;
		this.key = key;
		this.ref = "";
		
		this.collision = "meshShape";
		this.visible = true;
		this.mass = 0;
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
		super(scale, rotation, translation);
		this.id = id;
		this.key = key;
		this.ref = "";
		
		this.collision = "meshShape";
		this.visible = true;
		this.mass = 0;
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
	
	
// ========================== METHODS ==============================

	/**
	 * @see eu.ensg.jade.xml.XMLVector#toXMLElement(org.w3c.dom.Document)
	 */
	@Override
	public Element toXMLElement(Document doc) {
		Element model = super.toXMLElement(doc);
		doc.renameNode(model, null, "model");
		
		model.setAttribute("id", this.id);
		model.setAttribute("key", this.key);
		
		model.appendChild(doc.createElement("mass").appendChild(doc.createTextNode(String.valueOf(this.mass))));
		model.appendChild(doc.createElement("visible").appendChild(doc.createTextNode(String.valueOf(this.visible))));
		model.appendChild(doc.createElement("collisionShape").appendChild(doc.createTextNode(String.valueOf(this.collision))));
		
		return model;
	}
}

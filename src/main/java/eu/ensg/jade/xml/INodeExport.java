package eu.ensg.jade.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public interface INodeExport {
	public abstract Node toNode(Document doc);
}

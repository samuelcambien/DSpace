package org.dspace.app.sfx.service;

import org.dspace.content.Item;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.IOException;

/**
 * Created by kevin on 08/11/14.
 */
public interface SFXFileReaderService {

    /**
     * Loads the SFX configuration file
     *
     * @param fileName The name of the SFX configuration file
     * @param item The item to process, from which metadata values will be taken
     *
     * @return the SFX string
     * @throws java.io.IOException
     */
    public String loadSFXFile(String fileName, Item item) throws IOException;

    /** Parses XML file and returns XML document.
     * @param fileName XML file to parse
     * @return XML document or <B>null</B> if error occurred. The error is caught and logged.
     */
    public Document parseFile(String fileName);

    /**
     * Process the item, mapping each of its metadata fields defined in the
     * configuration file to an OpenURL parameter
     *
     * @param node DOM node of the mapping pair in the XML file (field element)
     * @param item The item to process, from which metadata values will be taken
     * @return processed fields.
     * @throws IOException
     */
    public String doNodes(Node node, Item item) throws IOException;

    /**
     * Returns element node
     *
     * @param node element (it is XML tag)
     * @return Element node otherwise null
     */
    public Node getElement(Node node);

    /**
     * Is Empty text Node *
     */
    public boolean isEmptyTextNode(Node nd);

    /**
     * Returns the value of the node's attribute named <name>
     */
    public String getAttribute(Node e, String name);

    /**
     * Returns the value found in the Text node (if any) in the
     * node list that's passed in.
     */
    public String getValue(Node node);
}
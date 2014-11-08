/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.xmlui.objectmanager;

import java.util.HashMap;
import java.util.Map;

import org.dspace.app.xmlui.wing.ObjectManager;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.browse.BrowseItem;
import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.handle.HandleServiceImpl;


/**
 * The Wing ObjectManager implemented specifically for DSpace. This manager 
 * is able identify all DSpace items, communities, and collections.
 * 
 * @author Scott Phillips
 */

public class DSpaceObjectManager implements ObjectManager
{

    /**
     * Manage the given object, if this manager is unable to manage the object then false must be returned.
     * 
     * @param object
     *            The object to be managed.
     * @return The object identifiers
     */
    public boolean manageObject(Object object)
    {
    	// Check that the object is of a type we can manage.
    	return (object instanceof BrowseItem) || (object instanceof Item) || (object instanceof Collection)
			    || (object instanceof Community);
    }
	
	
    /**
     * Return the metadata URL of the supplied object, assuming 
     * it's a DSpace item, community or collection.
     * 
     */
	public String getObjectURL(Object object) throws WingException 
	{
		if (object instanceof DSpaceObject)
		{
			DSpaceObject dso = (DSpaceObject) object;
			String handle = dso.getHandle();
			
			// If the object has a handle then reference it by its handle.
			if (handle != null)
			{
				return "/metadata/handle/" + handle + "/mets.xml";
			}
			else
			{
				// No handle then reference it by an internal ID.
				if (dso instanceof Item || dso instanceof BrowseItem)
		    	{
		    		return "/metadata/internal/item/" + dso.getID() + "/mets.xml";
		    	}
		    	else if (object instanceof Collection)
		    	{
		    		return "/metadata/internal/collection/" + dso.getID() + "/mets.xml";
		    	}
		    	else if (object instanceof Community)
		    	{
		    		return "/metadata/internal/community/" + dso.getID() + "/mets.xml";
		    	}
			}
		}
		
		return null;
	}
	
	/**
	 * Return a pretty specific string giving a hint to the theme as to what
	 * type of DSpace object is being referenced.
	 */
	public String getObjectType(Object object)
	{
		if (object instanceof Item || object instanceof BrowseItem)
    	{
    		return "DSpace Item";
    	}
    	else if (object instanceof Collection)
    	{
    		return "DSpace Collection";
    	}
    	else if (object instanceof Community)
    	{
    		return "DSpace Community";
    	}
			
		return null;
	}

    /**
     * Return a globally unique identifier for the repository. For dspace, we
     * use the handle prefix.
     */
	public String getRepositoryIdentifier(Object object) throws WingException
	{
		return HandleServiceImpl.getPrefix();
	}
	
	/**
	 * Return the metadata URL for this repository.
	 */
	public String getRepositoryURL(Object object) throws WingException
	{
		String handlePrefix = HandleServiceImpl.getPrefix();
		return "/metadata/internal/repository/"+handlePrefix +"/mets.xml";
	}
	
	/**
	 * For the DSpace implementation we just return a hash of one entry which contains
	 * a reference to this repository's metadata.
	 */
	public Map<String,String> getAllManagedRepositories() throws WingException
	{
		String handlePrefix = HandleServiceImpl.getPrefix();
		
		Map<String,String> allRepositories = new HashMap<String,String>();
		allRepositories.put(handlePrefix, "/metadata/internal/repository/"+handlePrefix +"/mets.xml");
		
		return allRepositories;
	}
}

package org.dspace.content.dao;

import org.dspace.content.DSpaceObject;
import org.dspace.core.Context;
import org.dspace.core.GenericDAO;

import java.sql.SQLException;
import java.util.UUID;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 3/06/14
 * Time: 13:12
 */
public interface DSpaceObjectDAO<T extends DSpaceObject> extends GenericDAO<T> {
}

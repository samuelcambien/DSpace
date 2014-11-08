package org.dspace.content.dao;

import org.dspace.content.DSpaceObject;
import org.dspace.core.Context;

import java.sql.SQLException;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 28/11/14
 * Time: 11:27
 */
public interface DSpaceObjectLegacySupportDAO<T extends DSpaceObject> extends DSpaceObjectDAO<T> {

    public T findByLegacyId(Context context, int legacyId, Class<T> clazz) throws SQLException;
}

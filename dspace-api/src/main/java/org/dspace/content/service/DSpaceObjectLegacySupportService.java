package org.dspace.content.service;

import org.dspace.content.DSpaceObject;
import org.dspace.core.Context;

import java.sql.SQLException;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 6/11/14
 * Time: 12:42
 */
public interface DSpaceObjectLegacySupportService<T extends DSpaceObject> {

    public T findByIdOrLegacyId(Context context, String id) throws SQLException;


    /**
     * Generic find for when the precise type of a DSO is not known, just the
     * a pair of type number and database ID.
     *
     * @param context - the context
     * @param id - the legacy id within table of type'd objects
     * @return the object found, or null if it does not exist.
     * @throws java.sql.SQLException only upon failure accessing the database.
     */
    public T findByLegacyId(Context context, int id) throws SQLException;
}

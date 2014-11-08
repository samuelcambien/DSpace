package org.dspace.identifier.dao;

import org.dspace.content.DSpaceObject;
import org.dspace.core.Context;
import org.dspace.core.GenericDAO;
import org.dspace.identifier.DOI;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by kevin on 01/05/14.
 */
public interface DOIDAO extends GenericDAO<DOI>
{
    public DOI findByDoi(Context context, String doi) throws SQLException;

    public DOI findDOIByDSpaceObject(Context context, DSpaceObject dso, List<Integer> statusToExclude) throws SQLException;

    public List<DOI> findByStatus(Context context, List<Integer> statuses) throws SQLException;

    public DOI findDOIByDSpaceObject(Context context, DSpaceObject dso) throws SQLException;
}

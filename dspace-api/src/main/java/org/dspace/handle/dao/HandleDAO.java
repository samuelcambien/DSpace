package org.dspace.handle.dao;

import org.dspace.content.DSpaceObject;
import org.dspace.core.Context;
import org.dspace.core.GenericDAO;
import org.dspace.handle.Handle;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: kevin
 * Date: 01/04/14
 * Time: 08:48
 * To change this template use File | Settings | File Templates.
 */
public interface HandleDAO extends GenericDAO<Handle> {

    public List<Handle> getHandlesByDSpaceObject(Context context, DSpaceObject dso) throws SQLException;

    public Handle findByHandle(Context context, String handle)throws SQLException;

    public List<Handle> findByPrefix(Context context, String prefix) throws SQLException;

    public long countHandlesByPrefix(Context context, String prefix) throws SQLException;

    int updateHandlesWithNewPrefix(Context context, String newPrefix, String oldPrefix) throws SQLException;
}

package org.dspace.app.requestitem.dao;

import org.dspace.app.requestitem.RequestItem;
import org.dspace.core.Context;
import org.dspace.core.GenericDAO;

import java.sql.SQLException;

/**
 * Created by kevin on 29/10/14.
 */
public interface RequestItemDAO extends GenericDAO<RequestItem> {

    public RequestItem findByToken(Context context, String token) throws SQLException;
}

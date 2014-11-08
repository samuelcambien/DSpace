package org.dspace.xmlworkflow.storedcomponents.service;

import org.dspace.content.Collection;
import org.dspace.core.Context;
import org.dspace.eperson.Group;
import org.dspace.xmlworkflow.storedcomponents.CollectionRole;

import java.sql.SQLException;
import java.util.List;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 4/11/14
 * Time: 08:32
 */
public interface CollectionRoleService {

    public CollectionRole find(Context context, int id) throws SQLException;

    public CollectionRole find(Context context, Collection collection, String role) throws SQLException;

    public List<CollectionRole> findByCollection(Context context, Collection collection) throws SQLException;

    public CollectionRole create(Context context, Collection collection, String roleId, Group group) throws SQLException;

    public void update(Context context, CollectionRole collectionRole) throws SQLException;

    public void delete(Context context, CollectionRole collectionRole) throws SQLException;

    public void deleteByCollection(Context context, Collection collection) throws SQLException;
}

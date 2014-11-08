package org.dspace.xmlworkflow.storedcomponents;

import org.dspace.content.Collection;
import org.dspace.core.Context;
import org.dspace.eperson.Group;
import org.dspace.xmlworkflow.storedcomponents.dao.CollectionRoleDAO;
import org.dspace.xmlworkflow.storedcomponents.service.CollectionRoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.List;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 4/11/14
 * Time: 08:32
 */
public class CollectionRoleServiceImpl implements CollectionRoleService {

    @Autowired(required = true)
    protected CollectionRoleDAO collectionRoleDAO;



    @Override
    public CollectionRole find(Context context, int id) throws SQLException {
        return collectionRoleDAO.findByID(context, CollectionRole.class, id);
    }

    @Override
    public CollectionRole find(Context context, Collection collection, String role) throws SQLException {
        return collectionRoleDAO.findByCollectionAndRole(context, collection, role);
    }

    @Override
    public List<CollectionRole> findByCollection(Context context, Collection collection) throws SQLException {
        return collectionRoleDAO.findByCollection(context, collection);
    }

    @Override
    public CollectionRole create(Context context, Collection collection, String roleId, Group group) throws SQLException {
        CollectionRole collectionRole = collectionRoleDAO.create(context, new CollectionRole());
        collectionRole.setCollection(collection);
        collectionRole.setRoleId(roleId);
        collectionRole.setGroup(group);
        update(context, collectionRole);
        return collectionRole;
    }

    @Override
    public void update(Context context, CollectionRole collectionRole) throws SQLException {
        collectionRoleDAO.save(context, collectionRole);

    }

    @Override
    public void delete(Context context, CollectionRole collectionRole) throws SQLException {
        collectionRoleDAO.delete(context, collectionRole);
    }

    @Override
    public void deleteByCollection(Context context, Collection collection) throws SQLException {
        collectionRoleDAO.deleteByCollection(context, collection);
    }
}

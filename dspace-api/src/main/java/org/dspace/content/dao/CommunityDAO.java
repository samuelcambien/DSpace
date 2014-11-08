package org.dspace.content.dao;

import org.dspace.content.Community;
import org.dspace.content.MetadataField;
import org.dspace.core.Context;
import org.dspace.eperson.EPerson;
import org.dspace.eperson.Group;

import java.sql.SQLException;
import java.util.List;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 14/03/14
 * Time: 15:13
 */
public interface CommunityDAO extends DSpaceObjectDAO<Community> {

    public List<Community> findAll(Context context, MetadataField sortField) throws SQLException;

    public Community findByAdminGroup(Context context, Group group) throws SQLException;

    public List<Community> findAllNoParent(Context context, MetadataField sortField) throws SQLException;

    public List<Community> findAuthorized(Context context, EPerson ePerson, List<Integer> actions) throws SQLException;

    public List<Community> findAuthorizedByGroup(Context context, EPerson currentUser, List<Integer> actions) throws SQLException;
}

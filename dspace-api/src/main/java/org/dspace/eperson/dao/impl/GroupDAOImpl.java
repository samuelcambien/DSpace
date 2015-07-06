package org.dspace.eperson.dao.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.dspace.content.MetadataField;
import org.dspace.core.Context;
import org.dspace.core.AbstractHibernateDSODAO;
import org.dspace.eperson.EPerson;
import org.dspace.eperson.Group;
import org.dspace.eperson.dao.GroupDAO;
import org.hibernate.Query;

import java.sql.SQLException;
import java.util.*;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 31/03/14
 * Time: 12:03
 */
public class GroupDAOImpl extends AbstractHibernateDSODAO<Group> implements GroupDAO {

    @Override
    public Group findByMetadataField(Context context, String searchValue, MetadataField metadataField) throws SQLException
    {
        StringBuilder queryBuilder = new StringBuilder();
        String groupTableName = "g";
        queryBuilder.append("SELECT ").append(groupTableName).append(" FROM Group as ").append(groupTableName);

        addMetadataLeftJoin(queryBuilder, groupTableName, Collections.singletonList(metadataField));
        addMetadataValueWhereQuery(queryBuilder, Collections.singletonList(metadataField), "=", null);

        Query query = createQuery(context, queryBuilder.toString());
        query.setParameter(metadataField.toString(), metadataField.getFieldID());
        query.setParameter("queryParam", searchValue);


        return uniqueResult(query);
    }

    @Override
    public List<Group> findAll(Context context, List<MetadataField> sortFields, String sortColumn) throws SQLException
    {
        StringBuilder queryBuilder = new StringBuilder();
        String groupTableName = "g";
        queryBuilder.append("SELECT ").append(groupTableName).append(" FROM Group as ").append(groupTableName);

        addMetadataLeftJoin(queryBuilder, groupTableName, sortFields);
        addMetadataSortQuery(queryBuilder, sortFields, Collections.singletonList(sortColumn));
//
        Query query = createQuery(context, queryBuilder.toString());
        for (MetadataField sortField : sortFields) {
            query.setParameter(sortField.toString(), sortField.getFieldID());
        }
        return list(query);
    }

    @Override
    public List<Group> findByEPerson(Context context, EPerson ePerson) throws SQLException {
        Query query = createQuery(context, "from Group where (from EPerson e where e.id = :eperson_id) in elements(epeople)");
        query.setParameter("eperson_id", ePerson.getID());
        return list(query);
    }

    @Override
    public List<Group> search(Context context, String query, List<MetadataField> queryFields, int offset, int limit) throws SQLException {
        String groupTableName = "g";
        String queryString = "SELECT " + groupTableName + " FROM Group as " + groupTableName;
        String queryParam = "%"+query.toLowerCase()+"%";
        Query hibernateQuery = getSearchQuery(context, queryString, queryParam, queryFields, ListUtils.EMPTY_LIST, null);

        if(0 <= offset)
        {
            hibernateQuery.setFirstResult(offset);
        }
        if(0 <= limit)
        {
            hibernateQuery.setMaxResults(limit);
        }
        return list(hibernateQuery);
    }

    @Override
    public int searchResultCount(Context context, String query, List<MetadataField> queryFields) throws SQLException {
        String groupTableName = "g";
        String queryString = "SELECT count(*) FROM Group as " + groupTableName;
        Query hibernateQuery = getSearchQuery(context, queryString, query, queryFields, ListUtils.EMPTY_LIST, null);

        return count(hibernateQuery);
    }

    protected Query getSearchQuery(Context context, String queryString, String queryParam, List<MetadataField> queryFields, List<MetadataField> sortFields, String sortField) throws SQLException {

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(queryString);
        Set<MetadataField> metadataFieldsToJoin = new LinkedHashSet<>();
        metadataFieldsToJoin.addAll(queryFields);
        if(CollectionUtils.isNotEmpty(sortFields))
        {
            metadataFieldsToJoin.addAll(sortFields);
        }

        if(!CollectionUtils.isEmpty(metadataFieldsToJoin)) {
            addMetadataLeftJoin(queryBuilder, "g", metadataFieldsToJoin);
        }
        if(queryParam != null) {
            addMetadataValueWhereQuery(queryBuilder, queryFields, "like", "g.id like :queryParam");
        }
        if(!CollectionUtils.isEmpty(sortFields)) {
            addMetadataSortQuery(queryBuilder, sortFields, Collections.singletonList(sortField));
        }

        Query query = createQuery(context, queryBuilder.toString());
        if(StringUtils.isNotBlank(queryParam)) {
            query.setParameter("queryParam", "%"+queryParam.toLowerCase()+"%");
        }
        for (MetadataField metadataField : metadataFieldsToJoin) {
            query.setParameter(metadataField.toString(), metadataField.getFieldID());
        }

        return query;
    }

    @Override
    public void delete(Context context, Group group) throws SQLException {
        Query query = getHibernateSession(context).createSQLQuery("DELETE FROM group2group WHERE parent_id=:groupId or child_id=:groupId");
        query.setParameter("groupId", group.getID());
        query.executeUpdate();
        super.delete(context, group);
    }
}

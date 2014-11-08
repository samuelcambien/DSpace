package org.dspace.eperson.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.dspace.content.MetadataField;
import org.dspace.core.Context;
import org.dspace.core.AbstractHibernateDSODAO;
import org.dspace.eperson.EPerson;
import org.dspace.eperson.Group;
import org.dspace.eperson.dao.EPersonDAO;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

import java.sql.SQLException;
import java.util.*;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 31/03/14
 * Time: 11:16
 */
public class EPersonDAOImpl extends AbstractHibernateDSODAO<EPerson> implements EPersonDAO {

    @Override
    public EPerson findByEmail(Context context, String email) throws SQLException
    {
        // All email addresses are stored as lowercase, so ensure that the email address is lowercased for the lookup
        Criteria criteria = createCriteria(context, EPerson.class);
        criteria.add(Restrictions.eq("email", email.toLowerCase()));
        return uniqueResult(criteria);
    }


    @Override
    public EPerson findByNetid(Context context, String netid) throws SQLException
    {
        Criteria criteria = createCriteria(context, EPerson.class);
        criteria.add(Restrictions.eq("netid", netid));
        return uniqueResult(criteria);
    }

    @Override
    public List<EPerson> search(Context context, String query, List<MetadataField> queryFields, List<MetadataField> sortFields, int offset, int limit) throws SQLException
    {
        String queryString = "SELECT person FROM EPerson as person ";
        String queryParam = "%"+query.toLowerCase()+"%";
        Query hibernateQuery = getSearchQuery(context, queryString, queryParam, queryFields, sortFields, null);

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
    public int searchResultCount(Context context, String query, List<MetadataField> queryFields) throws SQLException
    {
        String queryString = "SELECT count(*) FROM EPerson as person ";
        String queryParam = "%"+query.toLowerCase()+"%";
        Query hibernateQuery = getSearchQuery(context, queryString, queryParam, queryFields, null, null);

        return count(hibernateQuery);
    }

    @Override
    public List<EPerson> findAll(Context context, MetadataField metadataSortField, String sortField) throws SQLException {
        String queryString = "SELECT person FROM EPerson as person ";
        Query query = getSearchQuery(context, queryString, null, null, Collections.singletonList(metadataSortField), sortField);
        return list(query);

    }

    @Override
    public List<EPerson> findByGroups(Context context, Set<Group> groups) throws SQLException {
        Criteria criteria = createCriteria(context, EPerson.class);
        criteria.createAlias("groups", "g");
        Disjunction orRestriction = Restrictions.or();
        for(Group group : groups)
        {
            orRestriction.add(Restrictions.eq("g.id", group.getID()));
        }
        criteria.add(orRestriction);
        return list(criteria);
    }


    @Override
    public List<EPerson> findWithPasswordWithoutDigestAlgorithm(Context context) throws SQLException {
        Criteria criteria = createCriteria(context, EPerson.class);
        criteria.add(Restrictions.and(
                Restrictions.isNotNull("password"),
                Restrictions.isNull("digestAlgorithm")
        ));
        return list(criteria);
    }

    @Override
    public List<EPerson> findNotActiveSince(Context context, Date date) throws SQLException {
        Criteria criteria = createCriteria(context, EPerson.class);
        criteria.add(Restrictions.le("lastActive", date));
        return list(criteria);
    }

    protected Query getSearchQuery(Context context, String queryString, String queryParam, List<MetadataField> queryFields, List<MetadataField> sortFields, String sortField) throws SQLException {

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(queryString);
        Set<MetadataField> metadataFieldsToJoin = new LinkedHashSet<>();
        metadataFieldsToJoin.addAll(queryFields);
        metadataFieldsToJoin.addAll(sortFields);

        addMetadataLeftJoin(queryBuilder, EPerson.class.getSimpleName().toLowerCase(), metadataFieldsToJoin);
        addMetadataValueWhereQuery(queryBuilder, queryFields, "like", " person.id like :queryParam OR person.email like :queryParam");
        addMetadataSortQuery(queryBuilder, sortFields, Collections.singletonList(sortField));

        Query query = createQuery(context, queryString);
        if(StringUtils.isNotBlank(queryParam)) {
            query.setParameter("queryParam", queryParam);
        }

        return query;
    }
}

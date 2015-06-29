package org.dspace.content.dao.impl;

import org.apache.commons.collections.ListUtils;
import org.dspace.content.Community;
import org.dspace.content.MetadataField;
import org.dspace.content.dao.CommunityDAO;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.core.AbstractHibernateDSODAO;
import org.dspace.eperson.EPerson;
import org.dspace.eperson.Group;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 14/03/14
 * Time: 14:31
 */
public class CommunityDAOImpl extends AbstractHibernateDSODAO<Community> implements CommunityDAO {

    /**
     * Get a list of all communities in the system. These are alphabetically
     * sorted by community name.
     *
     * @param context
     *            DSpace context object
     *
     * @return the communities in the system
     */
    @Override
    public List<Community> findAll(Context context, MetadataField sortField) throws SQLException
    {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT community FROM Community as community ");
        addMetadataLeftJoin(queryBuilder, Community.class.getSimpleName().toLowerCase(), Arrays.asList(sortField));
        addMetadataSortQuery(queryBuilder, Arrays.asList(sortField), ListUtils.EMPTY_LIST);


        Query query = createQuery(context, queryBuilder.toString());
        query.setParameter(sortField.toString(), sortField.getFieldID());
        return list(query);
    }

    @Override
    public Community findByAdminGroup(Context context, Group group) throws SQLException {
        Criteria criteria = createCriteria(context, Community.class);
        criteria.add(Restrictions.eq("admins", group));
        return singleResult(criteria);
    }

    @Override
    public List<Community> findAllNoParent(Context context, MetadataField sortField) throws SQLException {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" FROM Community as community ");
        addMetadataLeftJoin(queryBuilder, Community.class.getSimpleName().toLowerCase(), Arrays.asList(sortField));
        addMetadataValueWhereQuery(queryBuilder, ListUtils.EMPTY_LIST, null, " community.parentCommunities IS EMPTY");
        addMetadataSortQuery(queryBuilder, Arrays.asList(sortField), ListUtils.EMPTY_LIST);

        Query query = createQuery(context, queryBuilder.toString());
        query.setParameter(sortField.toString(), sortField.getFieldID());

        return findMany(context, query);
    }

    @Override
    public List<Community> findAuthorized(Context context, EPerson ePerson, List<Integer> actions) throws SQLException {

        /*TableRowIterator tri = DatabaseManager.query(context,
                "SELECT \n" +
                        "  * \n" +
                        "FROM \n" +
                        "  public.eperson, \n" +
                        "  public.epersongroup2eperson, \n" +
                        "  public.epersongroup, \n" +
                        "  public.community, \n" +
                        "  public.resourcepolicy\n" +
                        "WHERE \n" +
                        "  epersongroup2eperson.eperson_id = eperson.eperson_id AND\n" +
                        "  epersongroup.eperson_group_id = epersongroup2eperson.eperson_group_id AND\n" +
                        "  resourcepolicy.epersongroup_id = epersongroup.eperson_group_id AND\n" +
                        "  resourcepolicy.resource_id = community.community_id AND\n" +
                        " ( resourcepolicy.action_id = 3 OR \n" +
                        "  resourcepolicy.action_id = 11) AND \n" +
                        "  resourcepolicy.resource_type_id = 4 AND eperson.eperson_id = ?", context.getCurrentUser().getID());
        */
        Criteria criteria = createCriteria(context, Community.class);
        criteria.createAlias("resourcePolicies", "resourcePolicy");

        Disjunction actionQuery = Restrictions.or();
        for (Integer action : actions)
        {
            actionQuery.add(Restrictions.eq("resourcePolicy.actionId", action));
        }
        criteria.add(Restrictions.and(
                Restrictions.eq("resourcePolicy.resourceTypeId", Constants.COMMUNITY),
                Restrictions.eq("resourcePolicy.eperson", ePerson),
                actionQuery
        ));
        return list(criteria);
    }

    @Override
    public List<Community> findAuthorizedByGroup(Context context, EPerson ePerson, List<Integer> actions) throws SQLException {
//        "SELECT \n" +
//                "  * \n" +
//                "FROM \n" +
//                "  public.eperson, \n" +
//                "  public.epersongroup2eperson, \n" +
//                "  public.epersongroup, \n" +
//                "  public.community, \n" +
//                "  public.resourcepolicy\n" +
//                "WHERE \n" +
//                "  epersongroup2eperson.eperson_id = eperson.eperson_id AND\n" +
//                "  epersongroup.eperson_group_id = epersongroup2eperson.eperson_group_id AND\n" +
//                "  resourcepolicy.epersongroup_id = epersongroup.eperson_group_id AND\n" +
//                "  resourcepolicy.resource_id = community.community_id AND\n" +
//                " ( resourcepolicy.action_id = 3 OR \n" +
//                "  resourcepolicy.action_id = 11) AND \n" +
//                "  resourcepolicy.resource_type_id = 4 AND eperson.eperson_id = ?", context.getCurrentUser().getID());
        StringBuilder query = new StringBuilder();
        query.append("select c from Community c join c.resourcePolicies rp join rp.epersonGroup rpGroup WHERE ");
        for (int i = 0; i < actions.size(); i++) {
            Integer action = actions.get(i);
            if(i != 0)
            {
                query.append(" AND ");
            }
            query.append("rp.actionId=").append(action);
        }
        query.append(" AND rp.resourceTypeId=").append(Constants.COMMUNITY);
        query.append(" AND rp.epersonGroup.id IN (select g.id from Group g where (from EPerson e where e.id = :eperson_id) in elements(epeople))");
        Query hibernateQuery = createQuery(context, query.toString());
        hibernateQuery.setParameter("eperson_id", ePerson.getID());
        return list(hibernateQuery);
    }
}

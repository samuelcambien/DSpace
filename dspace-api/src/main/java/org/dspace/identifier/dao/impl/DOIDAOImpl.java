package org.dspace.identifier.dao.impl;

import org.dspace.content.DSpaceObject;
import org.dspace.core.Context;
import org.dspace.core.AbstractHibernateDAO;
import org.dspace.identifier.DOI;
import org.dspace.identifier.dao.DOIDAO;
import org.hibernate.Criteria;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by kevin on 01/05/14.
 */
public class DOIDAOImpl extends AbstractHibernateDAO<DOI> implements DOIDAO {
    @Override
    public DOI findByDoi(Context context, String doi) throws SQLException {
        Criteria criteria = createCriteria(context, DOI.class);
        criteria.add(Restrictions.eq("doi", doi));
        return uniqueResult(criteria);
    }

    @Override
    public DOI findDOIByDSpaceObject(Context context, DSpaceObject dso, List<Integer> statusToExclude) throws SQLException {
        //SELECT * FROM Doi WHERE resource_type_id = ? AND resource_id = ? AND ((status != DOI.TO_BE_DELETED AND status != DOI.DELETED))
        Criteria criteria = createCriteria(context, DOI.class);
        Conjunction statusConjunction = Restrictions.and();
        for (Integer status : statusToExclude) {
            statusConjunction.add(Restrictions.not(Restrictions.eq("status", status)));
        }
        criteria.add(
                Restrictions.and(
                        Restrictions.eq("dSpaceObject", dso),
                        statusConjunction

                )
        );
        return singleResult(criteria);
    }

    @Override
    public List<DOI> findByStatus(Context context, List<Integer> statuses) throws SQLException {
        Criteria criteria = createCriteria(context, DOI.class);
        Disjunction statusQuery = Restrictions.or();
        for (Integer status : statuses) {
            statusQuery.add(Restrictions.eq("status", status));
        }
        return list(criteria);
    }

    @Override
    public DOI findDOIByDSpaceObject(Context context, DSpaceObject dso) throws SQLException {
        Criteria criteria = createCriteria(context, DOI.class);
        criteria.add(
                Restrictions.and(
                        Restrictions.eq("dSpaceObject", dso)
                )
        );
        return singleResult(criteria);
    }
}

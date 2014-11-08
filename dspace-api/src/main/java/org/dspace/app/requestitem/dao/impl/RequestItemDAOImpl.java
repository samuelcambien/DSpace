package org.dspace.app.requestitem.dao.impl;

import org.dspace.app.requestitem.RequestItem;
import org.dspace.app.requestitem.dao.RequestItemDAO;
import org.dspace.core.Context;
import org.dspace.core.AbstractHibernateDAO;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.sql.SQLException;

/**
 * Created by kevin on 29/10/14.
 */
public class RequestItemDAOImpl extends AbstractHibernateDAO<RequestItem> implements RequestItemDAO
{
    @Override
    public RequestItem findByToken(Context context, String token) throws SQLException {
        Criteria criteria = createCriteria(context, RequestItem.class);
        criteria.add(Restrictions.eq("token", token));
        return uniqueResult(criteria);
    }


}

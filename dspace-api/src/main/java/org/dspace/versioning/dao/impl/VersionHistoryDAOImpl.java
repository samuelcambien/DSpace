/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.versioning.dao.impl;

import org.dspace.content.Item;
import org.dspace.core.Context;
import org.dspace.core.AbstractHibernateDAO;
import org.dspace.versioning.VersionHistory;
import org.dspace.versioning.dao.VersionHistoryDAO;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.sql.SQLException;

/**
 *
 *
 * @author Fabio Bolognesi (fabio at atmire dot com)
 * @author Mark Diggory (markd at atmire dot com)
 * @author Ben Bosman (ben at atmire dot com)
 */
public class VersionHistoryDAOImpl extends AbstractHibernateDAO<VersionHistory> implements VersionHistoryDAO {

    @Override
    public VersionHistory findByItem(Context context, Item item) throws SQLException {
        Criteria criteria = createCriteria(context, VersionHistory.class);
        criteria.createAlias("versions", "v");
        criteria.add(Restrictions.eq("v.item", item));
        return singleResult(criteria);
    }
}

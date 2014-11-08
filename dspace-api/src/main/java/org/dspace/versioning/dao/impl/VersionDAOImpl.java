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
import org.dspace.versioning.Version;
import org.dspace.versioning.dao.VersionDAO;
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
public class VersionDAOImpl extends AbstractHibernateDAO<Version> implements VersionDAO
{

    @Override
    public Version findByItem(Context context, Item item) throws SQLException {
        Criteria criteria = createCriteria(context, Version.class);
        criteria.add(Restrictions.eq("item", item));
        return uniqueResult(criteria);
    }
}

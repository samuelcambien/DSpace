package org.dspace.storage.rdbms.hibernate.postgres;

import org.hibernate.dialect.PostgreSQL9Dialect;
import org.hibernate.metamodel.spi.TypeContributions;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.PostgresUUIDType;

import java.sql.Types;

/**
 * UUID's are not supported by default in hibernate due to differences in the database in order to fix this a custom sql dialect is needed.
 * Source: https://forum.hibernate.org/viewtopic.php?f=1&t=1014157
 *
 * User: kevin (kevin at atmire.com)
 * Date: 25/06/15
 * Time: 11:53
 */
public class DSpacePostgreSQL9Dialect extends PostgreSQL9Dialect
{
    @Override
    public void contributeTypes(final TypeContributions typeContributions, final ServiceRegistry serviceRegistry) {
        super.contributeTypes(typeContributions, serviceRegistry);
        typeContributions.contributeType(new InternalPostgresUUIDType());
    }

    protected static class InternalPostgresUUIDType extends PostgresUUIDType {

        @Override
        protected boolean registerUnderJavaType() {
            return true;
        }
    }
}

package org.dspace.storage.rdbms.migration;

import org.dspace.storage.rdbms.MigrationUtils;
import org.flywaydb.core.api.migration.MigrationChecksumProvider;
import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 6/03/15
 * Time: 08:39
 */
public class V6_0_2015_03_06__Dso_Uuid_Migration implements JdbcMigration, MigrationChecksumProvider {

    private int checksum = -1;


    @Override
    public void migrate(Connection connection) throws Exception {
        checksum += MigrationUtils.dropDBConstraint(connection, "eperson", "eperson_id", "pkey");
        checksum += MigrationUtils.dropDBConstraint(connection, "epersongroup", "eperson_group_id", "pkey");
        checksum += MigrationUtils.dropDBConstraint(connection, "community", "community_id", "pkey");
        checksum += MigrationUtils.dropDBConstraint(connection, "collection", "collection_id", "pkey");
        checksum += MigrationUtils.dropDBConstraint(connection, "item", "item_id", "pkey");
        checksum += MigrationUtils.dropDBConstraint(connection, "bundle", "bundle_id", "pkey");
        checksum += MigrationUtils.dropDBConstraint(connection, "bitstream", "bitstream_id", "pkey");
    }

    @Override
    public Integer getChecksum() {
        return checksum;
    }
}

package org.dspace.content.dao;

import org.dspace.content.MetadataSchema;
import org.dspace.core.Context;
import org.dspace.core.GenericDAO;

import java.sql.SQLException;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 21/03/14
 * Time: 11:41
 */
public interface MetadataSchemaDAO extends GenericDAO<MetadataSchema> {

    public MetadataSchema findByNamespace(Context context, String namespace) throws SQLException;

    public boolean uniqueNamespace(Context context, int metadataSchemaId, String namespace) throws SQLException;

    public boolean uniqueShortName(Context context, int metadataSchemaId, String name) throws SQLException;

    public MetadataSchema find(Context context, String shortName) throws SQLException;
}

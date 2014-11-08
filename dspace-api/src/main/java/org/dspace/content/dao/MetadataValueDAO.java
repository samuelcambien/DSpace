package org.dspace.content.dao;

import org.dspace.content.MetadataField;
import org.dspace.content.MetadataValue;
import org.dspace.core.Context;
import org.dspace.core.GenericDAO;

import java.sql.SQLException;
import java.util.List;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 21/03/14
 * Time: 10:33
 */
public interface MetadataValueDAO extends GenericDAO<MetadataValue> {

    public List<MetadataValue> findByField(Context context, MetadataField fieldId) throws SQLException;

    public List<MetadataValue> findByValueLike(Context context, String value) throws SQLException;

    public void deleteByMetadataField(Context context, MetadataField metadataField) throws SQLException;
}

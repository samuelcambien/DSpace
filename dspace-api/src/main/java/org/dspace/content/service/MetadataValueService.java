package org.dspace.content.service;

import org.dspace.authorize.AuthorizeException;
import org.dspace.content.DSpaceObject;
import org.dspace.content.MetadataField;
import org.dspace.content.MetadataValue;
import org.dspace.core.Context;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 23/10/14
 * Time: 15:19
 */
public interface MetadataValueService {

    /**
     * Creates a new metadata value.
     *
     * @param context
     *            DSpace context object
     * @throws java.sql.SQLException
     */
    public MetadataValue create(Context context, DSpaceObject dso, MetadataField metadataField) throws SQLException;

    /**
     * Retrieves the metadata value from the database.
     *
     * @param context dspace context
     * @param valueId database key id of value
     * @return recalled metadata value
     * @throws java.io.IOException
     * @throws SQLException
     */
    public MetadataValue find(Context context, int valueId)
            throws IOException, SQLException;


    /**
     * Retrieves the metadata values for a given field from the database.
     *
     * @param context dspace context
     * @param metadataField metadata field whose values to look for
     * @return a collection of metadata values
     * @throws IOException
     * @throws SQLException
     */
    public List<MetadataValue> findByField(Context context, MetadataField metadataField)
            throws IOException, SQLException;

    /**
     * Update the metadata value in the database.
     *
     * @param context dspace context
     * @throws SQLException
     */
    public void update(Context context, MetadataValue metadataValue) throws SQLException;

    public void update(Context context, MetadataValue metadataValue, boolean modifyParentObject) throws SQLException, AuthorizeException;

    /**
     * Delete the metadata field.
     *
     * @param context dspace context
     * @throws SQLException
     */
    public void delete(Context context, MetadataValue metadataValue) throws SQLException;

    public List<MetadataValue> findByValueLike(Context context, String value) throws SQLException;

    public void deleteByMetadataField(Context context, MetadataField metadataField) throws SQLException;
}

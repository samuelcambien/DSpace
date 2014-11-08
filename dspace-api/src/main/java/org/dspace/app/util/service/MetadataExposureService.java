package org.dspace.app.util.service;

import org.dspace.core.Context;

import java.sql.SQLException;

/**
 * Created by kevin on 08/11/14.
 */
public interface MetadataExposureService {

    /**
     * Returns whether the given metadata field should be exposed (visible). The metadata field is in the DSpace's DC notation: schema.element.qualifier
     *
     * @param context DSpace context
     * @param schema metadata field schema (namespace), e.g. "dc"
     * @param element metadata field element
     * @param qualifier metadata field qualifier
     *
     * @return true (hidden) or false (exposed)
     * @throws java.sql.SQLException
     */
    public boolean isHidden(Context context, String schema, String element, String qualifier)
            throws SQLException;
}

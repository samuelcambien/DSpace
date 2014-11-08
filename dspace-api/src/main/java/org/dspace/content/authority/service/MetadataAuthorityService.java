package org.dspace.content.authority.service;

import org.dspace.content.MetadataField;

import java.util.List;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 24/10/14
 * Time: 10:48
 */
public interface MetadataAuthorityService {

    /** Predicate - is field authority-controlled? */
    public boolean isAuthorityControlled(MetadataField metadataField);

    /** Predicate - is field authority-controlled? */
    public boolean isAuthorityControlled(String fieldKey);

    /** Predicate - is authority value required for field? */
    public boolean isAuthorityRequired(MetadataField metadataField);

    /** Predicate - is authority value required for field? */
    public boolean isAuthorityRequired(String fieldKey);


    /**
     * Construct a single key from the tuple of schema/element/qualifier
     * that describes a metadata field.  Punt to the function we use for
     * submission UI input forms, for now.
     */
    public String makeFieldKey(MetadataField metadataField);

    /**
     * Construct a single key from the tuple of schema/element/qualifier
     * that describes a metadata field.  Punt to the function we use for
     * submission UI input forms, for now.
     */
    public String makeFieldKey(String schema, String element, String qualifier);

    /**
     * Give the minimal level of confidence required to consider valid an authority value
     * for the given metadata.
     * @return the minimal valid level of confidence for the given metadata
     */
    public int getMinConfidence(MetadataField metadataField);

    /**
     * Return the list of metadata field with authority control. The strings
     * are in the form <code>schema.element[.qualifier]</code>
     *
     * @return the list of metadata field with authority control
     */
    public List<String> getAuthorityMetadata();
}

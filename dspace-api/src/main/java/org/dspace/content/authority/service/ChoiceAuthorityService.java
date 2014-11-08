package org.dspace.content.authority.service;

import org.dspace.content.Collection;
import org.dspace.content.MetadataValue;
import org.dspace.content.authority.Choices;

import java.util.List;

/**
 * Created by kevin on 06/12/14.
 */
public interface ChoiceAuthorityService
{

    /**
     *  Wrapper that calls getMatches method of the plugin corresponding to
     *  the metadata field defined by schema,element,qualifier.
     *
     * @see org.dspace.content.authority.ChoiceAuthority#getMatches(String, String, int, int, int, String)
     * @param schema schema of metadata field
     * @param element element of metadata field
     * @param qualifier qualifier of metadata field
     * @param query user's value to match
     * @param collection database ID of Collection for context (owner of Item)
     * @param start choice at which to start, 0 is first.
     * @param limit maximum number of choices to return, 0 for no limit.
     * @param locale explicit localization key if available, or null
     * @return a Choices object (never null).
     */
    public Choices getMatches(String schema, String element, String qualifier,
                              String query, Collection collection, int start, int limit, String locale);
    /**
     *  Wrapper calls getMatches method of the plugin corresponding to
     *  the metadata field defined by single field key.
     *
     * @see org.dspace.content.authority.ChoiceAuthority#getMatches(String, String, int, int, int, String)
     * @param fieldKey single string identifying metadata field
     * @param query user's value to match
     * @param collection database ID of Collection for context (owner of Item)
     * @param start choice at which to start, 0 is first.
     * @param limit maximum number of choices to return, 0 for no limit.
     * @param locale explicit localization key if available, or null
     * @return a Choices object (never null).
     */
    public Choices getMatches(String fieldKey, String query, Collection collection,
                              int start, int limit, String locale);

    public Choices getMatches(String fieldKey, String query, Collection collection, int start, int limit, String locale, boolean externalInput);

    /**
     *  Wrapper that calls getBestMatch method of the plugin corresponding to
     *  the metadata field defined by single field key.
     *
     * @see org.dspace.content.authority.ChoiceAuthority#getBestMatch(String, String, int, String)
     * @param fieldKey single string identifying metadata field
     * @param query user's value to match
     * @param collection database ID of Collection for context (owner of Item)
     * @param locale explicit localization key if available, or null
     * @return a Choices object (never null) with 1 or 0 values.
     */
    public Choices getBestMatch(String fieldKey, String query, Collection collection,
                                String locale);

    /**
     *  Wrapper that calls getLabel method of the plugin corresponding to
     *  the metadata field defined by schema,element,qualifier.
     */
    public String getLabel(MetadataValue metadataValue, String locale);

    /**
     *  Wrapper that calls getLabel method of the plugin corresponding to
     *  the metadata field defined by single field key.
     */
    public String getLabel(String fieldKey, String authKey, String locale);

    /**
     * Predicate, is there a Choices configuration of any kind for the
     * given metadata field?
     * @return true if choices are configured for this field.
     */
    public boolean isChoicesConfigured(String fieldKey);

    /**
     * Get the presentation keyword (should be "lookup", "select" or "suggest", but this
     * is an informal convention so it can be easily extended) for this field.
     *
     * @return configured presentation type for this field, or null if none found
     */
    public String getPresentation(String fieldKey);

    /**
     * Get the configured "closed" value for this field.
     *
     * @return true if choices are closed for this field.
     */
    public boolean isClosed(String fieldKey);

    /**
     * Wrapper to call plugin's getVariants().
     */
    public List<String> getVariants(MetadataValue metadataValue);
}
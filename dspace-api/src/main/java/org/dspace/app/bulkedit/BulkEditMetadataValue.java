package org.dspace.app.bulkedit;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 5/11/14
 * Time: 13:38
 */
public class BulkEditMetadataValue {

    private String schema;
    private String element;
    private String qualifier;
    private String language;
    private String value;
    private String authority;
    private int confidence;

    public BulkEditMetadataValue() {
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }

    public String getSchema() {
        return schema;
    }

    public String getElement() {
        return element;
    }

    public String getQualifier() {
        return qualifier;
    }

    public String getLanguage() {
        return language;
    }

    public String getValue() {
        return value;
    }

    public String getAuthority() {
        return authority;
    }

    public int getConfidence() {
        return confidence;
    }
}

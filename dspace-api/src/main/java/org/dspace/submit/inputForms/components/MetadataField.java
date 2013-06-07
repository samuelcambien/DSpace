package org.dspace.submit.inputForms.components;

import org.apache.commons.lang.StringUtils;
import org.dspace.content.DCValue;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 30/05/13
 * Time: 10:41
 */
public class MetadataField {
    private String schema;
    private String element;
    private String qualifier;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MetadataField that = (MetadataField) o;

        return StringUtils.equals(element, that.getElement()) && StringUtils.equals(qualifier, that.getQualifier()) && StringUtils.equals(schema, that.getSchema());
    }

    @Override
    public int hashCode() {
        int result = schema.hashCode();
        result = 31 * result + element.hashCode();
        result = 31 * result + (qualifier != null ? qualifier.hashCode() : 0);
        return result;
    }

    public String getSchema() {
        return schema;
    }

    public MetadataField(DCValue value) {
        this.schema = value.schema;
        this.element = value.element;
        this.qualifier = value.qualifier;
    }

    public MetadataField() {
    }

    public MetadataField(String schema, String element, String qualifier) {
        this.schema = schema;
        this.element = element;
        this.qualifier = qualifier;
    }

    public MetadataField(String schema, String element) {
        this.schema = schema;
        this.element = element;
        this.qualifier = null;
    }

    public void setSchema(String schema) {
        this.schema = schema;

    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }
}

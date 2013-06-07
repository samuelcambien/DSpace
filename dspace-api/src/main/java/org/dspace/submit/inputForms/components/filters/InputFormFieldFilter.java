package org.dspace.submit.inputForms.components.filters;

import org.dspace.app.util.SubmissionInfo;
import org.dspace.submit.inputForms.components.InputFormField;
import org.dspace.submit.inputForms.components.MetadataField;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 30/05/13
 * Time: 09:53
 */
public abstract class InputFormFieldFilter {

    private List<MetadataField> metadataField;

    public boolean isUsedBy(InputFormField inputFormField){
        return inputFormField.getMetadataField().equals(metadataField);
    }

    public abstract boolean isFieldActive(SubmissionInfo submissionInfo);


    public List<MetadataField> getMetadataField() {
        return metadataField;
    }

    @Required
    public void setMetadataField(List<MetadataField> metadataField) {
        this.metadataField = metadataField;
    }
}

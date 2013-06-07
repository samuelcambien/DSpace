package org.dspace.submit.inputForms.components;

import org.dspace.app.util.SubmissionInfo;
import org.dspace.submit.inputForms.components.filters.InputFormFieldFilter;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 30/05/13
 * Time: 09:30
 */
public class InputFormPage {

    private List<InputFormField> metadataFields;
    private List<InputFormFieldFilter> metadataFilters;

    public List<InputFormField> getMetadataFields(SubmissionInfo submissionInfo) {
        List<InputFormField> result = new ArrayList<InputFormField>();
        for (InputFormField metadataField : metadataFields) {
            if(isFieldAvailable(submissionInfo, metadataField))
            {
                result.add(metadataField);
            }
        }

        return result;
    }

    public List<InputFormField> getAllMetadataFields()
    {
        return metadataFields;
    }

    private boolean isFieldAvailable(SubmissionInfo submissionInfo, InputFormField metadataField) {
        boolean addField = true;
        for (int i = 0; i < getMetadataFilters().size(); i++) {
            InputFormFieldFilter filter = getMetadataFilters().get(i);
            if(!(filter.isUsedBy(metadataField) && filter.isFieldActive(submissionInfo))){
                addField = false;
            }

        }
        return addField;
    }

    @Required
    public void setMetadataFields(List<InputFormField> metadataFields) {
        this.metadataFields = metadataFields;
    }

    public List<InputFormFieldFilter> getMetadataFilters() {
        return metadataFilters;
    }

    public void setMetadataFilters(List<InputFormFieldFilter> metadataFilters) {
        this.metadataFilters = metadataFilters;
    }
}

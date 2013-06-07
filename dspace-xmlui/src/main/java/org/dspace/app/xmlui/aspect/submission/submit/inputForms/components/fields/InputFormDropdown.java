package org.dspace.app.xmlui.aspect.submission.submit.inputForms.components.fields;

import org.apache.commons.lang.StringUtils;
import org.dspace.app.xmlui.aspect.submission.submit.inputForms.components.InputFormField;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.List;
import org.dspace.app.xmlui.wing.element.Select;
import org.dspace.content.DCValue;
import org.dspace.content.InProgressSubmission;

import java.util.LinkedHashMap;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 30/05/13
 * Time: 15:20
 */
public class InputFormDropdown extends InputFormField {

    /**
     * Render a dropdown field to the DRI document. The dropdown field consists
     * of an HTML select box.
     *
     * @param form
     *                      The form list to add the field to
     * @param dcValues
     *                      The field's pre-existing values.
     */
    @Override
    public void renderField(List form, org.dspace.submit.inputForms.components.InputFormField formField, InProgressSubmission inProgressSubmission, DCValue[] dcValues, boolean fieldInError) throws WingException {
        if(!(formField instanceof org.dspace.submit.inputForms.components.fields.InputFormDropdown)){
            throw new UnsupportedOperationException("Dropdown cannot be used to render form field of type: " + formField.getClass());
        }

        String fieldName = getFieldName(formField.getMetadataField());

        // Plain old select list.
        Select select = form.addItem().addSelect(fieldName, "submit-select");

        //Setup the select field
        setLabelAndHelp(formField, select);
        renderRequiredAndError(formField, select, fieldInError);


        if (formField.isRepeatable() || dcValues.length > 1) {
            // Use the multiple functionality from the HTML
            // widget instead of DRI's version.
            select.setMultiple();
            select.setSize(6);
        }

        if (formField.isReadonly()) {
            select.setDisabled();
        }
        renderSelectOptions((org.dspace.submit.inputForms.components.fields.InputFormDropdown) formField, select);


        // Setup the field's pre-selected values
        for (DCValue dcValue : dcValues) {
            select.setOptionSelected(dcValue.value);
        }

    }

    protected void renderSelectOptions(org.dspace.submit.inputForms.components.fields.InputFormDropdown formField, Select select) throws WingException {
        // Setup the possible options
        LinkedHashMap<String, String> pairs = (formField).getValuePairs();
        for(String value : pairs.keySet()){
            String display = pairs.get(value);
            select.addOption(value, display);
        }
    }
}

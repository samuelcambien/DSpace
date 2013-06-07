package org.dspace.app.xmlui.aspect.submission.submit.inputForms.components.fields;

import org.apache.commons.lang.StringUtils;
import org.dspace.app.xmlui.aspect.submission.submit.inputForms.components.InputFormField;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.CheckBox;
import org.dspace.app.xmlui.wing.element.Field;
import org.dspace.app.xmlui.wing.element.List;
import org.dspace.app.xmlui.wing.element.Radio;
import org.dspace.content.DCValue;
import org.dspace.content.InProgressSubmission;

import java.util.LinkedHashMap;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 5/06/13
 * Time: 11:33
 */
public class InputFormList extends InputFormField {

    /**
     * Render a select-from-list field to the DRI document.
     * This field consists of either a series of checkboxes
     * (if repeatable) or a series of radio buttons (if not repeatable).
     * <P>
     * Note: This is NOT the same as a List element
     * (org.dspace.app.xmlui.wing.element.List).  It's just unfortunately
     * similarly named.
     *
     * @param form
     *                      The form list to add the field to
     * @param dcValues
     *                      The field's pre-existing values.
     */
    @Override
    public void renderField(List form, org.dspace.submit.inputForms.components.InputFormField formField, InProgressSubmission inProgressSubmission, DCValue[] dcValues, boolean fieldInError) throws WingException {
        Field listField;
        String fieldName = getFieldName(formField.getMetadataField());
        //if repeatable, this list of fields should be checkboxes
        if (formField.isRepeatable()) {
            listField = form.addItem().addCheckBox(fieldName);
        } else //otherwise this is a list of radio buttons
        {
            listField = form.addItem().addRadio(fieldName);
        }

        if (formField.isReadonly()) {
            listField.setDisabled();
        }

        //      Setup the field
        setLabelAndHelp(formField, listField);
        renderRequiredAndError(formField, listField, fieldInError);


        //Setup each of the possible options
        LinkedHashMap<String, String> pairs = ((org.dspace.submit.inputForms.components.fields.InputFormList)formField).getValuePairs();
        for(String value : pairs.keySet()){
            String display = pairs.get(value);

            if (listField instanceof CheckBox) {
                ((CheckBox) listField).addOption(value, display);
            } else if (listField instanceof Radio) {
                ((Radio) listField).addOption(value, display);
            }
        }

        // Setup the field's pre-selected values
        for (DCValue dcValue : dcValues) {
            if (listField instanceof CheckBox) {
                ((CheckBox) listField).setOptionSelected(dcValue.value);
            } else if (listField instanceof Radio) {
                ((Radio) listField).setOptionSelected(dcValue.value);
            }
        }


    }
}

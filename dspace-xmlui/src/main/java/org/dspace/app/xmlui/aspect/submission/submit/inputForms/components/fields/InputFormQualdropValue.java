package org.dspace.app.xmlui.aspect.submission.submit.inputForms.components.fields;

import org.apache.commons.lang.StringUtils;
import org.dspace.app.xmlui.aspect.submission.submit.inputForms.components.InputFormField;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.Composite;
import org.dspace.app.xmlui.wing.element.List;
import org.dspace.app.xmlui.wing.element.Select;
import org.dspace.app.xmlui.wing.element.Text;
import org.dspace.content.DCValue;
import org.dspace.content.InProgressSubmission;

import java.util.LinkedHashMap;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 5/06/13
 * Time: 10:43
 */
public class InputFormQualdropValue extends InputFormDropdown {

    /**
     * Render a qualdrop field to the DRI document. Qualdrop fields are complicated,
     * widget wise they are composed of two fields, a select and text box field.
     * The select field selects the metadata's qualifier and the text box is the
     * value. This means that that there is not just one metadata element that is
     * represented so the confusing part is that the name can change.
     *
     * @param form
     *                      The form list to add the field to
     * @param dcValues
     *                      The field's pre-existing values.
     */
    @Override
    public void renderField(List form, org.dspace.submit.inputForms.components.InputFormField formField, InProgressSubmission inProgressSubmission, DCValue[] dcValues, boolean fieldInError) throws WingException {
        if(!(formField instanceof org.dspace.submit.inputForms.components.fields.InputFormQualdropValue)){
            throw new UnsupportedOperationException("Qualdrop cannot be used to render form field of type: " + formField.getClass());
        }


        String fieldName = getFieldName(formField.getMetadataField());


        Composite qualdrop = form.addItem().addComposite(fieldName,"submit-qualdrop");
        Select qual = qualdrop.addSelect(fieldName+"_qualifier");
        Text value = qualdrop.addText(fieldName+"_value");

        // Setup the full field.
        setLabelAndHelp(formField, qualdrop);
        renderRequiredAndError(formField, qualdrop, fieldInError);



        if (formField.isRepeatable() && !formField.isReadonly())
        {
            qualdrop.enableAddOperation();
        }
        // Update delete based upon the filtered values.
        if ((formField.isRepeatable() || dcValues.length > 1) && !formField.isReadonly())
        {
            qualdrop.enableDeleteOperation();
        }

        if (formField.isReadonly())
        {
            qualdrop.setDisabled();
            qual.setDisabled();
            value.setDisabled();
        }

        // Setup the possible options
        renderSelectOptions((org.dspace.submit.inputForms.components.fields.InputFormDropdown) formField, qual);

        // Setup the field's values
        if (formField.isRepeatable() || dcValues.length > 1)
        {
                for (DCValue dcValue : dcValues)
                {
                        qual.addInstance().setOptionSelected(dcValue.qualifier);
                        value.addInstance().setValue(dcValue.value);
                        qualdrop.addInstance().setValue(dcValue.qualifier + ":" + dcValue.value);
                }
        }
        else if (dcValues.length == 1)
        {
                qual.setOptionSelected(dcValues[0].qualifier);
                value.setValue(dcValues[0].value);
        }
    }
}

package org.dspace.app.xmlui.aspect.submission.submit.inputForms.components.fields;

import org.dspace.app.xmlui.aspect.submission.submit.inputForms.components.InputFormField;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.List;
import org.dspace.app.xmlui.wing.element.Params;
import org.dspace.app.xmlui.wing.element.Select;
import org.dspace.content.DCValue;
import org.dspace.content.InProgressSubmission;
import org.dspace.content.authority.Choice;
import org.dspace.content.authority.ChoiceAuthorityManager;
import org.dspace.content.authority.Choices;
import org.dspace.content.authority.MetadataAuthorityManager;
import org.dspace.submit.inputForms.components.MetadataField;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 5/06/13
 * Time: 12:50
 */
public class InputFormChoiceSelectField extends InputFormField {

    /**
     * Render a dropdown field for a choice-controlled input of the
     * 'select' presentation to the DRI document. The dropdown field
     * consists of an HTML select box.
     *
     * @param form      The form list to add the field to
     * @param dcValues  The field's pre-existing values.
     */
    @Override
    public void renderField(List form, org.dspace.submit.inputForms.components.InputFormField formField, InProgressSubmission inProgressSubmission, DCValue[] dcValues, boolean fieldInError) throws WingException {
        MetadataField metadataField = formField.getMetadataField();
        String fieldName = getFieldName(metadataField);
        String fieldKey = MetadataAuthorityManager.makeFieldKey(metadataField.getSchema(), metadataField.getElement(), metadataField.getQualifier());

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
        } else {
            select.setSize(1);
        }

        if (formField.isReadonly()) {
            select.setDisabled();
        }

        Choices cs = ChoiceAuthorityManager.getManager().getMatches(fieldKey, "", inProgressSubmission.getCollection().getID(), 0, 0, null);
        if (dcValues.length == 0) {
            select.addOption(true, "", "");
        }
        for (Choice c : cs.values) {
            select.addOption(c.value, c.label);
        }

        // Setup the field's pre-selected values
        for (DCValue dcValue : dcValues) {
            select.setOptionSelected(dcValue.value);
        }

    }
}

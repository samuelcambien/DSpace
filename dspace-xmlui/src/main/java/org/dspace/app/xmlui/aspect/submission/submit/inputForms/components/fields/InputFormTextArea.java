package org.dspace.app.xmlui.aspect.submission.submit.inputForms.components.fields;

import org.apache.commons.lang.StringUtils;
import org.dspace.app.xmlui.aspect.submission.submit.inputForms.components.InputFormField;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.Instance;
import org.dspace.app.xmlui.wing.element.List;
import org.dspace.app.xmlui.wing.element.TextArea;
import org.dspace.content.DCValue;
import org.dspace.content.InProgressSubmission;
import org.dspace.content.authority.ChoiceAuthorityManager;
import org.dspace.content.authority.Choices;
import org.dspace.content.authority.MetadataAuthorityManager;
import org.dspace.submit.inputForms.components.MetadataField;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 4/06/13
 * Time: 14:30
 */
public class InputFormTextArea extends InputFormField {

    /**
     * Render a Text Area field to the DRI document. The text area is a simple
     * multi row and column text field.
     *
     * @param form
     *                      The form list to add the field to
     * @param dcValues
     *                      The field's pre-existing values.
     */
    @Override
    public void renderField(List form, org.dspace.submit.inputForms.components.InputFormField formField, InProgressSubmission inProgressSubmission, DCValue[] dcValues, boolean fieldInError) throws WingException {
        MetadataField metadataField = formField.getMetadataField();
        String fieldName = getFieldName(metadataField);

        // Plain old Textarea
        TextArea textArea = form.addItem().addTextArea(fieldName, "submit-textarea");

        // Setup the text area
        String fieldKey = MetadataAuthorityManager.makeFieldKey(metadataField.getSchema(), metadataField.getElement(), metadataField.getQualifier());
        boolean isAuth = MetadataAuthorityManager.getManager().isAuthorityControlled(fieldKey);
        if (isAuth) {
            textArea.setAuthorityControlled();
            textArea.setAuthorityRequired(MetadataAuthorityManager.getManager().isAuthorityRequired(fieldKey));
        }
        if (ChoiceAuthorityManager.getManager().isChoicesConfigured(fieldKey)) {
            textArea.setChoices(fieldKey);
            textArea.setChoicesPresentation(ChoiceAuthorityManager.getManager().getPresentation(fieldKey));
            textArea.setChoicesClosed(ChoiceAuthorityManager.getManager().isClosed(fieldKey));
        }
        setLabelAndHelp(formField, textArea);
        renderRequiredAndError(formField, textArea, fieldInError);


        if (formField.isRepeatable() && !formField.isReadonly()) {
            textArea.enableAddOperation();
        }
        if ((formField.isRepeatable() || dcValues.length > 1) && !formField.isReadonly()) {
            textArea.enableDeleteOperation();
        }

        if (formField.isReadonly()) {
            textArea.setDisabled();
        }

        // Setup the field's values
        if (formField.isRepeatable() || dcValues.length > 1) {
            for (DCValue dcValue : dcValues) {
                Instance ti = textArea.addInstance();
                ti.setValue(dcValue.value);
                if (isAuth) {
                    if (dcValue.authority == null || dcValue.authority.equals("")) {
                        ti.setAuthorityValue("", "blank");
                    } else {
                        ti.setAuthorityValue(dcValue.authority, Choices.getConfidenceText(dcValue.confidence));
                    }
                }
            }
        } else if (dcValues.length == 1) {
            textArea.setValue(dcValues[0].value);
            if (isAuth) {
                if (dcValues[0].authority == null || dcValues[0].authority.equals("")) {
                    textArea.setAuthorityValue("", "blank");
                } else {
                    textArea.setAuthorityValue(dcValues[0].authority, Choices.getConfidenceText(dcValues[0].confidence));
                }
            }
        }
    }
}

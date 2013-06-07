package org.dspace.app.xmlui.aspect.submission.submit.inputForms.components.fields;

import org.apache.commons.lang.StringUtils;
import org.dspace.app.xmlui.aspect.submission.submit.inputForms.components.InputFormField;
import org.dspace.app.xmlui.wing.Message;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.Composite;
import org.dspace.app.xmlui.wing.element.Instance;
import org.dspace.app.xmlui.wing.element.List;
import org.dspace.app.xmlui.wing.element.Text;
import org.dspace.content.DCPersonName;
import org.dspace.content.DCValue;
import org.dspace.content.InProgressSubmission;
import org.dspace.content.authority.ChoiceAuthorityManager;
import org.dspace.content.authority.Choices;
import org.dspace.content.authority.MetadataAuthorityManager;
import org.dspace.submit.inputForms.components.MetadataField;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 31/05/13
 * Time: 08:27
 */
public class InputFormName extends InputFormField {

    protected static final Message T_last_name_help=
        message("xmlui.Submission.submit.DescribeStep.last_name_help");
    protected static final Message T_first_name_help=
        message("xmlui.Submission.submit.DescribeStep.first_name_help");

    /**
     * Render a Name field to the DRI document. The name field consists of two
     * text fields, one for the last name and the other for a first name (plus
     * all other names).
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
        // The name field is a composite field containing two text fields, one
        // for first name the other for last name.
        Composite fullName = form.addItem().addComposite(fieldName, "submit-name");
        Text lastName = fullName.addText(fieldName + "_last");
        Text firstName = fullName.addText(fieldName + "_first");

        // Setup the full name
        setLabelAndHelp(formField, fullName);
        renderRequiredAndError(formField, fullName, fieldInError);


        if (formField.isRepeatable() && !formField.isReadonly()) {
            fullName.enableAddOperation();
        }
        if ((formField.isRepeatable() || dcValues.length > 1) && !formField.isReadonly()) {
            fullName.enableDeleteOperation();
        }
        String fieldKey = MetadataAuthorityManager.makeFieldKey(metadataField.getSchema(), metadataField.getElement(), metadataField.getQualifier());
        boolean isAuthorityControlled = MetadataAuthorityManager.getManager().isAuthorityControlled(fieldKey);
        if (isAuthorityControlled) {
            fullName.setAuthorityControlled();
            fullName.setAuthorityRequired(MetadataAuthorityManager.getManager().isAuthorityRequired(fieldKey));
        }
        if (ChoiceAuthorityManager.getManager().isChoicesConfigured(fieldKey)) {
            fullName.setChoices(fieldKey);
            fullName.setChoicesPresentation(ChoiceAuthorityManager.getManager().getPresentation(fieldKey));
            fullName.setChoicesClosed(ChoiceAuthorityManager.getManager().isClosed(fieldKey));
        }

        // Setup the first and last name
        lastName.setLabel(T_last_name_help);
        firstName.setLabel(T_first_name_help);

        if (formField.isReadonly()) {
            lastName.setDisabled();
            firstName.setDisabled();
            fullName.setDisabled();
        }

        // Setup the field's values
        if (formField.isRepeatable() || dcValues.length > 1) {
            for (DCValue dcValue : dcValues) {
                DCPersonName dpn = new DCPersonName(dcValue.value);

                lastName.addInstance().setValue(dpn.getLastName());
                firstName.addInstance().setValue(dpn.getFirstNames());
                Instance fi = fullName.addInstance();
                fi.setValue(dcValue.value);
                if (isAuthorityControlled) {
                    if (dcValue.authority == null || dcValue.authority.equals("")) {
                        fi.setAuthorityValue("", "blank");
                    } else {
                        fi.setAuthorityValue(dcValue.authority, Choices.getConfidenceText(dcValue.confidence));
                    }
                }
            }
        } else if (dcValues.length == 1) {
            DCPersonName dpn = new DCPersonName(dcValues[0].value);

            lastName.setValue(dpn.getLastName());
            firstName.setValue(dpn.getFirstNames());
            if (isAuthorityControlled) {
                if (dcValues[0].authority == null || dcValues[0].authority.equals("")) {
                    lastName.setAuthorityValue("", "blank");
                } else {
                    lastName.setAuthorityValue(dcValues[0].authority, Choices.getConfidenceText(dcValues[0].confidence));
                }
            }
        }
    }
}

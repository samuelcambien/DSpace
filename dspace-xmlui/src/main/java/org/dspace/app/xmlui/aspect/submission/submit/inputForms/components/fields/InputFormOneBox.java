package org.dspace.app.xmlui.aspect.submission.submit.inputForms.components.fields;

import org.apache.commons.lang.StringUtils;
import org.dspace.app.xmlui.aspect.submission.submit.inputForms.components.InputFormField;
import org.dspace.app.xmlui.wing.Message;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.Instance;
import org.dspace.app.xmlui.wing.element.List;
import org.dspace.app.xmlui.wing.element.Text;
import org.dspace.content.DCValue;
import org.dspace.content.InProgressSubmission;
import org.dspace.content.authority.ChoiceAuthorityManager;
import org.dspace.content.authority.Choices;
import org.dspace.content.authority.MetadataAuthorityManager;
import org.dspace.submit.inputForms.components.MetadataField;
import org.dspace.utils.DSpace;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 30/05/13
 * Time: 14:18
 */
public class InputFormOneBox extends InputFormField {
    private static final Message T_vocabulary_link = message("xmlui.Submission.submit.DescribeStep.controlledvocabulary.link");



    @Override
    public void renderField(List form, org.dspace.submit.inputForms.components.InputFormField inputFormField, InProgressSubmission inProgressSubmission, DCValue[] dcValues, boolean fieldInError) throws WingException {
        if (!(inputFormField instanceof org.dspace.submit.inputForms.components.fields.InputFormOneBox)) {
            throw new UnsupportedOperationException("Onebox cannot be used to render form field of type: " + inputFormField.getClass());
        }
        org.dspace.submit.inputForms.components.fields.InputFormOneBox formField = (org.dspace.submit.inputForms.components.fields.InputFormOneBox) inputFormField;


        MetadataField metadataField = inputFormField.getMetadataField();
        String fieldName = getFieldName(metadataField);

        // Both onebox and twobox consist a free form text field
        // that the user may enter any value. The difference between
        // the two is that a onebox should be rendered in one column
        // as twobox should be listed in a two column format. Since this
        // decision is not something the Aspect can effect we merely place
        // as a render hint.
        org.dspace.app.xmlui.wing.element.Item item = form.addItem();
        Text text = item.addText(fieldName, "submit-text");

        if (formField.getVocabulary() != null) {
            String vocabularyUrl = new DSpace().getConfigurationService().getProperty("dspace.url");
            vocabularyUrl += "/JSON/controlled-vocabulary?vocabularyIdentifier=" + formField.getVocabulary();
            //Also hand down the field name so our summoning script knows the field the selected value is to end up in
            vocabularyUrl += "&metadataFieldName=" + fieldName;
            item.addXref("vocabulary:" + vocabularyUrl).addContent(T_vocabulary_link);
        }

        // Setup the select field

        String fieldKey = MetadataAuthorityManager.makeFieldKey(metadataField.getSchema(), metadataField.getElement(), metadataField.getQualifier());
        boolean isAuth = MetadataAuthorityManager.getManager().isAuthorityControlled(fieldKey);
        if (isAuth) {
            text.setAuthorityControlled();
            text.setAuthorityRequired(MetadataAuthorityManager.getManager().isAuthorityRequired(fieldKey));
        }
        if (ChoiceAuthorityManager.getManager().isChoicesConfigured(fieldKey)) {
            text.setChoices(fieldKey);
            text.setChoicesPresentation(ChoiceAuthorityManager.getManager().getPresentation(fieldKey));
            text.setChoicesClosed(ChoiceAuthorityManager.getManager().isClosed(fieldKey));
        }

        setLabelAndHelp(formField, text);
        renderRequiredAndError(formField, text, fieldInError);

        if (formField.isRepeatable() && !formField.isReadonly()) {
            text.enableAddOperation();
        }
        if ((formField.isRepeatable() || dcValues.length > 1) && !formField.isReadonly()) {
            text.enableDeleteOperation();
        }

        if (formField.isReadonly()) {
            text.setDisabled();
        }

        // Setup the field's values
        if (formField.isRepeatable() || dcValues.length > 1) {
            for (DCValue dcValue : dcValues) {
                Instance ti = text.addInstance();
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
            text.setValue(dcValues[0].value);
            if (isAuth) {
                if (dcValues[0].authority == null || dcValues[0].authority.equals("")) {
                    text.setAuthorityValue("", "blank");
                } else {
                    text.setAuthorityValue(dcValues[0].authority, Choices.getConfidenceText(dcValues[0].confidence));
                }
            }
        }

    }
}

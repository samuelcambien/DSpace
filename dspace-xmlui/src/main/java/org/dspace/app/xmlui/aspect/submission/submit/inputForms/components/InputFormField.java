package org.dspace.app.xmlui.aspect.submission.submit.inputForms.components;

import org.apache.commons.lang.StringUtils;
import org.dspace.app.xmlui.cocoon.AbstractDSpaceTransformer;
import org.dspace.app.xmlui.wing.Message;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.Field;
import org.dspace.app.xmlui.wing.element.List;
import org.dspace.content.DCValue;
import org.dspace.content.InProgressSubmission;
import org.dspace.content.Item;
import org.dspace.submit.inputForms.components.MetadataField;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 30/05/13
 * Time: 14:18
 */
public abstract class InputFormField extends AbstractDSpaceTransformer {

    protected static final Message T_required_field= message("xmlui.Submission.submit.DescribeStep.required_field");

    public abstract void renderField(List form, org.dspace.submit.inputForms.components.InputFormField formField, InProgressSubmission inProgressSubmission, DCValue[] dcValues, boolean isFieldInError) throws WingException;

    /**
   	 * Return the HTML / DRI field name for the given input.
   	 *
   	 * @param metadataField
   	 * @return field name as a String (e.g. dc_contributor_editor)
   	 */
   	public static String getFieldName(MetadataField metadataField)
   	{
   		String dcSchema = metadataField.getSchema();
   		String dcElement = metadataField.getElement();
   		String dcQualifier = metadataField.getQualifier();
   		if (dcQualifier != null && ! dcQualifier.equals(Item.ANY))
   		{
   			return dcSchema + "_" + dcElement + '_' + dcQualifier;
   		}
   		else
   		{
   			return dcSchema + "_" + dcElement;
   		}
   	}

    public void setLabelAndHelp(org.dspace.submit.inputForms.components.InputFormField formField, Field wingComponent) throws WingException {
        wingComponent.setLabel(message(formField.getLabelMessageKey()));
        wingComponent.setHelp(message(formField.getHintMessageKey()));
    }

    public void renderRequiredAndError(org.dspace.submit.inputForms.components.InputFormField formField, Field wingComponent, boolean fieldInError) throws WingException {
        if (formField.isRequired()) {
            wingComponent.setRequired();
        }
        if (fieldInError) {
            if (StringUtils.isNotBlank(formField.getRequiredMessageKey())) {
                wingComponent.addError(message(formField.getRequiredMessageKey()));
            } else {
                wingComponent.addError(T_required_field);
            }
        }
    }

}

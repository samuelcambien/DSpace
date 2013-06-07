package org.dspace.app.xmlui.aspect.submission.submit.inputForms.components.fields;

import org.apache.commons.lang.StringUtils;
import org.dspace.app.xmlui.aspect.submission.submit.inputForms.components.InputFormField;
import org.dspace.app.xmlui.wing.Message;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.Composite;
import org.dspace.app.xmlui.wing.element.List;
import org.dspace.app.xmlui.wing.element.Select;
import org.dspace.app.xmlui.wing.element.Text;
import org.dspace.content.DCDate;
import org.dspace.content.DCValue;
import org.dspace.content.InProgressSubmission;

import java.util.Locale;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 30/05/13
 * Time: 14:58
 */
public class InputFormDate extends InputFormField {

    protected static final Message T_year=
        message("xmlui.Submission.submit.DescribeStep.year");
    protected static final Message T_month=
        message("xmlui.Submission.submit.DescribeStep.month");
    protected static final Message T_day=
        message("xmlui.Submission.submit.DescribeStep.day");

    /**
     * Render a date field to the DRI document. The date field consists of
     * three component fields, a 4 character text field for the year, a select
     * box for the month, and a 2 character text field for the day.
     *
     * @param form
     *                      The form list to add the field to
     * @param dcValues
     *                      The field's pre-existing values.
     */
    @Override
    public void renderField(List form, org.dspace.submit.inputForms.components.InputFormField formField, InProgressSubmission inProgressSubmission, DCValue[] dcValues, boolean fieldInError) throws WingException {
        String fieldName = getFieldName(formField.getMetadataField());

        // The date field consists of three primitive fields: a text field
        // for the year, followed by a select box of the months, follewed
        // by a text box for the day.
        Composite fullDate = form.addItem().addComposite(fieldName, "submit-date");
        Text year = fullDate.addText(fieldName + "_year");
        Select month = fullDate.addSelect(fieldName + "_month");
        Text day = fullDate.addText(fieldName + "_day");

        // Set up the full field
        setLabelAndHelp(formField, fullDate);
        renderRequiredAndError(formField, fullDate, fieldInError);


        if (formField.isRepeatable() && !formField.isReadonly()) {
            fullDate.enableAddOperation();
        }
        if ((formField.isRepeatable() || dcValues.length > 1) && !formField.isReadonly()) {
            fullDate.enableDeleteOperation();
        }

        if (formField.isReadonly()) {
            year.setDisabled();
            month.setDisabled();
            day.setDisabled();
        }

        // Setup the year field
        year.setLabel(T_year);
        year.setSize(4, 4);

        // Setup the month field
        month.setLabel(T_month);
        month.addOption(0, "");
        for (int i = 1; i < 13; i++) {
            month.addOption(i, org.dspace.content.DCDate.getMonthName(i, Locale.getDefault()));
        }

        // Setup the day field
        day.setLabel(T_day);
        day.setSize(2, 2);

        // Setup the field's values
        if (formField.isRepeatable() || dcValues.length > 1) {
            for (DCValue dcValue : dcValues) {
                DCDate dcDate = new DCDate(dcValue.value);

                year.addInstance().setValue(String.valueOf(dcDate.getYear()));
                month.addInstance().setOptionSelected(dcDate.getMonth());
                day.addInstance().setValue(String.valueOf(dcDate.getDay()));
                fullDate.addInstance().setValue(dcDate.toString());
            }
        } else if (dcValues.length == 1) {
            DCDate dcDate = new DCDate(dcValues[0].value);

            year.setValue(String.valueOf(dcDate.getYear()));
            month.setOptionSelected(dcDate.getMonth());

            // Check if the day field is not specified, if so then just
            // put a blank value in instead of the weird looking -1.
            if (dcDate.getDay() == -1) {
                day.setValue("");
            } else {
                day.setValue(String.valueOf(dcDate.getDay()));
            }
        }
    }
}

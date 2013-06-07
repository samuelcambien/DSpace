package org.dspace.app.xmlui.aspect.submission.submit.inputForms.components.fields;

import org.apache.commons.lang.StringUtils;
import org.dspace.app.xmlui.aspect.submission.submit.inputForms.components.InputFormField;
import org.dspace.app.xmlui.wing.Message;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.Composite;
import org.dspace.app.xmlui.wing.element.List;
import org.dspace.app.xmlui.wing.element.Text;
import org.dspace.content.DCSeriesNumber;
import org.dspace.content.DCValue;
import org.dspace.content.InProgressSubmission;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 5/06/13
 * Time: 11:06
 */
public class InputFormSeries extends InputFormField {

    protected static final Message T_series_name=
        message("xmlui.Submission.submit.DescribeStep.series_name");
    protected static final Message T_report_no=
        message("xmlui.Submission.submit.DescribeStep.report_no");

    /**
     * Render a series field to the DRI document. The series field conists of
     * two component text fields. When interpreted each of these fields are
     * combined back together to be a single value joined together by a
     * semicolon. The primary use case is for the journal or report number
     * the left hand side is the journal and the right hand side in a
     * unique number within the journal.
     *
     * @param form
     *                      The form list to add the field to
     * @param dcValues
     *                      The field's pre-existing values.
     */
    @Override
    public void renderField(List form, org.dspace.submit.inputForms.components.InputFormField formField, InProgressSubmission inProgressSubmission, DCValue[] dcValues, boolean fieldInError) throws WingException {
        String fieldName = getFieldName(formField.getMetadataField());


        // The series field consists of two parts, a series name (text field)
        // and report or paper number (also a text field).
        Composite fullSeries = form.addItem().addComposite(fieldName, "submit-series");
        Text series = fullSeries.addText(fieldName + "_series");
        Text number = fullSeries.addText(fieldName + "_number");

        // Setup the full field.
        setLabelAndHelp(formField, fullSeries);
        renderRequiredAndError(formField, fullSeries, fieldInError);


        if (formField.isRepeatable() && !formField.isReadonly()) {
            fullSeries.enableAddOperation();
        }
        if ((formField.isRepeatable() || dcValues.length > 1) && !formField.isReadonly()) {
            fullSeries.enableDeleteOperation();
        }

        series.setLabel(T_series_name);
        number.setLabel(T_report_no);

        if (formField.isReadonly()) {
            fullSeries.setDisabled();
            series.setDisabled();
            number.setDisabled();
        }

        // Setup the field's values
        if (formField.isRepeatable() || dcValues.length > 1) {
            for (DCValue dcValue : dcValues) {
                DCSeriesNumber dcSeriesNumber = new DCSeriesNumber(dcValue.value);

                series.addInstance().setValue(dcSeriesNumber.getSeries());
                number.addInstance().setValue(dcSeriesNumber.getNumber());
                fullSeries.addInstance().setValue(dcSeriesNumber.toString());
            }

        } else if (dcValues.length == 1) {
            DCSeriesNumber dcSeriesNumber = new DCSeriesNumber(dcValues[0].value);

            series.setValue(dcSeriesNumber.getSeries());
            number.setValue(dcSeriesNumber.getNumber());
        }


    }
}

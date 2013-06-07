package org.dspace.submit.inputForms.components.fields;

import org.dspace.app.util.Util;
import org.dspace.content.DCDate;
import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.dspace.content.MetadataField;
import org.dspace.submit.inputForms.components.InputFormField;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 29/05/13
 * Time: 13:07
 */
public class InputFormDate extends InputFormField {

    @Override
    public String getDisplayedValue(DCValue value) {
        return new DCDate(value.value).toString();
    }

    /**
     * Fill out a metadata date field with the value from a form. The date is
     * taken from the three parameters:
     *
     * element_qualifier_year element_qualifier_month element_qualifier_day
     *
     * The granularity is determined by the values that are actually set. If the
     * year isn't set (or is invalid)
     *
     * @param request
     *            the request object
     * @param item
     *            the item to update
     */
    @Override
    public void readInput(Item item, HttpServletRequest request, List<String> errorFields) {
        org.dspace.submit.inputForms.components.MetadataField metadataField = getMetadataField();
        String metadataFieldKey = MetadataField
                .formKey(metadataField.getSchema(), metadataField.getElement(), metadataField.getQualifier());

        int year = Util.getIntParameter(request, metadataFieldKey + "_year");
        int month = Util.getIntParameter(request, metadataFieldKey + "_month");
        int day = Util.getIntParameter(request, metadataFieldKey + "_day");

        // FIXME: Probably should be some more validation
        // Make a standard format date
        DCDate d = new DCDate(year, month, day, -1, -1, -1);

        // already done in doProcessing see also bug DS-203
        // item.clearMetadata(schema, element, qualifier, Item.ANY);

        if (year > 0)
        {
            // Only put in date if there is one!
            item.addMetadata(metadataField.getSchema(), metadataField.getElement(), metadataField.getQualifier(), null, d.toString());
        }
    }
}

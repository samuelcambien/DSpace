package org.dspace.app.webui.submit.inputForms.components.fields;

import org.dspace.app.webui.submit.inputForms.components.InputFormField;
import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.dspace.core.I18nUtil;
import org.dspace.submit.inputForms.components.MetadataField;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.fmt.LocaleSupport;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 12/06/13
 * Time: 09:33
 */
public class InputFormDate extends InputFormField {
    @Override
    public String getRenderFieldHtmlString(Item item, org.dspace.submit.inputForms.components.InputFormField formField, int fieldCountIncr, String label, HttpServletRequest request, PageContext pageContext, int collectionID) {

        MetadataField metadataField = formField.getMetadataField();
        String fieldName = getFieldName(metadataField);

        DCValue[] defaults = formField.getMetadataValues(item);
        int fieldCount = defaults.length + fieldCountIncr;
        StringBuilder result = new StringBuilder();
        org.dspace.content.DCDate dateIssued;

        if (fieldCount == 0)
            fieldCount = 1;

        for (int i = 0; i < fieldCount; i++) {
            if (i == 0)
                result.append("<tr><td class=\"submitFormLabel\">")
                        .append(label)
                        .append("</td>");
            else
                result.append("<tr><td>&nbsp;</td>");

            if (i < defaults.length)
                dateIssued = new org.dspace.content.DCDate(defaults[i].value);
            else
                dateIssued = new org.dspace.content.DCDate("");

            result.append("<td colspan=\"2\" nowrap=\"nowrap\" class=\"submitFormDateLabel\">")
                    //          .append("Month:<select name=\"")
                    .append(LocaleSupport.getLocalizedMessage(pageContext, "jsp.submit.edit-metadata.month"))
                    .append("<select name=\"")
                    .append(fieldName)
                    .append("_month");
            if (formField.isRepeatable() && i > 0) {
                result.append('_').append(i);
            }
            if (formField.isReadonly()) {
                result.append("\" disabled=\"disabled");
            }
            result.append("\"><option value=\"-1\"")
                    .append((dateIssued.getMonth() == -1 ? " selected=\"selected\"" : ""))
                            //          .append(">(No month)</option>");
                    .append(">")
                    .append(LocaleSupport.getLocalizedMessage(pageContext, "jsp.submit.edit-metadata.no_month"))
                    .append("</option>");

            for (int j = 1; j < 13; j++) {
                result.append("<option value=\"")
                        .append(j)
                        .append((dateIssued.getMonth() == j ? "\" selected=\"selected\"" : "\""))
                        .append(">")
                        .append(org.dspace.content.DCDate.getMonthName(j, I18nUtil.getSupportedLocale(request.getLocale())))
                        .append("</option>");
            }

            result.append("</select>")
                    //            .append("Day:<input type=text name=\"")
                    .append(LocaleSupport.getLocalizedMessage(pageContext, "jsp.submit.edit-metadata.day"))
                    .append("<input type=\"text\" name=\"")
                    .append(fieldName)
                    .append("_day");
            if (formField.isRepeatable() && i > 0)
                result.append("_").append(i);
            if (formField.isReadonly()) {
                result.append("\" disabled=\"disabled");
            }
            result.append("\" size=\"2\" maxlength=\"2\" value=\"")
                    .append((dateIssued.getDay() > 0 ?
                            String.valueOf(dateIssued.getDay()) : ""))
                            //          .append("\"/>Year:<input type=text name=\"")
                    .append("\"/>")
                    .append(LocaleSupport.getLocalizedMessage(pageContext, "jsp.submit.edit-metadata.year"))
                    .append("<input type=\"text\" name=\"")
                    .append(fieldName)
                    .append("_year");
            if (formField.isRepeatable() && i > 0)
                result.append("_").append(i);
            if (formField.isReadonly()) {
                result.append("\" disabled=\"disabled");
            }
            result.append("\" size=\"4\" maxlength=\"4\" value=\"")
                    .append((dateIssued.getYear() > 0 ?
                            String.valueOf(dateIssued.getYear()) : ""))
                    .append("\"/></td>\n");

            if (formField.isRepeatable() && !formField.isReadonly() && i < defaults.length) {
                // put a remove button next to filled in values
                result.append("<td><input type=\"submit\" name=\"submit_")
                        .append(fieldName)
                        .append("_remove_")
                        .append(i)
                                //            .append("\" value=\"Remove This Entry\"/> </td></tr>");
                        .append("\" value=\"")
                        .append(LocaleSupport.getLocalizedMessage(pageContext, "jsp.submit.edit-metadata.button.remove"))
                        .append("\"/> </td></tr>");
            } else if (formField.isRepeatable() && !formField.isReadonly() && i == fieldCount - 1) {
                // put a 'more' button next to the last space
                result.append("<td><input type=\"submit\" name=\"submit_")
                        .append(fieldName)
                                //            .append("_add\" value=\"Add More\"/> </td></tr>");
                        .append("_add\" value=\"")
                        .append(LocaleSupport.getLocalizedMessage(pageContext, "jsp.submit.edit-metadata.button.add"))
                        .append("\"/> </td></tr>");
            } else {
                // put a blank if nothing else
                result.append("<td>&nbsp;</td></tr>");
            }
        }

        return result.toString();

    }
}

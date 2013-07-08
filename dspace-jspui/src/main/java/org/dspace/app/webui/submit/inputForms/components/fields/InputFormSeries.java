package org.dspace.app.webui.submit.inputForms.components.fields;

import org.dspace.app.webui.submit.inputForms.components.InputFormField;
import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.dspace.submit.inputForms.components.MetadataField;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.fmt.LocaleSupport;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 12/06/13
 * Time: 10:19
 */
public class InputFormSeries extends InputFormField {

    @Override
    public String getRenderFieldHtmlString(Item item, org.dspace.submit.inputForms.components.InputFormField formField, int fieldCountIncr, HttpServletRequest request, PageContext pageContext, int collectionID) {
        MetadataField metadataField = formField.getMetadataField();
        String fieldName = getFieldName(metadataField);

        DCValue[] defaults = formField.getMetadataValues(item);
        int fieldCount = defaults.length + fieldCountIncr;
        StringBuilder sb = new StringBuilder();
        org.dspace.content.DCSeriesNumber sn;

        sb.append(renderHeaders(pageContext));


        if (fieldCount == 0)
            fieldCount = 1;

        for (int i = 0; i < fieldCount; i++) {
            if (i == 0)
                sb.append("<tr><td class=\"submitFormLabel\">")
                        .append(LocaleSupport.getLocalizedMessage(pageContext, formField.getLabelMessageKey()))
                        .append("</td>");
            else
                sb.append("<tr><td>&nbsp;</td>");

            if (i < defaults.length)
                sn = new org.dspace.content.DCSeriesNumber(defaults[i].value);
            else
                sn = new org.dspace.content.DCSeriesNumber();

            sb.append("<td><input type=\"text\" name=\"")
                    .append(fieldName)
                    .append("_series");
            if (formField.isRepeatable() && i != fieldCount)
                sb.append("_").append(i + 1);
            if (formField.isReadonly()) {
                sb.append("\" disabled=\"disabled");
            }
            sb.append("\" size=\"23\" value=\"")
                    .append(sn.getSeries().replaceAll("\"", "&quot;"))
                    .append("\"/></td>\n<td><input type=\"text\" name=\"")
                    .append(fieldName)
                    .append("_number");
            if (formField.isRepeatable() && i != fieldCount)
                sb.append("_").append(i + 1);
            if (formField.isReadonly()) {
                sb.append("\" disabled=\"disabled");
            }
            sb.append("\" size=\"23\" value=\"")
                    .append(sn.getNumber().replaceAll("\"", "&quot;"))
                    .append("\"/></td>\n");

            if (formField.isRepeatable() && !formField.isReadonly() && i < defaults.length) {
                // put a remove button next to filled in values
                sb.append("<td><input type=\"submit\" name=\"submit_")
                        .append(fieldName)
                        .append("_remove_")
                        .append(i)
//            .append("\" value=\"Remove This Entry\"/> </td></tr>");
                        .append("\" value=\"")
                        .append(LocaleSupport.getLocalizedMessage(pageContext, "jsp.submit.edit-metadata.button.remove"))
                        .append("\"/> </td></tr>");
            } else if (formField.isRepeatable() && !formField.isReadonly() && i == fieldCount - 1) {
                // put a 'more' button next to the last space
                sb.append("<td><input type=\"submit\" name=\"submit_")
                        .append(fieldName)
//            .append("_add\" value=\"Add More\"/> </td></tr>");
                        .append("_add\" value=\"")
                        .append(LocaleSupport.getLocalizedMessage(pageContext, "jsp.submit.edit-metadata.button.add"))
                        .append("\"/> </td></tr>");
            } else {
                // put a blank if nothing else
                sb.append("<td>&nbsp;</td></tr>");
            }
        }

        return sb.toString();

    }

    protected StringBuffer renderHeaders(PageContext pageContext) {
        StringBuffer headers = new StringBuffer();

        //Width hints used here to affect whole table
        headers.append("<tr><td width=\"40%\">&nbsp;</td>")
                .append("<td class=\"submitFormDateLabel\" width=\"5%\">")
//          .append("Series Name</td>")
                .append(LocaleSupport.getLocalizedMessage(pageContext, "jsp.submit.edit-metadata.seriesname"))
                .append("</td>")
                .append("<td class=\"submitFormDateLabel\" width=\"5%\">")
//          .append("Report or Paper No.</td>")
                .append(LocaleSupport.getLocalizedMessage(pageContext, "jsp.submit.edit-metadata.paperno"))
                .append("</td>")
                .append("<td width=\"40%\">&nbsp;</td>")
                .append("</tr>");
        return headers;
    }
}

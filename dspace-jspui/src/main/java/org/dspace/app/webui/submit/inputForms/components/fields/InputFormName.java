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
 * Time: 09:26
 */
public class InputFormName extends InputFormField {

    @Override
    public String getRenderFieldHtmlString(Item item, org.dspace.submit.inputForms.components.InputFormField inputFormField, int fieldCountIncr, String label, HttpServletRequest request, PageContext pageContext, int collectionID) {
        MetadataField metadataField = inputFormField.getMetadataField();
        String fieldName = getFieldName(metadataField);


        DCValue[] defaults = inputFormField.getMetadataValues(item);
        int fieldCount = defaults.length + fieldCountIncr;
        StringBuilder result = new StringBuilder();
        org.dspace.content.DCPersonName dpn;
        String auth;
        int conf;
        StringBuilder first = new StringBuilder();
        StringBuilder last = new StringBuilder();

        if (fieldCount == 0)
            fieldCount = 1;

        result.append(renderHeaders(pageContext));


        for (int i = 0; i < fieldCount; i++) {
            first.setLength(0);
            first.append(fieldName).append("_first");
            if (inputFormField.isRepeatable() && i != fieldCount - 1)
                first.append('_').append(i + 1);

            last.setLength(0);
            last.append(fieldName).append("_last");
            if (inputFormField.isRepeatable() && i != fieldCount - 1)
                last.append('_').append(i + 1);

            if (i == 0)
                result.append("<tr><td class=\"submitFormLabel\">")
                        .append(label)
                        .append("</td>");
            else
                result.append("<tr><td>&nbsp;</td>");

            if (i < defaults.length) {
                dpn = new org.dspace.content.DCPersonName(defaults[i].value);
                auth = defaults[i].authority;
                conf = defaults[i].confidence;
            } else {
                dpn = new org.dspace.content.DCPersonName();
                auth = "";
                conf = unknownConfidence;
            }

            result.append("<td><input type=\"text\" name=\"")
                    .append(last.toString())
                    .append("\" size=\"23\" ");
            if (inputFormField.isReadonly()) {
                result.append("disabled=\"disabled\" ");
            }
            result.append("value=\"")
                    .append(dpn.getLastName().replaceAll("\"", "&quot;")) // Encode "
                    .append("\"/></td>\n<td nowrap=\"nowrap\"><input type=\"text\" name=\"")
                    .append(first.toString())
                    .append("\" size=\"23\" ");
            if (inputFormField.isReadonly()) {
                result.append("disabled=\"disabled\" ");
            }
            result.append("value=\"")
                    .append(dpn.getFirstNames()).append("\"/>")
                    .append(doAuthority(pageContext, fieldName, i, fieldCount, fieldName,
                            auth, conf, true, inputFormField.isRepeatable(), defaults, null, collectionID, request))
                    .append("</td>\n");

            if (inputFormField.isRepeatable() && !inputFormField.isReadonly() && i < defaults.length) {
                // put a remove button next to filled in values
                result.append("<td><input type=\"submit\" name=\"submit_")
                        .append(fieldName)
                        .append("_remove_")
                        .append(i)
                                //            .append("\" value=\"Remove This Entry\"/> </td></tr>")
                        .append("\" value=\"")
                        .append(LocaleSupport.getLocalizedMessage(pageContext, "jsp.submit.edit-metadata.button.remove"))
                        .append("\"/> </td></tr>");
            } else if (inputFormField.isRepeatable() && !inputFormField.isReadonly() && i == fieldCount - 1) {
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

    protected StringBuilder renderHeaders(PageContext pageContext) {
        StringBuilder headers = new StringBuilder();
        //Width hints used here to affect whole table
        headers.append("<tr><td width=\"40%\">&nbsp;</td>")
                .append("<td class=\"submitFormDateLabel\" width=\"5%\">")
                        //             .append("Last name<br>e.g. <strong>Smith</strong></td>")
                .append(LocaleSupport.getLocalizedMessage(pageContext, "jsp.submit.edit-metadata.lastname"))
                .append("</td>")
                .append("<td class=\"submitFormDateLabel\" width=\"5%\">")
                        //             .append("First name(s) + \"Jr\"<br> e.g. <strong>Donald Jr</strong></td>")
                .append(LocaleSupport.getLocalizedMessage(pageContext, "jsp.submit.edit-metadata.firstname"))
                .append("</td>")
                .append("<td width=\"40%\">&nbsp;</td>")
                .append("</tr>");
        return headers;
    }
}

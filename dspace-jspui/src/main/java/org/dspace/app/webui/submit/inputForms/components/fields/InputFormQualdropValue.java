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
 * Time: 13:15
 */
public class InputFormQualdropValue extends InputFormField {

    @Override
    public String getRenderFieldHtmlString(Item item, org.dspace.submit.inputForms.components.InputFormField formField, int fieldCountIncr, String label, HttpServletRequest request, PageContext pageContext, int collectionID) {
        MetadataField metadataField = formField.getMetadataField();
        String fieldName = getFieldName(metadataField);

        DCValue[] defaults = formField.getMetadataValues(item);

//DCValue[] defaults = item.getMetadata(element, Item.ANY, Item.ANY);
        int fieldCount = defaults.length + fieldCountIncr;
        StringBuffer sb = new StringBuffer();
        String q, v, currentQual, currentVal;

        if (fieldCount == 0)
            fieldCount = 1;

        for (int j = 0; j < fieldCount; j++) {

            if (j < defaults.length) {
                currentQual = defaults[j].qualifier;
                if (currentQual == null) currentQual = "";
                currentVal = defaults[j].value;
            } else {
                currentQual = "";
                currentVal = "";
            }

            if (j == 0)
                sb.append("<tr><td class=\"submitFormLabel\">")
                        .append(label)
                        .append("</td>");
            else
                sb.append("<tr><td>&nbsp;</td>");

            // do the dropdown box
            sb.append("<td colspan=\"2\"><select name=\"")
                    .append(fieldName)
                    .append("_qualifier");
            if (formField.isRepeatable() && j != fieldCount - 1)
                sb.append("_").append(j + 1);
            if (formField.isReadonly()) {
                sb.append("\" disabled=\"disabled");
            }
            sb.append("\">");
            for (int i = 0; i < qualMap.size(); i += 2) {
                q = (String) qualMap.get(i);
                v = (String) qualMap.get(i + 1);
                sb.append("<option")
                        .append((v.equals(currentQual) ? " selected=\"selected\" " : ""))
                        .append(" value=\"")
                        .append(v)
                        .append("\">")
                        .append(q)
                        .append("</option>");
            }

            // do the input box
            sb.append("</select>&nbsp;<input type=\"text\" name=\"")
                    .append(fieldName)
                    .append("_value");
            if (formField.isRepeatable() && j != fieldCount - 1)
                sb.append("_").append(j + 1);
            if (formField.isReadonly()) {
                sb.append("\" disabled=\"disabled");
            }
            sb.append("\" size=\"34\" value=\"")
                    .append(currentVal.replaceAll("\"", "&quot;"))
                    .append("\"/></td>\n");

            if (formField.isRepeatable() && !formField.isReadonly() && j < defaults.length) {
                // put a remove button next to filled in values
                sb.append("<td><input type=\"submit\" name=\"submit_")
                        .append(fieldName)
                        .append("_remove_")
                        .append(j)
//            .append("\" value=\"Remove This Entry\"/> </td></tr>");
                        .append("\" value=\"")
                        .append(LocaleSupport.getLocalizedMessage(pageContext, "jsp.submit.edit-metadata.button.remove"))
                        .append("\"/> </td></tr>");
            } else if (formField.isRepeatable() && !formField.isReadonly() && j == fieldCount - 1) {
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
}

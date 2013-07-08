package org.dspace.app.webui.submit.inputForms.components.fields;

import org.dspace.app.webui.submit.inputForms.components.InputFormField;
import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.dspace.submit.inputForms.components.MetadataField;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import java.util.LinkedHashMap;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 12/06/13
 * Time: 10:41
 */
public class InputFormDropdown extends InputFormField {

    @Override
    public String getRenderFieldHtmlString(Item item, org.dspace.submit.inputForms.components.InputFormField formField, int fieldCountIncr, String label, HttpServletRequest request, PageContext pageContext, int collectionID) {
        if (!(formField instanceof org.dspace.submit.inputForms.components.fields.InputFormDropdown)) {
            throw new UnsupportedOperationException("Dropdown cannot be used to render form field of type: " + formField.getClass());
        }


        MetadataField metadataField = formField.getMetadataField();
        String fieldName = getFieldName(metadataField);


        DCValue[] defaults = formField.getMetadataValues(item);
        StringBuilder sb = new StringBuilder();
        int j;

        sb.append("<tr><td class=\"submitFormLabel\">")
                .append(label)
                .append("</td>");

        sb.append("<td colspan=\"2\">")
                .append("<select name=\"")
                .append(fieldName)
                .append("\"");
        if (formField.isRepeatable())
            sb.append(" size=\"6\"  multiple=\"multiple\"");
        if (formField.isReadonly()) {
            sb.append(" disabled=\"disabled\"");
        }
        sb.append(">");

        LinkedHashMap<String, String> values = ((org.dspace.submit.inputForms.components.fields.InputFormDropdown) formField).getValuePairs();
        for (String value : values.keySet()) {
            String display = values.get(value);
            for (j = 0; j < defaults.length; j++) {
                if (value.equals(defaults[j].value))
                    break;
            }
            sb.append("<option ")
                    .append(j < defaults.length ? " selected=\"selected\" " : "")
                    .append("value=\"")
                    .append(value.replaceAll("\"", "&quot;"))
                    .append("\">")
                    .append(display)
                    .append("</option>");
        }

        sb.append("</select></td></tr>");
        return sb.toString();
    }
}

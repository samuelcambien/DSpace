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
 * Time: 12:55
 */
public class InputFormList extends InputFormField {
    @Override
    public String getRenderFieldHtmlString(Item item, org.dspace.submit.inputForms.components.InputFormField formField, int fieldCountIncr, String label, HttpServletRequest request, PageContext pageContext, int collectionID) {
        MetadataField metadataField = formField.getMetadataField();
        String fieldName = getFieldName(metadataField);

        DCValue[] defaults = formField.getMetadataValues(item);
        LinkedHashMap<String, String> values = ((org.dspace.submit.inputForms.components.fields.InputFormList)formField).getValuePairs();

        int valueCount = values.size();

        StringBuilder sb = new StringBuilder();
        int j;

        int numColumns = 1;
        //if more than 3 display+value pairs, display in 2 columns to save space
        if (valueCount > 6)
            numColumns = 2;

        //print out the field label
        sb.append("<tr><td class=\"submitFormLabel\">")
                .append(label)
                .append("</td>");

        if (numColumns > 1)
            sb.append("<td valign=\"top\">");
        else
            sb.append("<td valign=\"top\" colspan=\"3\">");

        //flag that lets us know when we are in Column2
        boolean inColumn2 = false;

        //loop through all values
        for(String value : values.keySet()){
            //get display value and actual value
            String display = values.get(value);

            //check if this value has been selected previously
            for (j = 0; j < defaults.length; j++) {
                if (value.equals(defaults[j].value))
                    break;
            }

            // print input field
            sb.append("<input type=\"");

            //if repeatable, print a Checkbox, otherwise print Radio buttons
            if (formField.isRepeatable())
                sb.append("checkbox");
            else
                sb.append("radio");
            if (formField.isReadonly()) {
                sb.append("\" disabled=\"disabled");
            }
            sb.append("\" name=\"")
                    .append(fieldName)
                    .append("\"")
                    .append(j < defaults.length ? " checked=\"checked\" " : "")
                    .append(" value=\"")
                    .append(value.replaceAll("\"", "&quot;"))
                    .append("\">");

            //print display name immediately after input
            sb.append("&nbsp;")
                    .append(display)
                    .append("<br/>");

            // if we are writing values in two columns,
            // then start column 2 after half of the values
            if ((numColumns == 2) && (i + 2 >= (values.size() / 2)) && !inColumn2) {
                //end first column, start second column
                sb.append("</td>");
                sb.append("<td colspan=\"2\" valign=\"top\">");
                inColumn2 = true;
            }

        }//end for each value

        sb.append("</td></tr>");

        return sb.toString();
    }
}

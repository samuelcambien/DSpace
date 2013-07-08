package org.dspace.app.webui.submit.inputForms.components.fields;

import org.dspace.app.webui.submit.inputForms.components.InputFormField;
import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.dspace.content.authority.Choices;
import org.dspace.submit.inputForms.components.MetadataField;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.fmt.LocaleSupport;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 17/06/13
 * Time: 10:46
 */
public class InputFormChoiceSelectField extends InputFormField {

    @Override
    public String getRenderFieldHtmlString(Item item, org.dspace.submit.inputForms.components.InputFormField formField, int fieldCountIncr, HttpServletRequest request, PageContext pageContext, int collectionID) {

        DCValue[] defaults = formField.getMetadataValues(item);
        MetadataField metadataField = formField.getMetadataField();
        String fieldName = getFieldName(metadataField);


        StringBuilder sb = new StringBuilder();

        sb.append("<tr><td class=\"submitFormLabel\">")
          .append(LocaleSupport.getLocalizedMessage(pageContext, formField.getLabelMessageKey()))
          .append("</td>");
        sb.append("<td colspan=\"2\">")
          .append(doAuthority(pageContext, fieldName, 0, defaults.length,
                  fieldName, null, Choices.CF_UNSET, false, formField.isRepeatable(),
                  defaults, null, collectionID, request))

          .append("</td></tr>");
        return sb.toString();


    }
}

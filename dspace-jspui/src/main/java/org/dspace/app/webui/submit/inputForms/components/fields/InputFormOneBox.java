package org.dspace.app.webui.submit.inputForms.components.fields;

import org.dspace.app.webui.submit.inputForms.components.InputFormField;
import org.dspace.app.webui.submit.inputForms.components.InputFormVocabularyField;
import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.dspace.submit.inputForms.components.MetadataField;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.fmt.LocaleSupport;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 12/06/13
 * Time: 10:17
 */
public class InputFormOneBox extends InputFormVocabularyField {
    @Override
    public String getRenderFieldHtmlString(Item item, org.dspace.submit.inputForms.components.InputFormField formField, int fieldCountIncr, HttpServletRequest request, PageContext pageContext, int collectionID) {
        if (!(formField instanceof org.dspace.submit.inputForms.components.fields.InputFormOneBox)) {
            throw new UnsupportedOperationException("Onebox cannot be used to render form field of type: " + formField.getClass());
        }
        org.dspace.submit.inputForms.components.fields.InputFormOneBox inputFormOneBox = (org.dspace.submit.inputForms.components.fields.InputFormOneBox) formField;

        MetadataField metadataField = formField.getMetadataField();
        String fieldName = getFieldName(metadataField);


        DCValue[] defaults = formField.getMetadataValues(item);
        int fieldCount = defaults.length + fieldCountIncr;
        StringBuilder sb = new StringBuilder();
        String val, auth;
        int conf= 0;

        if (fieldCount == 0)
         fieldCount = 1;

        for (int i = 0; i < fieldCount; i++)
        {
           if (i == 0)
              sb.append("<tr><td class=\"submitFormLabel\">")
                .append(LocaleSupport.getLocalizedMessage(pageContext, formField.getLabelMessageKey()))
                .append("</td>");
           else
              sb.append("<tr><td>&nbsp;</td>");

           if (i < defaults.length)
           {
             val = defaults[i].value.replaceAll("\"", "&quot;");
             auth = defaults[i].authority;
             conf = defaults[i].confidence;
           }
           else
           {
             val = "";
             auth = "";
             conf= unknownConfidence;
           }

           sb.append("<td colspan=\"2\">");
           String fieldNameIdx = fieldName + ((formField.isRepeatable() && i != fieldCount-1)?"_" + (i+1):"");
           StringBuffer inputBlock = new StringBuffer("<input type=\"text\" name=\"")
             .append(fieldNameIdx)
             .append("\" id=\"")
             .append(fieldNameIdx).append("\" size=\"50\" value=\"")
             .append(val +"\"")
             .append((hasVocabulary(inputFormOneBox.getVocabulary())&& inputFormOneBox.isClosedVocabulary()) || formField.isReadonly()?" disabled=\"disabled\" ":"")
             .append("/>")
             .append(doControlledVocabulary(fieldNameIdx, request, pageContext, inputFormOneBox.getVocabulary(), formField.isReadonly()))
             .append("\n");
           sb.append(doAuthority(pageContext, fieldName, i,  fieldCount,
                              fieldName, auth, conf, false, formField.isRepeatable(),
                              defaults, inputBlock, collectionID, request))
             .append("</td>\n");

          if (formField.isRepeatable() && !formField.isReadonly() && i < defaults.length)
          {
             // put a remove button next to filled in values
             sb.append("<td><input type=\"submit\" name=\"submit_")
               .append(fieldName)
               .append("_remove_")
               .append(i)
        //             .append("\" value=\"Remove This Entry\"/> </td></tr>");
               .append("\" value=\"")
               .append(LocaleSupport.getLocalizedMessage(pageContext, "jsp.submit.edit-metadata.button.remove"))
               .append("\"/> </td></tr>");
          }
          else if (formField.isRepeatable() && !formField.isReadonly() && i == fieldCount - 1)
          {
             // put a 'more' button next to the last space
             sb.append("<td><input type=\"submit\" name=\"submit_")
               .append(fieldName)
        //             .append("_add\" value=\"Add More\"/> </td></tr>");
               .append("_add\" value=\"")
               .append(LocaleSupport.getLocalizedMessage(pageContext, "jsp.submit.edit-metadata.button.add"))
               .append("\"/> </td></tr>");
          }
          else
          {
             // put a blank if nothing else
             sb.append("<td>&nbsp;</td></tr>");
          }
        }

        return sb.toString();
    }
}

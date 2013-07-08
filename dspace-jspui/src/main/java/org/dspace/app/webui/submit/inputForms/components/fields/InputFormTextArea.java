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
 * Time: 09:48
 */
public class InputFormTextArea extends InputFormVocabularyField {
    @Override
    public String getRenderFieldHtmlString(Item item, org.dspace.submit.inputForms.components.InputFormField formField, int fieldCountIncr, HttpServletRequest request, PageContext pageContext, int collectionID) {
        if (!(formField instanceof org.dspace.submit.inputForms.components.fields.InputFormTextArea)) {
            throw new UnsupportedOperationException("Text Area cannot be used to render form field of type: " + formField.getClass());
        }
        org.dspace.submit.inputForms.components.fields.InputFormTextArea inputFormTextArea = (org.dspace.submit.inputForms.components.fields.InputFormTextArea) formField;

        MetadataField metadataField = formField.getMetadataField();
        String fieldName = getFieldName(metadataField);


        DCValue[] defaults = formField.getMetadataValues(item);
        int fieldCount = defaults.length + fieldCountIncr;
        StringBuffer sb = new StringBuffer();
        String val, auth;
        int conf = unknownConfidence;

        if (fieldCount == 0)
            fieldCount = 1;

        for (int i = 0; i < fieldCount; i++) {
            if (i == 0)
                sb.append("<tr><td class=\"submitFormLabel\">")
                        .append(LocaleSupport.getLocalizedMessage(pageContext, formField.getLabelMessageKey()))
                        .append("</td>");
            else
                sb.append("<tr><td>&nbsp;</td>");

            if (i < defaults.length) {
                val = defaults[i].value;
                auth = defaults[i].authority;
                conf = defaults[i].confidence;
            } else {
                val = "";
                auth = "";
            }
            sb.append("<td colspan=\"2\">\n");
            String fieldNameIdx = fieldName + ((formField.isRepeatable() && i != fieldCount - 1) ? "_" + (i + 1) : "");
            StringBuffer inputBlock = new StringBuffer().append("<textarea name=\"").append(fieldNameIdx)
                    .append("\" rows=\"4\" cols=\"45\" id=\"")
                    .append(fieldNameIdx).append("_id\" ")
                    .append((hasVocabulary(inputFormTextArea.getVocabulary()) && inputFormTextArea.isClosedVocabulary()) || formField.isReadonly() ? " disabled=\"disabled\" " : "")
                    .append(">")
                    .append(val)
                    .append("</textarea>\n")
                    .append(doControlledVocabulary(fieldNameIdx, request, pageContext, inputFormTextArea.getVocabulary(), formField.isReadonly()));
            sb.append(doAuthority(pageContext, fieldName, i, fieldCount, fieldName,
                    auth, conf, false, formField.isRepeatable(),
                    defaults, inputBlock, collectionID, request))
                    .append("</td>\n");

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
}

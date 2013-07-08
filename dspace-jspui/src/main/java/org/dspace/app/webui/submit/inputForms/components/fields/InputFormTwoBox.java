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
 * Time: 12:43
 */
public class InputFormTwoBox extends InputFormVocabularyField {

    @Override
    public String getRenderFieldHtmlString(Item item, org.dspace.submit.inputForms.components.InputFormField formField, int fieldCountIncr, HttpServletRequest request, PageContext pageContext, int collectionID) {
        if (!(formField instanceof org.dspace.submit.inputForms.components.fields.InputFormOneBox)) {
            throw new UnsupportedOperationException("InputFormTwoBox cannot be used to render form field of type: " + formField.getClass());
        }
        org.dspace.submit.inputForms.components.fields.InputFormTwoBox inputFormTwoBox = (org.dspace.submit.inputForms.components.fields.InputFormTwoBox) formField;


        MetadataField metadataField = formField.getMetadataField();
        String fieldName = getFieldName(metadataField);

        DCValue[] defaults = formField.getMetadataValues(item);
        int fieldCount = defaults.length + fieldCountIncr;
        StringBuilder sb = new StringBuilder();

        String fieldParam = "";

        if (fieldCount == 0)
            fieldCount = 1;

        for (int i = 0; i < fieldCount; i++) {
            if (i == 0) {
                sb.append("<tr><td class=\"submitFormLabel\">")
                        .append(LocaleSupport.getLocalizedMessage(pageContext, formField.getLabelMessageKey()))
                        .append("</td>");
            } else {
                sb.append("<tr><td>&nbsp;</td>");
            }

            if (i != fieldCount) {
                //param is field name and index, starting from 1 (e.g. myfield_2)
                fieldParam = fieldName + "_" + (i + 1);
            } else {
                //param is just the field name
                fieldParam = fieldName;
            }

            if (i < defaults.length) {
                sb.append("<td align=\"left\"><input type=\"text\" name=\"")
                        .append(fieldParam)
                        .append("\" size=\"15\" value=\"")
                        .append(defaults[i].value.replaceAll("\"", "&quot;"))
                        .append("\"")
                        .append((hasVocabulary(inputFormTwoBox.getVocabulary()) && inputFormTwoBox.isClosedVocabulary()) || formField.isReadonly() ? " disabled=\"disabled\" " : "")
                        .append("/>");
                if (!formField.isReadonly()) {
                    sb.append("&nbsp;<input type=\"submit\" name=\"submit_")
                            .append(fieldName)
                            .append("_remove_")
                            .append(i)
                            .append("\" value=\"")
                            .append(LocaleSupport.getLocalizedMessage(pageContext, "jsp.submit.edit-metadata.button.remove2"))
                            .append("\"/>");
                }
                sb.append(doControlledVocabulary(fieldParam, pageContext, vocabulary, formField.isReadonly()))
                        .append("</td>\n");
            } else {
                sb.append("<td align=\"left\"><input type=\"text\" name=\"")
                        .append(fieldParam)
                        .append("\" size=\"15\"")
                        .append((hasVocabulary(vocabulary) && closedVocabulary) || formField.isReadonly() ? " disabled=\"disabled\" " : "")
                        .append("/>")
                        .append(doControlledVocabulary(fieldParam, pageContext, vocabulary, formField.isReadonly()))
                        .append("</td>\n");
            }

            i++;

            if (i != fieldCount) {
                //param is field name and index, starting from 1 (e.g. myfield_2)
                fieldParam = fieldName + "_" + (i + 1);
            } else {
                //param is just the field name
                fieldParam = fieldName;
            }

            if (i < defaults.length) {
                sb.append("<td align=\"left\"><input type=\"text\" name=\"")
                        .append(fieldParam)
                        .append("\" size=\"15\" value=\"")
                        .append(defaults[i].value.replaceAll("\"", "&quot;"))
                        .append("\"")
                        .append((hasVocabulary(vocabulary) && closedVocabulary) || formField.isReadonly() ? " disabled=\"disabled\" " : "")
                        .append("/>");
                if (!formField.isReadonly()) {
                    sb.append("&nbsp;<input type=\"submit\" name=\"submit_")
                            .append(fieldName)
                            .append("_remove_")
                            .append(i)
                            .append("\" value=\"")
                            .append(LocaleSupport.getLocalizedMessage(pageContext, "jsp.submit.edit-metadata.button.remove2"))
                            .append("\"/>");
                }

                sb.append(doControlledVocabulary(fieldParam, pageContext, vocabulary, formField.isReadonly()))
                        .append("</td></tr>\n");
            } else {
                sb.append("<td align=\"left\"><input type=\"text\" name=\"")
                        .append(fieldParam)
                                //.append("\" size=\"15\"/></td>");
                        .append("\" size=\"15\"")
                        .append((hasVocabulary(vocabulary) && closedVocabulary) || formField.isReadonly() ? " disabled=\"disabled\" " : "")
                        .append("/>")
                        .append(doControlledVocabulary(fieldParam, pageContext, vocabulary, formField.isReadonly()))
                        .append("</td>\n");

                if (i + 1 >= fieldCount && !formField.isReadonly()) {
                    sb.append("<td><input type=\"submit\" name=\"submit_")
                            .append(fieldName)
                            .append("_add\" value=\"")
                            .append(LocaleSupport.getLocalizedMessage(pageContext, "jsp.submit.edit-metadata.button.add"))
                            .append("\"/></td>\n");
                } else {
                    sb.append("</td>");
                }
                sb.append("<td>&nbsp;</td></tr>");
            }
        }

        return sb.toString();
    }
}

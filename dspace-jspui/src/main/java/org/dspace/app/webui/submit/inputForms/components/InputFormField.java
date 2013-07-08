package org.dspace.app.webui.submit.inputForms.components;

import org.apache.commons.lang.StringUtils;
import org.dspace.app.util.SubmissionInfo;
import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.dspace.content.authority.ChoiceAuthorityManager;
import org.dspace.content.authority.Choices;
import org.dspace.content.authority.MetadataAuthorityManager;
import org.dspace.core.ConfigurationManager;
import org.dspace.submit.inputForms.components.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.fmt.LocaleSupport;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 12/06/13
 * Time: 09:22
 */
public abstract class InputFormField {

    // An unknown value of confidence for new, empty input fields,
    // so no icon appears yet.
    protected int unknownConfidence = Choices.CF_UNSET - 100;

    public String getRenderHintErrorHtmlString(SubmissionInfo si, org.dspace.submit.inputForms.components.InputFormField formField, PageContext pageContext)
    {
        String fieldName = getFieldName(formField.getMetadataField());
        if(formField.getRequiredMessageKey() != null)
        {
            if((si.getMissingFields() != null) && (si.getMissingFields().contains(fieldName)) && si.getJumpToField()==null || si.getJumpToField().length()==0)
                         si.setJumpToField(fieldName);

            return "<tr><td colspan=\"4\" class=\"submitFormWarn\">" +
                        LocaleSupport.getLocalizedMessage(pageContext, formField.getRequiredMessageKey()) +
                                                 "<a name=\""+fieldName+"\"></a></td></tr>";
        }else
        if(formField.getHintMessageKey() != null)
        {
            return "<tr><td colspan=\"4\" class=\"submitFormHelp\">" +
                        LocaleSupport.getLocalizedMessage(pageContext, formField.getHintMessageKey()) +
                                            "</td></tr>";



        }
        return "";
    }

    public abstract String getRenderFieldHtmlString(Item item,
                                                    org.dspace.submit.inputForms.components.InputFormField formField,
                                                    int fieldCountIncr,
                                                    HttpServletRequest request,
                                                    PageContext pageContext,
                                                    int collectionID);

    // Render the choice/authority controlled entry, or, if not indicated,
    // returns the given default inputBlock
    protected StringBuffer doAuthority(PageContext pageContext, String fieldName,
                                       int idx, int fieldCount, String fieldInput, String authorityValue,
                                       int confidenceValue, boolean isName, boolean repeatable,
                                       DCValue[] dcvs, StringBuffer inputBlock, int collectionID,
                                       HttpServletRequest request) {
        MetadataAuthorityManager mam = MetadataAuthorityManager.getManager();
        ChoiceAuthorityManager cam = ChoiceAuthorityManager.getManager();
        StringBuffer sb = new StringBuffer();

        if (cam.isChoicesConfigured(fieldName)) {
            boolean authority = mam.isAuthorityControlled(fieldName);
            boolean required = authority && mam.isAuthorityRequired(fieldName);
            boolean isSelect = "select".equals(cam.getPresentation(fieldName)) && !isName;

            // if this is not the only or last input, append index to input @names
            String authorityName = fieldName + "_authority";
            String confidenceName = fieldName + "_confidence";
            if (repeatable && !isSelect && idx != fieldCount - 1) {
                fieldInput += '_' + String.valueOf(idx + 1);
                authorityName += '_' + String.valueOf(idx + 1);
                confidenceName += '_' + String.valueOf(idx + 1);
            }

            String confidenceSymbol = confidenceValue == unknownConfidence ? "blank" : Choices.getConfidenceText(confidenceValue).toLowerCase();
            String confIndID = fieldInput + "_confidence_indicator_id";
            if (authority) {
                sb.append(" <img id=\"" + confIndID + "\" title=\"")
                        .append(LocaleSupport.getLocalizedMessage(pageContext, "jsp.authority.confidence.description." + confidenceSymbol))
                        .append("\" class=\"ds-authority-confidence cf-")
                                // set confidence to cf-blank if authority is empty
                        .append(authorityValue == null || authorityValue.length() == 0 ? "blank" : confidenceSymbol)
                        .append(" \" src=\"").append(request.getContextPath()).append("/image/confidence/invisible.gif\" />")
                        .append("<input type=\"text\" value=\"").append(authorityValue != null ? authorityValue : "")
                        .append("\" id=\"").append(authorityName)
                        .append("\" name=\"").append(authorityName).append("\" class=\"ds-authority-value\"/>")
                        .append("<input type=\"hidden\" value=\"").append(confidenceSymbol)
                        .append("\" id=\"").append(confidenceName)
                        .append("\" name=\"").append(confidenceName)
                        .append("\" class=\"ds-authority-confidence-input\"/>");
            }

            // suggest is not supported for name input type
            if ("suggest".equals(cam.getPresentation(fieldName)) && !isName) {
                if (inputBlock != null)
                    sb.insert(0, inputBlock);
                sb.append("<span id=\"").append(fieldInput).append("_indicator\" style=\"display: none;\">")
                        .append("<img src=\"").append(request.getContextPath()).append("/image/authority/load-indicator.gif\" alt=\"Loading...\"/>")
                        .append("</span><div id=\"").append(fieldInput).append("_autocomplete\" class=\"autocomplete\" style=\"display: none;\"> </div>");

                sb.append("<script type=\"text/javascript\">")
                        .append("var gigo = DSpaceSetupAutocomplete('edit_metadata',")
                        .append("{ metadataField: '").append(fieldName).append("', isClosed: '").append(required ? "true" : "false").append("', inputName: '")
                        .append(fieldInput).append("', authorityName: '").append(authorityName).append("', containerID: '")
                        .append(fieldInput).append("_autocomplete', indicatorID: '").append(fieldInput).append("_indicator', ")
                        .append("contextPath: '").append(request.getContextPath())
                        .append("', confidenceName: '").append(confidenceName)
                        .append("', confidenceIndicatorID: '").append(confIndID)
                        .append("', collection: ").append(String.valueOf(collectionID))
                        .append(" }); </script>");
            }

            // put up a SELECT element containing all choices
            else if (isSelect) {
                sb.append("<select id=\"").append(fieldInput)
                        .append("_id\" name=\"").append(fieldInput)
                        .append("\" size=\"").append(String.valueOf(repeatable ? 6 : 1))
                        .append(repeatable ? "\" multiple>\n" : "\">\n");
                Choices cs = cam.getMatches(fieldName, "", collectionID, 0, 0, null);
                // prepend unselected empty value when nothing can be selected.
                if (!repeatable && cs.defaultSelected < 0 && dcvs.length == 0)
                    sb.append("<option value=\"\"><!-- empty --></option>\n");
                for (int i = 0; i < cs.values.length; ++i) {
                    boolean selected = false;
                    for (DCValue dcv : dcvs) {
                        if (dcv.value.equals(cs.values[i].value))
                            selected = true;
                    }
                    sb.append("<option value=\"")
                            .append(cs.values[i].value.replaceAll("\"", "\\\""))
                            .append("\"")
                            .append(selected ? " selected>" : ">")
                            .append(cs.values[i].label).append("</option>\n");
                }
                sb.append("</select>\n");
            }

            // use lookup for any other presentation style (i.e "select")
            else {
                if (inputBlock != null)
                    sb.insert(0, inputBlock);
                sb.append("<input type=\"image\" name=\"").append(fieldInput).append("_lookup\" ")
                        .append("onclick=\"javascript: return DSpaceChoiceLookup('")
                        .append(request.getContextPath()).append("/tools/lookup.jsp','")
                        .append(fieldName).append("','edit_metadata','")
                        .append(fieldInput).append("','").append(authorityName).append("','")
                        .append(confIndID).append("',")
                        .append(String.valueOf(collectionID)).append(",")
                        .append(String.valueOf(isName)).append(",false);\"")
                        .append(" title=\"")
                        .append(LocaleSupport.getLocalizedMessage(pageContext, "jsp.tools.lookup.lookup"))
                        .append("\" width=\"16px\" height=\"16px\" src=\"" + request.getContextPath() + "/image/authority/zoom.png\" />");
            }
        } else if (inputBlock != null)
            sb = inputBlock;
        return sb;
    }

    public boolean hasVocabulary(org.dspace.submit.inputForms.components.InputFormField formField)
    {
        if(formField instanceof org.dspace.submit.inputForms.components.InputFormVocabularyField)
        {
            String vocabularyKey = ((org.dspace.submit.inputForms.components.InputFormVocabularyField) formField).getVocabulary();
            return hasVocabulary(vocabularyKey);
        }else{
            return false;
        }

    }

    protected boolean hasVocabulary(String vocabulary) {
        boolean enabled = ConfigurationManager.getBooleanProperty("webui.controlledvocabulary.enable");
        boolean useWithCurrentField = StringUtils.isNotBlank(vocabulary);
        boolean has = false;

        if (enabled && useWithCurrentField) {
            has = true;
        }
        return has;
    }

    public String getFieldName(MetadataField metadataField)
    {
        if (metadataField.getQualifier() != null && !metadataField.getQualifier().equals(Item.ANY))
           return metadataField.getSchema() + "_" + metadataField.getElement() + '_' + metadataField.getQualifier();
        else
           return metadataField.getSchema() + "_" + metadataField.getElement();

    }
}

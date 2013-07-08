package org.dspace.app.webui.submit.inputForms.components;

import org.dspace.core.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.fmt.LocaleSupport;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 13/06/13
 * Time: 15:57
 */
public abstract class InputFormVocabularyField extends InputFormField {
    // This method is resposible for showing a link next to an input box
    // that pops up a window that to display a controlled vocabulary.
    // It should be called from the doOneBox and doTwoBox methods.
    // It must be extended to work with doTextArea.
    protected String doControlledVocabulary(String fieldName, HttpServletRequest request, PageContext pageContext, String vocabulary, boolean readonly) {
        String link = "";
        boolean enabled = ConfigurationManager.getBooleanProperty("webui.controlledvocabulary.enable");
        boolean useWithCurrentField = vocabulary != null && !"".equals(vocabulary);

        if (enabled && useWithCurrentField && !readonly) {
            // Deal with the issue of _0 being removed from fieldnames in the configurable submission system
            if (fieldName.endsWith("_0")) {
                fieldName = fieldName.substring(0, fieldName.length() - 2);
            }
            link = "<br/>" +
                    "<a href='javascript:void(null);' onclick='javascript:popUp(\"" +
                    request.getContextPath() + "/controlledvocabulary/controlledvocabulary.jsp?ID=" +
                    fieldName + "&amp;vocabulary=" + vocabulary + "\")'>" +
                    "<span class='controlledVocabularyLink'>" +
                    LocaleSupport.getLocalizedMessage(pageContext, "jsp.submit.edit-metadata.controlledvocabulary") +
                    "</span>" +
                    "</a>";
        }

        return link;
    }

}

package org.dspace.submit.inputForms.components.fields;

import common.Logger;
import org.dspace.app.util.Util;
import org.dspace.content.Item;
import org.dspace.content.MetadataField;
import org.dspace.content.authority.Choices;
import org.dspace.content.authority.MetadataAuthorityManager;
import org.dspace.submit.inputForms.components.InputFormField;
import org.dspace.submit.inputForms.components.InputFormVocabularyField;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 29/05/13
 * Time: 12:43
 */
public class InputFormOneBox extends InputFormVocabularyField {

    private static final Logger log = Logger.getLogger(InputFormOneBox.class);

    /**
     * Fill out an item's metadata values from a plain standard text field. If
     * the field isn't repeatable, the input field name is called:
     *
     * element_qualifier
     *
     * or for an unqualified element:
     *
     * element
     *
     * Repeated elements are appended with an underscore then an integer. e.g.:
     *
     * dc_title_alternative dc_title_alternative_1
     *
     * The values will be put in separate DCValues, ordered as they appear in
     * the list. These will replace any existing values.
     *
     * @param request
     *            the request object
     * @param item
     *            the item to update
     */
    @Override
    public void readInput(Item item, HttpServletRequest request, List<String> errorFields) {
        // FIXME: Of course, language should be part of form, or determined
                // some other way
        String metadataField = MetadataField.formKey(getMetadataField().getSchema(), getMetadataField().getElement(), getMetadataField().getQualifier());

        String fieldKey = MetadataAuthorityManager.makeFieldKey(getMetadataField().getSchema(), getMetadataField().getElement(), getMetadataField().getQualifier());
        boolean isAuthorityControlled = MetadataAuthorityManager.getManager().isAuthorityControlled(fieldKey);

        // Values to add
        List<String> vals = null;
        List<String> auths = null;
        List<String> confs = null;

        if (isRepeatable()) {
            vals = getRepeatedParameter(request, metadataField, metadataField);
            if (isAuthorityControlled) {
                auths = getRepeatedParameter(request, metadataField, metadataField + "_authority");
                confs = getRepeatedParameter(request, metadataField, metadataField + "_confidence");
            }

            // Find out if the relevant "remove" button was pressed
            // TODO: These separate remove buttons are only relevant
            // for DSpace JSP UI, and the code below can be removed
            // once the DSpace JSP UI is obsolete!
            String buttonPressed = Util.getSubmitButton(request, "");
            String removeButton = "submit_" + metadataField + "_remove_";

            if (buttonPressed.startsWith(removeButton)) {
                int valToRemove = Integer.parseInt(buttonPressed
                        .substring(removeButton.length()));

                vals.remove(valToRemove);
                if (isAuthorityControlled) {
                    auths.remove(valToRemove);
                    confs.remove(valToRemove);
                }
            }
        } else {
            // Just a single name
            vals = new LinkedList<String>();
            String value = request.getParameter(metadataField);
            if (value != null) {
                vals.add(value.trim());
            }
            if (isAuthorityControlled) {
                auths = new LinkedList<String>();
                confs = new LinkedList<String>();
                String av = request.getParameter(metadataField + "_authority");
                String cv = request.getParameter(metadataField + "_confidence");
                auths.add(av == null ? "" : av.trim());
                confs.add(cv == null ? "" : cv.trim());
            }
        }

        // Remove existing values, already done in doProcessing see also bug DS-203
        // item.clearMetadata(schema, element, qualifier, Item.ANY);

        // Put the names in the correct form
        for (int i = 0; i < vals.size(); i++) {
            // Add to the database if non-empty
            String s = vals.get(i);
            if ((s != null) && !s.equals("")) {
                if (isAuthorityControlled) {
                    String authKey = auths.size() > i ? auths.get(i) : null;
                    String sconf = (authKey != null && confs.size() > i) ? confs.get(i) : null;
                    if (MetadataAuthorityManager.getManager().isAuthorityRequired(fieldKey) &&
                            (authKey == null || authKey.length() == 0)) {
                        log.warn("Skipping value of " + metadataField + " because the required Authority key is missing or empty.");
                        errorFields.add(metadataField);
                    } else {
                        item.addMetadata(getMetadataField().getSchema(), getMetadataField().getElement(), getMetadataField().getQualifier(), LANGUAGE_QUALIFIER, s,
                                authKey, (sconf != null && sconf.length() > 0) ?
                                Choices.getConfidenceValue(sconf) : Choices.CF_ACCEPTED);
                    }
                } else {
                    item.addMetadata(getMetadataField().getSchema(), getMetadataField().getElement(), getMetadataField().getQualifier(), LANGUAGE_QUALIFIER, s);
                }
            }
        }
    }
}

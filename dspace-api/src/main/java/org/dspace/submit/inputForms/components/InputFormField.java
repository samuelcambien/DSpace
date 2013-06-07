package org.dspace.submit.inputForms.components;

import common.Logger;
import org.apache.commons.lang.StringUtils;
import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.dspace.core.ConfigurationManager;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 29/05/13
 * Time: 12:40
 */
public abstract class InputFormField {

    private static final Logger log = Logger.getLogger(InputFormField.class);

    private boolean readonly;
    private MetadataField metadataField;
    private boolean repeatable;
    private String labelMessageKey;
    private String hintMessageKey;
    private String requiredMessageKey;
        // the metadata language qualifier
    public static final String LANGUAGE_QUALIFIER = getDefaultLanguageQualifier();



    public abstract void readInput(Item item, HttpServletRequest request, List<String> errorFields);


    public DCValue[] getMetadataValues(Item item){
        MetadataField metadataField = getMetadataField();
        return item.getMetadata(metadataField.getSchema(), metadataField.getElement(), metadataField.getQualifier(), Item.ANY);
    }

    public void clearMetadataValues(Item item)
    {
        MetadataField metadataField = getMetadataField();
        item.clearMetadata(metadataField.getSchema(), metadataField.getElement(), metadataField.getQualifier(), Item.ANY);
    }

    public boolean isMetadataInError(Item item)
    {

        DCValue[] metadataValues = getMetadataValues(item);
        return isRequired() && metadataValues.length == 0;

    }

    /**
     * Retrieve the displayed value for this field, this can sometimes differ from the stored value
     * For example a dropdown has a stored and a displayed value
     * @param value the value we want the display value for
     * @return the value to be displayed
     */
    public String getDisplayedValue(DCValue value){
        return value.value;
    }

    public MetadataField getMetadataField() {
        return metadataField;
    }

    @Required
    public void setMetadataField(MetadataField metadataField) {
        this.metadataField = metadataField;
    }

    @Required
    public boolean isRepeatable() {
        return repeatable;
    }

    @Required
    public void setRepeatable(boolean repeatable) {
        this.repeatable = repeatable;
    }

    public String getLabelMessageKey() {
        return labelMessageKey;
    }

    @Required
    public void setLabelMessageKey(String labelMessageKey) {
        this.labelMessageKey = labelMessageKey;
    }

    public String getHintMessageKey() {
        return hintMessageKey;
    }

    public void setHintMessageKey(String hintMessageKey) {
        this.hintMessageKey = hintMessageKey;
    }

    public String getRequiredMessageKey() {
        return requiredMessageKey;
    }

    public void setRequiredMessageKey(String requiredMessageKey) {
        this.requiredMessageKey = requiredMessageKey;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    /**
     * Get repeated values from a form. If "foo" is passed in as the parameter,
     * values in the form of parameters "foo", "foo_1", "foo_2", etc. are
     * returned.
     * <P>
     * This method can also handle "composite fields" (metadata fields which may
     * require multiple params, etc. a first name and last name).
     *
     * @param request
     *            the HTTP request containing the form information
     * @param metadataField
     *            the metadata field which can store repeated values
     * @param param
     *            the repeated parameter on the page (used to fill out the
     *            metadataField)
     *
     * @return a List of Strings
     */
    protected List<String> getRepeatedParameter(HttpServletRequest request,
            String metadataField, String param)
    {
        List<String> vals = new LinkedList<String>();

        int i = 1;    //start index at the first of the previously entered values
        boolean foundLast = false;

        // Iterate through the values in the form.
        while (!foundLast)
        {
            String s = null;

            //First, add the previously entered values.
            // This ensures we preserve the order that these values were entered
            s = request.getParameter(param + "_" + i);

            // If there are no more previously entered values,
            // see if there's a new value entered in textbox
            if (s==null)
            {
                s = request.getParameter(param);
                //this will be the last value added
                foundLast = true;
            }

            // We're only going to add non-null values
            if (s != null)
            {
                boolean addValue = true;

                // Check to make sure that this value was not selected to be
                // removed.
                // (This is for the "remove multiple" option available in
                // Manakin)
                String[] selected = request.getParameterValues(metadataField
                        + "_selected");

                if (selected != null)
                {
                    for (int j = 0; j < selected.length; j++)
                    {
                        if (selected[j].equals(metadataField + "_" + i))
                        {
                            addValue = false;
                        }
                    }
                }

                if (addValue)
                {
                    vals.add(s.trim());
                }
            }

            i++;
        }

        log.debug("getRepeatedParameter: metadataField=" + metadataField
                + " param=" + metadataField + ", return count = "+vals.size());

        return vals;
    }

    /**
     * @return the default language qualifier for metadata
     */

    public static String getDefaultLanguageQualifier()
    {
       String language = "";
       language = ConfigurationManager.getProperty("default.language");
       if (StringUtils.isEmpty(language))
       {
           language = "en";
       }
       return language;
    }

    public boolean isRequired() {
        return requiredMessageKey != null;
    }


}

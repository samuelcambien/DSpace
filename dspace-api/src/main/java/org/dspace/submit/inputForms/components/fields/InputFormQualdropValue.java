package org.dspace.submit.inputForms.components.fields;

import org.apache.commons.lang.StringUtils;
import org.dspace.app.util.Util;
import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.dspace.content.MetadataField;
import org.dspace.submit.inputForms.components.InputFormFieldSelectable;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 29/05/13
 * Time: 13:13
 */
public class InputFormQualdropValue extends InputFormFieldSelectable {

    @Override
    public String getDisplayedValue(DCValue metadataValue) {

        String displayQual = getValuePairs().get(metadataValue.qualifier);
        if (displayQual!=null && displayQual.length()>0)
        {
            return displayQual + ":" + metadataValue.value;
        }else{
            return metadataValue.qualifier + ":" + metadataValue.value;
        }
    }

    @Override
    public DCValue[] getMetadataValues(Item item) {
        // Determine the real field's values. Since the qualifier is
        // selected we need to search through all the metadata and see
        // if any match for another field, if not we assume that this field
        // should handle it.
        org.dspace.submit.inputForms.components.MetadataField metadataField = getMetadataField();
        DCValue[] unfiltered = item.getMetadata(metadataField.getSchema(), metadataField.getElement(), Item.ANY, Item.ANY);
        ArrayList<DCValue> filtered = new ArrayList<DCValue>();
        for (DCValue dcValue : unfiltered)
        {
            //Check if this qualifier is available
            if (getValuePairs().containsValue(dcValue.qualifier))
            {
                filtered.add( dcValue );
            }
        }
        return filtered.toArray(new DCValue[filtered.size()]);
    }

    @Override
    public void clearMetadataValues(Item item) {
        org.dspace.submit.inputForms.components.MetadataField metadataField = getMetadataField();
        //Clear it for all our qualifiers
        for(String qualifier : getValuePairs().keySet())
        {
            item.clearMetadata(metadataField.getSchema(), metadataField.getElement(), qualifier, Item.ANY);
        }
    }

    @Override
    public void readInput(Item item, HttpServletRequest request, List<String> errorFields) {
        String buttonPressed = Util.getSubmitButton(request, null);

        String metadataElementField = MetadataField
                .formKey(getMetadataField().getSchema(), getMetadataField().getElement(), null);

        List<String> quals = getRepeatedParameter(request, metadataElementField, metadataElementField + "_qualifier");
        List<String> vals = getRepeatedParameter(request, metadataElementField, metadataElementField + "_value");
        for (int z = 0; z < vals.size(); z++)
        {
            String thisQual = quals.get(z);
            if ("".equals(thisQual))
            {
                thisQual = null;
            }
            String thisVal = vals.get(z);
            if (!StringUtils.equals(buttonPressed, "submit_" + metadataElementField + "_remove_" + z)
                    && !thisVal.equals(""))
            {
                item.addMetadata(getMetadataField().getSchema(), getMetadataField().getElement(), thisQual, null, thisVal);
            }
        }

    }
}

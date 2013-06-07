package org.dspace.submit.inputForms.components.fields;

import org.dspace.content.Item;
import org.dspace.content.MetadataField;
import org.dspace.submit.inputForms.components.InputFormFieldSelectable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 5/06/13
 * Time: 11:29
 */
public class InputFormList extends InputFormFieldSelectable {

    @Override
    public void readInput(Item item, HttpServletRequest request, List<String> errorFields) {
        String metadataField = MetadataField
                .formKey(getMetadataField().getSchema(), getMetadataField().getElement(), getMetadataField().getQualifier());

        String[] vals = request.getParameterValues(metadataField);
        if (vals != null)
        {
            for (String val : vals) {
                if (!val.equals("")) {
                    item.addMetadata(getMetadataField().getSchema(), getMetadataField().getElement(), getMetadataField().getQualifier(), LANGUAGE_QUALIFIER,
                            val);
                }
            }
        }
    }
}

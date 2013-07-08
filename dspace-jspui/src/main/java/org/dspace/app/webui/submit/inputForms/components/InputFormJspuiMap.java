package org.dspace.app.webui.submit.inputForms.components;

import org.dspace.content.authority.ChoiceAuthorityManager;
import org.dspace.content.authority.MetadataAuthorityManager;
import org.dspace.submit.inputForms.components.MetadataField;

import java.util.HashMap;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 17/06/13
 * Time: 10:56
 */
public class InputFormJspuiMap<K,V> extends HashMap {

    public V get(org.dspace.submit.inputForms.components.InputFormField formField){
        MetadataField metadataField = formField.getMetadataField();
        String fieldKey = MetadataAuthorityManager.makeFieldKey(metadataField.getSchema(), metadataField.getElement(), metadataField.getQualifier());
        ChoiceAuthorityManager cmgr = ChoiceAuthorityManager.getManager();
        if (cmgr.isChoicesConfigured(fieldKey) && "select".equals(cmgr.getPresentation(fieldKey)))
        {
            return (V) get("inputFormChoiceSelectField");


        }else{
            return (V) get((Object)formField);
        }

    }


}

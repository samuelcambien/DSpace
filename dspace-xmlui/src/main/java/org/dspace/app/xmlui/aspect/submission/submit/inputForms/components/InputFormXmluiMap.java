package org.dspace.app.xmlui.aspect.submission.submit.inputForms.components;

import org.dspace.app.xmlui.wing.element.Params;
import org.dspace.content.authority.ChoiceAuthorityManager;
import org.dspace.content.authority.MetadataAuthorityManager;
import org.dspace.submit.inputForms.components.MetadataField;

import java.util.HashMap;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 7/06/13
 * Time: 08:05
 */
public class InputFormXmluiMap<K,V> extends HashMap {

    public V get(org.dspace.submit.inputForms.components.InputFormField formField){
        MetadataField metadataField = formField.getMetadataField();
        String fieldKey = MetadataAuthorityManager.makeFieldKey(metadataField.getSchema(), metadataField.getElement(), metadataField.getQualifier());
        ChoiceAuthorityManager cmgr = ChoiceAuthorityManager.getManager();
        if (cmgr.isChoicesConfigured(fieldKey) && Params.PRESENTATION_SELECT.equals(cmgr.getPresentation(fieldKey)))
        {
            return (V) get("inputFormChoiceSelectField");


        }else{
            return (V) get((Object)formField);
        }

    }

}

package org.dspace.submit.inputForms.components;

import org.springframework.beans.factory.annotation.Required;

import java.util.LinkedHashMap;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 29/05/13
 * Time: 13:13
 */
public abstract class InputFormFieldSelectable extends InputFormField {
    private LinkedHashMap<String, String> valuePairs;


    public LinkedHashMap<String, String> getValuePairs() {
        return valuePairs;
    }

    @Required
    public void setValuePairs(LinkedHashMap<String, String> valuePairs) {
        this.valuePairs = valuePairs;
    }
}

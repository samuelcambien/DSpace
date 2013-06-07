package org.dspace.submit.inputForms.components;

import org.dspace.content.Collection;
import org.dspace.content.Community;

import java.sql.SQLException;
import java.util.Map;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 30/05/13
 * Time: 13:18
 */
public class InputFormMap {

    public static final String DEFAULT = "default";
    private Map<String, InputForm> inputFormMap;


    public InputForm getInputForm(Collection collection) throws SQLException {
        InputForm inputForm = inputFormMap.get(collection.getHandle());
        if(inputForm == null)
        {
            inputForm = getInputForm((Community) collection.getParentObject());
        }
        if(inputForm == null)
        {
            inputForm = inputFormMap.get(DEFAULT);
        }

        if(inputForm == null)
        {
            throw new IllegalStateException("No valid input forms located for collection: " + collection.getHandle());
        }else{
            return inputForm;
        }
    }

    protected InputForm getInputForm(Community community) throws SQLException {

        if(inputFormMap.get(community.getHandle()) != null)
        {
            return inputFormMap.get(community.getHandle());
        }else
        if(community.getParentCommunity() != null){
            return getInputForm(community.getParentCommunity());
        }
        return null;
    }


    public void setInputFormMap(Map<String, InputForm> inputFormMap) {
        this.inputFormMap = inputFormMap;
    }
}

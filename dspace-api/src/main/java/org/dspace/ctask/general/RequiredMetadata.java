/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.ctask.general;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dspace.content.Collection;
import org.dspace.content.DCValue;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.core.Constants;
import org.dspace.curate.AbstractCurationTask;
import org.dspace.curate.Curator;
import org.dspace.curate.Suspendable;
import org.dspace.submit.inputForms.components.*;
import org.dspace.utils.DSpace;

/**
 * RequiredMetadata task compares item metadata with fields 
 * marked as required in input-forms.xml. The task succeeds if all
 * required fields are present in the item metadata, otherwise it fails.
 * Primarily a curation task demonstrator.
 *
 * @author richardrodgers
 */
@Suspendable
public class RequiredMetadata extends AbstractCurationTask
{
    // map of DCInputSets
    private InputFormMap inputFormsMap = null;
    // map of required fields
    private Map<String, List<String>> reqMap = new HashMap<String, List<String>>();
    
    @Override 
    public void init(Curator curator, String taskId) throws IOException
    {
        super.init(curator, taskId);
        inputFormsMap = new DSpace().getServiceManager().getServiceByName(InputFormMap.class.getName(), InputFormMap.class);
    }

    /**
     * Perform the curation task upon passed DSO
     *
     * @param dso the DSpace object
     * @throws IOException
     */
    @Override
    public int perform(DSpaceObject dso) throws IOException
    {
        if (dso.getType() == Constants.ITEM)
        {
            Item item = (Item)dso;
            int count = 0;
            try
            {
                StringBuilder sb = new StringBuilder();
                String handle = item.getHandle();
                if (handle == null)
                {
                    // we are still in workflow - no handle assigned
                    handle = "in workflow";
                }
                sb.append("Item: ").append(handle);
                for (String req : getReqList(item.getOwningCollection()))
                {
                    DCValue[] vals = item.getMetadata(req);
                    if (vals.length == 0)
                    {
                        sb.append(" missing required field: ").append(req);
                        count++;
                    }
                }
                if (count == 0)
                {
                    sb.append(" has all required fields");
                }
                report(sb.toString());
                setResult(sb.toString());
            }
            catch (SQLException sqlE)
            {
                throw new IOException(sqlE.getMessage(), sqlE);
            }
            return (count == 0) ? Curator.CURATE_SUCCESS : Curator.CURATE_FAIL;
        }
        else
        {
           setResult("Object skipped");
           return Curator.CURATE_SKIP;
        }
    }
    
    private List<String> getReqList(Collection collection) throws SQLException {
        List<String> reqList = reqMap.get(collection.getHandle());
        if (reqList == null)
        {
            reqList = new ArrayList<String>();
            InputForm inputForm = inputFormsMap.getInputForm(collection);

            for (int i = 0; i < inputForm.getPages().size(); i++)
            {
                InputFormPage page = inputForm.getPages().get(i);

                for (InputFormField inputFormField : page.getAllMetadataFields())
                {
                    if (inputFormField.isRequired())
                    {
                        MetadataField metadataField = inputFormField.getMetadataField();
                        StringBuilder sb = new StringBuilder();
                        sb.append(metadataField.getSchema()).append(".");
                        sb.append(metadataField.getElement()).append(".");
                        String qual = metadataField.getQualifier();
                        if (qual == null)
                        {
                            qual = "";
                        }
                        sb.append(qual);
                        reqList.add(sb.toString());
                    }
                }
            }
            reqMap.put(collection.getHandle(), reqList);
        }
        return reqList;
    }
}

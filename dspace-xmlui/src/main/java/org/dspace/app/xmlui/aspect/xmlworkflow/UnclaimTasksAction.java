/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.xmlui.aspect.xmlworkflow;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.log4j.Logger;
import org.dspace.app.xmlui.utils.ContextUtil;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.dspace.xmlworkflow.*;
import org.dspace.xmlworkflow.storedcomponents.ClaimedTask;
import org.dspace.xmlworkflow.storedcomponents.XmlWorkflowItem;

import java.util.Map;

/**
 * Unclaim all the selected workflow items. This action returns these
 * tasks to the general pool for other users to select from.
 *
 * @author Bram De Schouwer (bram.deschouwer at dot com)
 * @author Kevin Van de Velde (kevin at atmire dot com)
 * @author Ben Bosman (ben at atmire dot com)
 * @author Mark Diggory (markd at atmire dot com)
 */
public class UnclaimTasksAction extends AbstractAction
{

    private static final Logger log = Logger.getLogger(UnclaimTasksAction.class);

    public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters parameters) throws Exception {
        Request request = ObjectModelHelper.getRequest(objectModel);
        Context context = ContextUtil.obtainContext(objectModel);

    	// Or the user selected a checkbox full of workflow IDs
    	String[] workflowIDs = request.getParameterValues("workflowandstepID");
    	if (workflowIDs != null)
    	{
    		for (String workflowID : workflowIDs)
    		{
                XmlWorkflowItem workflowItem = XmlWorkflowItem.find(context, Integer.valueOf(workflowID.split(":")[0]));

                ClaimedTask pooledTask = ClaimedTask.findByWorkflowIdAndEPerson(context, workflowItem.getID(), context.getCurrentUser().getID());
                XmlWorkflowServiceImpl.deleteClaimedTask(context, workflowItem, pooledTask);

                WorkflowRequirementsServiceImpl.removeClaimedUser(context, workflowItem, context.getCurrentUser(), workflowID.split(":")[1]);
                log.info(LogManager.getHeader(context, "unclaim_workflow", "workflow_id=" + workflowItem.getID()));

            }
        }
        return null;
    }
}

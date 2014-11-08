package org.dspace.xmlworkflow.factory;

import org.dspace.content.Collection;
import org.dspace.core.Context;
import org.dspace.xmlworkflow.WorkflowConfigurationException;
import org.dspace.xmlworkflow.state.Step;
import org.dspace.xmlworkflow.state.Workflow;
import org.dspace.xmlworkflow.state.actions.WorkflowActionConfig;

import java.io.IOException;
import java.sql.SQLException;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 4/11/14
 * Time: 15:48
 */
public interface XmlWorkflowFactory {

    public Workflow getWorkflow(Collection collection) throws IOException, WorkflowConfigurationException, SQLException;

    public Step createStep(Workflow workflow, String stepID) throws WorkflowConfigurationException, IOException;

    public WorkflowActionConfig createWorkflowActionConfig(String actionID);
}

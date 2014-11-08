package org.dspace.xmlworkflow.service;

import org.dspace.authorize.AuthorizeException;
import org.dspace.core.Context;
import org.dspace.eperson.EPerson;
import org.dspace.workflow.WorkflowService;
import org.dspace.workflow.WorkflowException;
import org.dspace.xmlworkflow.RoleMembers;
import org.dspace.xmlworkflow.state.Step;
import org.dspace.xmlworkflow.state.Workflow;
import org.dspace.xmlworkflow.state.actions.ActionResult;
import org.dspace.xmlworkflow.state.actions.WorkflowActionConfig;
import org.dspace.xmlworkflow.storedcomponents.ClaimedTask;
import org.dspace.xmlworkflow.storedcomponents.PoolTask;
import org.dspace.xmlworkflow.storedcomponents.XmlWorkflowItem;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 4/11/14
 * Time: 12:01
 */
public interface XmlWorkflowService extends WorkflowService<XmlWorkflowItem> {

    public void alertUsersOnTaskActivation(Context c, XmlWorkflowItem wfi, String emailTemplate, List<EPerson> epa, String ...arguments) throws IOException, SQLException, MessagingException;

    public WorkflowActionConfig doState(Context c, EPerson user, HttpServletRequest request, int workflowItemId, Workflow workflow, WorkflowActionConfig currentActionConfig) throws SQLException, AuthorizeException, IOException, MessagingException, WorkflowException;

    public WorkflowActionConfig processOutcome(Context c, EPerson user, Workflow workflow, Step currentStep, WorkflowActionConfig currentActionConfig, ActionResult currentOutcome, XmlWorkflowItem wfi, boolean enteredNewStep) throws IOException, AuthorizeException, SQLException, WorkflowException;

    public void deleteAllTasks(Context context, XmlWorkflowItem wi) throws SQLException, AuthorizeException;

    public void deleteAllPooledTasks(Context c, XmlWorkflowItem wi) throws SQLException, AuthorizeException;

    public void deletePooledTask(Context context, XmlWorkflowItem wi, PoolTask task) throws SQLException, AuthorizeException;

    public void deleteClaimedTask(Context c, XmlWorkflowItem wi, ClaimedTask task) throws SQLException, AuthorizeException;

    public void createPoolTasks(Context context, XmlWorkflowItem wi, RoleMembers assignees, Step step, WorkflowActionConfig action)
            throws SQLException, AuthorizeException;

    public void createOwnedTask(Context context, XmlWorkflowItem wi, Step step, WorkflowActionConfig action, EPerson e) throws SQLException, AuthorizeException;

    public String getEPersonName(EPerson ePerson);
}

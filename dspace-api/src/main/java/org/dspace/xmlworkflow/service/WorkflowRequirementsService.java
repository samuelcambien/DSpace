package org.dspace.xmlworkflow.service;

import org.dspace.authorize.AuthorizeException;
import org.dspace.core.Context;
import org.dspace.eperson.EPerson;
import org.dspace.xmlworkflow.WorkflowConfigurationException;
import org.dspace.xmlworkflow.state.Step;
import org.dspace.xmlworkflow.storedcomponents.XmlWorkflowItem;

import java.io.IOException;
import java.sql.SQLException;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 4/11/14
 * Time: 15:34
 */
public interface WorkflowRequirementsService {

    public static final String WORKFLOW_SCHEMA = "workflow";


    /**
     * Adds a claimed user in the metadata
     * if enough users have claimed this task (claimed or finished) to meet the required number
     * the pooled tasks will be deleted
     * @param context the dspace context
     * @param wfi the workflow item
     * @param step the step for which we are accepting
     * @param user the current user
     * @throws SQLException ...
     * @throws AuthorizeException ...
     * @throws IOException ...
     */
    public void addClaimedUser(Context context, XmlWorkflowItem wfi, Step step, EPerson user) throws SQLException, AuthorizeException, IOException;

    public void removeClaimedUser(Context context, XmlWorkflowItem wfi, EPerson user, String stepID) throws SQLException, IOException, WorkflowConfigurationException, AuthorizeException;

    /**
     * Adds a finished user in the metadata
     * this method will also remove the user from the inprogress metadata
     * @param context the dspace context
     * @param wfi the workflow item
     * @param user the current user
     * @throws AuthorizeException ...
     * @throws SQLException ...
     */
    public void addFinishedUser(Context context, XmlWorkflowItem wfi, EPerson user) throws AuthorizeException, SQLException;

    public void clearInProgressUsers(Context context, XmlWorkflowItem wfi) throws AuthorizeException, SQLException;
}

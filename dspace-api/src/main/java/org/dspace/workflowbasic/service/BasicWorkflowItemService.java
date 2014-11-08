package org.dspace.workflowbasic.service;

import org.dspace.core.Context;
import org.dspace.eperson.EPerson;
import org.dspace.workflow.WorkflowItemService;
import org.dspace.workflowbasic.BasicWorkflowItem;

import java.sql.SQLException;
import java.util.List;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 3/11/14
 * Time: 11:46
 */
public interface BasicWorkflowItemService extends WorkflowItemService<BasicWorkflowItem>
{

    public List<BasicWorkflowItem> findPooledTasks(Context context, EPerson ePerson) throws SQLException;

    public List<BasicWorkflowItem> findByOwner(Context context, EPerson ePerson) throws SQLException;
}

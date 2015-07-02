package org.dspace.xmlworkflow.storedcomponents.service;

import org.dspace.authorize.AuthorizeException;
import org.dspace.core.Context;
import org.dspace.eperson.EPerson;
import org.dspace.service.DSpaceCRUDService;
import org.dspace.xmlworkflow.storedcomponents.WorkflowItemRole;
import org.dspace.xmlworkflow.storedcomponents.XmlWorkflowItem;

import java.sql.SQLException;
import java.util.List;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 4/11/14
 * Time: 10:17
 */
public interface WorkflowItemRoleService extends DSpaceCRUDService<WorkflowItemRole> {

    public List<WorkflowItemRole> find(Context context, XmlWorkflowItem workflowItem, String role) throws SQLException;

    public List<WorkflowItemRole> findByWorkflowItem(Context context, XmlWorkflowItem xmlWorkflowItem) throws SQLException;

    public void deleteForWorkflowItem(Context context, XmlWorkflowItem wfi) throws SQLException, AuthorizeException;

    public List<WorkflowItemRole> findByEPerson(Context context, EPerson ePerson) throws SQLException;
}

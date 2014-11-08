package org.dspace.xmlworkflow.storedcomponents;

import org.dspace.authorize.AuthorizeException;
import org.dspace.core.Context;
import org.dspace.eperson.EPerson;
import org.dspace.xmlworkflow.storedcomponents.dao.WorkflowItemRoleDAO;
import org.dspace.xmlworkflow.storedcomponents.service.WorkflowItemRoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 4/11/14
 * Time: 10:39
 */
public class WorkflowItemRoleServiceImpl implements WorkflowItemRoleService {

    @Autowired(required = true)
    private WorkflowItemRoleDAO workflowItemRoleDAO;

    @Override
    public List<WorkflowItemRole> find(Context context, XmlWorkflowItem workflowItem, String role) throws SQLException {
        return workflowItemRoleDAO.findByWorkflowItemAndRole(context, workflowItem, role);
    }

    @Override
    public List<WorkflowItemRole> findByWorkflowItem(Context context, XmlWorkflowItem xmlWorkflowItem) throws SQLException {
        return workflowItemRoleDAO.findByWorkflowItem(context, xmlWorkflowItem);
    }

    @Override
    public void deleteForWorkflowItem(Context context, XmlWorkflowItem xmlWorkflowItem) throws SQLException, AuthorizeException {
        Iterator<WorkflowItemRole> workflowItemRoles = findByWorkflowItem(context, xmlWorkflowItem).iterator();
        while (workflowItemRoles.hasNext()) {
            WorkflowItemRole workflowItemRole = workflowItemRoles.next();
            workflowItemRoles.remove();
            delete(context, workflowItemRole);
        }
    }

    @Override
    public List<WorkflowItemRole> findByEPerson(Context context, EPerson ePerson) throws SQLException {
        return workflowItemRoleDAO.findByEPerson(context, ePerson);
    }

    @Override
    public WorkflowItemRole create(Context context) throws SQLException, AuthorizeException {
        return workflowItemRoleDAO.create(context, new WorkflowItemRole());
    }

    @Override
    public WorkflowItemRole find(Context context, int id) throws SQLException {
        return workflowItemRoleDAO.findByID(context, WorkflowItemRole.class, id);
    }

    @Override
    public void update(Context context, WorkflowItemRole workflowItemRole) throws SQLException, AuthorizeException {
        workflowItemRoleDAO.save(context, workflowItemRole);
    }

    @Override
    public void delete(Context context, WorkflowItemRole workflowItemRole) throws SQLException, AuthorizeException {
        workflowItemRoleDAO.delete(context, workflowItemRole);
    }
}

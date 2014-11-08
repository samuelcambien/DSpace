package org.dspace.xmlworkflow.storedcomponents.service;

import org.dspace.authorize.AuthorizeException;
import org.dspace.core.Context;
import org.dspace.eperson.EPerson;
import org.dspace.service.DSpaceCRUDService;
import org.dspace.xmlworkflow.storedcomponents.PoolTask;
import org.dspace.xmlworkflow.storedcomponents.XmlWorkflowItem;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 4/11/14
 * Time: 09:44
 */
public interface PoolTaskService extends DSpaceCRUDService<PoolTask>
{
    public List<PoolTask> findByEperson(Context context, EPerson ePerson) throws SQLException, AuthorizeException, IOException;

    public List<PoolTask> find(Context context, XmlWorkflowItem workflowItem) throws SQLException;

    public PoolTask findByWorkflowIdAndEPerson(Context context, XmlWorkflowItem workflowItem, EPerson ePerson)
            throws SQLException, AuthorizeException, IOException;

    public void deleteByWorkflowItem(Context context, XmlWorkflowItem xmlWorkflowItem) throws SQLException, AuthorizeException;

    public List<PoolTask> findByEPerson(Context context, EPerson ePerson) throws SQLException;
}

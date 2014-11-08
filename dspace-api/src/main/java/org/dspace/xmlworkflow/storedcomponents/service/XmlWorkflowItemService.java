package org.dspace.xmlworkflow.storedcomponents.service;

import org.dspace.content.Collection;
import org.dspace.core.Context;
import org.dspace.workflow.WorkflowItemService;
import org.dspace.xmlworkflow.storedcomponents.XmlWorkflowItem;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 4/11/14
 * Time: 11:00
 */
public interface XmlWorkflowItemService extends WorkflowItemService<XmlWorkflowItem> {

    /**
     * return all workflowitems for a certain page
     *
     * @param context  active context
     * @return WorkflowItem list of all the workflow items in system
     */
    public List<XmlWorkflowItem> findAll(Context context, Integer page, Integer pagesize) throws SQLException;

    /**
     * return all workflowitems for a certain page with a certain collection
     *
     * @param collection  active context
     * @return WorkflowItem list of all the workflow items in system
     */
    public List<XmlWorkflowItem> findAllInCollection(Context context, Integer page, Integer pagesize, Collection collection) throws SQLException;

    /**
     * return how many workflow items appear in the database
     *
     * @param context  active context
     * @return the number of workflow items
     */
    public int countAll(Context context) throws SQLException;

    /**
     * return how many workflow items that appear in the collection
     *
     * @param context  active context
     * @return the number of workflow items
     */
    public int countAllInCollection(Context context, Collection collection) throws SQLException;


}

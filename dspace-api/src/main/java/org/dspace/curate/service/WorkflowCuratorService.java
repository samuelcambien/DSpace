package org.dspace.curate.service;

import org.dspace.authorize.AuthorizeException;
import org.dspace.core.Context;
import org.dspace.curate.Curator;
import org.dspace.workflowbasic.BasicWorkflowItem;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by kevin on 08/11/14.
 */
public interface WorkflowCuratorService {


    public boolean needsCuration(BasicWorkflowItem wfi);

    /**
     * Determines and executes curation on a Workflow item.
     *
     * @param c the context
     * @param wfi the workflow item
     * @return true if curation was completed or not required,
     *         false if tasks were queued for later completion,
     *         or item was rejected
     * @throws AuthorizeException
     * @throws IOException
     * @throws SQLException
     */
    public boolean doCuration(Context c, BasicWorkflowItem wfi)
            throws AuthorizeException, IOException, SQLException;


    /**
     * Determines and executes curation of a Workflow item.
     *
     * @param c the user context
     * @param wfId the workflow id
     * @throws AuthorizeException
     * @throws IOException
     * @throws SQLException
     */
    public boolean curate(Curator curator, Context c, String wfId)
            throws AuthorizeException, IOException, SQLException;

    public boolean curate(Curator curator, Context c, BasicWorkflowItem wfi)
            throws AuthorizeException, IOException, SQLException;
}

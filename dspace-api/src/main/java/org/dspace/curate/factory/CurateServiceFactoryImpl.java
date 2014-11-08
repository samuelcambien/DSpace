package org.dspace.curate.factory;

import org.dspace.curate.service.WorkflowCuratorService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 27/05/14
 * Time: 09:44
 */
public class CurateServiceFactoryImpl extends CurateServiceFactory {

    @Autowired(required = true)
    private WorkflowCuratorService workflowCurator;

    @Override
    public WorkflowCuratorService getWorkflowCuratorService() {
        return workflowCurator;
    }
}

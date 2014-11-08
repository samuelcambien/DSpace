package org.dspace.curate.factory;

import org.dspace.curate.service.WorkflowCuratorService;
import org.dspace.utils.DSpace;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 27/05/14
 * Time: 09:40
 */
public abstract class CurateServiceFactory {

    public abstract WorkflowCuratorService getWorkflowCuratorService();

    public static CurateServiceFactory getInstance()
    {
        return new DSpace().getServiceManager().getServiceByName("curateServiceFactory", CurateServiceFactory.class);
    }
}

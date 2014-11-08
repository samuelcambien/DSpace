package org.dspace.embargo.factory;

import org.dspace.embargo.service.EmbargoService;
import org.dspace.utils.DSpace;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 27/05/14
 * Time: 09:40
 */
public abstract class EmbargoServiceFactory {

    public abstract EmbargoService getEmbargoService();

    public static EmbargoServiceFactory getInstance()
    {
        return new DSpace().getServiceManager().getServiceByName("embargoServiceFactory", EmbargoServiceFactory.class);
    }
}

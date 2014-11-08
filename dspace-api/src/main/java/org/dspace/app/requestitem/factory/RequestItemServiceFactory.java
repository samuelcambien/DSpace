package org.dspace.app.requestitem.factory;

import org.dspace.app.requestitem.service.RequestItemService;
import org.dspace.handle.service.HandleService;
import org.dspace.utils.DSpace;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 27/05/14
 * Time: 09:40
 */
public abstract class RequestItemServiceFactory {

    public abstract RequestItemService getRequestItemService();

    public static RequestItemServiceFactory getInstance()
    {
        return new DSpace().getServiceManager().getServiceByName("requestItemServiceFactory", RequestItemServiceFactory.class);
    }
}

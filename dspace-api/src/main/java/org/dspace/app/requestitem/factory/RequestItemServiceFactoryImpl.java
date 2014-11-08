package org.dspace.app.requestitem.factory;

import org.dspace.app.requestitem.service.RequestItemService;
import org.dspace.handle.service.HandleService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 27/05/14
 * Time: 09:44
 */
public class RequestItemServiceFactoryImpl extends RequestItemServiceFactory {

    @Autowired(required = true)
    private RequestItemService requestItemService;

    @Override
    public RequestItemService getRequestItemService() {
        return requestItemService;
    }
}

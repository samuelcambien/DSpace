package org.dspace.event.factory;

import org.dspace.authorize.service.AuthorizeService;
import org.dspace.authorize.service.ResourcePolicyService;
import org.dspace.event.service.EventService;
import org.dspace.utils.DSpace;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 27/05/14
 * Time: 09:40
 */
public abstract class EventServiceFactory {

    public abstract EventService getEventService();

    public static EventServiceFactory getInstance()
    {
        return new DSpace().getServiceManager().getServiceByName("eventServiceFactory", EventServiceFactory.class);
    }
}

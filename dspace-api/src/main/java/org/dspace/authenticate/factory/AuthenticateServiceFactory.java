package org.dspace.authenticate.factory;

import org.dspace.authenticate.service.AuthenticationService;
import org.dspace.authorize.service.AuthorizeService;
import org.dspace.authorize.service.ResourcePolicyService;
import org.dspace.utils.DSpace;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 27/05/14
 * Time: 09:40
 */
public abstract class AuthenticateServiceFactory {

    public abstract AuthenticationService getAuthenticationService();

    public static AuthenticateServiceFactory getInstance()
    {
        return new DSpace().getServiceManager().getServiceByName("authenticateServiceFactory", AuthenticateServiceFactory.class);
    }
}

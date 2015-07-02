package org.dspace.authenticate.factory;

import org.dspace.authenticate.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 27/05/14
 * Time: 09:44
 */
public class AuthenticateServiceFactoryImpl extends AuthenticateServiceFactory {

    @Autowired(required = true)
    private AuthenticationService authenticationService;

    @Override
    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }
}

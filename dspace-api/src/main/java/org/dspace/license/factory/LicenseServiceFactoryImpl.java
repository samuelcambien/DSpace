package org.dspace.license.factory;

import org.dspace.license.service.CreativeCommonsService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 27/05/14
 * Time: 09:44
 */
public class LicenseServiceFactoryImpl extends LicenseServiceFactory {

    @Autowired(required = true)
    private CreativeCommonsService creativeCommonsService;

    @Override
    public CreativeCommonsService getCreativeCommonsService() {
        return creativeCommonsService;
    }
}

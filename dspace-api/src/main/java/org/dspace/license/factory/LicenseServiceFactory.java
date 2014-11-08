package org.dspace.license.factory;

import org.dspace.license.service.CreativeCommonsService;
import org.dspace.utils.DSpace;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 27/05/14
 * Time: 09:40
 */
public abstract class LicenseServiceFactory {

    public abstract CreativeCommonsService getCreativeCommonsService();

    public static LicenseServiceFactory getInstance()
    {
        return new DSpace().getServiceManager().getServiceByName("licenseServiceFactory", LicenseServiceFactory.class);
    }
}

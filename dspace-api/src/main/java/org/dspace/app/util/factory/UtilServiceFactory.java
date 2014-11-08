package org.dspace.app.util.factory;

import org.dspace.app.util.service.MetadataExposureService;
import org.dspace.app.util.service.OpenSearchService;
import org.dspace.app.util.service.WebAppService;
import org.dspace.utils.DSpace;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 2/06/14
 * Time: 16:03
 */
public abstract class UtilServiceFactory
{
    public abstract WebAppService getWebAppService();

    public abstract OpenSearchService getOpenSearchService();

    public abstract MetadataExposureService getMetadataExposureService();

    public static UtilServiceFactory getInstance()
    {
        return new DSpace().getServiceManager().getServiceByName("appUtilServiceFactory", UtilServiceFactory.class);
    }

}

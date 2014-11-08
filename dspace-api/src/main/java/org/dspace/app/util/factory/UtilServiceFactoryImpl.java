package org.dspace.app.util.factory;

import org.dspace.app.util.service.MetadataExposureService;
import org.dspace.app.util.service.OpenSearchService;
import org.dspace.app.util.service.WebAppService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 2/06/14
 * Time: 16:04
 */
public class UtilServiceFactoryImpl extends UtilServiceFactory {

    @Autowired(required = true)
    private MetadataExposureService metadataExposureService;
    @Autowired(required = true)
    private OpenSearchService openSearchService;
    @Autowired(required = true)
    private WebAppService webAppService;

    @Override
    public WebAppService getWebAppService() {
        return webAppService;
    }

    @Override
    public OpenSearchService getOpenSearchService() {
        return openSearchService;
    }

    @Override
    public MetadataExposureService getMetadataExposureService() {
        return metadataExposureService;
    }
}

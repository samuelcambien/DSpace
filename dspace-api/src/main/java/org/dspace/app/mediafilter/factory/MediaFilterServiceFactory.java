package org.dspace.app.mediafilter.factory;

import org.dspace.app.mediafilter.service.MediaFilterService;
import org.dspace.utils.DSpace;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 28/05/14
 * Time: 08:47
 */
public abstract class MediaFilterServiceFactory {

    public abstract MediaFilterService getMediaFilterService();

    public static MediaFilterServiceFactory getInstance(){
        return new DSpace().getServiceManager().getServiceByName("mediaFilterServiceFactory", MediaFilterServiceFactory.class);
    }
}

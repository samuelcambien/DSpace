package org.dspace.app.mediafilter.factory;

import org.dspace.app.mediafilter.service.MediaFilterService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 28/05/14
 * Time: 08:48
 */
public class MediaFilterServiceFactoryImpl extends MediaFilterServiceFactory {

    @Autowired(required = true)
    private MediaFilterService mediaFilterService;

    @Override
    public MediaFilterService getMediaFilterService() {
        return mediaFilterService;
    }
}

package org.dspace.versioning.factory;

import org.dspace.versioning.service.VersionHistoryService;
import org.dspace.versioning.service.VersioningService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 8/05/14
 * Time: 14:00
 */
public class VersionServiceFactoryImpl extends VersionServiceFactory {

    @Autowired(required = true)
    protected VersionHistoryService versionHistoryService;

    @Autowired(required = true)
    protected VersioningService versionService;

    @Override
    public VersionHistoryService getVersionHistoryService() {
        return versionHistoryService;
    }

    @Override
    public VersioningService getVersionService() {
        return versionService;
    }
}

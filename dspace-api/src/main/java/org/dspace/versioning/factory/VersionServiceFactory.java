package org.dspace.versioning.factory;

import org.dspace.utils.DSpace;
import org.dspace.versioning.service.VersionHistoryService;
import org.dspace.versioning.service.VersioningService;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 8/05/14
 * Time: 13:16
 */
public abstract class VersionServiceFactory {

    public abstract VersionHistoryService getVersionHistoryService();

    public abstract VersioningService getVersionService();

    public static VersionServiceFactory getInstance()
    {
        return new DSpace().getServiceManager().getServiceByName("versionServiceFactory", VersionServiceFactory.class);
    }
}

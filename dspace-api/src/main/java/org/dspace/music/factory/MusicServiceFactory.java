/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.music.factory;

import org.dspace.music.service.AlbumService;
import org.dspace.services.factory.DSpaceServicesFactory;

/**
 * Abstract factory to get services for the Music workload, use MusicServiceFactory.getInstance() to retrieve an
 * implementation
 */
public abstract class MusicServiceFactory {

    /**
     * This method will return an instance of the AlbumService
     * @return An instance of the AlbumService
     */
    public abstract AlbumService getAlbumService();

    /**
     * Use this method to retrieve an implementation of the MusicServiceFactory to use to retrieve the different beans
     * @return An implementation of the MusicServiceFactory
     */
    public static MusicServiceFactory getInstance() {
        return DSpaceServicesFactory.getInstance().getServiceManager()
                                    .getServiceByName("musicServiceFactory", MusicServiceFactory.class);
    }
}

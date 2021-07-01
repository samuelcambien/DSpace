/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.music.factory.impl;

import org.dspace.music.factory.MusicServiceFactory;
import org.dspace.music.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The implementation for the {@link MusicServiceFactory}
 */
public class MusicServiceFactoryImpl extends MusicServiceFactory {

    @Autowired
    private AlbumService albumService;

    @Override
    public AlbumService getAlbumService() {
        return albumService;
    }
}

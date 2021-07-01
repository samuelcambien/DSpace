/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.builder;

import java.sql.SQLException;
import java.util.Date;

import org.dspace.authorize.AuthorizeException;
import org.dspace.core.Context;
import org.dspace.music.Album;
import org.dspace.music.service.AlbumService;

public class AlbumBuilder extends AbstractBuilder<Album, AlbumService> {

    private Album album;

    protected AlbumBuilder(Context context, Album album) {
        super(context);
        this.album = album;
    }

    public static AlbumBuilder createAlbum(Context context, String title, String artist) {
        Album album = albumService.create(context, title, artist);
        return new AlbumBuilder(context, album);
    }

    public AlbumBuilder withReleaseDate(Date releaseDate) {
        album.setReleaseDate(releaseDate);
        return this;
    }

    @Override
    public void cleanup() throws Exception {
        try (Context c = new Context()) {
            c.turnOffAuthorisationSystem();
            // Ensure object and any related objects are reloaded before checking to see what needs cleanup
            album = c.reloadEntity(album);
            if (album != null) {
                delete(c, album);
            }
            c.complete();
            indexingService.commit();
        }
    }

    @Override
    public Album build() throws SQLException, AuthorizeException {
        return album;
    }

    @Override
    public void delete(Context c, Album dso) throws Exception {
        if (dso != null) {
            getService().delete(c, dso);
        }
    }

    @Override
    protected AlbumService getService() {
        return albumService;
    }
}

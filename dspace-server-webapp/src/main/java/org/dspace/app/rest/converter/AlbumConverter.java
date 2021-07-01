/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.rest.converter;

import static org.dspace.app.rest.model.AlbumRest.DATE_FORMAT;

import java.util.Date;

import org.dspace.app.rest.model.AlbumRest;
import org.dspace.app.rest.projection.Projection;
import org.dspace.music.Album;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This converter will convert an object of {@Link Album} to an object of {@link AlbumRest}
 */
@Component
public class AlbumConverter implements DSpaceConverter<Album, AlbumRest> {

    @Autowired
    private ConverterService converter;

    @Override
    public AlbumRest convert(Album album, Projection projection) {
        AlbumRest albumRest = new AlbumRest();
        albumRest.setId(album.getID());
        albumRest.setArtist(album.getArtist());
        albumRest.setTitle(album.getTitle());
        albumRest.setReleaseDate(formatDate(album.getReleaseDate()));
        albumRest.setProjection(projection);
        return albumRest;
    }

    @Override
    public Class<Album> getModelClass() {
        return Album.class;
    }

    public static String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        return DATE_FORMAT.format(date);
    }
}

/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.rest.matcher;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.dspace.app.rest.converter.AlbumConverter.formatDate;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import java.util.Date;
import java.util.UUID;

import org.dspace.music.Album;
import org.hamcrest.Matcher;

public class AlbumMatcher {

    private AlbumMatcher() {
    }

    public static Matcher<Object> matchAlbum(UUID id, String title, String artist, Date releaseDate) {
        return allOf(
            hasJsonPath("$.id", is(id + "")),
            hasJsonPath("$.title", is(title)),
            hasJsonPath("$.artist", is(artist)),
            hasJsonPath("$.releaseDate", is(formatDate(releaseDate)))
        );
    }

    public static Matcher<Object> matchAlbum(Album album) {
        return matchAlbum(album.getID(), album.getTitle(), album.getArtist(), album.getReleaseDate());
    }
}

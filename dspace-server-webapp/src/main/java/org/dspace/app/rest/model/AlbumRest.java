/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.rest.model;

import java.text.SimpleDateFormat;
import java.util.UUID;

import org.dspace.app.rest.RestResourceController;
import org.dspace.music.Album;

/**
 * This class serves as a REST representation for the {@link Album} class
 */
public class AlbumRest extends BaseObjectRest<UUID> {

    public static final String NAME = "album";
    public static final String PLURAL_NAME = "albums";
    public static final String CATEGORY = RestAddressableModel.MUSIC;

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public String getCategory() {
        return CATEGORY;
    }

    public Class getController() {
        return RestResourceController.class;
    }

    public String getType() {
        return NAME;
    }

    private String artist;
    private String title;
    private String releaseDate;

    @Override
    public UUID getId() {
        return id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}

/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.music;

import static org.apache.logging.log4j.LogManager.getLogger;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.Logger;
import org.dspace.core.Context;
import org.dspace.music.dao.AlbumDAO;
import org.dspace.music.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The implementation for the {@link AlbumService} class
 */
public class AlbumServiceImpl implements AlbumService {

    private static final Logger log = getLogger(AlbumService.class);

    @Autowired
    private AlbumDAO albumDAO;

    @Override
    public Album create(Context context, String title, String artist) {

        Album album = new Album();
        album.setTitle(title);
        album.setArtist(artist);

        try {
            return albumDAO.create(context, album);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countTotal(Context context) {
        try {
            return albumDAO.countRows(context);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Album> find(Context context, UUID id) {
        try {
            return Optional.ofNullable(albumDAO.findByID(context, Album.class, id));
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Album> findAll(Context context) {
        try {
            return albumDAO.findAll(context, Album.class);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Album> findAll(Context context, int limit, int offset) {
        try {
            return albumDAO.findAll(context, Album.class, limit, offset);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Context context, Album album) {
        try {
            albumDAO.delete(context, album);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Album> findByArtist(Context context, String artist) {
        try {
            return albumDAO.findByArtist(context, artist);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}

/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.music.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.dspace.content.Item;
import org.dspace.core.Context;
import org.dspace.music.Album;

/**
 * An interface for the AlbumService with methods regarding the Album workload
 */
public interface AlbumService {

    Album create(Context context, String title, String artist);

    int countTotal(Context context);

    Optional<Album> find(Context context, UUID id);

    List<Album> findAll(Context context);

    List<Album> findAll(Context context, int limit, int offset);

    void delete(Context context, Album album);

    List<Album> findByArtist(Context context, String artist, int limit, int offset);

    int countByArtist(Context context, String artist);

    Optional<Album> findByItem(Context context, Item item);
}

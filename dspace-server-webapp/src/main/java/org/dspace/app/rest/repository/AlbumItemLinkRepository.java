/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.rest.repository;

import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;

import org.dspace.app.rest.model.AlbumRest;
import org.dspace.app.rest.model.ItemRest;
import org.dspace.app.rest.projection.Projection;
import org.dspace.content.Item;
import org.dspace.core.Context;
import org.dspace.music.Album;
import org.dspace.music.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * This is the {@link LinkRestRepository} implementation that takes care of retrieving the
 * {@link Item} object for the Album endpoints
 */
@Component(AlbumRest.CATEGORY + "." + AlbumRest.NAME + "." + AlbumRest.ITEM)
public class AlbumItemLinkRepository extends AbstractDSpaceRestRepository implements LinkRestRepository {

    @Autowired
    private AlbumRestRepository albumRestRepository;

    @Autowired
    private AlbumService albumService;

    @PreAuthorize("hasPermission(#albumId, 'ALBUM', 'ITEM_READ')")
    public ItemRest getAlbumItem(
            @Nullable HttpServletRequest request,
            UUID albumId,
            @Nullable Pageable pageable,
            Projection projection) {

        Context context = obtainContext();
        Album album = albumService.find(context, albumId).orElseThrow(() -> new ResourceNotFoundException(
                        AlbumRest.CATEGORY + "." + AlbumRest.NAME + " with id: " + albumId + " not found"));

        return Optional.ofNullable(album.getItem())
                .map(item -> (ItemRest) converter.toRest(item, projection))
                .orElse(null);
    }
}

/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.rest.repository;

import java.util.List;
import java.util.UUID;

import org.dspace.app.rest.converter.ConverterService;
import org.dspace.app.rest.model.AlbumRest;
import org.dspace.core.Context;
import org.dspace.music.Album;
import org.dspace.music.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * The repository for the Process workload
 */
@Component(AlbumRest.CATEGORY + "." + AlbumRest.NAME)
public class AlbumRestRepository extends DSpaceRestRepository<AlbumRest, UUID> {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private ConverterService converterService;

    @Override
    public AlbumRest findOne(Context context, UUID uuid) {
        return albumService
                .find(context, uuid)
                .map(album -> (AlbumRest) converter.toRest(album, utils.obtainProjection()))
                .orElse(null);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<AlbumRest> findAll(Context context, Pageable pageable) {
        int total = albumService.countTotal(context);
        List<Album> albums = albumService.findAll(context);
        return converter.toRestPage(albums, pageable, total, utils.obtainProjection());
    }

    @Override
    public Class<AlbumRest> getDomainClass() {
        return AlbumRest.class;
    }
}

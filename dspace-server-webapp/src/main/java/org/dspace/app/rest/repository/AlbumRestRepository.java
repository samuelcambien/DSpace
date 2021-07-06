/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.rest.repository;

import static org.dspace.app.rest.converter.AlbumConverter.parseDate;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dspace.app.rest.converter.ConverterService;
import org.dspace.app.rest.exception.UnprocessableEntityException;
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
        List<Album> albums = albumService.findAll(context, pageable.getPageSize(), (int) pageable.getOffset());
        return converter.toRestPage(albums, pageable, total, utils.obtainProjection());
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public AlbumRest createAndReturn(Context context) {

        AlbumRest albumRest;
        try {
            albumRest = new ObjectMapper().readValue(
                    getRequestService().getCurrentRequest().getHttpServletRequest().getInputStream(),
                    AlbumRest.class
            );
        } catch (Exception e) {
            throw new UnprocessableEntityException("Error parsing request body", e);
        }

        if (isBlank(albumRest.getTitle())) {
            throw new UnprocessableEntityException("Required property missing: album title");
        }

        if (isBlank(albumRest.getArtist())) {
            throw new UnprocessableEntityException("Required property missing: album artist");
        }

        Album album = albumService.create(context, albumRest.getTitle(), albumRest.getArtist());
        album.setReleaseDate(parseDate(albumRest.getReleaseDate()));
        return converter.toRest(album, utils.obtainProjection());
    }

    @Override
    public Class<AlbumRest> getDomainClass() {
        return AlbumRest.class;
    }
}
/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.rest.repository;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.dspace.app.rest.converter.AlbumConverter.parseDate;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dspace.app.rest.converter.ConverterService;
import org.dspace.app.rest.exception.TranslatableUnprocessableEntityException;
import org.dspace.app.rest.exception.UnprocessableEntityException;
import org.dspace.app.rest.model.AlbumRest;
import org.dspace.app.rest.model.patch.Patch;
import org.dspace.app.rest.repository.patch.ResourcePatch;
import org.dspace.core.Context;
import org.dspace.music.Album;
import org.dspace.music.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
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

    @Autowired
    private ResourcePatch<Album> resourcePatch;

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

        checkRequiredProperties(albumRest);

        Album album = albumService.create(context, albumRest.getTitle(), albumRest.getArtist());
        album.setReleaseDate(parseDate(albumRest.getReleaseDate()));
        return converter.toRest(album, utils.obtainProjection());
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public AlbumRest put(Context context, HttpServletRequest request, String apiCategory, String model, UUID id,
                         JsonNode jsonNode) {

        AlbumRest albumRest;
        try {
            albumRest = new ObjectMapper().readValue(jsonNode.toString(), AlbumRest.class);
        } catch (IOException e) {
            throw new UnprocessableEntityException("Error parsing album: " + e.getMessage());
        }

        checkRequiredProperties(albumRest);

        Album album = albumService.find(context, id).orElseThrow(
            () -> new ResourceNotFoundException(apiCategory + "." + model + " with id: " + id + " not found")
        );

        album.setArtist(albumRest.getArtist());
        album.setTitle(albumRest.getTitle());
        album.setReleaseDate(parseDate(albumRest.getReleaseDate()));

        return converter.toRest(album, utils.obtainProjection());
    }

    private void checkRequiredProperties(AlbumRest albumRest) {
        if (isBlank(albumRest.getTitle())) {
            throw new TranslatableUnprocessableEntityException("org.dspace.app.rest.exception.Album.title_missing");
        }

        if (isBlank(albumRest.getArtist())) {
            throw new TranslatableUnprocessableEntityException("org.dspace.app.rest.exception.Album.artist_missing");
        }
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    protected void patch(Context context, HttpServletRequest request, String apiCategory, String model, UUID id,
                    Patch patch) throws SQLException {

        Album album = albumService.find(context, id).orElseThrow(() -> new ResourceNotFoundException(
                AlbumRest.CATEGORY + "." + AlbumRest.NAME + " with id: " + id + " not found"));
        resourcePatch.patch(context, album, patch.getOperations());
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void delete(Context context, UUID id) {

        Album album = albumService.find(context, id).orElseThrow(() -> new ResourceNotFoundException(
                AlbumRest.CATEGORY + "." + AlbumRest.NAME + " with id: " + id + " not found"));
        albumService.delete(context, album);
    }

    @Override
    public Class<AlbumRest> getDomainClass() {
        return AlbumRest.class;
    }
}

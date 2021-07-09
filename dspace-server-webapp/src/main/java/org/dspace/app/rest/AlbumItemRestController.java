/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.rest;

import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.dspace.app.rest.utils.ContextUtil.obtainContext;
import static org.dspace.app.rest.utils.RegexUtils.REGEX_REQUESTMAPPING_IDENTIFIER_AS_UUID;
import static org.dspace.core.Constants.ITEM;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dspace.app.rest.converter.ConverterService;
import org.dspace.app.rest.exception.DSpaceBadRequestException;
import org.dspace.app.rest.exception.UnprocessableEntityException;
import org.dspace.app.rest.model.AlbumRest;
import org.dspace.app.rest.model.ItemRest;
import org.dspace.app.rest.utils.Utils;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.core.Context;
import org.dspace.music.Album;
import org.dspace.music.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller will handle all the incoming calls on the api/music/albums/{uuid}/item endpoint
 * where the uuid corresponds to the album of which you want to edit the item.
 * This controller can:
 * -
 */
@RestController
@RequestMapping(value = "/api/" +
        AlbumRest.CATEGORY + "/" +
        AlbumRest.PLURAL_NAME + "/" +
        REGEX_REQUESTMAPPING_IDENTIFIER_AS_UUID + "/" +
        AlbumRest.ITEM)
public class AlbumItemRestController {

    @Autowired
    AlbumService albumService;

    @Autowired
    ConverterService converter;

    @Autowired
    Utils utils;

    @RequestMapping(method = POST, consumes = "text/uri-list")
    public ItemRest addItem(@PathVariable UUID uuid, HttpServletRequest request)
            throws SQLException {

        Context context = obtainContext(request);
        Album album = getAlbum(context, uuid);
        Item item = getItem(context, request);

        if (album.getItem() != null) {
            throw new DSpaceBadRequestException("This album is already linked to an item");
        }
        if (albumService.findByItem(context, item).isPresent()) {
            throw new DSpaceBadRequestException("This item is already linked to an album");
        }

        album.setItem(item);
        context.commit();

        return converter.toRest(context.reloadEntity(item), utils.obtainProjection());
    }

    @RequestMapping(method = PUT, consumes = "text/uri-list")
    public ItemRest moveItem(@PathVariable UUID uuid, HttpServletRequest request)
            throws SQLException {

        Context context = obtainContext(request);
        Album album = getAlbum(context, uuid);
        Item item = getItem(context, request);

        albumService.findByItem(context, item).ifPresent(otherAlbum -> otherAlbum.setItem(null));
        album.setItem(item);
        context.commit();

        return converter.toRest(context.reloadEntity(item), utils.obtainProjection());
    }

    @RequestMapping(method = DELETE)
    public void removeItem(@PathVariable UUID uuid, HttpServletRequest request, HttpServletResponse response)
            throws SQLException {

        Context context = obtainContext(request);
        Album album = getAlbum(context, uuid);
        album.setItem(null);
        context.commit();
        response.setStatus(SC_NO_CONTENT);
    }

    private Album getAlbum(Context context, @PathVariable UUID uuid) {
        return albumService.find(context, uuid).orElseThrow(() -> new ResourceNotFoundException(
                AlbumRest.CATEGORY + "." + AlbumRest.NAME + " with id: " + uuid + " not found"
        ));
    }

    private Item getItem(Context context, HttpServletRequest request) {

        List<String> uriList = utils.getStringListFromRequest(request);
        if (uriList.size() != 1) {
            throw new UnprocessableEntityException("A single uri should be provided");
        }
        List<DSpaceObject> dsoList = utils.constructDSpaceObjectList(context, uriList);
        if (dsoList.size() != 1 || dsoList.get(0).getType() != ITEM) {
            throw new UnprocessableEntityException("The uri cannot be resolved to an item");
        }
        return (Item) dsoList.get(0);
    }
}

/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.rest;

import static java.util.UUID.randomUUID;
import static org.dspace.app.rest.model.AlbumRest.DATE_FORMAT;
import static org.dspace.builder.AlbumBuilder.createAlbum;
import static org.dspace.builder.CollectionBuilder.createCollection;
import static org.dspace.builder.CommunityBuilder.createCommunity;
import static org.dspace.builder.ItemBuilder.createItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.dspace.app.rest.test.AbstractControllerIntegrationTest;
import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.Item;
import org.dspace.music.Album;
import org.dspace.music.service.AlbumService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AlbumItemRestControllerIT extends AbstractControllerIntegrationTest {

    @Autowired
    private AlbumService albumService;

    private Item item1;
    private Item item2;
    private Album album1;
    private Album album2;
    private Collection collection;

    @Before
    public void setup() throws Exception {

        context.turnOffAuthorisationSystem();

        Community community = createCommunity(context)
                .build();
        collection = createCollection(context, community)
                .build();
        item1 = createItem(context, collection)
                .build();
        item2 = createItem(context, collection)
                .build();

        album1 = createAlbum(context, "test title 1", "test artist 1")
                .withReleaseDate(DATE_FORMAT.parse("1988-04-07"))
                .withItem(item1)
                .build();
        album2 = createAlbum(context, "test title 2", "test artist 2")
                .build();
    }

    @Test
    public void testAddAlbumItem() throws Exception {

        String authToken = getAuthToken(eperson.getEmail(), password);

        getClient(authToken)
                .perform(post("/api/music/albums/" + album2.getID() + "/item")
                        .content("/api/core/items/" + item2.getID())
                        .contentType(textUriContentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item2.getID() + "")));

        getClient(authToken)
                .perform(get("/api/music/albums/" + album2.getID() + "/item"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item2.getID() + "")));
    }

    @Test
    public void testAddAlbumItemBadRequest() throws Exception {

        String authToken = getAuthToken(admin.getEmail(), password);

        getClient(authToken)
                .perform(post("/api/music/albums/" + album1.getID() + "/item")
                        .content("/api/core/items/" + item2.getID())
                        .contentType(textUriContentType))
                .andExpect(status().isBadRequest());

        getClient(authToken)
                .perform(post("/api/music/albums/" + album2.getID() + "/item")
                        .content("/api/core/items/" + item1.getID())
                        .contentType(textUriContentType))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddAlbumItemNotFound() throws Exception {

        String authToken = getAuthToken(admin.getEmail(), password);
        getClient(authToken)
                .perform(post("/api/music/albums/" + randomUUID() + "/item")
                        .content("/api/core/items/" + item2.getID())
                        .contentType(textUriContentType))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddAlbumItemUnprocessableEntity() throws Exception {

        String authToken = getAuthToken(admin.getEmail(), password);
        getClient(authToken)
                .perform(post("/api/music/albums/" + album1.getID() + "/item")
                        .content("/api/core/items/" + randomUUID())
                        .contentType(textUriContentType))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testMoveAlbumItem() throws Exception {

        String authToken = getAuthToken(eperson.getEmail(), password);

        getClient(getAuthToken(eperson.getEmail(), password))
                .perform(put("/api/music/albums/" + album2.getID() + "/item")
                        .content("/api/core/items/" + item1.getID())
                        .contentType(textUriContentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item1.getID() + "")));

        getClient(authToken)
                .perform(get("/api/music/albums/" + album2.getID() + "/item"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item1.getID() + "")));

        getClient(authToken)
                .perform(get("/api/music/albums/" + album1.getID() + "/item"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testMoveAlbumItemNotFound() throws Exception {

        String authToken = getAuthToken(admin.getEmail(), password);
        getClient(authToken)
                .perform(put("/api/music/albums/" + randomUUID() + "/item")
                        .content("/api/core/items/" + item1.getID())
                        .contentType(textUriContentType))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testMoveAlbumItemUnprocessableEntity() throws Exception {

        String authToken = getAuthToken(admin.getEmail(), password);
        getClient(authToken)
                .perform(put("/api/music/albums/" + album2.getID() + "/item")
                        .content("/api/core/items/" + randomUUID())
                        .contentType(textUriContentType))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testRemoveAlbumItem() throws Exception {

        String authToken = getAuthToken(admin.getEmail(), password);

        getClient(authToken)
                .perform(delete("/api/music/albums/" + album1.getID() + "/item"))
                .andExpect(status().isNoContent());

        getClient(authToken)
                .perform(get("/api/music/albums/" + album1.getID() + "/item"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testRemoveAlbumItemNotFound() throws Exception {

        String authToken = getAuthToken(admin.getEmail(), password);
        getClient(authToken)
                .perform(delete("/api/music/albums/" + randomUUID() + "/item"))
                .andExpect(status().isNotFound());
    }
}

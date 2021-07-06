/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.rest;

import static org.dspace.app.rest.matcher.AlbumMatcher.matchAlbum;
import static org.dspace.app.rest.model.AlbumRest.DATE_FORMAT;
import static org.dspace.builder.AlbumBuilder.createAlbum;
import static org.dspace.builder.CollectionBuilder.createCollection;
import static org.dspace.builder.CommunityBuilder.createCommunity;
import static org.dspace.builder.ItemBuilder.createItem;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

public class AlbumRestRepositoryIT extends AbstractControllerIntegrationTest {

    @Autowired
    private AlbumService albumService;

    private Item item;
    private Album album1;
    private Album album2;
    private Album album3;

    @Before
    public void setup() throws Exception {

        context.turnOffAuthorisationSystem();

        Community community = createCommunity(context)
                .build();
        Collection collection = createCollection(context, community)
                .build();
        item = createItem(context, collection)
                .build();

        album1 = createAlbum(context, "test title 1", "test artist 1")
                .withReleaseDate(DATE_FORMAT.parse("1988-04-07"))
                .withItem(item)
                .build();
        album2 = createAlbum(context, "test title 2", "test artist 2")
                .build();
        album3 = createAlbum(context, "test title 3", "test artist 3")
                .build();
    }

    @Test
    public void testFindOne() throws Exception {

        String authToken = getAuthToken(admin.getEmail(), password);
        getClient(authToken)
                .perform(get("/api/music/albums/" + album1.getID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", matchAlbum(album1)));
    }

    @Test
    public void testFindAll() throws Exception {

        String authToken = getAuthToken(admin.getEmail(), password);
        getClient(authToken)
                .perform(get("/api/music/albums/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.albums", containsInAnyOrder(
                        matchAlbum(album1),
                        matchAlbum(album2),
                        matchAlbum(album3)
                )));
    }

    @Test
    public void testFindAllWithPagination() throws Exception {

        String authToken = getAuthToken(admin.getEmail(), password);

        getClient(authToken)
                .perform(get("/api/music/albums/")
                        .param("size", "2")
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.number", is(1)))
                .andExpect(jsonPath("$.page.size", is(2)))
                .andExpect(jsonPath("$.page.totalPages", is(2)))
                .andExpect(jsonPath("$.page.totalElements", is(3)));

        getClient(authToken)
                .perform(get("/api/music/albums/")
                        .param("size", "2")
                        .param("page", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.number", is(2)))
                .andExpect(jsonPath("$.page.size", is(2)))
                .andExpect(jsonPath("$.page.totalPages", is(2)))
                .andExpect(jsonPath("$.page.totalElements", is(3)));
    }

    @Test
    public void testAlbumItem() throws Exception {

        String authToken = getAuthToken(admin.getEmail(), password);
        getClient(authToken)
                .perform(get("/api/music/albums/" + album1.getID() + "/item"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getID() + "")));
    }
}

/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.rest;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_PATCH_JSON;
import static org.dspace.app.rest.matcher.AlbumMatcher.matchAlbum;
import static org.dspace.app.rest.model.AlbumRest.DATE_FORMAT;
import static org.dspace.builder.AlbumBuilder.createAlbum;
import static org.dspace.builder.CollectionBuilder.createCollection;
import static org.dspace.builder.CommunityBuilder.createCommunity;
import static org.dspace.builder.ItemBuilder.createItem;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Locale;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dspace.app.rest.model.AlbumRest;
import org.dspace.app.rest.model.patch.AddOperation;
import org.dspace.app.rest.model.patch.ReplaceOperation;
import org.dspace.app.rest.test.AbstractControllerIntegrationTest;
import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.Item;
import org.dspace.core.I18nUtil;
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
    private Album album4;
    private Album album5;
    private Album album6;
    private Album album7;

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
        album3 = createAlbum(context, "test title 3", "another test artist")
                .build();
        album4 = createAlbum(context, "test title 4", "another test artist")
                .build();
        album5 = createAlbum(context, "test title 5", "another test artist")
                .build();
        album6 = createAlbum(context, "test title 6", "another test artist")
                .build();
        album7 = createAlbum(context, "test title 7", "another test artist")
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
                        matchAlbum(album3),
                        matchAlbum(album4),
                        matchAlbum(album5),
                        matchAlbum(album6),
                        matchAlbum(album7)
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
                .andExpect(jsonPath("$.page.totalPages", is(4)))
                .andExpect(jsonPath("$.page.totalElements", is(7)));

        getClient(authToken)
                .perform(get("/api/music/albums/")
                        .param("size", "2")
                        .param("page", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.number", is(2)))
                .andExpect(jsonPath("$.page.size", is(2)))
                .andExpect(jsonPath("$.page.totalPages", is(4)))
                .andExpect(jsonPath("$.page.totalElements", is(7)));
    }

    @Test
    public void testAlbumItem() throws Exception {

        String authToken = getAuthToken(admin.getEmail(), password);
        getClient(authToken)
                .perform(get("/api/music/albums/" + album1.getID() + "/item"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getID() + "")));
    }

    @Test
    public void testCreate() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        AlbumRest albumRest = new AlbumRest();
        albumRest.setTitle("test title");
        albumRest.setArtist("test artist");

        String authToken = getAuthToken(admin.getEmail(), password);
        getClient(authToken)
                .perform(post("/api/music/albums/")
                        .content(mapper.writeValueAsBytes(albumRest))
                                .contentType(contentType))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(not(empty()))))
                .andExpect(jsonPath("$.title", is("test title")))
                .andExpect(jsonPath("$.artist", is("test artist")));
    }

    @Test
    public void testCreateUnauthorized() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        AlbumRest albumRest = new AlbumRest();
        albumRest.setTitle("test title");
        albumRest.setArtist("test artist");

        getClient()
                .perform(post("/api/music/albums/")
                        .content(mapper.writeValueAsBytes(albumRest))
                                .contentType(contentType))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testCreateForbidden() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        AlbumRest albumRest = new AlbumRest();
        albumRest.setTitle("test title");
        albumRest.setArtist("test artist");

        String authToken = getAuthToken(eperson.getEmail(), password);
        getClient(authToken)
                .perform(post("/api/music/albums/")
                        .content(mapper.writeValueAsBytes(albumRest))
                                .contentType(contentType))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testCreateUnprocessableEntity() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        AlbumRest albumRest = new AlbumRest();
        albumRest.setArtist("test artist");

        String authToken = getAuthToken(admin.getEmail(), password);
        getClient(authToken)
                .perform(post("/api/music/albums/")
                        .content(mapper.writeValueAsBytes(albumRest))
                        .contentType(contentType)
                        .header("Accept-Language", "pl"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(status().reason(is(I18nUtil.getMessage(
                        "org.dspace.app.rest.exception.Album.title_missing", new Locale("pl")
                ))));
    }

    @Test
    public void testUpdate() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        AlbumRest albumRest = new AlbumRest();
        albumRest.setTitle("test title updated");
        albumRest.setArtist("test artist updated");

        String authToken = getAuthToken(admin.getEmail(), password);
        getClient(authToken)
                .perform(put("/api/music/albums/" + album1.getID())
                        .content(mapper.writeValueAsBytes(albumRest))
                        .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(album1.getID() + "")))
                .andExpect(jsonPath("$.title", is("test title updated")))
                .andExpect(jsonPath("$.artist", is("test artist updated")));
    }

    @Test
    public void testUpdateUnauthorized() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        AlbumRest albumRest = new AlbumRest();
        albumRest.setTitle("test title updated");
        albumRest.setArtist("test artist updated");

        getClient()
                .perform(put("/api/music/albums/" + album1.getID())
                        .content(mapper.writeValueAsBytes(albumRest))
                        .contentType(contentType))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testUpdateForbidden() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        AlbumRest albumRest = new AlbumRest();
        albumRest.setTitle("test title updated");
        albumRest.setArtist("test artist updated");

        String authToken = getAuthToken(eperson.getEmail(), password);
        getClient(authToken)
                .perform(put("/api/music/albums/" + album1.getID())
                        .content(mapper.writeValueAsBytes(albumRest))
                        .contentType(contentType))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUpdateNotFound() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        AlbumRest albumRest = new AlbumRest();
        albumRest.setTitle("test title updated");
        albumRest.setArtist("test artist updated");

        String authToken = getAuthToken(admin.getEmail(), password);
        getClient(authToken)
                .perform(put("/api/music/albums/" + randomUUID())
                        .content(mapper.writeValueAsBytes(albumRest))
                        .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateUnprocessableEntity() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        AlbumRest albumRest = new AlbumRest();
        albumRest.setTitle("test title updated");

        String authToken = getAuthToken(admin.getEmail(), password);
        getClient(authToken)
                .perform(put("/api/music/albums/" + album1.getID())
                        .content(mapper.writeValueAsBytes(albumRest))
                        .contentType(contentType))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testPatch() throws Exception {

        String patchBody = getPatchContent(asList(
                new ReplaceOperation("/title", "test title patched"),
                new ReplaceOperation("/artist", "test artist patched")
        ));

        String authToken = getAuthToken(admin.getEmail(), password);
        getClient(authToken)
                .perform(patch("/api/music/albums/" + album1.getID())
                        .content(patchBody)
                        .contentType(APPLICATION_JSON_PATCH_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(album1.getID() + "")))
                .andExpect(jsonPath("$.title", is("test title patched")))
                .andExpect(jsonPath("$.artist", is("test artist patched")));
    }

    @Test
    public void testPatchUnauthorized() throws Exception {

        String patchBody = getPatchContent(asList(
                new ReplaceOperation("/title", "test title patched"),
                new ReplaceOperation("/artist", "test artist patched")
        ));

        getClient()
                .perform(patch("/api/music/albums/" + album1.getID())
                        .content(patchBody)
                        .contentType(APPLICATION_JSON_PATCH_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testPatchForbidden() throws Exception {

        String patchBody = getPatchContent(asList(
                new ReplaceOperation("/title", "test title patched"),
                new ReplaceOperation("/artist", "test artist patched")
        ));

        String authToken = getAuthToken(eperson.getEmail(), password);
        getClient(authToken)
                .perform(patch("/api/music/albums/" + album1.getID())
                        .content(patchBody)
                        .contentType(APPLICATION_JSON_PATCH_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testPatchNotFound() throws Exception {

        String patchBody = getPatchContent(asList(
                new ReplaceOperation("/title", "test title patched"),
                new ReplaceOperation("/artist", "test artist patched")
        ));

        String authToken = getAuthToken(admin.getEmail(), password);
        getClient(authToken)
                .perform(patch("/api/music/albums/" + randomUUID())
                        .content(patchBody)
                        .contentType(APPLICATION_JSON_PATCH_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPatchBadRequest() throws Exception {

        String patchBody = getPatchContent(asList(
                new AddOperation("/title", "test title patched")
        ));

        String authToken = getAuthToken(admin.getEmail(), password);
        getClient(authToken)
                .perform(patch("/api/music/albums/" + album1.getID())
                        .content(patchBody)
                        .contentType(APPLICATION_JSON_PATCH_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDelete() throws Exception {

        String authToken = getAuthToken(admin.getEmail(), password);
        getClient(authToken)
                .perform(delete("/api/music/albums/" + album1.getID()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteUnauthorized() throws Exception {

        getClient()
                .perform(delete("/api/music/albums/" + album1.getID()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testDeleteForbidden() throws Exception {

        String authToken = getAuthToken(eperson.getEmail(), password);
        getClient(authToken)
                .perform(delete("/api/music/albums/" + album1.getID()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteNotFound() throws Exception {

        String authToken = getAuthToken(admin.getEmail(), password);
        getClient(authToken)
                .perform(delete("/api/music/albums/" + randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindByArtist() throws Exception {

        String authToken = getAuthToken(admin.getEmail(), password);
        getClient(authToken)
                .perform(get("/api/music/albums/search/findByArtist")
                        .param("artist", "another test artist"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.albums", containsInAnyOrder(
                        matchAlbum(album3),
                        matchAlbum(album4),
                        matchAlbum(album5),
                        matchAlbum(album6),
                        matchAlbum(album7)
                )));
    }

    @Test
    public void testSearchLinks() throws Exception {

        String authToken = getAuthToken(admin.getEmail(), password);
        getClient(authToken)
                .perform(get("/api/music/albums/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links", hasJsonPath("$.findByArtist")));
    }
}

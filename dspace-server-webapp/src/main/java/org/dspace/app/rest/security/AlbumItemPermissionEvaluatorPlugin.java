/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.rest.security;

import static java.util.Arrays.asList;
import static org.dspace.app.rest.utils.ContextUtil.obtainContext;
import static org.dspace.core.Constants.READ;
import static org.dspace.core.Constants.WRITE;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.UUID;

import org.dspace.authorize.service.AuthorizeService;
import org.dspace.core.Context;
import org.dspace.music.service.AlbumService;
import org.dspace.services.RequestService;
import org.dspace.services.model.Request;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Permission Evaluator Plugin to check whether the current user has rights on the item linked to an album.
 */
@Component
public class AlbumItemPermissionEvaluatorPlugin extends RestObjectPermissionEvaluatorPlugin {

    private static final Logger log = getLogger(AlbumItemPermissionEvaluatorPlugin.class);

    private static final String TARGET_TYPE = "ALBUM";
    private static final String ITEM_READ_PERMISSION = "ITEM_READ";
    private static final String ITEM_WRITE_PERMISSION = "ITEM_WRITE";

    @Autowired
    private RequestService requestService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private AuthorizeService authorizeService;

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
                                 Object permission) {
        if (targetId == null ||
                !TARGET_TYPE.equals(targetType) ||
                !asList(ITEM_READ_PERMISSION, ITEM_WRITE_PERMISSION).contains(permission.toString())) {
            return false;
        }

        Request request = requestService.getCurrentRequest();
        Context context = obtainContext(request.getServletRequest());

        return albumService.find(context, UUID.fromString(targetId.toString()))
                .map(album -> {
                    try {
                        if (album.getItem() == null) {
                            return true;
                        }
                        switch (permission.toString()) {
                            case ITEM_READ_PERMISSION:
                                return authorizeService.authorizeActionBoolean(context, album.getItem(), READ);
                            case ITEM_WRITE_PERMISSION:
                                return authorizeService.authorizeActionBoolean(context, album.getItem(), WRITE);
                            default:
                        }
                    } catch (SQLException e) {
                        log.error(e.getMessage(), e);
                    }
                    return false;
                })
                .orElse(false);
    }

    @Override
    public boolean hasDSpacePermission(Authentication authentication, Serializable targetId, String targetType,
                                       DSpaceRestPermission restPermission) {
        return false;
    }
}

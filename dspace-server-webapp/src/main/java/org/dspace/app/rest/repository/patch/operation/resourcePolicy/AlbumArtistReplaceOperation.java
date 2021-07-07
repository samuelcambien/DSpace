/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.rest.repository.patch.operation.resourcePolicy;

import java.sql.SQLException;

import org.dspace.app.rest.exception.DSpaceBadRequestException;
import org.dspace.app.rest.model.patch.Operation;
import org.dspace.app.rest.repository.patch.operation.PatchOperation;
import org.dspace.core.Context;
import org.dspace.music.Album;
import org.springframework.stereotype.Component;

/**
 * Implementation for Album artist replace patch.
 *
 * Example: <code>
 * curl -X PATCH http://${dspace.server.url}/api/music/albums/<:albumId>
 *     -H "Content-Type: application/json"
 *     -d '[{ "op": "replace", "path": "/artist", "value": "New Artist"]'
 * </code>
 */
@Component
public class AlbumArtistReplaceOperation extends PatchOperation<Album> {

    @Override
    public Album perform(Context context, Album resource, Operation operation) throws SQLException {
        checkOperationValue(operation.getValue());
        if (this.supports(resource, operation)) {
            resource.setArtist((String) operation.getValue());
            return resource;
        } else {
            throw new DSpaceBadRequestException(this.getClass() + " does not support this operation");
        }
    }

    @Override
    public boolean supports(Object objectToMatch, Operation operation) {
        return objectToMatch instanceof Album &&
                operation.getOp().trim().equalsIgnoreCase(OPERATION_REPLACE) &&
                operation.getPath().trim().equalsIgnoreCase("/artist");
    }
}

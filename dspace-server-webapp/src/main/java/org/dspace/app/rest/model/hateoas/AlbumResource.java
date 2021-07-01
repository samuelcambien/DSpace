/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.rest.model.hateoas;

import org.dspace.app.rest.model.AlbumRest;
import org.dspace.app.rest.model.hateoas.annotations.RelNameDSpaceResource;
import org.dspace.app.rest.utils.Utils;
import org.dspace.scripts.Process;

/**
 * The Resource representation of a {@link Process} object
 */
@RelNameDSpaceResource(AlbumRest.NAME)
public class AlbumResource extends DSpaceResource<AlbumRest> {
    public AlbumResource(AlbumRest content, Utils utils) {
        super(content, utils);
    }
}

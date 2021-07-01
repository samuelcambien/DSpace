/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.music.dao;

import java.sql.SQLException;
import java.util.List;

import org.dspace.core.Context;
import org.dspace.core.GenericDAO;
import org.dspace.music.Album;

/**
 * This is the Data Access Object for the {@link Album} object
 */
public interface AlbumDAO extends GenericDAO<Album> {

    int countRows(Context context) throws SQLException;

    List<Album> findByArtist(Context context, String artist) throws SQLException;
}

/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.music.dao.impl;

import java.sql.SQLException;
import java.util.List;
import javax.persistence.Query;

import org.dspace.core.AbstractHibernateDAO;
import org.dspace.core.Context;
import org.dspace.music.Album;
import org.dspace.music.dao.AlbumDAO;

/**
 *
 * Implementation class for {@link AlbumDAO}
 */
public class AlbumDAOImpl extends AbstractHibernateDAO<Album> implements AlbumDAO {

    @Override
    public int countRows(Context context) throws SQLException {
        return count(createQuery(context, "SELECT count(*) FROM Album"));
    }

    @Override
    public List<Album> findByArtist(Context context, String artist) throws SQLException {
        Query query = createQuery(context, "FROM Album WHERE artist = :artist");
        query.setParameter("artist", artist);
        return list(query);
    }
}



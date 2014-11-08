/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.content.dao;

import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.Item;
import org.dspace.content.MetadataField;
import org.dspace.core.Context;
import org.dspace.eperson.EPerson;

import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;

public interface ItemDAO extends DSpaceObjectLegacySupportDAO<Item>
{
    public Iterator<Item> findAll(Context context, boolean archived) throws SQLException;

    public Iterator<Item> findAll(Context context, boolean archived, boolean withdrawn) throws SQLException;

    public Iterator<Item> findBySubmitter(Context context, EPerson eperson) throws SQLException;

    public Iterator<Item> findBySubmitter(Context context, EPerson eperson, MetadataField metadataField) throws SQLException;

    public Iterator<Item> findByMetadataField(Context context, MetadataField metadataField, String value, boolean inArchive) throws SQLException;

    public Iterator<Item> findByAuthorityValue(Context context, MetadataField metadataField, String authority, boolean inArchive) throws SQLException;

    public Iterator<Item> findArchivedByCollection(Context context, Collection collection, Integer limit, Integer offset) throws SQLException;

    public Iterator<Item> findAllByCollection(Context context, Collection collection) throws SQLException;

    public int countItems(Context context, Collection collection, boolean includeArchived, boolean includeWithdrawn) throws SQLException;
}

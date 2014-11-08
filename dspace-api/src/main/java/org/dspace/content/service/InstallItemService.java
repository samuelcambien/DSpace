package org.dspace.content.service;

import org.dspace.authorize.AuthorizeException;
import org.dspace.content.InProgressSubmission;
import org.dspace.content.Item;
import org.dspace.core.Context;

import java.io.IOException;
import java.sql.SQLException;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 20/11/14
 * Time: 15:44
 */
public interface InstallItemService {

    /**
     * Take an InProgressSubmission and turn it into a fully-archived Item,
     * creating a new Handle.
     *
     * @param context
     *            DSpace Context
     * @param is
     *            submission to install
     *
     * @return the fully archived Item
     */
    public Item installItem(Context context, InProgressSubmission is)
            throws SQLException, AuthorizeException;

    /**
     * Take an InProgressSubmission and turn it into a fully-archived Item.
     *
     * @param c  current context
     * @param is
     *            submission to install
     * @param suppliedHandle
     *            the existing Handle to give to the installed item
     *
     * @return the fully archived Item
     */
    public Item installItem(Context c, InProgressSubmission is,
            String suppliedHandle) throws SQLException,
            IOException, AuthorizeException;

    /**
     * Turn an InProgressSubmission into a fully-archived Item, for
     * a "restore" operation such as ingestion of an AIP to recreate an
     * archive.  This does NOT add any descriptive metadata (e.g. for
     * provenance) to preserve the transparency of the ingest.  The
     * ingest mechanism is assumed to have set all relevant technical
     * and administrative metadata fields.
     *
     * @param c  current context
     * @param is
     *            submission to install
     * @param suppliedHandle
     *            the existing Handle to give the installed item, or null
     *            to create a new one.
     *
     * @return the fully archived Item
     */
    public Item restoreItem(Context c, InProgressSubmission is,
            String suppliedHandle) throws SQLException, IOException, AuthorizeException;

    /**
     * Generate provenance-worthy description of the bitstreams contained in an
     * item.
     *
     * @param myitem  the item to generate description for
     *
     * @return provenance description
     */
    public String getBitstreamProvenanceMessage(Item myitem)
    						throws SQLException;

}

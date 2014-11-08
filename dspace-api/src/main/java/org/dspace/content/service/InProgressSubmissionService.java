package org.dspace.content.service;

import org.dspace.authorize.AuthorizeException;
import org.dspace.content.InProgressSubmission;
import org.dspace.core.Context;

import java.io.IOException;
import java.sql.SQLException;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 3/11/14
 * Time: 10:16
 */
public interface InProgressSubmissionService<T extends InProgressSubmission> {

    /**
     * Deletes submission wrapper, doesn't delete item contents
     */
    public void deleteWrapper(Context context, T inProgressSubmission) throws SQLException, AuthorizeException;

    /**
     * Update the submission, including the unarchived item.
     */
    public void update(Context context, T inProgressSubmission) throws SQLException, AuthorizeException;
}

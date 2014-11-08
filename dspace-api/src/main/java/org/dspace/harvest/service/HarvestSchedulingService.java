package org.dspace.harvest.service;

import org.dspace.authorize.AuthorizeException;

import java.io.IOException;
import java.sql.SQLException;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 18/11/14
 * Time: 09:25
 */
public interface HarvestSchedulingService {

    /**
     * Start harvest scheduler.
     */
    void startNewScheduler() throws SQLException, AuthorizeException;

    /**
     * Stop an active harvest scheduler.
     */
    void stopScheduler() throws SQLException, AuthorizeException;

    /**
   	 * Pause an active harvest scheduler.
   	 */
    void pauseScheduler() throws SQLException, AuthorizeException;

    /**
   	 * Resume a paused harvest scheduler.
   	 */
    void resumeScheduler() throws SQLException, AuthorizeException;


    void resetScheduler() throws SQLException, AuthorizeException, IOException;
}

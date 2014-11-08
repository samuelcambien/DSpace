package org.dspace.core.service;

import org.dspace.core.Context;

import java.sql.SQLException;

/**
 * This interface allows the developer to retrieve a context when the DSpace kernel has not yet been started.
 *
 * User: kevin (kevin at atmire.com)
 * Date: 5/02/15
 * Time: 11:40
 */
public interface ContextFactoryService {

    /**
     * Retrieve a context when the DSpace kernel isn't available yet
     * @return a dspace context
     * @throws SQLException
     */
    public Context getContextNoKernel() throws SQLException;
}

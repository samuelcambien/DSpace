package org.dspace.core;

import org.dspace.core.service.ContextFactoryService;
import org.dspace.event.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 5/02/15
 * Time: 11:42
 */
public class ContextFactoryServiceImpl implements ContextFactoryService {

    @Autowired(required = true)
    protected EventService eventService;

    @Autowired(required = true)
    protected DBConnection dbConnection;

    @Override
    public Context getContextNoKernel() throws SQLException {
        return new Context(eventService, dbConnection);
    }
}

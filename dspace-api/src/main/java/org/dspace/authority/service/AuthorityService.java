package org.dspace.authority.service;

import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Item;
import org.dspace.core.Context;

import java.sql.SQLException;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 3/12/14
 * Time: 12:37
 */
public interface AuthorityService {

    public void indexItem(Context context, Item item) throws SQLException, AuthorizeException;

    public boolean isConfigurationValid();

}

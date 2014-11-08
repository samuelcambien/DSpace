package org.dspace.app.requestitem.service;

import org.dspace.app.requestitem.RequestItem;
import org.dspace.content.Bitstream;
import org.dspace.content.Item;
import org.dspace.core.Context;

import java.sql.SQLException;

/**
 * Created by kevin on 29/10/14.
 */
public interface RequestItemService {

    /**
     * Generate a unique id of the request and put it into the DB
     * @param context
     * @return
     * @throws java.sql.SQLException
     */
    public String createRequest(Context context, Bitstream bitstream, Item item, boolean allFiles, String reqEmail, String reqName, String reqMessage)
            throws SQLException;

    public RequestItem findByToken(Context context, String token);

    /**
     * Save updates to the record. Only accept_request, and decision_date are set-able.
     * @param context
     */
    public void update(Context context, RequestItem requestItem);



}

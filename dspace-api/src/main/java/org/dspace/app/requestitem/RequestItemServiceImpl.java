package org.dspace.app.requestitem;

import org.apache.log4j.Logger;
import org.dspace.app.requestitem.dao.RequestItemDAO;
import org.dspace.app.requestitem.service.RequestItemService;
import org.dspace.content.Bitstream;
import org.dspace.content.Item;
import org.dspace.core.Context;
import org.dspace.core.Utils;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.Date;

/**
 * Created by kevin on 29/10/14.
 */
public class RequestItemServiceImpl implements RequestItemService {

    private final Logger log = Logger.getLogger(RequestItemServiceImpl.class);

    @Autowired(required = true)
    protected RequestItemDAO requestItemDAO;

    @Override
    public String createRequest(Context context, Bitstream bitstream, Item item, boolean allFiles, String reqEmail, String reqName, String reqMessage) throws SQLException {
        RequestItem requestItem = requestItemDAO.create(context, new RequestItem());

        requestItem.setToken(Utils.generateHexKey());
        requestItem.setBitstream(bitstream);
        requestItem.setItem(item);
        requestItem.setAllfiles(allFiles);
        requestItem.setReqEmail(reqEmail);
        requestItem.setReqName(reqName);
        requestItem.setReqMessage(reqMessage);
        requestItem.setRequest_date(new Date());

        requestItemDAO.save(context, requestItem);

        if (log.isDebugEnabled())
        {
            log.debug("Created requestitem_token " + requestItem.getID()
                    + " with token " + requestItem.getToken() +  "\"");
        }
        return requestItem.getToken();
    }

    @Override
    public RequestItem findByToken(Context context, String token) {
        try {
            return requestItemDAO.findByToken(context, token);
        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public void update(Context context, RequestItem requestItem) {
        try {
            requestItemDAO.save(context, requestItem);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }
}

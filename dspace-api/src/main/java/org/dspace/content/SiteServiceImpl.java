package org.dspace.content;

import org.dspace.content.dao.SiteDAO;
import org.dspace.content.service.SiteService;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.handle.service.HandleService;
import org.dspace.services.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 18/07/14
 * Time: 16:09
 */
public class SiteServiceImpl implements SiteService {

    @Autowired(required = true)
    protected HandleService handleService;

    @Autowired(required = true)
    protected ConfigurationService configurationService;

    @Autowired(required = true)
    protected SiteDAO siteDAO;

    @Override
    public Site createSite(Context context) throws SQLException {
        Site site = findSite(context);
        if(site == null)
        {
            //Only one site can be created at any point in time
            site = siteDAO.create(context, new Site());
            handleService.createHandle(context, site, configurationService.getProperty("handle.prefix") + "/0");
        }
        return site;
    }

    @Override
    public Site findSite(Context context) throws SQLException {
        return siteDAO.findSite(context);
    }
}

package org.dspace.storage.rdbms;

import org.apache.log4j.Logger;
import org.dspace.content.service.SiteService;
import org.dspace.core.Context;
import org.dspace.services.KernelStartupCallbackService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 13/02/15
 * Time: 14:35
 */
public class SiteServiceInitializer implements KernelStartupCallbackService {

    private Logger log = Logger.getLogger(SiteServiceInitializer.class);

    @Autowired(required = true)
    protected SiteService siteService;

    @Override
    public void executeCallback() {
        // After every migrate, ensure default Site is setup correctly.
        Context context = null;
        try
        {
            context = new Context();
            context.turnOffAuthorisationSystem();
            // While it's not really a formal "registry", we need to ensure the
            // default, required Groups exist in the DSpace database
            if(siteService.findSite(context) == null)
            {
                siteService.createSite(context);
            }
            context.restoreAuthSystemState();
            // Commit changes and close context
            context.complete();
        }
        catch(Exception e)
        {
            log.error("Error attempting to add/update default DSpace Groups", e);
        }
        finally
        {
            // Clean up our context, if it still exists & it was never completed
            if(context!=null && context.isValid())
                context.abort();
        }


    }
}

package org.dspace.app.itemexport.factory;

import org.dspace.app.itemexport.service.ItemExportService;
import org.dspace.eperson.service.AccountService;
import org.dspace.eperson.service.EPersonService;
import org.dspace.eperson.service.GroupService;
import org.dspace.eperson.service.RegistrationDataService;
import org.dspace.utils.DSpace;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 28/05/14
 * Time: 08:47
 */
public abstract class ItemExportServiceFactory {

    public abstract ItemExportService getItemExportService();

    public static ItemExportServiceFactory getInstance(){
        return new DSpace().getServiceManager().getServiceByName("itemExportServiceFactory", ItemExportServiceFactory.class);
    }
}

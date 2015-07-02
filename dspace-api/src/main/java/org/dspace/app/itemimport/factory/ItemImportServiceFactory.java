package org.dspace.app.itemimport.factory;

import org.dspace.app.itemimport.service.ItemImportService;
import org.dspace.utils.DSpace;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 28/05/14
 * Time: 08:47
 */
public abstract class ItemImportServiceFactory {

    public abstract ItemImportService getItemImportService();

    public static ItemImportServiceFactory getInstance(){
        return new DSpace().getServiceManager().getServiceByName("itemImportServiceFactory", ItemImportServiceFactory.class);
    }
}

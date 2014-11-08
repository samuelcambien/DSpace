package org.dspace.app.itemimport.factory;

import org.dspace.app.itemexport.service.ItemExportService;
import org.dspace.app.itemimport.service.ItemImportService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 28/05/14
 * Time: 08:48
 */
public class ItemImportServiceFactoryImpl extends ItemImportServiceFactory {

    @Autowired(required = true)
    private ItemImportService itemImportService;

    @Override
    public ItemImportService getItemImportService() {
        return itemImportService;
    }
}

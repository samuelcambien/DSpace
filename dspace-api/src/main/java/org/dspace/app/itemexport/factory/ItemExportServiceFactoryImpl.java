package org.dspace.app.itemexport.factory;

import org.dspace.app.itemexport.service.ItemExportService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 28/05/14
 * Time: 08:48
 */
public class ItemExportServiceFactoryImpl extends ItemExportServiceFactory {

    @Autowired(required = true)
    private ItemExportService itemExportService;

    @Override
    public ItemExportService getItemExportService() {
        return itemExportService;
    }
}

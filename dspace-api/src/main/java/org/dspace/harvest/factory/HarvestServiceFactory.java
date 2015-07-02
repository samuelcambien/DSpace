package org.dspace.harvest.factory;

import org.dspace.harvest.service.HarvestSchedulingService;
import org.dspace.harvest.service.HarvestedCollectionService;
import org.dspace.harvest.service.HarvestedItemService;
import org.dspace.utils.DSpace;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 27/05/14
 * Time: 09:40
 */
public abstract class HarvestServiceFactory {

    public abstract HarvestedCollectionService getHarvestedCollectionService();

    public abstract HarvestedItemService getHarvestedItemService();

    public abstract HarvestSchedulingService getHarvestSchedulingService();

    public static HarvestServiceFactory getInstance()
    {
        return new DSpace().getServiceManager().getServiceByName("harvestServiceFactory", HarvestServiceFactory.class);
    }
}

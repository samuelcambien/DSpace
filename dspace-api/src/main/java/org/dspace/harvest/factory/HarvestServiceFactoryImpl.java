package org.dspace.harvest.factory;

import org.dspace.handle.service.HandleService;
import org.dspace.harvest.service.HarvestSchedulingService;
import org.dspace.harvest.service.HarvestedCollectionService;
import org.dspace.harvest.service.HarvestedItemService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 27/05/14
 * Time: 09:44
 */
public class HarvestServiceFactoryImpl extends HarvestServiceFactory {

    @Autowired(required = true)
    private HarvestedItemService harvestedItemService;
    @Autowired(required = true)
    private HarvestedCollectionService harvestedCollectionService;
    @Autowired(required = true)
    private HarvestSchedulingService harvestSchedulingService;

    @Override
    public HarvestedCollectionService getHarvestedCollectionService() {
        return harvestedCollectionService;
    }

    @Override
    public HarvestedItemService getHarvestedItemService() {
        return harvestedItemService;
    }

    @Override
    public HarvestSchedulingService getHarvestSchedulingService() {
        return harvestSchedulingService;
    }
}

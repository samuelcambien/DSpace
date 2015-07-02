package org.dspace.statistics.factory;

import org.dspace.statistics.service.ElasticSearchLoggerService;
import org.dspace.statistics.service.SolrLoggerService;
import org.dspace.utils.DSpace;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 27/05/14
 * Time: 09:40
 */
public abstract class StatisticsServiceFactory {

    public abstract SolrLoggerService getSolrLoggerService();

    public abstract ElasticSearchLoggerService getElasticSearchLoggerService();

    public static StatisticsServiceFactory getInstance()
    {
        return new DSpace().getServiceManager().getServiceByName("statisticsServiceFactory", StatisticsServiceFactory.class);
    }
}

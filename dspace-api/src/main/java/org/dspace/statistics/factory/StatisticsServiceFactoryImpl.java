package org.dspace.statistics.factory;

import org.dspace.statistics.service.ElasticSearchLoggerService;
import org.dspace.statistics.service.SolrLoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 27/05/14
 * Time: 09:44
 */
public class StatisticsServiceFactoryImpl extends StatisticsServiceFactory {

    @Autowired(required = true)
    @Lazy
    private ElasticSearchLoggerService elasticSearchLogger;

    @Autowired(required = true)
    private SolrLoggerService solrLoggerService;

    @Override
    public SolrLoggerService getSolrLoggerService() {
        return solrLoggerService;
    }

    @Override
    public ElasticSearchLoggerService getElasticSearchLoggerService() {
        return elasticSearchLogger;
    }
}

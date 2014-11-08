package org.dspace.authority.factory;

import org.dspace.authority.AuthoritySearchService;
import org.dspace.authority.AuthorityTypes;
import org.dspace.authority.indexer.AuthorityIndexerInterface;
import org.dspace.authority.indexer.AuthorityIndexingService;
import org.dspace.authority.service.AuthorityService;
import org.dspace.authority.service.AuthorityValueService;
import org.dspace.utils.DSpace;

import java.util.List;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 27/05/14
 * Time: 09:40
 */
public abstract class AuthorityServiceFactory {

    public abstract AuthorityValueService getAuthorityValueService();

    public abstract AuthorityTypes getAuthorTypes();

    public abstract AuthorityIndexingService getAuthorityIndexingService();

    public abstract AuthoritySearchService getAuthoritySearchService();

    public abstract AuthorityService getAuthorityService();

    public abstract List<AuthorityIndexerInterface> getAuthorityIndexers();

    public static AuthorityServiceFactory getInstance()
    {
        return new DSpace().getServiceManager().getServiceByName("authorityServiceFactory", AuthorityServiceFactory.class);
    }
}

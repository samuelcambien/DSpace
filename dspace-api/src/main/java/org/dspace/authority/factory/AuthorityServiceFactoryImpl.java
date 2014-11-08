package org.dspace.authority.factory;

import org.dspace.authority.AuthoritySearchService;
import org.dspace.authority.AuthorityTypes;
import org.dspace.authority.indexer.AuthorityIndexerInterface;
import org.dspace.authority.indexer.AuthorityIndexingService;
import org.dspace.authority.service.AuthorityService;
import org.dspace.authority.service.AuthorityValueService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 27/05/14
 * Time: 09:44
 */
public class AuthorityServiceFactoryImpl extends AuthorityServiceFactory {

    @Autowired(required = true)
    private AuthorityValueService authorityValueService;

    @Autowired(required = true)
    private AuthorityTypes authorityTypes;

    @Autowired(required = true)
    private AuthorityService authorityService;

    @Autowired(required = true)
    private AuthorityIndexingService authorityIndexingService;

    @Autowired(required = true)
    private AuthoritySearchService authoritySearchService;

    @Autowired(required = true)
    private List<AuthorityIndexerInterface> authorityIndexerInterfaces;

    @Override
    public AuthorityValueService getAuthorityValueService() {
        return authorityValueService;
    }

    @Override
    public AuthorityTypes getAuthorTypes() {
        return authorityTypes;
    }

    @Override
    public AuthorityIndexingService getAuthorityIndexingService() {
        return authorityIndexingService;
    }

    @Override
    public AuthoritySearchService getAuthoritySearchService() {
        return authoritySearchService;
    }

    @Override
    public AuthorityService getAuthorityService() {
        return authorityService;
    }

    @Override
    public List<AuthorityIndexerInterface> getAuthorityIndexers() {
        return authorityIndexerInterfaces;
    }
}

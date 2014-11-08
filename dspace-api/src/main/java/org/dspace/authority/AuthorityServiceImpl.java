package org.dspace.authority;

import org.dspace.authority.indexer.AuthorityIndexerInterface;
import org.dspace.authority.indexer.AuthorityIndexingService;
import org.dspace.authority.service.AuthorityService;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Item;
import org.dspace.core.Context;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.List;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 8/12/14
 * Time: 08:51
 */
public class AuthorityServiceImpl implements AuthorityService{


    @Autowired(required = true)
    protected AuthorityIndexingService indexingService;
    @Autowired(required = true)
    protected List<AuthorityIndexerInterface> indexers;

    @Override
    public void indexItem(Context context, Item item) throws SQLException, AuthorizeException {
        if(!isConfigurationValid()){
            //Cannot index, configuration not valid
            return;
        }

        for (AuthorityIndexerInterface indexerInterface : indexers) {

            indexerInterface.init(context , item);
            while (indexerInterface.hasMore()) {
                AuthorityValue authorityValue = indexerInterface.nextValue();
                if(authorityValue != null)
                    indexingService.indexContent(authorityValue, true);
            }
            //Close up
            indexerInterface.close();
        }
        //Commit to our server
        indexingService.commit();
    }

    @Override
    public boolean isConfigurationValid()
    {
        if(!indexingService.isConfiguredProperly()){
            return false;
        }

        for (AuthorityIndexerInterface indexerInterface : indexers) {
            if(!indexerInterface.isConfiguredProperly()){
                return false;
            }
        }
        return true;
    }

}

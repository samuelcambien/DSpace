package org.dspace.disseminate.factory;

import org.dspace.authenticate.service.AuthenticationService;
import org.dspace.disseminate.service.CitationDocumentService;
import org.dspace.utils.DSpace;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 27/05/14
 * Time: 09:40
 */
public abstract class DisseminateServiceFactory {

    public abstract CitationDocumentService getCitationDocumentService();

    public static DisseminateServiceFactory getInstance()
    {
        return new DSpace().getServiceManager().getServiceByName("disseminateServiceFactory", DisseminateServiceFactory.class);
    }
}

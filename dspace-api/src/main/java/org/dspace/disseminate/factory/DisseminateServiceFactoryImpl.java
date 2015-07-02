package org.dspace.disseminate.factory;

import org.dspace.disseminate.service.CitationDocumentService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 27/05/14
 * Time: 09:44
 */
public class DisseminateServiceFactoryImpl extends DisseminateServiceFactory {

    @Autowired(required = true)
    private CitationDocumentService citationDocumentService;

    @Override
    public CitationDocumentService getCitationDocumentService() {
        return citationDocumentService;
    }
}

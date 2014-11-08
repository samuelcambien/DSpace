package org.dspace.app.sfx.factory;

import org.dspace.app.mediafilter.service.MediaFilterService;
import org.dspace.app.sfx.service.SFXFileReaderService;
import org.dspace.utils.DSpace;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 28/05/14
 * Time: 08:47
 */
public abstract class SfxServiceFactory {

    public abstract SFXFileReaderService getSfxFileReaderService();

    public static SfxServiceFactory getInstance(){
        return new DSpace().getServiceManager().getServiceByName("sfxServiceFactory", SfxServiceFactory.class);
    }
}

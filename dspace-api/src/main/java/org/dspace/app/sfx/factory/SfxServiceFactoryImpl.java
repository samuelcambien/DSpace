package org.dspace.app.sfx.factory;

import org.dspace.app.sfx.service.SFXFileReaderService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 28/05/14
 * Time: 08:48
 */
public class SfxServiceFactoryImpl extends SfxServiceFactory {

    @Autowired(required = true)
    private SFXFileReaderService sfxFileReaderService;

    @Override
    public SFXFileReaderService getSfxFileReaderService() {
        return sfxFileReaderService;
    }
}

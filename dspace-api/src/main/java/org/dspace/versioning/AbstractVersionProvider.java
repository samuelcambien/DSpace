/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.versioning;

import org.apache.commons.lang.ObjectUtils;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.*;
import org.dspace.content.service.BitstreamService;
import org.dspace.content.service.BundleService;
import org.dspace.content.service.ItemService;
import org.dspace.core.Context;
import org.dspace.storage.bitstore.BitstreamStorageServiceImpl;
import org.dspace.storage.bitstore.service.BitstreamStorageService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 *
 *
 * @author Fabio Bolognesi (fabio at atmire dot com)
 * @author Mark Diggory (markd at atmire dot com)
 * @author Ben Bosman (ben at atmire dot com)
 */
public abstract class AbstractVersionProvider {

    private Set<String> ignoredMetadataFields;

    @Autowired(required = true)
    protected BitstreamService bitstreamService;
    @Autowired(required = true)
    protected BitstreamStorageService bitstreamStorageService;
    @Autowired(required = true)
    protected BundleService bundleService;
    @Autowired(required = true)
    protected ItemService itemService;

    protected void copyMetadata(Context context, Item itemNew, Item nativeItem) throws SQLException {
        List<MetadataValue> md = itemService.getMetadata(nativeItem, Item.ANY, Item.ANY, Item.ANY, Item.ANY);
        for (MetadataValue aMd : md) {
            MetadataField metadataField = aMd.getMetadataField();
            MetadataSchema metadataSchema = metadataField.getMetadataSchema();
            String unqualifiedMetadataField = metadataSchema.getName() + "." + metadataField.getElement();
            if(getIgnoredMetadataFields().contains(metadataField.toString('.')) ||
                    getIgnoredMetadataFields().contains(unqualifiedMetadataField + "." + Item.ANY))
            {
                //Skip this metadata field
                continue;
            }

            itemService.addMetadata(context, itemNew, metadataField, aMd.getLanguage(), aMd.getValue(), aMd.getAuthority(), aMd.getConfidence());
        }
    }

    protected void createBundlesAndAddBitstreams(Context c, Item itemNew, Item nativeItem) throws SQLException, AuthorizeException, IOException {
        for(Bundle nativeBundle : nativeItem.getBundles())
        {
            Bundle bundleNew = bundleService.create(c, itemNew, nativeBundle.getName());

            for(BundleBitstream nativeBundleBitstream : nativeBundle.getBitstreams())
            {
                Bitstream nativeBitstream = nativeBundleBitstream.getBitstream();

                Bitstream bitstreamNew = createBitstream(c, nativeBitstream);
                bundleService.addBitstream(c, bundleNew, bitstreamNew);

                if(nativeBundle.getPrimaryBitstream() != null && nativeBundle.getPrimaryBitstream().getID().equals(nativeBitstream.getID()))
                {
                    bundleNew.setPrimaryBitstreamID(bitstreamNew);
                }
            }
        }
    }


    protected Bitstream createBitstream(Context context, Bitstream nativeBitstream) throws AuthorizeException, SQLException, IOException {
        Bitstream newBitstream = bitstreamStorageService.clone(context, nativeBitstream);
	    List<MetadataValue> bitstreamMeta = bitstreamService.getMetadata(nativeBitstream, Item.ANY, Item.ANY, Item.ANY, Item.ANY);
	    for (MetadataValue value : bitstreamMeta) {
		    bitstreamService.addMetadata(context, newBitstream, value.getMetadataField(), value.getLanguage(), value.getValue(), value.getAuthority(), value.getConfidence());
	    }
	    bitstreamService.update(context, newBitstream);
	    return newBitstream;
    }

    public void setIgnoredMetadataFields(Set<String> ignoredMetadataFields) {
        this.ignoredMetadataFields = ignoredMetadataFields;
    }

    public Set getIgnoredMetadataFields() {
        return ignoredMetadataFields;
    }
}

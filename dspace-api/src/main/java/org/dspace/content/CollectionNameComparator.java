package org.dspace.content;

import java.util.Comparator;

/**
 * Created by kevin on 06/12/14.
 */
public class CollectionNameComparator implements Comparator<Collection> {
    @Override
    public int compare(Collection collection1, Collection collection2) {
        return collection1.getName().compareTo(collection2.getName());
    }
}

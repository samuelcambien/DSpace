package org.dspace.event.service;

import org.dspace.event.Dispatcher;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 17/11/14
 * Time: 14:25
 */
public interface EventService {

    // The name of the default dispatcher assigned to every new context unless
    // overridden
    public static final String DEFAULT_DISPATCHER = "default";

    /**
     * Get dispatcher for configuration named by "name". Returns cached instance
     * if one exists.
     */
    public Dispatcher getDispatcher(String name);

    public void returnDispatcher(String key, Dispatcher disp);

    public int getConsumerIndex(String consumerClass);
}

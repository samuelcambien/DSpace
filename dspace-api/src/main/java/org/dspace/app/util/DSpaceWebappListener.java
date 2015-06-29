package org.dspace.app.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 25/06/15
 * Time: 15:42
 */
public class DSpaceWebappListener implements ServletContextListener {

    private AbstractDSpaceWebapp webApp;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        /*
        * Register that this application is running.
        */

       try {
           Class webappClass = Class.forName("org.dspace.utils.DSpaceWebapp");
           webApp = (AbstractDSpaceWebapp) webappClass.newInstance();
           webApp.register();
       } catch (ClassNotFoundException ex) {
           event.getServletContext().log("Can't create webapp MBean:  " + ex.getMessage());
       } catch (InstantiationException ex) {
           event.getServletContext().log("Can't create webapp MBean:  " + ex.getMessage());
       } catch (IllegalAccessException ex) {
           event.getServletContext().log("Can't create webapp MBean:  " + ex.getMessage());
       }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        webApp.deregister();
    }
}

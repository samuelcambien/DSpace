package org.dspace.eperson;

import org.dspace.authorize.AuthorizeException;
import org.dspace.core.Context;
import org.dspace.eperson.factory.EPersonServiceFactory;
import org.dspace.eperson.service.GroupService;

import java.sql.SQLException;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 24/10/14
 * Time: 16:21
 */
public class GroupCLITool {

    /**
     * Main script used to set the group names for anonymous group & admin group, only to be called once on DSpace fresh_install
     * @param args not used
     * @throws java.sql.SQLException database exception
     * @throws org.dspace.authorize.AuthorizeException should not occur since we disable authentication for this method.
     */
    public static void main(String[] args) throws SQLException, AuthorizeException {
        Context context = new Context();
        context.turnOffAuthorisationSystem();

        EPersonServiceFactory.getInstance().getGroupService().initDefaultGroupNames(context);

        //Clear the events to avoid the consumers which aren't needed at this time
        context.getEvents().clear();
        context.complete();
    }
}

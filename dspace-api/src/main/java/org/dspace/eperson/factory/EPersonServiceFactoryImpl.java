package org.dspace.eperson.factory;

import org.dspace.eperson.service.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 28/05/14
 * Time: 08:48
 */
public class EPersonServiceFactoryImpl extends EPersonServiceFactory {

    @Autowired(required = true)
    private GroupService groupService;
    @Autowired(required = true)
    private EPersonService epersonService;
    @Autowired(required = true)
    private RegistrationDataService registrationDataService;
    @Autowired(required = true)
    private AccountService accountService;
    @Autowired(required = true)
    private SubscribeService subscribeService;
    @Autowired(required = true)
    private SupervisorService supervisorService;

    @Override
    public EPersonService getEPersonService() {
        return epersonService;
    }

    @Override
    public GroupService getGroupService() {
        return groupService;
    }

    @Override
    public RegistrationDataService getRegistrationDataService() {
        return registrationDataService;
    }

    @Override
    public AccountService getAccountService() {
        return accountService;
    }

    @Override
    public SubscribeService getSubscribeService() {
        return subscribeService;
    }

    @Override
    public SupervisorService getSupervisorService() {
        return supervisorService;
    }
}

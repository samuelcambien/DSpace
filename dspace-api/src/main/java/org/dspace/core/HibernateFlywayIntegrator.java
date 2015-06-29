package org.dspace.core;

import org.dspace.storage.rdbms.DatabaseLegacyReindexer;
import org.dspace.storage.rdbms.DatabaseRegistryUpdater;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.*;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

import java.util.ArrayList;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 5/02/15
 * Time: 16:00
 */
public class HibernateFlywayIntegrator implements Integrator {
    @Override
    public void integrate(Configuration configuration, SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
        final Flyway flyway = new Flyway();
        String url = (String) sessionFactory.getProperties().get("hibernate.connection.url");
        String username = (String) sessionFactory.getProperties().get("hibernate.connection.username");
        String password = (String) sessionFactory.getProperties().get("hibernate.connection.password");

        flyway.setDataSource(url, username, password);
        flyway.setEncoding("UTF-8");

        ArrayList<String> scriptLocations = new ArrayList<>();

        String hibernateType = "";
        Dialect dialect = Dialect.getDialect(sessionFactory.getProperties());
        if(dialect instanceof Oracle8iDialect)
        {
            hibernateType = "oracle";
        }else if (dialect instanceof PostgreSQL81Dialect)
        {
            hibernateType = "postgres";
        }else{
            hibernateType = "h2";
        }


        // Also add the Java package where Flyway will load SQL migrations from (based on DB Type)
        scriptLocations.add("classpath:org.dspace.storage.rdbms.sqlmigration." + hibernateType);

        // Also add the Java package where Flyway will load Java migrations from
        // NOTE: this also loads migrations from any sub-package
        scriptLocations.add("classpath:org.dspace.storage.rdbms.migration");


        flyway.setLocations(scriptLocations.toArray(new String[scriptLocations.size()]));

//        flyway.setCallbacks(new DatabaseRegistryUpdater(), new DatabaseLegacyReindexer());
        try {
            flyway.migrate();
        } catch (FlywayException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void integrate(MetadataImplementor metadata, SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {

    }

    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {

    }
}

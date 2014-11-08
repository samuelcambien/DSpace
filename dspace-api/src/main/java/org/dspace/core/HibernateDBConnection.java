package org.dspace.core;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.sql.SQLException;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 3/06/14
 * Time: 08:47
 */
public class HibernateDBConnection implements DBConnection<Session> {

    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public Session getSession() throws SQLException {
        if(!isTransActionAlive()){
            sessionFactory.getCurrentSession().beginTransaction();
        }
        return sessionFactory.getCurrentSession();
//        HibernateUtil.beginTransaction();
//        return HibernateUtil.getSession();
    }

    @Override
    public boolean isTransActionAlive() {
//        return HibernateUtil.isTransActionAlive();
        Transaction transaction = getTransaction();
        return transaction != null && transaction.isActive();
    }

    protected Transaction getTransaction() {
        return sessionFactory.getCurrentSession().getTransaction();
    }

    @Override
    public boolean isSessionAlive() {
//        return HibernateUtil.isSessionAlive();
        return sessionFactory.getCurrentSession() != null && sessionFactory.getCurrentSession().getTransaction() != null && sessionFactory.getCurrentSession().getTransaction().isActive();
    }

    @Override
    public void rollback() throws SQLException {
        if(isTransActionAlive()){
            getTransaction().rollback();
        }
//        HibernateUtil.rollbackTransaction();
    }

    @Override
    public void closeDBConnection() throws SQLException {
//        HibernateUtil.closeSession();
        if(sessionFactory.getCurrentSession() != null && sessionFactory.getCurrentSession().isOpen())
        {
            sessionFactory.getCurrentSession().close();
        }
    }

    @Override
    public void commit() throws SQLException {
        if(isTransActionAlive() && !getTransaction().wasRolledBack())
        {
            getTransaction().commit();
//            sessionFactory.getCurrentSession().beginTransaction();
        }
//        HibernateUtil.commitTransaction();
//        HibernateUtil.flushSession();
    }

    @Override
    public void shutdown() {
//        HibernateUtil.shutdown();
    }

}
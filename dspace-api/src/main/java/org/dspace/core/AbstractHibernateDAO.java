package org.dspace.core;

import org.dspace.core.GenericDAO;
import org.hibernate.*;
import org.hibernate.criterion.Projections;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 14/03/14
 * Time: 13:50
 */
public abstract class AbstractHibernateDAO<T> implements GenericDAO<T> {

    @Override
    public T create(Context context, T t) throws SQLException {
        getHibernateSession(context).persist(t);
//        t = (T) getHibernateSession(context).merge(t);
//        getHibernateSession(context).flush();
        return t;
    }

    @Override
    public void save(Context context, T t) throws SQLException {
//        t = (T) getHibernateSession(context).merge(t);
//        getHibernateSession(context).persist(t);
//        getHibernateSession(context).flush();
    }

    protected Session getHibernateSession(Context context) throws SQLException {
        return ((Session) context.getDBConnection().getSession());
    }

    @Override
    public void delete(Context context, T t) throws SQLException {
//        getHibernateSession(context).delete(getHibernateSession(context).merge(t));
        getHibernateSession(context).delete(t);
//        getHibernateSession(context).flush();
    }

    @Override
    public List<T> findAll(Context context, Class<T> clazz) throws SQLException {
        return list(createCriteria(context, clazz));
    }

    @Override
    public T findUnique(Context context, String query) throws SQLException {
        @SuppressWarnings("unchecked")
        T result = (T) createQuery(context, query).uniqueResult();
        return result;
    }

    @Override
    public T findByID(Context context, Class clazz, UUID id) throws SQLException {
        @SuppressWarnings("unchecked")
        T result = (T) getHibernateSession(context).get(clazz, id);
        return result;
    }

    @Override
    public T findByID(Context context, Class clazz, int id) throws SQLException {
        @SuppressWarnings("unchecked")
        T result = (T) getHibernateSession(context).get(clazz, id);
        return result;
    }

    @Override
    public List<T> findMany(Context context, String query) throws SQLException {
        @SuppressWarnings("unchecked")
        List<T> result = (List<T>) createQuery(context, query).list();
        return result;
    }

    @Override
    public List nativeSQLQuery(Context context, String query, boolean flushQueries) throws SQLException {
        SQLQuery sqlQuery = getHibernateSession(context).createSQLQuery(query);
        if(flushQueries){
            //Optional, flush queries when executing, could be usefull since you are using native queries & these sometimes require this option.
            sqlQuery.setFlushMode(FlushMode.ALWAYS);
        }
        return sqlQuery.list();
    }

    public List<T> findMany(Context context, Query query) throws SQLException {
        @SuppressWarnings("unchecked")
        List<T> result = (List<T>) query.list();
        return result;
    }

    public Criteria createCriteria(Context context, Class<T> persistentClass) throws SQLException {
        return getHibernateSession(context).createCriteria(persistentClass);
    }

    public Criteria createCriteria(Context context, Class<T> persistentClass, String alias) throws SQLException {
        return getHibernateSession(context).createCriteria(persistentClass, alias);
    }

    public Query createQuery(Context context, String query) throws SQLException {
        return getHibernateSession(context).createQuery(query);
    }

    public List<T> list(Criteria criteria)
    {
        @SuppressWarnings("unchecked")
        List<T> result = (List<T>) criteria.list();
        return result;
    }

    public List<T> list(Query query)
    {
        @SuppressWarnings("unchecked")
        List<T> result = (List<T>) query.list();
        return result;
    }

    public T uniqueResult(Criteria criteria)
    {
        @SuppressWarnings("unchecked")
        T result = (T) criteria.uniqueResult();
        return result;
    }

    public T uniqueResult(Query query)
    {
        @SuppressWarnings("unchecked")
        T result = (T) query.uniqueResult();
        return result;
    }

    public Iterator<T> iterate(Query query)
    {
        @SuppressWarnings("unchecked")
        Iterator<T> result = (Iterator<T>) query.iterate();
        return result;
    }

    public int count(Criteria criteria)
    {
        return ((Long) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
    }

    public int count(Query query)
    {
        return ((Long) query.uniqueResult()).intValue();
    }

    public long countLong(Criteria criteria)
    {
        return (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
    }
}

package org.daimler.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

/**
 * An abstract, parametrized DAO which supports the common generic operations
 *
 * @author abhilash.ghosh
 */
public abstract class AbstractDAO<T extends Serializable> {
    private Class<T> clazz;

    @Autowired
    private SessionFactory sessionFactory;

    public void setClazz( final Class<T> clazzToSet){
        clazz = clazzToSet;
    }

    public T findOne( final long id ){
        return (T) getCurrentSession().get( clazz, id );
    }

    public List<T> findAll(){
        return getCurrentSession()
                .createQuery( "from " + clazz.getName() ).list();
    }

    public T save( final T entity ){
        getCurrentSession().persist( entity );
        return entity;
    }

    public T update( final T entity ){
        return (T) getCurrentSession().merge( entity );
    }

    public void delete( final T entity ){
        Object deletedObj = getCurrentSession().merge(entity);
        getCurrentSession().delete(deletedObj);
    }

    public void deleteById( final long id ){
        final T entity = findOne( id);
        delete( entity );
    }

    protected final Session getCurrentSession(){
        return sessionFactory.getCurrentSession();
    }
}

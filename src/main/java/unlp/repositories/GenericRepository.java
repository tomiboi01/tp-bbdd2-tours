package unlp.repositories;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class GenericRepository<T> {
    @Autowired
    protected SessionFactory sessionFactory;

    private final Class<T> entityClass;

    protected GenericRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public T save(T entity){
        Session session = sessionFactory.getCurrentSession();
        session.persist(entity);
        return entity;
    }

    public T update(T entity)
    {
        Session session = sessionFactory.getCurrentSession();
        return (T) session.merge(entity);
    }

    public Optional<T> findById(Long id)
    {
        Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.get(entityClass, id));
    }

    public boolean delete(T entity)
{
        Session session = sessionFactory.getCurrentSession();
        session.remove(entity);
        return true;
    }

    public List<T> findAll()
    {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from " + entityClass.getName(), entityClass).getResultList();
    }
    
}

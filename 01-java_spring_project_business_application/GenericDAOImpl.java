package ...mvc.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import ...mvc.dao.interfaces.GenericDAO;

/**
 * Generic class that implements generic dao interface
 * 
 * @author pramod
 * 
 * @param <T>
 *            generic type, It is going to be casted to child type at runtime
 * @param <ID>
 *            Primary key type of child class
 */
public abstract class GenericDAOImpl<T, ID extends Serializable> implements
		GenericDAO<T, ID> {

	private Class<T> persistentClass;
	private String className = "";

	@Autowired
	private SessionFactory sessionFactory;
	// Log
	private static Logger logger = Logger.getLogger("generic dao");

	@SuppressWarnings("unchecked")
	public GenericDAOImpl() {

		System.out.println("Generic dao impl created ");

		this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];

		className = persistentClass.getSimpleName();
	}

	/** Function used in child to get current session. */
	protected Session getSession() {
		Session session = SessionFactoryUtils.getSession(this.sessionFactory,
				true);

		return session;
	}

	/** Get child class using reflecton */
	private Class<T> getPersistentClass() {
		return persistentClass;
	}

	@SuppressWarnings("unchecked")
	public T findById(ID id) {
		logger.info("Finding " + className + " by id");

		return (T) getSession().load(getPersistentClass(), id);
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		logger.info("Finding all " + className);

		List<T> allObjects = null;

		allObjects = getSession().createQuery("from " + className).list();

		return allObjects;
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll(int startIndex, int fetchSize) {

		logger.info("Fetching " + fetchSize + " elements starting from "
				+ startIndex);

		Criteria criteria = getSession().createCriteria(persistentClass);
		criteria.setFirstResult(startIndex);
		criteria.setFetchSize(fetchSize);

		return criteria.list();

	}

	public void create(T entity) {
		logger.info("Creating new " + className);

		getSession().save(entity);

	}

	public void update(T entity) {
		logger.info("Updating " + className);

		getSession().update(entity);
	}

	public void merge(T entity) {
		logger.info("Merging " + className);

		getSession().merge(entity);
	}

	public void delete(T entity) {
		logger.info("Deleting " + className);

		getSession().delete(entity);
	}

	public void delete(ID id) {
		logger.info("Deleting " + className);

		T entity = findById(id);

		if (entity != null)
			delete(entity);
	}

}


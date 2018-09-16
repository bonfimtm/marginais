package br.com.marginais.model.facade;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 *
 * @author Thiago Bonfim
 * @param <T>
 */
public class AbstractFacade<T> {

	private Class<T> entityClass;
	private EntityManagerFactory factory;
	private EntityManager em;

	public AbstractFacade(Class<T> entityClass) {
		this.entityClass = entityClass;
		initEntityManager();
	}
	
	protected void initEntityManager() {
		this.factory = Persistence.createEntityManagerFactory("MARGINAISPU");
		this.em = this.factory.createEntityManager();
	}

	protected EntityManager getEntityManager() {
		if (!em.isOpen()){
			initEntityManager();
		}
		return em;
	}

	public void comm() {
		try {
			getEntityManager().getTransaction().commit();
		} catch (Exception e) {
			getEntityManager().getTransaction().rollback();
		}
	}

	protected void close() {
		getEntityManager().close();
		this.factory.close();
	}

	/*
	 * @Override
	 * 
	 * @SuppressWarnings("FinalizeDeclaration") protected void finalize() throws
	 * Throwable { super.finalize(); getEntityManager().close();
	 * this.factory.close(); }
	 */

	public void create(T entity) {
		try {
			EntityTransaction tx = getEntityManager().getTransaction();
			tx.begin();
			getEntityManager().persist(entity);
			getEntityManager().flush();
			tx.commit();
		} finally {
			close();
		}
	}

	public void edit(T entity) {
		try {
			EntityTransaction tx = getEntityManager().getTransaction();
			tx.begin();
			getEntityManager().merge(entity);
			tx.commit();
		} finally {
			close();
		}
	}

	public void remove(T entity) {
		try {
			EntityTransaction tx = getEntityManager().getTransaction();
			tx.begin();
			getEntityManager().remove(getEntityManager().merge(entity));
			tx.commit();
		} finally {
			close();
		}
	}

	public T find(Object id) {
		try {
			return getEntityManager().find(entityClass, id);
		} finally {
			close();
		}
	}

	public List<T> findAll() {
		try {
			javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
			cq.select(cq.from(entityClass));
			return getEntityManager().createQuery(cq).getResultList();
		} finally {
			close();
		}
	}

	public List<T> findRange(int[] range) {
		try {
			javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
			cq.select(cq.from(entityClass));
			javax.persistence.Query q = getEntityManager().createQuery(cq);
			q.setMaxResults(range[1] - range[0] + 1);
			q.setFirstResult(range[0]);
			return q.getResultList();
		} finally {
			close();
		}
	}

	public int count() {
		try {
			javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
			javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
			cq.select(getEntityManager().getCriteriaBuilder().count(rt));
			javax.persistence.Query q = getEntityManager().createQuery(cq);
			return ((Long) q.getSingleResult()).intValue();
		} finally {
			close();
		}
	}
}

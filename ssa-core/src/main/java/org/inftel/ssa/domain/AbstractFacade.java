package org.inftel.ssa.domain;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

/**
 *
 * @author ibaca
 */
public abstract class AbstractFacade<T> {

	private Class<T> entityClass;

	public AbstractFacade(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	protected abstract EntityManager getEntityManager();

	public void create(T entity) {
		getEntityManager().persist(entity);
	}

	public void edit(T entity) {
		getEntityManager().merge(entity);
	}

	public void remove(T entity) {
		getEntityManager().remove(getEntityManager().merge(entity));
	}

	public T find(Object id) {
		return getEntityManager().find(entityClass, id);
	}

	public List<T> findAll() {
		javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
		cq.select(cq.from(entityClass));
		return getEntityManager().createQuery(cq).getResultList();
	}

	public List<T> findRange(int[] range) {
		CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
		cq.select(cq.from(entityClass));
		Query q = getEntityManager().createQuery(cq);
		q.setMaxResults(range[1] - range[0]);
		q.setFirstResult(range[0]);
		return q.getResultList();
	}

	public List<T> find(Integer startPosition, Integer maxResult, String sortField, Boolean ascOrder, Map<String, String> filters) {
		CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = qb.createQuery(entityClass);
		Root<T> from = cq.from(entityClass);
		CriteriaQuery<T> select = cq.select(from);

		if (sortField != null && sortField.trim().length() > 0) {
			boolean asc = (ascOrder == null) ? true : (ascOrder == Boolean.TRUE) ? true : false;
			cq.orderBy((asc) ? qb.asc(from.get(sortField)) : qb.desc(from.get(sortField)));
		}

		filters = (filters == null) ? Collections.<String, String>emptyMap() : filters;
		for (String column : filters.keySet()) {
			Expression<String> literal = qb.upper(qb.literal(filters.get(column)));
			Predicate predicate = qb.like(qb.upper(from.<String>get(column)), literal);
			cq.where(predicate);
		}

		TypedQuery<T> q = getEntityManager().createQuery(select);
		if (startPosition != null) {
			q.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			q.setMaxResults(maxResult);
		}

		return q.getResultList();
	}

	public int count() {
		javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
		javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
		cq.select(getEntityManager().getCriteriaBuilder().count(rt));
		javax.persistence.Query q = getEntityManager().createQuery(cq);
		return ((Long) q.getSingleResult()).intValue();
	}
}

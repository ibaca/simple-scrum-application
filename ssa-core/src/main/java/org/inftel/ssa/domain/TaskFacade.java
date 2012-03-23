
package org.inftel.ssa.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author ibaca
 */
@Stateless
public class TaskFacade extends AbstractFacade<Task> {
    @PersistenceContext(unitName = "ssa-persistence-unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TaskFacade() {
        super(Task.class);
    }

    public List<Task> findByProjectSince(Long projectId, Date date) {
        if (date == null) {
            return findAll();
        }

        CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Task> cq = qb.createQuery(Task.class);
        Root<Task> from = cq.from(Task.class);

        List<Predicate> predicates = new ArrayList<Predicate>(2);

        if (date != null && date.getTime() > 0) {
            Expression<Date> sinceColumn = from.get("updated");
            Expression<Date> sinceLiteral = qb.literal(date);
            predicates.add(qb.greaterThan(sinceColumn, sinceLiteral));
        }

        Expression<Long> projectColumn = from.get("project").get("id");
        Expression<Long> projectLiteral = qb.literal(projectId);
        predicates.add(qb.equal(projectColumn, projectLiteral));

        cq.select(from).where(predicates.toArray(new Predicate[0]));
        TypedQuery<Task> q = getEntityManager().createQuery(cq);
        return q.getResultList();
    }

    // internal test usage
    TaskFacade(EntityManager em) {
        this();
        this.em = em;
    }

}

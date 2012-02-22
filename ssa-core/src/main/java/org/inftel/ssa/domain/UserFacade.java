package org.inftel.ssa.domain;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ibaca
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {

	@PersistenceContext(unitName = "ssa-persistence-unit")
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public UserFacade() {
		super(User.class);
	}

	public User findByEmail(String email) {
		List<User> results = getEntityManager().createNamedQuery("User.findByEmail", User.class).setParameter("email", email).getResultList();
		return (results.isEmpty()) ? null : results.iterator().next();
	}

	// internal test usage
	UserFacade(EntityManager em) {
		this();
		this.em = em;
	}
}

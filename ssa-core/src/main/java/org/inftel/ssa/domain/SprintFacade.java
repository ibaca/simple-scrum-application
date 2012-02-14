package org.inftel.ssa.domain;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ibaca
 */
@Stateless
public class SprintFacade extends AbstractFacade<Sprint> {
  @PersistenceContext(unitName = "ssa-persistence-unit")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public SprintFacade() {
    super(Sprint.class);
  }
  
}

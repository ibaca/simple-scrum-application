package org.inftel.ssa.domain;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ibaca
 */
@Stateless
public class ProjectFacade extends AbstractFacade<Project> {
  @PersistenceContext(unitName = "ssa-persistence-unit")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public ProjectFacade() {
    super(Project.class);
  }
  
}

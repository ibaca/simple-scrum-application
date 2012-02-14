package org.inftel.ssa.domain;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ibaca
 */
@Stateless
public class CommentFacade extends AbstractFacade<Comment> {
  @PersistenceContext(unitName = "ssa-persistence-unit")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public CommentFacade() {
    super(Comment.class);
  }
  
}

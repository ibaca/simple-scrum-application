package org.inftel.ssa.services;

import javax.ejb.Local;

/**
 *
 * @author ibaca
 */
@Local
public interface ResourceService {

	public org.inftel.ssa.domain.Project findProjects(java.lang.Long id);

	public java.util.Collection<org.inftel.ssa.domain.Project> findProjects(java.lang.Integer startPosition, java.lang.Integer maxResult, java.lang.String sortField, java.lang.Boolean ascOrder, java.util.Map<java.lang.String, java.lang.String> filters);

	public void saveProject(org.inftel.ssa.domain.Project project);

	public void removeProject(org.inftel.ssa.domain.Project project);

	public int countProjects();
}

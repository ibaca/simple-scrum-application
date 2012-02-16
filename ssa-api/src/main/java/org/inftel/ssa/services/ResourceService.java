package org.inftel.ssa.services;

import javax.ejb.Local;

/**
 *
 * @author ibaca
 */
@Local
public interface ResourceService {

	public org.inftel.ssa.domain.Project findProjects(java.lang.Long id);

	public java.util.List<org.inftel.ssa.domain.Project> findProjects(java.lang.Integer startPosition, java.lang.Integer maxResult, java.lang.String sortField, java.lang.Boolean ascOrder, java.util.Map<java.lang.String, java.lang.String> filters);

	public void saveProject(org.inftel.ssa.domain.Project project);
	
	public void saveSprint(org.inftel.ssa.domain.Sprint sprint);
	
	public void saveTask(org.inftel.ssa.domain.Task task);

	public void removeProject(org.inftel.ssa.domain.Project project);

	public int countProjects();

	public java.util.List<org.inftel.ssa.domain.Task> findTaksByProject(org.inftel.ssa.domain.Project project, java.lang.Integer startPosition, java.lang.Integer maxResult, java.lang.String sortField, java.lang.Boolean ascOrder, java.util.Map<java.lang.String, java.lang.String> filters);

	public java.util.List<org.inftel.ssa.domain.Task> findTaksBySprint(org.inftel.ssa.domain.Project project, java.lang.Integer startPosition, java.lang.Integer maxResult, java.lang.String sortField, java.lang.Boolean ascOrder, java.util.Map<java.lang.String, java.lang.String> filters);

	public java.util.List<org.inftel.ssa.domain.Task> findTaksByUser(org.inftel.ssa.domain.User user, java.lang.Integer startPosition, java.lang.Integer maxResult, java.lang.String sortField, java.lang.Boolean ascOrder, java.util.Map<java.lang.String, java.lang.String> filters);

}

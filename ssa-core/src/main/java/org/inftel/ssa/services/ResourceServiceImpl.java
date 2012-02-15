package org.inftel.ssa.services;

import java.util.Collection;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.inftel.ssa.domain.BaseEntity;
import org.inftel.ssa.domain.Product;
import org.inftel.ssa.domain.ProductFacade;
import org.inftel.ssa.domain.Project;
import org.inftel.ssa.domain.ProjectFacade;

/**
 *
 * @author ibaca
 */
@Stateless
public class ResourceServiceImpl implements ResourceService {

	@EJB
	private ProjectFacade projects;

	@Override
	public Project findProjects(Long id) {
		return projects.find(id);
	}

	@Override
	public Collection<Project> findProjects(Integer startPosition, Integer maxResult, String sortField, Boolean ascOrder, Map<String, String> filters) {
		return projects.find(startPosition, maxResult, sortField, ascOrder, filters);
	}

	@Override
	public void saveProject(Project project) {
		if (project.isNew()) {
			projects.create(project);
		} else {
			projects.edit(project);
		}
	}

	@Override
	public void removeProject(Project project) {
		//TODO borrar es mala idea, crear un campo tipo deceased
	}

	@Override
	public int countProjects() {
		return projects.count();
	}
}

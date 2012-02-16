package org.inftel.ssa.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.inftel.ssa.domain.*;

/**
 *
 * @author ibaca
 */
@Stateless
public class ResourceServiceImpl implements ResourceService {

	@EJB
	private ProjectFacade projects;
	
	@EJB
	private TaskFacade tasks;
	
	@EJB
	private SprintFacade sprints;

	@Override
	public Project findProjects(Long id) {
		return projects.find(id);
	}

	@Override
	public List<Project> findProjects(Integer startPosition, Integer maxResult, String sortField, Boolean ascOrder, Map<String, String> filters) {
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
	public void saveSprint(Sprint sprint) {
		if (sprint.isNew()) {
			sprints.create(sprint);
		} else {
			sprints.edit(sprint);
		}
	}
	
	@Override
	public void saveTask(Task task) {
		if (task.isNew()) {
			tasks.create(task);
		} else {
			tasks.edit(task);
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
	
	@Override
	public List<Task> findTaksByProject(Project project, Integer startPosition, Integer maxResult, String sortField, Boolean ascOrder, Map<String, String> filters) {
		if (filters == null) {
			filters = new HashMap<String, String>(1);
		}
		filters.put("project.id", project.getId().toString());
		return tasks.find(startPosition, maxResult, sortField, ascOrder, filters);
	}
	
	@Override
	public List<Task> findTaksBySprint(Project project, Integer startPosition, Integer maxResult, String sortField, Boolean ascOrder, Map<String, String> filters) {
		if (filters == null) {
			filters = new HashMap<String, String>(1);
		}
		filters.put("sprint.id", project.getId().toString());
		return tasks.find(startPosition, maxResult, sortField, ascOrder, filters);
	}
	
	@Override
	public List<Task> findTaksByUser(User user, Integer startPosition, Integer maxResult, String sortField, Boolean ascOrder, Map<String, String> filters) {
		if (filters == null) {
			filters = new HashMap<String, String>(1);
		}
		filters.put("user.id", user.getId().toString());
		return tasks.find(startPosition, maxResult, sortField, ascOrder, filters);
	}
}

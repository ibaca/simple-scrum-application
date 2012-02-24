package org.inftel.ssa.services;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
	@PersistenceContext(unitName = "ssa-persistence-unit")
	private EntityManager em;

	@Override
	public Project saveProject(Project project) {
		if (project.isNew()) {
			projects.create(project);
			return project;
		} else {
			return projects.edit(project);
		}
	}

	@Override
	public Sprint saveSprint(Sprint sprint) {
		if (sprint.isNew()) {
			sprints.create(sprint);
			return sprint;
		} else {
			return sprints.edit(sprint);
		}
	}

	@Override
	public Task saveTask(Task task) {
		if (task.isNew()) {
			tasks.create(task);
			return task;
		} else {
			return tasks.edit(task);
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
	public Project findProject(Long id) {
		return projects.find(id);
	}
	
	@Override
	public Task findTask(Long id) {
		return tasks.find(id);
	}
	
	@Override
	public Sprint findSprint(Long id) {
		return sprints.find(id);
	}

	@Override
	public List<Project> findProjects(Integer startPosition, Integer maxResult, String sortField, Boolean ascOrder, Map<String, String> filters) {
		return projects.find(startPosition, maxResult, sortField, ascOrder, filters);
	}

	@Override
	public List<Task> findTaksByProject(Project project, Integer startPosition, Integer maxResult, String sortField, Boolean ascOrder, Map<String, String> filters) {
		if (filters == null) {
			filters = new HashMap<String, String>(1);
		}
		Map<String, String> modified = new HashMap<String, String>(filters);
		modified.put("project.id", project.getId().toString());
		return tasks.find(startPosition, maxResult, sortField, ascOrder, modified);
	}
	
	@Override
	public int countTasksByProject(Project project, String sortField, Boolean ascOrder, Map<String, String> filters) {
		if (filters == null) {
			filters = new HashMap<String, String>(1);
		}
		Map<String, String> modified = new HashMap<String, String>(filters);
		modified.put("project.id", project.getId().toString());
		return tasks.count(sortField, ascOrder, modified);
	}
	
	@Override
	public int countProjectsByUser(User user, String sortField, Boolean ascOrder, Map<String, String> filters) {
		if (filters == null) {
			filters = new HashMap<String, String>(1);
		}
		Map<String, String> modified = new HashMap<String, String>(filters);
		modified.put("user.id", user.getId().toString());
		return projects.count(sortField, ascOrder, modified);
	}

	@Override
	public int countSprintsByProject(Project project, String sortField, Boolean ascOrder, Map<String, String> filters) {
		if (filters == null) {
			filters = new HashMap<String, String>(1);
		}
		Map<String, String> modified = new HashMap<String, String>(filters);
		modified.put("project.id", project.getId().toString());
		return sprints.count(sortField, ascOrder, modified);
	}

	@Override
	public int countTasksByUser(User user, String sortField, Boolean ascOrder, Map<String, String> filters) {
		if (filters == null) {
			filters = new HashMap<String, String>(1);
		}
		Map<String, String> modified = new HashMap<String, String>(filters);
		modified.put("user.id", user.getId().toString());
		return sprints.count(sortField, ascOrder, modified);
	}

	@Override
	public List<Task> findTaksBySprint(Project project, Integer startPosition, Integer maxResult, String sortField, Boolean ascOrder, Map<String, String> filters) {
		if (filters == null) {
			filters = new HashMap<String, String>(1);
		}
		Map<String, String> modified = new HashMap<String, String>(filters);
		modified.put("sprint.id", project.getId().toString());
		return tasks.find(startPosition, maxResult, sortField, ascOrder, modified);
	}

	@Override
	public List<Task> findTaksByUser(User user, Integer startPosition, Integer maxResult, String sortField, Boolean ascOrder, Map<String, String> filters) {
		if (filters == null) {
			filters = new HashMap<String, String>(1);
		}
		Map<String, String> modified = new HashMap<String, String>(filters);
		modified.put("user.id", user.getId().toString());
		return tasks.find(startPosition, maxResult, sortField, ascOrder, modified);
	}

	@Override
	public List<Project> findProjectsByUser(User user, Integer startPosition, Integer maxResult, String sortField, Boolean ascOrder, Map<String, String> filters) {
		if (filters == null) {
			filters = new HashMap<String, String>(1);
		}
		Map<String, String> modified = new HashMap<String, String>(filters);
		modified.put("user.id", user.getId().toString());
		return projects.find(startPosition, maxResult, sortField, ascOrder, modified);
	}

	@Override
	public List<Sprint> findSprintsByProject(Project project, Integer startPosition, Integer maxResult, String sortField, Boolean ascOrder, Map<String, String> filters) {
		if (filters == null) {
			filters = new HashMap<String, String>(1);
		}
		Map<String, String> modified = new HashMap<String, String>(filters);
		modified.put("project.id", project.getId().toString());
		return sprints.find(startPosition, maxResult, sortField, ascOrder, modified);
	}

}

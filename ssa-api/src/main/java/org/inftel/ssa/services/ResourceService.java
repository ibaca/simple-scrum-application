package org.inftel.ssa.services;

import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import org.inftel.ssa.domain.Project;
import org.inftel.ssa.domain.Sprint;
import org.inftel.ssa.domain.Task;
import org.inftel.ssa.domain.User;

/**
 *
 * @author ibaca
 */
@Local
public interface ResourceService {

	public Project saveProject(Project project);
	
	public Sprint saveSprint(Sprint sprint);
	
	public Task saveTask(Task task);

	public void removeProject(Project project);

	public int countProjects();
	
	public Project findProject(Long id);
	
	public Task findTask(Long id);
	
	public Sprint findSprint(Long id);
	
	public int countTasksByProject(Project project, String sortField, Boolean ascOrder, Map<String, String> filters);
	
	public int countTasksByUser(User user, String sortField, Boolean ascOrder, Map<String, String> filters);
	
	public int countSprintsByProject(Project project, String sortField, Boolean ascOrder, Map<String, String> filters);
	
	public List<Project> findProjects(Integer startPosition, Integer maxResult, String sortField, Boolean ascOrder, Map<String, String> filters);

	public List<Task> findTaksByProject(Project project, Integer startPosition, Integer maxResult, String sortField, Boolean ascOrder, Map<String, String> filters);

	public List<Task> findTaksBySprint(Project project, Integer startPosition, Integer maxResult, String sortField, Boolean ascOrder, Map<String, String> filters);

	public List<Task> findTaksByUser(User user, Integer startPosition, Integer maxResult, String sortField, Boolean ascOrder, Map<String, String> filters);

	public List<Sprint> findSprintsByProject(Project project, Integer startPosition, Integer maxResult, String sortField, Boolean ascOrder, Map<String, String> filters);

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.ssa.web;

import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.inftel.ssa.domain.Project;
import org.inftel.ssa.domain.Sprint;
import org.inftel.ssa.domain.User;
import org.inftel.ssa.services.ResourceService;
import org.inftel.ssa.services.SessionService;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Jesus Ruiz Oliva
 */
@ManagedBean
@SessionScoped
public class TaskManager {
    	
	@EJB
	private ResourceService resources;
	@ManagedProperty(value = "#{sprintManager}")
	private SprintManager sprintManager;
        @ManagedProperty(value = "#{projectManager}")
	private ProjectManager projectManager;
	private static final long serialVersionUID = 1L;
	private Sprint sprint;
	private LazyDataModel<Project> tasks = new LazyDataModel() {

		@Override
		public List load(int first, int pageSize, String sortField, org.primefaces.model.SortOrder sortOrder, Map filters) {
			
                        return resources.findTaksBySprint(projectManager.getCurrentProject(), first, pageSize, sortField, sortOrder.equals(SortOrder.ASCENDING), filters);
			
		}

		@Override
		public void setRowIndex(int rowIndex) {
			/*
			 * The following is in ancestor (LazyDataModel): this.rowIndex = rowIndex == -1 ?
			 * rowIndex : (rowIndex % pageSize);
			 */
			if (rowIndex == -1 || getPageSize() == 0) {
				super.setRowIndex(-1);
			} else {
				super.setRowIndex(rowIndex);
			}
		}
	};

    /**
     * Creates a new instance of taskManager
     */
    public TaskManager() {
    }

    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    public SprintManager getSprintManager() {
        return sprintManager;
    }

    public void setSprintManager(SprintManager sprintManager) {
        this.sprintManager = sprintManager;
    }

    public LazyDataModel<Project> getTasks() {
        tasks.setRowCount(sprintManager.getCurrentSprint().getTasks().size());
        return tasks;
    }

    public void setTasks(LazyDataModel<Project> tasks) {
        this.tasks = tasks;
    }

    public ProjectManager getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }
    public void create(){
        
    }
    public void remove(){
        
    }
    public void edit(){
        
    }
   
}

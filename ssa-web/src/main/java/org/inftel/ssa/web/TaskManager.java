/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.ssa.web;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.inftel.ssa.domain.Project;
import org.inftel.ssa.domain.Task;
import org.inftel.ssa.domain.TaskStatus;
import org.inftel.ssa.services.ResourceService;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Jesus Ruiz Oliva
 */
@ManagedBean
@SessionScoped
public class TaskManager implements Serializable {

    private final static Logger logger = Logger.getLogger(TaskManager.class.getName());
    @EJB
    private ResourceService resources;
    @ManagedProperty(value = "#{sprintManager}")
    private SprintManager sprintManager;
    @ManagedProperty(value = "#{projectManager}")
    private ProjectManager projectManager;
    @ManagedProperty(value = "#{userManager}")
    private UserManager userManager;
    private static final long serialVersionUID = 1L;
    private Task currentTask;
    private boolean accepted;
    private int taskStatus;
    private LazyDataModel<Task> tasks = new LazyDataModel() {

        @Override
        public List load(int first, int pageSize, String sortField, org.primefaces.model.SortOrder sortOrder, Map filters) {
            logger.log(Level.INFO, "lazy data model [first={0}, pageSize={1}, sortField={2}, sortOrder={3}, filters={4}",
                    new Object[]{first, pageSize, sortField, sortOrder, filters});
            
            Map<String,String> filter = new HashMap<String, String>();
            filter.put("summary","prototipo");
            Boolean asc = sortOrder == SortOrder.ASCENDING;
            return resources.findTaksByProject(projectManager.getCurrentProject(), first, pageSize, sortField, asc, filter);
            
        }

        @Override
        public void setRowIndex(int rowIndex) {
            /*
             * The following is in ancestor (LazyDataModel): this.rowIndex =
             * rowIndex == -1 ? rowIndex : (rowIndex % pageSize);
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

    public SprintManager getSprintManager() {
        return sprintManager;
    }

    public void setSprintManager(SprintManager sprintManager) {
        this.sprintManager = sprintManager;
    }

    public LazyDataModel<Task> getTasks() {
        int rows = projectManager.getCurrentProject(true).getTasks().size();
        logger.info("row count=" + rows);
        tasks.setRowCount(rows);
        return tasks;
    }

    public void setTasks(LazyDataModel<Task> tasks) {
        this.tasks = tasks;
    }

    public ProjectManager getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    public String create() {
        Task task = new Task();
        setCurrentTask(task);
        return "/task/create.xhtml";

    }

    public String save() {
        if (currentTask != null) {
            Project project = projectManager.getCurrentProject();
            currentTask.setProject(project);
            resources.saveTask(currentTask);

        }
        return "/task/index.xhtml";
    }

    public void remove() {
    }

    public String edit() {
        setCurrentTask(tasks.getRowData());
        return "/task/create.xhtml";
    }

    public boolean getAccepted(){
        setAccepted(currentTask.getUser()!=null);
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        currentTask.setUser(getUserManager().getCurrentUser());
        this.accepted = accepted;
    }
      

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public int getTaskStatus() {
        return currentTask.getStatus().ordinal();
    }

    public void setTaskStatus(int taskStatus) {
        currentTask.setStatus(TaskStatus.values()[taskStatus]);
        this.taskStatus = taskStatus;
    }
    
}

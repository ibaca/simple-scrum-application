/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.ssa.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.inftel.ssa.domain.Sprint;
import org.inftel.ssa.domain.Task;
import org.inftel.ssa.services.ResourceService;
import org.primefaces.model.DualListModel;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Jesus Ruiz Oliva
 */
@ManagedBean(name = "sprintManager")
@SessionScoped
public class SprintManager implements Serializable {

    private final static Logger logger = Logger.getLogger(SprintManager.class.getName());
    @EJB
    private ResourceService resources;
    private static final long serialVersionUID = 1L;
    private LazyDataModel<Sprint> sprints;
    private Sprint currentSprint;
    @ManagedProperty("#{projectManager}")
    private ProjectManager projectManager;
    private DualListModel<Task> tasks;

    public SprintManager() {
        super();

    }

    @PostConstruct
    public void init() {
        sprints = new LazyDataModel() {

            @Override
            public List load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {
                logger.log(Level.INFO, "lazy data model [first={0}, pageSize={1}, sortField={2}, sortOrder={3}, filters={4}",
                        new Object[]{first, pageSize, sortField, sortOrder, filters});

                addTask();
                return resources.findSprintsByProject(projectManager.getCurrentProject(), first, pageSize, sortField, sortOrder.equals(SortOrder.ASCENDING), filters);
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

    }

    public LazyDataModel<Sprint> getSprints() {
        int rows = projectManager.getCurrentProject(true).getSprints().size();
        logger.info("list row count=" + rows);
        sprints.setRowCount(rows);
        return sprints;
    }

    public void setSprints(LazyDataModel<Sprint> sprints) {
        this.sprints = sprints;
    }

    public Sprint getCurrentSprint() {
        return currentSprint;
    }

    public void setCurrentSprint(Sprint currentSprint) {
        this.currentSprint = currentSprint;
    }

    public String remove() {
        Sprint currentSprint = sprints.getRowData();
        //Remover currentSprint;        
        return "/sprint/index.xhtml";
    }

    public String create() {
        Sprint sprint = new Sprint();
        setCurrentSprint(sprint);
        return "/sprint/create?faces-redirect=true";
    }

    public String save() {        
        if (currentSprint != null) {
            currentSprint.setProject(projectManager.getCurrentProject());
            resources.saveSprint(currentSprint);
        }
        return "/sprint/index?faces-redirect=true";
    }
    public String saveEdit(){
        logger.info("Salvando edicion...");
        if (currentSprint != null) {
            currentSprint.setProject(projectManager.getCurrentProject());
            logger.info("Salvando edicion...");
            List<Task> source = tasks.getSource();
            for(Task task:source){   
                logger.info("source: " + task.getSummary());
                resources.findTask(task.getId()).setSprint(null);
            }
            List<Task> target = tasks.getTarget();
            for(Task task:target){
                logger.info("target: " + task.getSummary());
                resources.findTask(task.getId()).setSprint(currentSprint);
            }
            tasks.getTarget();
            resources.saveSprint(currentSprint);
        }
        return "/sprint/index?faces-redirect=true";
    }

    public ProjectManager getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    public String edit() {
        setCurrentSprint(sprints.getRowData());
        return "/sprint/edit?faces-redirect=true";
    }

    public void addTask() {

        List<Task> tasksSource = new ArrayList<Task>();
        List<Task> tasksTarget = new ArrayList<Task>();
        List<Task> tasksCurrentProject = projectManager.getCurrentProject().getTasks();
        for (Task task : tasksCurrentProject) {

            if (task.getSprint() == null) {
                tasksSource.add(task);
            } else {
                tasksTarget.add(task);
            }
        }
        tasks = new DualListModel<Task>(tasksSource, tasksTarget);
    }

    public DualListModel<Task> getTasks() {
        return tasks;
    }

    public void setTasks(DualListModel<Task> tasks) {
        this.tasks = tasks;
    }    
}

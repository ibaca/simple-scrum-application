/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.ssa.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.inftel.ssa.domain.Project;
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
public class ProjectManager implements Serializable {

    @EJB
    private SessionService sessionService;
    @EJB
    private ResourceService resources;
    private static final long serialVersionUID = 8799656478674718638L;
    private LazyDataModel<Project> projects;
    private Project currentProject;
    private User selectedUser;
    public ProjectManager() {
        super();
    }

    @PostConstruct
    public void initialize() {
        projects = new LazyDataModel() {

            @Override
            public List load(int first, int pageSize, String sortField, org.primefaces.model.SortOrder sortOrder, Map filters) {
                //filters.put("user",user.getId().toString());
                return resources.findProjects(first, pageSize, sortField, sortOrder.equals(SortOrder.ASCENDING), filters);
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

    public LazyDataModel<Project> getProjects() {
        projects.setRowCount(resources.countProjects());

        return projects;
    }

    public void setProjects(LazyDataModel<Project> projects) {
        this.projects = projects;
    }

    public Project getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(Project currentProject) {
        if (currentProject.getLinks() == null) {
            currentProject.setLinks(Collections.<String, String>emptyMap());
        }
        this.currentProject = currentProject;
    }

    public String showSprints() {
        setCurrentProject(projects.getRowData());
        return "/sprint/show.xhtml";
    }

    public String remove() {
        Project project = projects.getRowData();
        resources.removeProject(project);
        return "/project/show.xhtml";
    }

    public String create() {
        Project project = new Project();
        setCurrentProject(project);
        return "/project/create.xhtml";
    }

    public String save() {
        if (currentProject != null) {
            List<User> currentUser = new ArrayList<User>();
            currentUser.add(sessionService.currentUser());            
            currentProject.setUsers(currentUser);
            resources.saveProject(currentProject);
        }
        return "/project/show.xhtml";
    }

    public String edit() {
        setCurrentProject(projects.getRowData());
        return "/project/edit.xhtml";
    }

    public String cancelEdit() {
        return "/project/show.xhtml";
    }

    public String userRole() {
        return sessionService.currentUser().getUserRole();
    }


    public void addUser() {
        
        List<User> users = currentProject.getUsers();
        users.add(getSelectedUser());
        currentProject.setUsers(users);
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

       
    
}

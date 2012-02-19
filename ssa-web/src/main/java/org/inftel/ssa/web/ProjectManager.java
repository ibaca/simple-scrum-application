/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.ssa.web;

import java.io.Serializable;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
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
	@ManagedProperty(value = "#{userManager}")
	private UserManager userManager;
	private static final long serialVersionUID = 1L;
	private Project currentProject;
	private User selectedUser;
	private LazyDataModel<Project> projects = new LazyDataModel() {

		@Override
		public List load(int first, int pageSize, String sortField, org.primefaces.model.SortOrder sortOrder, Map filters) {
			filters.put("users.id", userManager.getCurrentUser().getId().toString());
			return resources.findProjects(first, pageSize, sortField, sortOrder.equals(SortOrder.ASCENDING), filters);
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

	public ProjectManager() {
	}

	@PostConstruct
	public void initialize() {
		this.currentProject = new Project();
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
	
	public String changeCurrentProject(Project newProject) {
		setCurrentProject(newProject);
		return "/project/home.xhtml";
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
			User user = userManager.getCurrentUser();
			resources.saveProject(currentProject);
			// Si reciencreado o si no contiene el usuario actual se añade
			if (currentProject.getUsers() == null || !currentProject.getUsers().contains(user)) {
				addUser(user);
			}
		}
		return "/project/home.xhtml";
	}

	public String addUser(String email) {
		User user = sessionService.findByEmail(email);
		if (user == null) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("No se ha podido añadir usuario", "El email no esta registrado en la base de datos"));
			return null;
		} else {
			return addUser(user);
		}
	}

	public String addUser(User user) {
		if (currentProject.getUsers() == null) {
			currentProject.setUsers(new HashSet<User>());
		}
		currentProject.getUsers().add(user);
		user.getProjects().add(currentProject);
		sessionService.saveUser(user);
		return null;
	}

	public String edit() {
		setCurrentProject(projects.getRowData());
		return "/project/edit.xhtml";
	}

	public String cancelEdit() {
		return "/project/show.xhtml";
	}

	public String userRole() {
		return userManager.getCurrentUser().getUserRole();
	}

	public void addUser() {
		Set<User> users = currentProject.getUsers();
		users.add(getSelectedUser());
		currentProject.setUsers(users);
	}

	public User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
}

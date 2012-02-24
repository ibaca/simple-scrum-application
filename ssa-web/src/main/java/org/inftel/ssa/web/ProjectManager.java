package org.inftel.ssa.web;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.lang3.StringUtils;
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
@ManagedBean(name = "projectManager")
@SessionScoped
public class ProjectManager implements Serializable {

	private final static Logger logger = Logger.getLogger(ProjectManager.class.getName());
	@EJB
	private SessionService sessionService;
	@EJB
	private ResourceService resources;
	@ManagedProperty(value = "#{userManager}")
	private UserManager userManager;
	@ManagedProperty(value = "#{mailManager}")
	private MailManager mailManager;
	private static final long serialVersionUID = 1L;
	private Project currentProject;
	private User selectedUser;
	private Project createProject;
	private String addUserDialogEmail;
	private int statistcs = 0;
	private LazyDataModel<Project> projects = new LazyDataModel() {

		@Override
		public List load(int first, int pageSize, String sortField, org.primefaces.model.SortOrder sortOrder, Map filters) {
			logger.log(Level.INFO, "lazy data model [first={0}, pageSize={1}, sortField={2}, sortOrder={3}, filters={4}]", new Object[]{first, pageSize, sortField, sortOrder, filters});
			setRowCount(resources.countProjectsByUser(userManager.getCurrentUser(), sortField, sortOrder == SortOrder.ASCENDING, filters));
			return resources.findProjectsByUser(userManager.getCurrentUser(), first, pageSize, sortField, sortOrder == SortOrder.ASCENDING, filters);
		}
	};

	public ProjectManager() {
	}

	@PostConstruct
	public void initialize() {
		this.currentProject = new Project();
	}

	public LazyDataModel<Project> getProjects() {
		return projects;
	}

	public void setProjects(LazyDataModel<Project> projects) {
		this.projects = projects;
	}

	public Project getCurrentProject() {
		return currentProject;
	}

	public int getStatistcs() {
		return statistcs;
	}

	public void setStatistcs(int statistcs) {
		this.statistcs = statistcs;
	}

	public Project getCurrentProject(boolean refresh) {
		if (refresh && currentProject != null && currentProject.getId() != null) {
			currentProject = resources.findProject(currentProject.getId());
		}
		return getCurrentProject();
	}

	public void setCurrentProject(Project currentProject) {
		if (currentProject.getLinks() == null) {
			currentProject.setLinks(Collections.<String, String>emptyMap());
		}
		this.editableLinks = null;
		this.currentProject = currentProject;
	}

	public String changeCurrentProject(Project newProject) {
		setCurrentProject(resources.findProject(newProject.getId()));
		return "/project/show";
	}

	public String showSprints() {
		setCurrentProject(projects.getRowData());
		return "/sprint/show";
	}

	public String remove() {
		Project project = projects.getRowData();
		resources.removeProject(project);
		return "/project/show";
	}

	public String create() {
		return "/project/create";
	}

	public String save() {
		if (currentProject != null) {
			User user = userManager.getCurrentUser();
			saveEditableLinks(); // Se copian los editable links a currentProject
			currentProject = resources.saveProject(currentProject);
			// Si reciencreado o si no contiene el usuario actual se añade
			if (currentProject.getUsers() == null || !currentProject.getUsers().contains(user)) {
				addUser(user);
			}
		}
		return null;//return "/project/show?faces-redirect=true";¿Por alguna razon quieres permanecer en la pagina de edit?
	}

	public String createSave() {
		if (createProject != null) {
			User current = userManager.getCurrentUser();
			getCreateProject().getUsers().add(current);
			current.getProjects().add(createProject);
			sessionService.saveUser(current); //FIXME esto no es lo correcto! pero es necesario
			changeCurrentProject(resources.saveProject(createProject));
			createProject = null;
		}
		return "/project/show?faces-redirect=true";
	}

	public String addUserByEmail(String email) {
		logger.log(Level.INFO, "buscando usuario con mail {0} al proyecto {1}", new Object[]{email, currentProject.getName()});
		User user = sessionService.findByEmail(email);
		if (user == null) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("No se ha podido añadir usuario", "El email no esta registrado en la base de datos"));
			return null;
		} else {
			this.addUserDialogEmail = "";
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
		// Si se ha agregado el usuario se notifica por email
		getMailManager().sendProjectAssigned(user.getEmail(), getCurrentProject().getName());
		return null;
	}

	public String edit() {
		setCurrentProject(projects.getRowData());
		editableLinks = null;
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

	public Project getCreateProject() {
		return (createProject == null) ? createProject = new Project() : createProject;
	}

	public void setCreateProject(Project createProject) {
		this.createProject = createProject;
	}

	public String getAddUserDialogEmail() {
		return addUserDialogEmail;
	}

	public void setAddUserDialogEmail(String addUserDialogEmail) {
		this.addUserDialogEmail = addUserDialogEmail;
	}

	public MailManager getMailManager() {
		return mailManager;
	}

	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}

	// -------------------------------------------------------------------------.---- Editable Links
	public final static class Link implements Serializable {

		private String text;
		private String url;

		public Link() {
		}

		public Link(String text, String url) {
			this.text = text;
			this.url = url;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public boolean empty() {
			return StringUtils.isBlank(url) && StringUtils.isBlank(text);
		}
	}
	private List<Link> editableLinks;

	public List<Link> getEditableLinks() {
		if (editableLinks == null) {
			Map<String, String> origin = getCurrentProject().getLinks();
			List<Link> destination = new ArrayList<Link>(origin.size());
			for (String key : origin.keySet()) {
				destination.add(new Link(key, origin.get(key)));
			}
			if (destination.isEmpty()) {
				destination.add(new Link());
			}
			editableLinks = destination;
		}
		return editableLinks;
	}

	public void setEditableLinks(List<Link> links) {
		logger.log(Level.INFO, "setting links {0}", links);
		// save in current project
	}

	/**
	 * Añade un nuevo link a la lista de links editable.
	 */
	public String addLink() {
		editableLinks.add(new Link());
		return null;
	}

	/**
	 * Copia los valores de editable link a current project.
	 */
	private void saveEditableLinks() {
		if (editableLinks != null && currentProject != null) {

			Map<String, String> links = currentProject.getLinks();
			links.clear();
			for (Link link : getEditableLinks()) {
				if (!link.empty()) {
					links.put(link.text, link.url);
				}
			}
		}
		editableLinks = null;
	}
}

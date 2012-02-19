package org.inftel.ssa.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsbaes
 */
@Entity
@Table(name = "projects")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Project.findAll", query = "SELECT p FROM Project p"),
	@NamedQuery(name = "Project.findByProject", query = "SELECT p FROM Project p WHERE p.id = :idproject"),
	@NamedQuery(name = "Project.findByName", query = "SELECT p FROM Project p WHERE p.name = :name"),
	@NamedQuery(name = "Project.findByDescription", query = "SELECT p FROM Project p WHERE p.description = :description")})
public class Project extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String name;
	private String summary;
	@Lob
	private String description;
	@Temporal(TemporalType.TIMESTAMP)
	private Date opened;
	@Temporal(TemporalType.TIMESTAMP)
	private Date started;
	@Temporal(TemporalType.TIMESTAMP)
	private Date closed;
	private String company;
	@ElementCollection
	private Map<String, String> links;
	@ElementCollection
	private Set<String> labels;
	private String license;
	
	@OneToMany(mappedBy = "project")
	private List<Task> tasks;
	@OneToMany(mappedBy = "project")
	private List<Sprint> sprints;
	@ManyToMany
	@JoinTable(name="project_users")
	private Set<User> users;

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	public Set<String> getLabels() {
		return labels;
	}

	public void setLabels(Set<String> labels) {
		this.labels = labels;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public Date getClosed() {
		return closed;
	}

	public void setClosed(Date closed) {
		this.closed = closed;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, String> getLinks() {
		return links;
	}

	public void setLinks(Map<String, String> links) {
		this.links = links;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getOpened() {
		return opened;
	}

	public void setOpened(Date opened) {
		this.opened = opened;
	}

	public List<Sprint> getSprints() {
		return sprints;
	}

	public void setSprints(List<Sprint> sprints) {
		this.sprints = sprints;
	}

	public Date getStarted() {
		return started;
	}

	public void setStarted(Date started) {
		this.started = started;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
}

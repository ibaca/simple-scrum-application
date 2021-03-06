package org.inftel.ssa.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jsbaes
 */
@Entity
@Table(name = "users")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
	@NamedQuery(name = "User.findByIduser", query = "SELECT u FROM User u WHERE u.id = :iduser"),
	@NamedQuery(name = "User.findByNickname", query = "SELECT u FROM User u WHERE u.nickname = :nickname"),
	@NamedQuery(name = "User.findByFullname", query = "SELECT u FROM User u WHERE u.fullName = :fullname"),
	@NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
	@NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password")})
public class User extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String nickname;
	private String fullName;
	// @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
	@Column(unique = true, nullable = false)
	private String email;
	private String company;
	@ManyToMany(mappedBy = "users", cascade = CascadeType.ALL)
	private Set<Project> projects;
	private String password;
	@OneToMany(mappedBy = "user")
	private List<Task> tasks;
	private String userRole;

	public User() {
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Set<Project> getProjects() {
		return (projects == null) ? projects = new HashSet<Project>(0) : projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullname) {
		this.fullName = fullname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@XmlTransient
	public List<Task> getTasks() {
		return (tasks == null) ? tasks = new ArrayList<Task>(0) : tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String role) {
		this.userRole = role;
	}

	@Override
	public String toString() {
		StringBuilder build = new StringBuilder(super.toString());
		build.subSequence(0, build.length());
		build.append(", email=").append(getEmail());
		build.append("]");
		return build.toString();
	}
	
}

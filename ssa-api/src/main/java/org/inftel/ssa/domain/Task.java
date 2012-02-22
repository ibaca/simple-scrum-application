package org.inftel.ssa.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jsbaes
 */
@Entity
@Table(name = "tasks")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Task.findAll", query = "SELECT t FROM Task t"),
	@NamedQuery(name = "Task.findByIdtask", query = "SELECT t FROM Task t WHERE t.id = :idtask"),
	@NamedQuery(name = Task.FIND_HOURS_TOFINISH, query = "SELECT SUM(t.remaining) FROM Task t WHERE t.sprint.id = :idsprint AND t.user.id = :iduser AND t.remaining > 0"),
	@NamedQuery(name = Task.FIND_TASK_TOFINISH, query = "SELECT count(t) FROM Task t WHERE t.sprint.id = :idsprint AND t.user.id = :iduser AND t.remaining > 0"),
	@NamedQuery(name = Task.FIND_TASK_STATUS_PROJECT, query = "SELECT count(t) FROM Task t WHERE t.project.id = :idproject AND t.status = :idstatus"),
	@NamedQuery(name = Task.FIND_TASK_STATUS_USER, query = "SELECT count(t) FROM Task t WHERE t.user.id = :iduser AND t.status = :idstatus"),
	@NamedQuery(name = Task.FIND_TASK_STATUS_SPRINT, query = "SELECT count(t) FROM Task t WHERE t.sprint.id = :idsprint AND t.status = :idstatus"),
	@NamedQuery(name = Task.FIND_USERS_SPRINT, query = "SELECT DISTINCT t.user FROM Task t WHERE t.sprint.id = :idsprint")})
public class Task extends BaseEntity {

	private static final long serialVersionUID = 1L;
	public static final String FIND_HOURS_TOFINISH = "Task.findHoursRemByUserAndSprint";
	public static final String FIND_TASK_TOFINISH = "Task.findTaskRemByUserAndSprint";
	public static final String FIND_TASK_STATUS_PROJECT = "countByProjectAndStatus";
	public static final String FIND_TASK_STATUS_USER = "countByUserAndStatus";
	public static final String FIND_TASK_STATUS_SPRINT = "countBySprintAndStatus";
	public static final String FIND_USERS_SPRINT = "Task.findUsersBySprint";
	private String summary;
	@Lob
	private String description;
	/**
	 * Horas estimadas, al crearse debe coincidir con remainig
	 */
	private Integer estimated;
	/**
	 * Horas dedicadas a la tarea
	 */
	private Integer burned;
	/**
	 * Horas restantes en la tarea
	 */
	private Integer remaining;
	/**
	 * Prioridada de la tarea FIXME esto igual deberia ser parte de story
	 */
	private Integer priority;
	/**
	 * Fecha en la que se inicia la tarea
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date beginDate;
	/**
	 * Fecha en la que se termina la tarea
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	/** Indica el estado de la tarea */
	private TaskStatus status;
	
	@ManyToOne
	private User user;
	@ManyToOne
	private Sprint sprint;
	@ManyToOne
	private Project project;
	@OneToMany(mappedBy = "task")
	private List<Comment> comments;

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Integer getBurned() {
		return burned;
	}

	public void setBurned(Integer burned) {
		this.burned = burned;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getEstimated() {
		return estimated;
	}

	public void setEstimated(Integer estimated) {
		this.estimated = estimated;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Integer getRemaining() {
		return remaining;
	}

	public void setRemaining(Integer remaining) {
		this.remaining = remaining;
	}

	public Sprint getSprint() {
		return sprint;
	}

	public void setSprint(Sprint sprint) {
		this.sprint = sprint;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public String toString() {
		StringBuilder build = new StringBuilder(super.toString());
		build.subSequence(0, build.length());
		build.append(", summary=").append(getSummary());
		build.append(", status=").append(getStatus());
		build.append("]");
		return build.toString();
	}
}

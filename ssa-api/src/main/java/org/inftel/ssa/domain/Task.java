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
    @NamedQuery(name = "Task.findByName", query = "SELECT t FROM Task t WHERE t.name = :name"),
    @NamedQuery(name = "Task.findByDescription", query = "SELECT t FROM Task t WHERE t.description = :description"),
    @NamedQuery(name = "Task.findByEstimated", query = "SELECT t FROM Task t WHERE t.estimated = :estimated"),
    @NamedQuery(name = "Task.findByDedicated", query = "SELECT t FROM Task t WHERE t.dedicated = :dedicated"),
    @NamedQuery(name = "Task.findByToFinish", query = "SELECT t FROM Task t WHERE t.toFinish = :toFinish"),
    @NamedQuery(name = "Task.findByPriority", query = "SELECT t FROM Task t WHERE t.priority = :priority"),
    @NamedQuery(name = "Task.findByStartDate", query = "SELECT t FROM Task t WHERE t.startDate = :startDate"),
    @NamedQuery(name = "Task.findByCompletionDate", query = "SELECT t FROM Task t WHERE t.completionDate = :completionDate"),
    @NamedQuery(name = Task.FIND_TOFINISH, query="SELECT t FROM Task t WHERE t.sprint.id = :idsprint AND t.user.id = :iduser AND t.toFinish > 0"),
    @NamedQuery(name = Task.FIND_USERS_SPRINT, query="SELECT DISTINCT t.user.id FROM Task t WHERE t.sprint.id = :idsprint")})

public class Task extends BaseEntity {

    private static final long serialVersionUID = 1L;
    public static final String FIND_TOFINISH = "Task.findByUserAndSprint";
    public static final String FIND_USERS_SPRINT = "Task.findUsersBySprint";
    private String name;
    private String description;
    private Integer estimated;
    private Integer dedicated;
    private Integer toFinish;
    private Integer priority;
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date completionDate;
    @ManyToOne
    private User user;
    private String status;
    @ManyToOne
    private Sprint sprint;
    @ManyToOne(optional = false)
    private Project project;
    @OneToMany(mappedBy = "task")
    private List<Comment> comments;

    /**
     * Nombre de la tareas
     *
     * @return cadena con el nombre
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Descripción de la tareas
     *
     * @return cadena con el nombre
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Tiempo estimado inicialmente (en horas) para completar la tarea
     *
     * @return horas estimadas
     */
    public Integer getEstimated() {
        return estimated;
    }

    public void setEstimated(Integer estimated) {
        this.estimated = estimated;
    }

    /**
     * Tiempo dedicado (en horas) para completar la tarea
     *
     * @return horas completadas
     */
    public Integer getDedicated() {
        return dedicated;
    }

    public void setDedicated(Integer dedicated) {
        this.dedicated = dedicated;
    }

    /**
     * Tiempo estimado (en horas) para terminar la tarea
     *
     * @return horas para terminar
     */
    public Integer getToFinish() {
        return toFinish;
    }

    public void setToFinish(Integer toFinish) {
        this.toFinish = toFinish;
    }

    /**
     * Prioridad de la tarea
     *
     * @return número de prioridad
     */
    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
    
    /**
     * Fecha de comienzo de la tarea
     *
     * @return fecha de comienzo
     */
    
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

   /**
     * Fecha en que se ha completado la tarea
     *
     * @return fecha en que se ha completado
     */
    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    /**
     * Usuario que tiene asiganda la tarea
     *
     * @return usuario asignado
     */
    public User getIduser() {
        return user;
    }

    public void setIduser(User iduser) {
        this.user = iduser;
    }

    /**
     * Estado de la tarea
     *
     * @return fecha de comienzo
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Sprint que tiene asignada la tarea
     *
     * @return fecha de comienzo
     */
    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    /**
     * Proyecto al que pertenece la tarea
     *
     * @return fecha de comienzo
     */
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * Lista de Comentarios asociados a la tarea
     *
     * @return fecha de comienzo
     */
    @XmlTransient
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    
}

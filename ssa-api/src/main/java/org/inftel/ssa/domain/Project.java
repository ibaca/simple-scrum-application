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
@Table(name = "projects")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Project.findAll", query = "SELECT p FROM Project p"),
    @NamedQuery(name = "Project.findByIdproject", query = "SELECT p FROM Project p WHERE p.id = :idproject"),
    @NamedQuery(name = "Project.findByName", query = "SELECT p FROM Project p WHERE p.name = :name"),
    @NamedQuery(name = "Project.findByDescription", query = "SELECT p FROM Project p WHERE p.description = :description"),
    @NamedQuery(name = "Project.findByStartDate", query = "SELECT p FROM Project p WHERE p.startDate = :startDate"),
    @NamedQuery(name = "Project.findByFinishDate", query = "SELECT p FROM Project p WHERE p.finishDate = :finishDate")})
public class Project extends BaseEntity {

    private static final long serialVersionUID = 1L;
    private String name;
    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishDate;
    private String company;
    @OneToMany(mappedBy = "project")
    private List<Task> tasks;
    @OneToMany(mappedBy = "project")
    private List<Sprint> sprints;

    /**
     * Nombre del proyecto
     *
     * @return cadena con el nombre del proyecto
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre del proyecto
     *
     * @param name Texto con el nombre del proyecto
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Descripción del proyecto
     *
     * @return cadena con la descripción del proyecto
     */
    public String getDescription() {
        return description;
    }

    /**
     * Establece la descripción del proyecto
     *
     * @param description Texto con la descripción del proyecto
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Fecha de comienzo del proyecto
     *
     * @return fecha de comienzo
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Establece la fecha de comienzo del proyecto
     *
     * @param name starDate de comienzo
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Fecha de finalización del proyecto
     *
     * @return fecha de finalización
     */
    public Date getFinishDate() {
        return finishDate;
    }

    /**
     * Establece la fecha de finalización del proyecto
     *
     * @param finishDate fecha de fin
     */
    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    /**
     * Empresa para la que se realiza el proyecto
     *
     * @return nombre de la empresa
     */
    public String getCompany() {
        return company;
    }

    /**
     * Establece la empresa para la que se desrrolla el proyecto
     *
     * @param company cadena con el nombre de la empresa
     */
    public void setCompany(String company) {
        this.company = company;
    }

    @XmlTransient
    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @XmlTransient
    public List<Sprint> getSprints() {
        return sprints;
    }

    public void setSprints(List<Sprint> sprints) {
        this.sprints = sprints;
    }
}

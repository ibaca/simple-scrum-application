package org.inftel.ssa.domain;

import java.util.Collections;
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
@Table(name = "sprints")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Sprint.findAll", query = "SELECT s FROM Sprint s"),
	@NamedQuery(name = "Sprint.findByIdsprint", query = "SELECT s FROM Sprint s WHERE s.id = :sprint"),
	@NamedQuery(name = "Sprint.findByDescription", query = "SELECT s FROM Sprint s WHERE s.description = :description"),
	@NamedQuery(name = "Sprint.findByStartDate", query = "SELECT s FROM Sprint s WHERE s.startDate = :startDate"),
	@NamedQuery(name = "Sprint.findByFinishDate", query = "SELECT s FROM Sprint s WHERE s.finishDate = :finishDate")})
public class Sprint extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String description;
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date finishDate;
	@OneToMany(mappedBy = "sprint")
	private List<Task> tasks;
	@ManyToOne(optional = false)
	private Project project;
	@ManyToOne(optional = false)
	private Product product;

	/**
	 * Descripción del Sprint
	 *
	 * @return cadena con la descripción del Sprint
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Establece la descripción del Sprint
	 *
	 * @param description Texto con la descripción del Sprint
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Fecha de comienzo del Sprint
	 *
	 * @return fecha de comienzo
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Establece la fecha de comienzo del Sprint
	 *
	 * @param startDate fecha de comienzo
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Fecha de fin del Sprint
	 *
	 * @return fecha de fin
	 */
	public Date getFinishDate() {
		return finishDate;
	}

	/**
	 * Establece la fecha de finalizacion del Sprint
	 *
	 * @param finishDate fecha de fin
	 */
	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	/**
	 * Lista de tareas asocias al Sprint
	 *
	 * @return lista de tareas
	 */
	@XmlTransient
	public List<Task> getTasks() {
		return Collections.unmodifiableList(tasks);
	}

	/**
	 * Establece la lista de tareas asociadas al Sprint
	 *
	 * @param taskList Lista de tareas
	 */
	public void setTasks(List<Task> taskList) {
		this.tasks = taskList;
	}

	/**
	 * Proyecto asociado al Sprint
	 *
	 * @return identificador del proyecto asociado
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * Establece el proyecto al que pertenece el Sprint
	 *
	 * @param project proyecto asociado
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
